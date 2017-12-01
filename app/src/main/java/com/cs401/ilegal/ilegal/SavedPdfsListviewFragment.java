package com.cs401.ilegal.ilegal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hooman on 11/7/2016.
 */

public class SavedPdfsListviewFragment extends Fragment {

    View view;

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    private ArrayList<String> savedPdfArray;
    private ArrayList<String> savedPdfIDs;

    JSONArray savedPdfJSONArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories_listview,container,false);

        mainListView = (ListView) view.findViewById( R.id.category_list );


        savedPdfArray = new ArrayList<String>();
        savedPdfIDs=new ArrayList<String>();

        //----------------------------

        new AsyncTask<Integer, Void, ArrayList<String> >(){
            @Override
            protected ArrayList<String> doInBackground(Integer... params) {
                HashMap<String, String> typeHashMap = new HashMap<String,String>();
                typeHashMap.put("Type","4");
                typeHashMap.put("UserUniqueID",UserSingleton.getInstance().getID());
                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/ListPDF",typeHashMap);
                //handling the response

                try {
                    JSONObject resultJson = new JSONObject(response);
                    if(resultJson.getString("Success").equals("true"))
                    {
                        savedPdfJSONArray = resultJson.getJSONArray("UserDocs");
                        for (int i = 0; i < savedPdfJSONArray.length(); i++) {
                            savedPdfArray.add(savedPdfJSONArray.getJSONArray(i).getString(2));
                            savedPdfIDs.add(savedPdfJSONArray.getJSONArray(i).getString(0));
                        }

                    }
                    Log.d("the json",resultJson.toString(4));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                return savedPdfArray;
            }

            @Override
            protected void onPostExecute(ArrayList<String> pdfList)
            {
                super.onPostExecute(pdfList);


                Log.d("size",Integer.toString(pdfList.size()));
                listAdapter = new ArrayAdapter<String>(SavedPdfsListviewFragment.this.getActivity(), R.layout.category_listview, pdfList);

                mainListView.setAdapter( listAdapter );


                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            String path = savedPdfJSONArray.getJSONArray(position).getString(1);
                            path=path.replace("\\/", "/");
                            path=path.replaceAll("/var/www/html","");

                            String pdfName=savedPdfJSONArray.getJSONArray(position).getString(2).replace(".pdf","");
                            PdfInfo filledPdfInfo = new PdfInfo(pdfName,null,null);


                            Intent i = new Intent(SavedPdfsListviewFragment.this.getActivity(),CompletedPdfActivity.class);
                            i.putExtra("FilledURL",path);
                            i.putExtra("pdfObject",(Serializable) filledPdfInfo);
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        }.execute(1, 2, 3, 4, 5);


        //for deleting a certain pdf
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Confirm Delete...");
                alert.setMessage("Are you sure you want to delete this document?");

                //if they confirm that they'd like to delete
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                HashMap<String, String> typeHashMap = new HashMap<String,String>();
                                typeHashMap.put("PDFID",savedPdfIDs.get(position));
                                String response = CreateAccountActivity.performPostCall("http://159.203.67.188:8080/Dev/DropPDF",typeHashMap);

                                try {
                                    JSONObject resultJson = new JSONObject(response);
                                    Log.d("long press successful",resultJson.toString(4));
                                    if(resultJson.getString("SUCCESS").equals("true"))
                                    {
                                        //deleteing the title and id of pdf since it was deleted
                                        publishProgress();
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }


                            //override method for refreshing the adapter and listview once user wouldd like to delete a document
                            @Override
                            protected void onProgressUpdate(Void... values) {
                                super.onProgressUpdate(values);

                                savedPdfArray.remove(position);
                                savedPdfIDs.remove(position);
                                listAdapter.notifyDataSetChanged();
                            }
                        }.execute();

                        dialog.dismiss();
                    }
                });

                //if they don't want to delete
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();


                return true;
            }
        });




        return view;
    }
}
