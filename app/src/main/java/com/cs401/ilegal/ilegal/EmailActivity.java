package com.cs401.ilegal.ilegal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {

    EditText subject;
    EditText email;
    EditText message;
    Button sendButton;
    private Email emailObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        subject = (EditText) findViewById(R.id.subject);
        email = (EditText) findViewById(R.id.email);
        message = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail(email.getText())) {
                    Toast.makeText(EmailActivity.this,"Make sure your email is valid.",Toast.LENGTH_LONG).show();
                } else if (subject.getText().length() == 0 || message.getText().length() == 0) {
                    Toast.makeText(EmailActivity.this,"Make sure all fields are filled out.",Toast.LENGTH_LONG).show();
                } else {
                    // Create instance of email sender class
                    emailObject = new Email();
                    emailObject.setAddress(email.getText().toString());
                    emailObject.setSubject(subject.getText().toString());
                    emailObject.setMessage(message.getText().toString());
                    try {
                        new EmailTask().execute();
                    } catch (Exception e) {
                        Toast.makeText(EmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }

    private class EmailTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            EmailSender mailer = new EmailSender();
            boolean emailResult = mailer.sendEmailToUser(emailObject.getAddress(), emailObject.getSubject(), emailObject.getMessage());
            if (emailResult == false){
                return "Email sending failure";
            } else {
                return "Email successfully sent...";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(EmailActivity.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    //function to validate email format
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
