package com.example.myapplication.Models;

public class TravelDoc extends FirebaseUserModel {
    private String documentUrl;

    public TravelDoc() {
    }

    public TravelDoc(String id, String documentUrl) {
        super(id);
        this.documentUrl = documentUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }


    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
