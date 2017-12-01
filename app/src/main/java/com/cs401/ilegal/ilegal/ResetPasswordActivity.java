package com.cs401.ilegal.ilegal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText new_pass;
    EditText confirm_pass;
    Button reset_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        new_pass = (EditText) findViewById(R.id.new_password);
        confirm_pass = (EditText) findViewById(R.id.confirm_password);
        reset_button = (Button) findViewById(R.id.reset);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = new_pass.getText().toString();
                String confirm_password = confirm_pass.getText().toString();
                if (password.length() == 0 || confirm_password.length() == 0) {
                    Toast.makeText(ResetPasswordActivity.this,"Make sure all fields are filled out.",Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirm_password) || password.length()<6) {
                    Toast.makeText(ResetPasswordActivity.this,"Make sure your passwords match and 6 characters or longer!",Toast.LENGTH_LONG).show();
                } else {
                    // Encrypt password
                    String hashedPasswordText = HashPassword.encryptPassword(password);
                    // Set password in database
                    final HashMap<String,String> passwordInfo = new HashMap<String,String>();
                    passwordInfo.put("Username",UserSingleton.getInstance().getEmail());
                    passwordInfo.put("Password",hashedPasswordText);

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
                                    Toast.makeText(ResetPasswordActivity.this, "Reset password success...", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                {
                                    if(resultJson.getString("Success").equals("false"))
                                    {
                                        Toast.makeText(ResetPasswordActivity.this, "Reset password failure...", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }.execute(1, 2, 3, 4, 5);


                    // Update user singleton
                    //UserSingleton.getInstance().setPassword(password);

                    finish();
                }
            }
        });
    }
}