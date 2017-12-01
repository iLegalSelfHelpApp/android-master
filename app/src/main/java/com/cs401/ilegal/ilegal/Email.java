package com.cs401.ilegal.ilegal;

/**
 * Created by pooja on 9/21/2017.
 */
import java.io.Serializable;

public class Email implements Serializable{
    private String address;
    private String subject;
    private String message;

    public String getAddress() {
        return address;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



