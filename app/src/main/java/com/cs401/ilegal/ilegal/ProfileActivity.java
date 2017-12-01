package com.cs401.ilegal.ilegal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by seanyuan on 9/17/17.
 */

public class ProfileActivity extends AppCompatActivity{
    private static final String TAG = ProfileFragment.class.getSimpleName();
    List<String> FieldsToChange = new  ArrayList<String>(Arrays.asList("FirstName", "LastName","Email","License","Address1", "Address2","City","ZipCode","State"));
    //Bundle key
    public static final String ARGS_POSITION = "args_position";
    List<String> states = new ArrayList<>(Arrays.asList("Select State",
            "Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware", "District of Columbia",
            "Florida",
            "Georgia",
            "Guam",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Northern Marianas Islands",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia", "Virgin Islands",
            "Washington", "West Virginia",
            "Wisconsin",
            "Wyoming"));

    EditText mFirst;
    EditText mLast;
    EditText mEmail;
    EditText mLicense;
    EditText mAddress;
    EditText mCity;
    Spinner mState;
    EditText mZip;
    EditText mAddressTwo;

    String lastNameText;
    String firstNameText;
    String addressText;
    String addressTwoText;
    String cityText;
    String stateText;
    String emailText;
    String postalCodeText;
    String licenseNumberText;
    String userId;
    FloatingActionButton edit;
    Boolean editing;
    ProgressDialog progress;

    HashMap<String, String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        progress = new ProgressDialog(ProfileActivity.this);
        editing = false;


        mFirst = (EditText) findViewById(R.id.editFirst);
        mLast = (EditText) findViewById(R.id.editLast);
        mEmail = (EditText) findViewById(R.id.editEmail);
        mLicense = (EditText) findViewById(R.id.editLicense);
        mAddress = (EditText) findViewById(R.id.editAddress);
        mCity = (EditText) findViewById(R.id.editCity);
        mState = (Spinner) findViewById(R.id.editstate);
        mZip = (EditText) findViewById(R.id.editZip);
        mAddressTwo = (EditText) findViewById(R.id.editAddressTwo);
        mFirst.setEnabled(false);
        mLast.setEnabled(false);
        mEmail.setEnabled(false);
        mLicense.setEnabled(false);
        mAddress.setEnabled(false);
        mCity.setEnabled(false);
        mState.setEnabled(false);
        mZip.setEnabled(false);
        mAddressTwo.setEnabled(false);
        edit = (FloatingActionButton) findViewById(R.id.editor);
        UserSingleton user = UserSingleton.getInstance();

        mFirst.setText(user.getFirstName());
        mLast.setText(user.getLastName());
        mEmail.setText(user.getEmail());
        mLicense.setText(user.getLicenseNumber());
        mAddress.setText(user.getAddress1());
        mAddressTwo.setText(user.getAddress2());
        mCity.setText(user.getCity());
        mState.setSelection(states.indexOf(user.getState()));
        mZip.setText(user.getPostalCode());
        userId = user.getID();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    progress.setTitle("Updating Profile");
                    progress.setMessage("Just one moment...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    edit.setImageResource(R.drawable.editicon);
                    updateaddressTwo();
                    updateaddress();
                    updatecity();
                    updateemail();
                    updatefirst();
                    updatelast();
                    updatelicense();
                    updatepostal();
                    updateaddressTwo();
                    updateState();
                    mFirst.setEnabled(false);
                    mLast.setEnabled(false);
                    mEmail.setEnabled(false);
                    mLicense.setEnabled(false);
                    mAddress.setEnabled(false);
                    mCity.setEnabled(false);
                    mState.setEnabled(false);
                    mZip.setEnabled(false);
                    mAddressTwo.setEnabled(false);
                    editing = false;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    }, 5000);

                } else {
                    editing = true;
                    mFirst.setEnabled(true);
                    mLast.setEnabled(true);
                    mEmail.setEnabled(true);
                    mLicense.setEnabled(true);
                    mAddress.setEnabled(true);
                    mCity.setEnabled(true);
                    mState.setEnabled(true);
                    mZip.setEnabled(true);
                    mAddressTwo.setEnabled(true);
                    edit.setImageResource(R.drawable.check);
                }
            }
        });
    }

    private void updatefirst() {
        firstNameText = mFirst.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "FirstName");
                typeHashMap.put("NewValue", firstNameText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        //Toast.makeText(getActivity(), "Success in Changing Your First Name", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setFirstName(firstNameText);
                    } else {
                        Toast.makeText(ProfileActivity.this, "FAIL!!! in Changing Your First Name", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d("exceptionnnnn", "exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }

    private void updatelast() {
        lastNameText = mLast.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "LastName");
                typeHashMap.put("NewValue", lastNameText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        // Toast.makeText(getActivity(), "Success in Changing Your Last Name", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setLastName(lastNameText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }

    private void updateemail() {
        emailText = mEmail.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "Email");
                typeHashMap.put("NewValue", emailText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("Success").equals("true")) {
                        //Toast.makeText(getActivity(), "Success in Changing Your Email", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setEmail(emailText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }

    private void updatelicense() {
        licenseNumberText = mLicense.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "License");
                typeHashMap.put("NewValue", licenseNumberText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        // Toast.makeText(getActivity(), "Success in Changing Your License", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setLicenseNumber(licenseNumberText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }

    private void updateaddress() {
        addressText = mAddress.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "Address1");
                typeHashMap.put("NewValue", addressText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        //  Toast.makeText(getActivity(), "Success in Changing Your Address", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setAddress1(addressText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }
    private void updateaddressTwo() {
        addressTwoText = mAddressTwo.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "Address2");
                typeHashMap.put("NewValue", addressTwoText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        //  Toast.makeText(getActivity(), "Success in Changing Your Address", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setAddress2(addressTwoText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }

    private void updatecity() {
        cityText = mCity.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "City");
                typeHashMap.put("NewValue", cityText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        // Toast.makeText(getActivity(), "Success in Changing Your City", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setCity(cityText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }
    private void updateState() {
        stateText= mState.getItemAtPosition(mState.getSelectedItemPosition()).toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "State");
                typeHashMap.put("NewValue", stateText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        // Toast.makeText(getActivity(), "Success in Changing Your City", Toast.LENGTH_LONG).show();
                        UserSingleton.getInstance().setState(stateText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }


    private void updatepostal() {
        postalCodeText = mZip.getText().toString();
        new AsyncTask<Integer, Void, String>() {
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Id", userId);
                typeHashMap.put("FieldToChange", "ZipCode");
                typeHashMap.put("NewValue", postalCodeText);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateUser", typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is", resultJson.toString());
                    if (resultJson.getString("ErrorCode").equals("0")) {
                        Toast.makeText(ProfileActivity.this, "Successfully updated profile.", Toast.LENGTH_SHORT).show();
                        UserSingleton.getInstance().setPostalCode(postalCodeText);
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }
}
