package com.cs401.ilegal.ilegal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.io.Serializable;

public class PreviewPdfAcitivity extends AppCompatActivity {

    WebView pdfPreview;
    Button editButton;
    PdfInfo selectedPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pdf_acitivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//enabling backbutton on actionbar

        selectedPdf=(PdfInfo) getIntent().getSerializableExtra("pdfObject");

        pdfPreview= (WebView) findViewById(R.id.pdf_preview);

        pdfPreview.getSettings().setJavaScriptEnabled(true);
        String pdfUrl = selectedPdf.getPdfURL();
        Log.d("the pdf url1",pdfUrl);
        pdfPreview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+"http://159.203.67.188/pdfs//" + pdfUrl);

        editButton = (Button) findViewById(R.id.fill_out_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("exceptionnnnn","exceptionnn");
                Intent i = new Intent(PreviewPdfAcitivity.this,FillPdfActivity.class);
                i.putExtra("pdfObject",(Serializable) selectedPdf);
                startActivity(i);
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
