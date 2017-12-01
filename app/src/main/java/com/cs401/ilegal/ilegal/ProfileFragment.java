package com.cs401.ilegal.ilegal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Marek_Foster on 4/9/17.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

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

    Button mLogOut;
    EditText mFirst;
    EditText mLast;
    EditText mEmail;
    EditText mLicense;
    EditText mAddress;
    EditText mCity;
    Spinner mState;
    EditText mZip;

    String lastNameText;
    String firstNameText;
    String addressText;
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

    public ProfileFragment() {
        // Required empty public constructor
    }

    //TODO store newInstance input into fragment argument
    public static ProfileFragment newInstance() {  //this is the factory part
        Bundle args = new Bundle();

        ProfileFragment f = new ProfileFragment();
        f.setArguments(args);

        return f;
    }

    //TODO read bundle argument
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        progress = new ProgressDialog(getActivity());
        editing = false;

        //findViews
        mLogOut = (Button) v.findViewById(R.id.buttonLogOut);
        mLogOut.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                //create intent to start Create activity
                Intent i = new Intent(getActivity(), MainActivity.class);
                //there is no position
                startActivity(i);

                //sign out

            }
        });

        mFirst = (EditText) v.findViewById(R.id.editFirst);
        mLast = (EditText) v.findViewById(R.id.editLast);
        mEmail = (EditText) v.findViewById(R.id.editEmail);
        mLicense = (EditText) v.findViewById(R.id.editLicense);
        mAddress = (EditText) v.findViewById(R.id.editAddress);
        mCity = (EditText) v.findViewById(R.id.editCity);
        mState = (Spinner) v.findViewById(R.id.state);
        mZip = (EditText) v.findViewById(R.id.editZip);
        mFirst.setEnabled(false);
        mLast.setEnabled(false);
        mEmail.setEnabled(false);
        mLicense.setEnabled(false);
        mAddress.setEnabled(false);
        mCity.setEnabled(false);
        mState.setEnabled(false);
        mZip.setEnabled(false);
        edit = (FloatingActionButton) v.findViewById(R.id.editor);
        UserSingleton user = UserSingleton.getInstance();

        mFirst.setText(user.getFirstName());
        mLast.setText(user.getLastName());
        mEmail.setText(user.getEmail());
        mLicense.setText(user.getLicenseNumber());
        mAddress.setText(user.getAddress1());

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
                    updateaddress();
                    updatecity();
                    updateemail();
                    updatefirst();
                    updatelast();
                    updatelicense();
                    updatepostal();
                    mFirst.setEnabled(false);
                    mLast.setEnabled(false);
                    mEmail.setEnabled(false);
                    mLicense.setEnabled(false);
                    mAddress.setEnabled(false);
                    mCity.setEnabled(false);
                    mState.setEnabled(false);
                    mZip.setEnabled(false);
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
                    edit.setImageResource(R.drawable.check);
                }
            }
        });
        return  v;
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
                    } else {
                        Toast.makeText(getActivity(), "FAIL!!! in Changing Your First Name", Toast.LENGTH_LONG).show();
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
                       // Toast.makeText(getActivity(), "Success in Changing Your Zip Code", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }
            }
        }.execute(1, 2, 3, 4, 5);
    }
}