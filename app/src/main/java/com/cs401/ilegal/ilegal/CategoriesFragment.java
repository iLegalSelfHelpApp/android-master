package com.cs401.ilegal.ilegal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hooman on 11/7/2016.
 */

public class CategoriesFragment extends Fragment {

    View view;

    private ListView mainListView ;
    private GridView mainListViewNew;
    private ArrayAdapter<String> listAdapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories_listview,container,false);

        mainListView = (ListView) view.findViewById( R.id.category_list );
      //  mainListViewNew = (GridView) view.findViewById(R.id.category_list);

        final ArrayList<String> categoryList = new ArrayList<String>();

        //----------------------------

        new AsyncTask<Integer, Void, ArrayList<String> >(){
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

            @Override
            protected void onPostExecute(ArrayList<String> categoryList)
            {
                super.onPostExecute(categoryList);


                Log.d("size",Integer.toString(categoryList.size()));
                listAdapter = new ArrayAdapter<String>(CategoriesFragment.this.getActivity(), R.layout.category_listview, categoryList);

                mainListView.setAdapter( listAdapter );


                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(CategoriesFragment.this.getActivity(),DocsListviewActivity.class);
                        i.putExtra("category", mainListView.getItemAtPosition(position).toString());
                        startActivity(i);
                    }
                });


            }
        }.execute(1, 2, 3, 4, 5);


        return view;
    }
}
