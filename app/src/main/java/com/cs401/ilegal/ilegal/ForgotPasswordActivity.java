package com.cs401.ilegal.ilegal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cs401.ilegal.ilegal.CreateAccountActivity;
import com.cs401.ilegal.ilegal.HashPassword;
import com.cs401.ilegal.ilegal.MainActivity;
import com.cs401.ilegal.ilegal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;


public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    Button sendButton;
    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 9;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Recover Password");

        email = (EditText) findViewById(R.id.email);
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail(email.getText())) {
                    Toast.makeText(ForgotPasswordActivity.this,"Make sure your email is valid.",Toast.LENGTH_LONG).show();
                } else {
                    // Create instance of email sender class
                    // Generate new random password
                    // Set password of user in DB to new encrypted password
                    String newPassword = generateRandomString();
                    UserSingleton.getInstance().setPassword(newPassword);

                    final HashMap<String,String> passwordInfo = new HashMap<String,String>();
                    passwordInfo.put("Username",email.getText().toString());
                    passwordInfo.put("Password",HashPassword.encryptPassword(newPassword));

                    new AsyncTask<Integer, Void, JSONObject>(){
                        @Override
                        protected JSONObject doInBackground(Integer... params) {
                            JSONObject resultJson=new JSONObject();

                            String response= CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/ChangePassword",passwordInfo);

                            try {
                                resultJson = new JSONObject(response);
                                Log.d("Result JSONN",resultJson.toString(1));
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
                                    try {
                                        new EmailTask().execute();
                                    } catch (Exception e) {
                                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                }
                                else
                                {
                                    if(resultJson.getString("Success").equals("false"))
                                    {
                                        Toast.makeText(ForgotPasswordActivity.this, "User profile unable to be located", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }.execute(1, 2, 3, 4, 5);
                }
            }
        });
    }

    //GENERATE TEMPORARY PASSWORD
    public String generateRandomString(){
        StringBuffer randStr = new StringBuffer();
        for(int i = 0; i < RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        randStr.append('!');
        return randStr.toString();
    }

    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }


    private class EmailTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params)
        {
            ForgotPasswordEmailSender mailer = new ForgotPasswordEmailSender();
            boolean emailResult = mailer.sendEmailToUser(UserSingleton.getInstance().getEmail(), UserSingleton.getInstance().getPassword());
            if (emailResult == false){
                return "Temporary password reset and email sending failure";
            } else {
                return "Password temporarily reset and email successfully sent to " + UserSingleton.getInstance().getEmail();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(ForgotPasswordActivity.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
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
