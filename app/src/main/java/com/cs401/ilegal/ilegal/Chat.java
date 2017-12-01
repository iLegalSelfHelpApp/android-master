package com.cs401.ilegal.ilegal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by seanyuan on 11/28/17.
 */

public class Chat extends AppCompatActivity {
    ListView listView;
    MessageAdapter adapter;
    String username;
    String id;
    String myaccount;
    String targetacccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent i = getIntent();
        final String useremail = i.getStringExtra("useremail");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText input = (EditText) findViewById(R.id.input);
        listView = (ListView) findViewById(R.id.list);
        targetacccount = useremail;

        String myemail = UserSingleton.getInstance().getEmail();
        myaccount = myemail.replace("@","").replace(".","");
        showAllOldMessages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = UserSingleton.getInstance().getEmail();
                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(Chat.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                } else {
                    if(myaccount.equals(MainNavigationActivity.adminemail)){
                        username = "iLegal Help Desk";
                        FirebaseDatabase.getInstance()
                                .getReference().child("Chat/"+myaccount+targetacccount)
                                .push()
                                .setValue(new ChatMessages(input.getText().toString(),
                                        username,
                                        id)
                                );
                        input.setText("");
                    }else{
                        username = UserSingleton.getInstance().getFirstName();
                        FirebaseDatabase.getInstance()
                                .getReference().child("Chat/"+MainNavigationActivity.adminemail+UserSingleton.getInstance().getEmail().split("@")[0])
                                .push()
                                .setValue(new ChatMessages(input.getText().toString(),
                                        username,
                                        id)
                                );
                    }
                    input.setText("");
                }
            }
        });
    }
    private void showAllOldMessages() {
        if(myaccount.equals(MainNavigationActivity.adminemail)) {
            adapter = new MessageAdapter(this, ChatMessages.class, R.layout.chat_message_in,
                    FirebaseDatabase.getInstance().getReference().child("Chat/"+MainNavigationActivity.adminemail+targetacccount));
            listView.setAdapter(adapter);
        }else{
            adapter = new MessageAdapter(this, ChatMessages.class, R.layout.chat_message_in,
                    FirebaseDatabase.getInstance().getReference().child("Chat/"+MainNavigationActivity.adminemail+UserSingleton.getInstance().getEmail().split("@")[0]));
            listView.setAdapter(adapter);
        }
    }
}
