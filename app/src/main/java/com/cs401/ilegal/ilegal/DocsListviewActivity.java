package com.cs401.ilegal.ilegal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DocsListviewActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayList<PdfInfo> pdfs;
    private PdfAdapter pdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docs_listview);
        //setContentView(R.layout.activity_docs_listview); THIS LINE IS IN MINE BUT MAKES HIMMATS CRASH ONCE CATEGORY CLICKED
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enabling backbutton on actionbar

        mainListView = (ListView) findViewById(R.id.docs_listview); //taken out bc was giving errors

        pdfs=new ArrayList<PdfInfo>();//arraylist that will hold pdfInfo objects from json
        final ArrayList<String> pdfNames = new ArrayList<String>();//array list that holds the names of the pdfs from json

        //getting the category name from previous activity
        Intent i = getIntent();
        final String category = i.getStringExtra("category");
        setTitle(category);

        //---------------------Getting PDF Info from JSON and setting ListView------------------
        new AsyncTask<Integer, Void, ArrayList<String> >(){
            @Override
            protected ArrayList<String> doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String,String>();
                typeHashMap.put("Type","2");
                typeHashMap.put("Category",category);
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/ListPDF",typeHashMap);
                //handling the response

                try {
                    JSONObject resultJson = new JSONObject(response);
                    if(resultJson.getString("Success").equals("true"))
                    {
                        JSONArray pdfArray = resultJson.getJSONArray("PDFS");
                        for (int i = 0; i < pdfArray.length(); i++) {
                            JSONArray pdfSubArray = pdfArray.getJSONArray(i);
                            String fileName = pdfSubArray.getString(0);
                            Log.d("file id is", pdfSubArray.getString(2));
                            Log.d("file url is",pdfSubArray.getString(1));
                            PdfInfo singlePdf = new PdfInfo(pdfSubArray.getString(0),pdfSubArray.getString(2),pdfSubArray.getString(1));
                            pdfs.add(singlePdf);
                            pdfNames.add(fileName);
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }



                return pdfNames;
            }//end of onDoInBackground

            @Override
            protected void onPostExecute(ArrayList<String> pdfNames)
            {

                super.onPostExecute(pdfNames);
                if(pdfs.isEmpty()){
                    PdfInfo singlePdf = new PdfInfo("Nothing Here!", "000", "No PDF's available for this category.");
                    pdfs.add(singlePdf);
                    pdfAdapter = new PdfAdapter(DocsListviewActivity.this,pdfs);
                    mainListView.setAdapter(pdfAdapter);
                }else{
                    pdfAdapter = new PdfAdapter(DocsListviewActivity.this,pdfs);
                    mainListView.setAdapter(pdfAdapter);
                    mainListView.setOnItemClickListener((new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            Intent i = new Intent(DocsListviewActivity.this,PreviewPdfAcitivity.class);
                            i.putExtra("pdfObject",(Serializable)pdfs.get(position));
                            startActivity(i);
                        }
                    }));
                }
            }//end of onPostExecute

        }.execute(1, 2, 3, 4, 5);//end of async task
    }//end of onCreate


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


    private class PdfAdapter extends ArrayAdapter<PdfInfo>
    {
        public PdfAdapter(Context context, ArrayList<PdfInfo> pdfs)
        {
            super(context,0,pdfs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PdfInfo pdf = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_pdf_item, parent, false);
            }
            TextView pdfName = (TextView) convertView.findViewById(R.id.txt);
            pdfName.setText(pdf.getPdfName());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
            if(pdf.getPdfName().equals("Nothing Here!")){
                imageView.setImageResource(R.drawable.pdfgrey);
            }else{
                imageView.setImageResource(R.drawable.pdficon);
            }
            TextView subtitle = (TextView) convertView.findViewById(R.id.desc);
            subtitle.setText(pdf.getPdfURL());



            return convertView;
        }

    }
}
