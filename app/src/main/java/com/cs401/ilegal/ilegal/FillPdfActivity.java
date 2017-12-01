package com.cs401.ilegal.ilegal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class FillPdfActivity extends AppCompatActivity {

    Button fillButton;

    LinearLayout ll;

    PdfInfo selectedPDF;

    JSONArray fieldsArray;

    ArrayList<Spinner> spinnerArrayList;
    ArrayList<CheckBox> checkBoxArrayList;
    ArrayList<EditText> editTextArrayList;

    Map<String, Integer> secondaryFields = new HashMap<>();
    Map<String, Integer> secondaryFieldsMapping = new HashMap<>();
    Map<Integer, String> originalSecondary = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_pdf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enabling backbutton on actionbar

        selectedPDF=(PdfInfo) getIntent().getSerializableExtra("pdfObject");
        Log.d("the pdfname is",selectedPDF.getPdfName());

        spinnerArrayList=new ArrayList<Spinner>();
        checkBoxArrayList=new ArrayList<CheckBox>();
        editTextArrayList=new ArrayList<EditText>();


        ll=(LinearLayout) findViewById(R.id.activity_fill_pdf);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,50);

        final LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(0,20,0,0);

        //--------------------------------------------------

        new AsyncTask<Integer, Void, String>(){
            @Override
            protected String doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String,String>();
                typeHashMap.put("PdfID",selectedPDF.getPdfID());
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/FillPDF",typeHashMap);

                return response;
            }

            @Override
            protected void onPostExecute(String response)
            {
                super.onPostExecute(response);

                try {
                    JSONObject resultJson = new JSONObject(response);
                    Log.d("json is",resultJson.toString());
                    if(resultJson.getString("Success").equals("true"))
                    {
                        fieldsArray = resultJson.getJSONArray("Fields");
                        for(int i=0;i<fieldsArray.length();i++)
                        {
                            JSONObject fieldObject = fieldsArray.getJSONObject(i);
                            String type = fieldObject.getString("type");
                            final String key = fieldObject.getString("name");
                            JSONArray valueArray = fieldObject.getJSONArray("value");

                            if(type.equals("Btn"))//checkbox type/field
                            {
                                TextView blah = new TextView(FillPdfActivity.this);
                                blah.setLayoutParams(textViewParams);
                                blah.setText(key);
                                blah.setTextSize(20);
                                blah.setGravity(Gravity.CENTER);
                                ll.addView(blah);
                                Log.d("length of array is:", Integer.toString(valueArray.length()));
                                RadioGroup lr = new RadioGroup(FillPdfActivity.this);
                                lr.setOrientation(LinearLayout.VERTICAL);
                                Log.d("length of array is:", Integer.toString(valueArray.length()-1));
                                for (int j = 0; j < valueArray.length(); j++)
                                {
                                    Log.d("count",Integer.toString(j));
                                    CheckBox checkBox = new CheckBox(FillPdfActivity.this);
                                    checkBox.setId((View.generateViewId()));
                                    Log.d("The value is",valueArray.getString(j));
                                    checkBox.setText(valueArray.getString(j));
                                    lr.addView(checkBox);
                                    checkBoxArrayList.add(checkBox);
                                }
                                ll.addView(lr);

                            }
                            else if(type.equals("Ch"))//dropdown field
                            {
                                TextView blah = new TextView(FillPdfActivity.this);
                                blah.setLayoutParams(textViewParams);
                                blah.setText(key);
                                blah.setTextSize(20);
                                blah.setGravity(Gravity.CENTER);
                                ll.addView(blah);

                                ArrayList<String> spinnerArray = new ArrayList<String>();
                                Spinner spinner = new Spinner(FillPdfActivity.this);
                                for (int j = 0; j < valueArray.length(); j++) {
                                    spinnerArray.add(valueArray.getString(j));
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FillPdfActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                                spinner.setAdapter(spinnerArrayAdapter);
                                ll.addView(spinner);
                                spinnerArrayList.add(spinner);
                            }
                            else if(type.equals("Tx"))//text fields to fill
                            {
                                TextView blah = new TextView(FillPdfActivity.this);
                                blah.setLayoutParams(textViewParams);
                                blah.setText(key);
                                blah.setTextSize(20);
                                blah.setGravity(Gravity.CENTER);
                                ll.addView(blah);

                                final EditText theTextfield = new EditText(FillPdfActivity.this);


//                                if(key.equalsIgnoreCase("First Name"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getFirstName());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Last Name"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getLastName());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Full Name"))
//                                {
//                                    Log.d("Full Name","Full Name");
//                                    theTextfield.setText(UserSingleton.getInstance().getFirstName()+" "+UserSingleton.getInstance().getLastName());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Email"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getEmail());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Address 1"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getAddress1());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Address 2"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getAddress2());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Address"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getAddress1()+" "+UserSingleton.getInstance().getAddress2());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("City"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getCity());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("State"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getState());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Zip Code"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getPostalCode());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Phone Number"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getPhoneNumber());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("Date of Birth"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getDob());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }
//                                else if(key.equalsIgnoreCase("License Number"))
//                                {
//                                    theTextfield.setText(UserSingleton.getInstance().getLicenseNumber());
//                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
//                                }

                                new AsyncTask<Integer, Void, String>(){
                                    protected String doInBackground(Integer... params) {
                                        HashMap<String, String> reqHashMap = new HashMap<String,String>();
                                        reqHashMap.put("PdfID",selectedPDF.getPdfID());
                                        reqHashMap.put("UserID",UserSingleton.getInstance().getID() + "");
                                        reqHashMap.put("FieldName", key);
                                        String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/GetMappedField",reqHashMap);
                                        return response;
                                    }

                                    @Override
                                    protected void onPostExecute(String response) {
                                        super.onPostExecute(response);

                                        try {
                                            JSONObject resultJson = new JSONObject(response);
                                            Log.d("json is",resultJson.toString());
                                            if(resultJson.getString("Success").equals("true"))
                                            {
                                                if (resultJson.getBoolean("isMapped")) {
                                                    String autofill = resultJson.getString("autofill");
                                                    theTextfield.setText(autofill);
                                                    theTextfield.setBackgroundColor(getResources().getColor(R.color.splashScreen));
                                                    if (resultJson.getBoolean("isSecondary")) {
                                                        secondaryFields.put(key, resultJson.getInt("secondaryID"));
                                                        secondaryFieldsMapping.put(key, resultJson.getInt("mappingID"));
                                                        if (resultJson.getInt("secondaryID") != -1) {
                                                            originalSecondary.put(resultJson.getInt("secondaryID"), autofill);
                                                        }
                                                    }
                                                }
                                            }

                                        } catch (JSONException e) {
                                            Log.d("exception in text","exception in text");
                                            e.printStackTrace();
                                        }
                                    }

                                }.execute(1, 2, 3, 4, 5);

                                ll.addView(theTextfield);
                                editTextArrayList.add(theTextfield);
                            }


                        }
                        ll.addView(fillButton);
                    }
                }
                catch (JSONException e) {
                    Log.d("exceptionnnnn","exceptionnn");
                    e.printStackTrace();
                }

            }
        }.execute(1, 2, 3, 4, 5);

        //--------------------------------------

        fillButton = new Button(FillPdfActivity.this);
        fillButton.setText("Done");

        fillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fillbutton","Fillbutton");
                try
                {
                    Iterator<Spinner> spinnerIterator=spinnerArrayList.iterator();
                    Iterator<CheckBox> checkBoxIterator = checkBoxArrayList.iterator();
                    Iterator<EditText> editTextIterator = editTextArrayList.iterator();

                    final JSONObject mainObject = new JSONObject();
                    for(int i=0;i<fieldsArray.length();i++)
                    {


                        JSONObject fieldObject = fieldsArray.getJSONObject(i);
                        String type = fieldObject.getString("type");
                        String key = fieldObject.getString("name");
                        JSONArray valueArray = fieldObject.getJSONArray("value");

                        JSONObject fillFieldObject = new JSONObject();

                        if (type.equals("Btn"))//checkbox type/field
                        {
                            fillFieldObject.put("type","Btn");
                            JSONArray filledValueArray = new JSONArray();
                            for (int j = 0; j < valueArray.length(); j++)
                            {
                                CheckBox cb = checkBoxIterator.next();
                                if(cb.isChecked())
                                {
                                    Log.d("is this checkbox value?",cb.getText().toString());
                                    filledValueArray.put(cb.getText().toString());
                                }
                            }
                            fillFieldObject.put("value",filledValueArray);

                        }
                        else if (type.equals("Ch"))//dropdown field
                        {
                            String selectedText = spinnerIterator.next().getSelectedItem().toString();
                            Log.d("is this dropdown value?",selectedText);

                            fillFieldObject.put("type","Ch");
                            JSONArray filledValueArray = new JSONArray();
                            filledValueArray.put(selectedText);
                            fillFieldObject.put("value",filledValueArray);
                        }
                        else if (type.equals("Tx"))//text fields to fill
                        {
                            final String fieldText = editTextIterator.next().getText().toString();
                           /* for(int j=0;j<editTextArrayList.size();j++)
                            {
                                Log.d("fuck me",editTextArrayList.get(j).getText().toString());
                            }*/
                            //EditText field = editTextIterator.next();
                           // String value = field.getText().toString();

                            Log.d("is this text value?",fieldText);
                            JSONArray filledValueArray = new JSONArray();
                            filledValueArray.put(fieldText);
                            fillFieldObject.put("type","Tx");
                            fillFieldObject.put("value",filledValueArray);

                            final Integer secondaryID = secondaryFields.get(key);
                            final Integer mappingID = secondaryFieldsMapping.get(key);
                            if (secondaryID != null && mappingID != null) {

                                final String originalValue = originalSecondary.get(secondaryID);
                                boolean updateSecondary = false;
                                if (originalValue == null) {
                                    updateSecondary = true;
                                } else if (!originalValue.equals(fieldText)) {
                                    updateSecondary = true;
                                }

                                if (updateSecondary) {
                                    // updating secondary profile info
                                    new AsyncTask<Integer, Void, String>(){
                                        protected String doInBackground(Integer... params) {
                                            HashMap<String, String> reqHashMap = new HashMap<String,String>();
                                            reqHashMap.put("SecondaryID", secondaryID + "");
                                            reqHashMap.put("MappingID", mappingID + "");
                                            reqHashMap.put("UserID",UserSingleton.getInstance().getID() + "");
                                            reqHashMap.put("Value", fieldText);

                                            String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/UpdateSecondary",reqHashMap);
                                            return response;
                                        }

                                        @Override
                                        protected void onPostExecute(String response) {
                                            super.onPostExecute(response);

                                            try {
                                                JSONObject resultJson = new JSONObject(response);
                                                Log.d("json is",resultJson.toString());
                                                if(resultJson.getString("Success").equals("true"))
                                                {

                                                }

                                            } catch (JSONException e) {
                                                Log.d("exception in text","exception in text");
                                                e.printStackTrace();
                                            }
                                        }

                                    }.execute(1, 2, 3, 4, 5);
                                }
                            }
                        }
                        mainObject.put(key,fillFieldObject);
                    }
                    Log.d("created json is",mainObject.toString());

                    new AsyncTask<Integer, Void, Void >(){
                        @Override
                        protected Void doInBackground(Integer... params) {
                            HashMap<String, String> typeHashMap = new HashMap<String,String>();
                            typeHashMap.put("PDFKEY","Test3");
                            typeHashMap.put("PDFID",selectedPDF.getPdfID());
                            typeHashMap.put("USERID",UserSingleton.getInstance().getID());
                            typeHashMap.put("pdfJsonResults",mainObject.toString());
                            String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/FinPDF",typeHashMap);
                            Log.d("the response is",response);

                            try {
                                JSONObject filledPdfJSON = new JSONObject(response);
                                if(filledPdfJSON.getString("Success").equals("true"))
                                {
                                    String fileURL = filledPdfJSON.getString("FileURL");
                                    fileURL=fileURL.replace("\\/", "/");
                                    fileURL=fileURL.replaceAll("/var/www/html","");
                                    Log.d("url coming in is",fileURL);
                                    Intent i = new Intent(FillPdfActivity.this,CompletedPdfActivity.class);
                                    i.putExtra("FilledURL",fileURL);
                                    i.putExtra("pdfObject",(Serializable) selectedPDF);
                                    startActivity(i);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("excetoi","exceptio");
                            }

                            return null;
                        }
                    }.execute(1, 2, 3, 4, 5);//end of async task

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

}
