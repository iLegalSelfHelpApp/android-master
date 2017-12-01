package com.cs401.ilegal.ilegal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

//Craeted by Himmat Singh March 23rd, 2017

public class HighLevelCategories extends Fragment {

    private TextView adoptionView;
    private TextView civilView;
    private TextView crimeView;
    private TextView familyView;
    private TextView feeView;
    private TextView nameChangeView;
    private TextView probateView;
    private TextView smallClaims;
    private TextView trafficView;
    private FloatingActionButton emailButton;
    View v;
    //private GridView mainListViewNew; HIMMAT
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    public HighLevelCategories() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_docs_listview, container, false);

        final ArrayList<String> categoryList = new ArrayList<String>();
        adoptionView = (TextView) v.findViewById(R.id.adoption);
        civilView = (TextView) v.findViewById(R.id.civil);
        crimeView = (TextView) v.findViewById(R.id.crime);
        familyView = (TextView) v.findViewById(R.id.family);
        feeView = (TextView) v.findViewById(R.id.fee);
        nameChangeView = (TextView) v.findViewById(R.id.name);
        probateView = (TextView) v.findViewById(R.id.probate);
        smallClaims = (TextView) v.findViewById(R.id.claim);
        trafficView = (TextView) v.findViewById(R.id.traffic);
        emailButton = (FloatingActionButton) v.findViewById(R.id.email);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(HighLevelCategories.this.getActivity(),EmailActivity.class);
                startActivity(i);
            }
        });

        new AsyncTask<Integer, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Integer... params) { //returns an array list of strings for the sections in the PDF
                HashMap<String, String> typeHashMap = new HashMap<String, String>();
                typeHashMap.put("Type", "1");
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/ListPDF", typeHashMap);
                //handling the response

                try {
                    JSONObject resultJson = new JSONObject(response);
                    if (resultJson.getString("Success").equals("true")) {
                        JSONArray categoriesArray = resultJson.getJSONArray("Categories");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            categoryList.add(categoriesArray.getString(i));
                        }

                    }
                    Log.d("the json", resultJson.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return categoryList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> categoryList) {
                super.onPostExecute(categoryList);


                Log.d("size", Integer.toString(categoryList.size()));

                adoptionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Adoption");
                        startActivity(i);
                    }
                });
                civilView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Civil");
                        startActivity(i);
                    }
                });
                crimeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Crime");
                        startActivity(i);
                    }
                });
                familyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Family");
                        startActivity(i);
                    }
                });
                feeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Fee");
                        startActivity(i);
                    }
                });
                nameChangeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Name Change");
                        startActivity(i);
                    }
                });
                probateView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Probate");
                        startActivity(i);
                    }
                });
                smallClaims.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Small Claims");
                        startActivity(i);
                    }
                });

                trafficView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HighLevelCategories.this.getActivity(), DocsListviewActivity.class);
                        i.putExtra("category", "Traffic View");
                        startActivity(i);
                    }
                });

               /* mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(CategoriesFragment.this.getActivity(),DocsListviewActivity.class);
                        i.putExtra("category",mainListView.getItemAtPosition(position).toString());
                        startActivity(i);
                    }
                });*/


            }
        }.execute(1, 2, 3, 4, 5);

        return v;
    }

}


//-------------------------
       /* final ArrayList<String> categoryList = new ArrayList<String>();
        new AsyncTask<Integer, Void, ArrayList<String>>(){
            @Override
            protected ArrayList<String> doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String,String>();
                typeHashMap.put("Type","1");
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/ListPDF",typeHashMap);
                //handling the response

                try {
                    JSONObject resultJson = new JSONObject(response);
                    if(resultJson.getString("Success").equals("true"))
                    {
                        JSONArray categoriesArray = resultJson.getJSONArray("Categories");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            categoryList.add(categoriesArray.getString(i));
                        }

                    }
                    Log.d("the json",resultJson.toString(4));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                return categoryList;
            }

        }.execute(1, 2, 3, 4, 5);
//--------------------------------------

        return v;

    }*/

