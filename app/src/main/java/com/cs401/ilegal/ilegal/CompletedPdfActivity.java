package com.cs401.ilegal.ilegal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CompletedPdfActivity extends AppCompatActivity {

    WebView filledPdfWebview;
    String filledPdfURL;
    Button emailButton;
    Button doneButton;

    File outputDir;
    PdfInfo filledPDfInfo;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_pdf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enabling backbutton on actionbar


        filledPdfWebview= (WebView) findViewById(R.id.filled_pdf);
        emailButton= (Button) findViewById(R.id.email_button);
        doneButton= (Button) findViewById(R.id.done_button);

        filledPdfURL=getIntent().getStringExtra("FilledURL");
        filledPDfInfo = (PdfInfo) getIntent().getSerializableExtra("pdfObject");

        outputDir=this.getCacheDir();

        filledPdfWebview.getSettings().setJavaScriptEnabled(true);
        filledPdfWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+"http://159.203.67.188" + filledPdfURL);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new AsyncTask<Void, Void,Void>()
                    {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try
                            {
                                int permission = ActivityCompat.checkSelfPermission(CompletedPdfActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions(
                                            CompletedPdfActivity.this,
                                            PERMISSIONS_STORAGE,
                                            REQUEST_EXTERNAL_STORAGE
                                    );
                                }

                                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                                File folder = new File(extStorageDirectory, "pdf");
                                folder.mkdir();
                                File file = new File(folder, filledPDfInfo.getPdfName()+".pdf");
                                file.createNewFile();

                                FileOutputStream f = new FileOutputStream(file);
                                URL u = new URL("http://159.203.67.188" + filledPdfURL);
                                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                                c.setRequestMethod("GET");
                                c.setDoOutput(true);
                                c.connect();

                                InputStream in = c.getInputStream();

                                byte[] buffer = new byte[1024];
                                int len1 = 0;
                                while ((len1 = in.read(buffer)) > 0) {
                                    f.write(buffer, 0, len1);
                                }
                                f.close();
                                Log.d("it works!","it works");


                                //==================================

                                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/pdf/"+filledPDfInfo.getPdfName()+".pdf"));
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, UserSingleton.getInstance().getEmail());
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, filledPDfInfo.getPdfName());
                                //emailIntent.putExtra(Intent.EXTRA_TEXT, ViewAllAccountFragment.selectac+" PDF Report");
                                emailIntent.setType("application/pdf");
                                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(emailIntent, "Send email using:"));

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    }.execute();
            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CompletedPdfActivity.this,MainNavigationActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //deleting file that was saved as result of emailing
        File deletedFile = new File(Environment.getExternalStorageDirectory()+"/pdf/"+filledPDfInfo.getPdfName()+".pdf");
        deletedFile.delete();

    }
}
