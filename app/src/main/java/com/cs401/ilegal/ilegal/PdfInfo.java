package com.cs401.ilegal.ilegal;

import java.io.Serializable;

/**
 * Created by Hooman on 10/16/2016.
 */

//model class for info of each pdf
public class PdfInfo implements Serializable
{
    private String pdfName;
    private String pdfID;
    private String pdfURL;

    public PdfInfo(String pdfName, String pdfID, String pdfURL) {
        this.pdfName = pdfName;
        this.pdfID = pdfID;
        this.pdfURL = pdfURL;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getPdfID() {
        return pdfID;
    }

    public String getPdfURL() {
        return pdfURL;
    }
}
