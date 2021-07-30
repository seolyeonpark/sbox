package com.example.soonbox_findpw;

public class Chat {

    public String email;
    public String text;
    public String prifileUrl;

    public Chat() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Chat(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getPrifileUrl() {
        return prifileUrl;
    }

    public void setPrifileUrl(String prifileUrl) {
        this.prifileUrl = prifileUrl;
    }
}
