package com.cs401.ilegal.ilegal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatListviewActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayList<String> Names;
    private UserAdapter userAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_listview);
        //setContentView(R.layout.activity_docs_listview); THIS LINE IS IN MINE BUT MAKES HIMMATS CRASH ONCE CATEGORY CLICKED
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enabling backbutton on actionbar
        mDatabase = FirebaseDatabase.getInstance()
                .getReference().child("Chat/");
        Names = new ArrayList<String>();
        mainListView = (ListView) findViewById(R.id.users_listview);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> name = (Map<String, Object>)dataSnapshot.getValue();
                    Iterator it = name.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        String user = (String) pair.getKey();
                        user = user.replace("seanyuanuscedu", "");
                        System.out.println("Adding " + user);
                        Names.add(user);
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                if(Names.isEmpty()){
                    Names.add("No Chats");
                }
                userAdapter = new UserAdapter(ChatListviewActivity.this,Names);
                mainListView.setAdapter(userAdapter);
                mainListView.setOnItemClickListener((new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent i = new Intent(ChatListviewActivity.this,Chat.class);
                        i.putExtra("useremail",Names.get(position));
                        startActivity(i);
                    }
                }));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
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


    private class UserAdapter extends ArrayAdapter<String>
    {
        public UserAdapter(Context context, ArrayList<String> pdfs)
        {
            super(context,0,pdfs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String pdf = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user_item, parent, false);
            }
            TextView pdfName = (TextView) convertView.findViewById(R.id.name);
            pdfName.setText(pdf);
            return convertView;
        }

    }
}
