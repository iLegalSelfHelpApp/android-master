package com.cs401.ilegal.ilegal;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CreateAccountActivity extends AppCompatActivity {

    EditText lastName;
    EditText firstName;
    EditText password;
    EditText confirmPassword;
    EditText addressLine1;
    EditText addressLine2;
    EditText city;
    Spinner state;
    EditText email;
    EditText postalCode;
    EditText phoneNumber;
    EditText dateOfBirth;
    EditText licenseNumber;
    Button createAccountButton;

    String hashedPasswordText;
    String lastNameText;
    String firstNameText;
    String passwordText;
    String confirmPasswordText;
    String addressLine1Text;
    String addressLine2Text;
    String cityText;
    String stateText;
    String emailText;
    String postalCodeText;
    String phoneNumberText;
    String dateOfBirthText;
    String licenseNumberText;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    HashMap<String,String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Registration");
        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirm_password);
        addressLine1 = (EditText)findViewById(R.id.address_line_1);
        addressLine2 = (EditText)findViewById(R.id.address_line_2);
        city=(EditText) findViewById(R.id.city);
        state=(Spinner) findViewById(R.id.state);
        postalCode = (EditText)findViewById(R.id.postal_code);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        dateOfBirth = (EditText)findViewById(R.id.date_of_birth);
        licenseNumber = (EditText)findViewById(R.id.license_number);
        createAccountButton = (Button) findViewById(R.id.sign_up_button);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccountActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashedPasswordText=HashPassword.encryptPassword(password.getText().toString());
                lastNameText =lastName.getText().toString();
                firstNameText=firstName.getText().toString();
                passwordText=password.getText().toString();
                confirmPasswordText=confirmPassword.getText().toString();
                addressLine1Text=addressLine1.getText().toString();
                addressLine2Text=addressLine2.getText().toString();
                cityText=city.getText().toString();
                stateText=state.getItemAtPosition(state.getSelectedItemPosition()).toString();
                emailText=email.getText().toString();
                postalCodeText=postalCode.getText().toString();
                phoneNumberText=phoneNumber.getText().toString();
                dateOfBirthText=dateOfBirth.getText().toString();
                licenseNumberText=licenseNumber.getText().toString();

                Boolean fieldsValidated=true;


                if(!passwordText.equals(confirmPasswordText) || passwordText.length()<6)
                {
                    password.setError("Make sure your passwords match and are 6 characters or longer!");
                    confirmPassword.setError("Make sure your passwords match and 6 characters or longer!");
                    fieldsValidated=false;
                }
                if(!MainActivity.isValidEmail(emailText))
                {
                    email.setError("Enter a valid email!");
                    fieldsValidated=false;
                }
                if(addressLine1Text.length()<5)
                {
                    addressLine1.setError("Make sure you entered a valid address");
                    fieldsValidated=false;
                }
                if(state.getSelectedItemPosition()==0)
                {
                    TextView errorText = (TextView)state.getSelectedView();
                    errorText.setError("Select a State");
                    errorText.setTextColor(Color.RED);

                    fieldsValidated=false;
                }
                if((postalCodeText.length()!=5))
                {
                    postalCode.setError("Invalid Postal Code!");
                    fieldsValidated=false;
                }
                if(!android.util.Patterns.PHONE.matcher(phoneNumberText).matches() || phoneNumberText.length()!=10)
                {
                    phoneNumber.setError("Invalid Phone Number!");
                    fieldsValidated=false;
                }
                if(dateOfBirthText=="")
                {
                    dateOfBirth.setError("Select your Date of Birth!");
                    fieldsValidated=false;
                }
                if(licenseNumberText.length()<6)
                {
                    licenseNumber.setError("Invalid License Number");
                    fieldsValidated=false;
                }

                //if all fields are validated then create user by adding to database
                if(fieldsValidated)
                {
                    Log.d("Validated","Validated");
                    userInfo = new HashMap<String, String>();
                    userInfo.put("FirstName",firstNameText);
                    userInfo.put("LastName",lastNameText);
                    userInfo.put("DOB",dateOfBirthText);
                    userInfo.put("Address1",addressLine1Text);
                    userInfo.put("City",cityText);
                    userInfo.put("State",stateText);
                    userInfo.put("PostalCode",postalCodeText);
                    userInfo.put("PhoneNumber",phoneNumberText);
                    userInfo.put("LicenseNumber",licenseNumberText);
                    userInfo.put("EmailAddress",emailText);
                    userInfo.put("Password",hashedPasswordText);


                    new AsyncTask<Integer, Void, Void>(){
                        String message;

                        @Override
                        protected Void doInBackground(Integer... params) {
                            String response= performPostCall("http://159.203.67.188:8080/Dev/SignUp",userInfo);
                            Log.d("The response is",response);

                            //handling the response
                            try {
                                JSONObject resultJson = new JSONObject(response);
                                if(resultJson.getString("Success").equals("false"))
                                {
                                    Log.d("something went wrong",resultJson.toString(1));
                                }
                                else
                                {
                                    message=resultJson.getString("Message");
                                    Log.d("Message is",message);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result)
                        {
                            ProgressDialog progress = new ProgressDialog(CreateAccountActivity.this);
                            progress.setTitle("Registering User");
                            progress.setMessage("Please wait...");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();
                            new CountDownTimer(5000, 1000) {
                                public void onFinish() {
                                    //
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
// To dismiss the dialog
                            progress.dismiss();
                            if(message.equals("Email already in use."))
                            {
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Email already in use.", Snackbar.LENGTH_LONG)
                                        .show();
                                email.setError("Email already exists!");
                            }else{
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Registration successful.", Snackbar.LENGTH_LONG)
                                        .show();

                                System.out.println("scheduling user");
                                int interval = 30*24*60*60;
                                //int interval = 30000;
                                Calendar cal = Calendar.getInstance();
                                Intent intent = new Intent(CreateAccountActivity.this, CivilCodeAlarm.class);
                                PendingIntent sender = PendingIntent.getBroadcast(CreateAccountActivity.this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager am = (AlarmManager) CreateAccountActivity.this.getSystemService(CreateAccountActivity.this.ALARM_SERVICE);
                                am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, sender);
                                finish();
                            }

                        }
                    }.execute(1, 2, 3, 4, 5);

                    Log.d("Validated","Validated");

                }

                else
                {
                    fieldsValidated=true;
                    Log.d("Not Validated","Not Validated");
                }




            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }



    public static boolean isNumeric(String s) {
        boolean isValidInteger = false;
        try
        {
            Integer.parseInt(s);

            // s is a valid integer

            isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {
            // s is not an integer
        }

        return isValidInteger;
    }


    public static String  performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="broken";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    class CreateAccountThread extends AsyncTask<Void, Void, Void>
    {
        private String message;
        @Override
        protected Void doInBackground(Void... params)
        {

            return null;
        }

    }
}
