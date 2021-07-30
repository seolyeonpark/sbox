package com.example.soonbox_findpw;

import com.google.firebase.database.IgnoreExtraProperties;

public class UploadLink {

    public String uploaded;

    public UploadLink() {
        // Default constructor required for calls to DataSnapshot.getValue(Upload.class)
    }

    public UploadLink(String uploaded) {
        this.uploaded= uploaded;
    }
    public String getUploaded(){return uploaded;}

    public void setUploaded(String uploaded){this.uploaded= uploaded;}
}
