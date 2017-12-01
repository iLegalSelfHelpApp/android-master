package com.cs401.ilegal.ilegal;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    TextView civilcode;
    TextView createAccount;
    TextView forgotPassword;
    Button loginButton;
    String savedemail;
    CheckBox rememberme;
    private PendingIntent pendingIntent;
    Boolean userIsScheduled;
    static String civiltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences("loginstatus", 0);
        savedemail = settings.getString("email", "");
        //never save password - privacy security
        email= (EditText) findViewById(R.id.email);
        email.setText(savedemail);
        password=(EditText) findViewById(R.id.password);
        createAccount=(TextView) findViewById(R.id.create_account);
        forgotPassword=(TextView) findViewById(R.id.forgot_password);
        civilcode = (TextView) findViewById(R.id.civilcode);
        if (getIntent().hasExtra("code")) { //Null Checking
            String value = getIntent().getExtras().getString("code");
            civilcode.setText(value);
        }
        loginButton = (Button) findViewById(R.id.login_button);
        rememberme = (CheckBox) findViewById(R.id.rememberme);
        rememberme.setChecked(settings.getBoolean("checked", false));
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CreateAccountActivity.class);
                startActivity(i);
            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(email.getText()) && password.getText().length()>=6)
                {
                    Log.d("this is the password",HashPassword.encryptPassword(password.getText().toString()));

                    final HashMap<String,String> loginInfo = new HashMap<String,String>();
                    loginInfo.put("Username",email.getText().toString());
                    loginInfo.put("Password",HashPassword.encryptPassword(password.getText().toString()));

                    new AsyncTask<Integer, Void, JSONObject>(){

                        @Override
                        protected JSONObject doInBackground(Integer... params) {
                            JSONObject resultJson=new JSONObject();

                            String response= CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/SignIn",loginInfo);

                            try {
                                resultJson = new JSONObject(response);
                                Log.d("Result JSONN",resultJson.toString(1));

                                UserSingleton user = UserSingleton.getInstance();

                                user.setFirstName(resultJson.getString("FirstName"));
                                user.setLastName(resultJson.getString("LastName"));
                                user.setEmail(resultJson.getString("EmailAddress"));
                                user.setAddress1(resultJson.getString("Address1"));
                                user.setAddress2(resultJson.getString("Address2"));
                                user.setCity(resultJson.getString("City"));
                                user.setState(resultJson.getString("State"));
                                user.setPostalCode(resultJson.getString("PostalCode"));
                                user.setPhoneNumber(resultJson.getString("PhoneNumber"));
                                user.setDob(resultJson.getString("DOB"));
                                user.setLicenseNumber(resultJson.getString("LicenseNumber"));
                                user.setID(resultJson.getString("UserId"));

                                Log.d("singleton",user.toString());



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return resultJson;
                        }


                        @Override
                        protected void onPostExecute(JSONObject resultJson) {
                            super.onPostExecute(resultJson);

                            try {
                                if(resultJson.getString("Success").equals("true"))
                                {
                                    SharedPreferences settings = getSharedPreferences("loginstatus", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    /*if(!userIsScheduled){
                                        System.out.println("Not scheduled, now scheduling");
                                        notificationScheduler();
                                    }else{
                                        System.out.println("User is already scheduled");
                                    }*/
                                    if(rememberme.isChecked()){
                                        editor.putString("email", email.getText().toString());
                                        editor.putBoolean("checked", true);
                                        editor.commit();
                                    }else{
                                        editor.clear(); //clear all stored data
                                        editor.commit();
                                    }
                                    Intent i = new Intent(MainActivity.this,MainNavigationActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                                else
                                {
                                        if(resultJson.getString("Success").equals("false"))
                                        {
                                            Toast.makeText(MainActivity.this, "Wrong email/password!", Toast.LENGTH_LONG).show();
                                        }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }.execute(1, 2, 3, 4, 5);

            }
                else Toast.makeText(MainActivity.this,"Make sure your email and password are correct.",Toast.LENGTH_LONG).show();
            }
        });
    }


    //function to validate email format
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
