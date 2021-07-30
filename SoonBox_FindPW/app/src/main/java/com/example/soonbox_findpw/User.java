package com.example.soonbox_findpw;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userName;
    public String editTextEmail;
    public String studentnumber;
    public String editTextPassword;
    public String uploaded;
    public String purchased;
    public String profileimage; //+

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String editTextEmail, String studentnumber, String editTextPassword, String uploaded, String purchased) {
        this.userName = userName;
        this.editTextEmail = editTextEmail;
        this.studentnumber= studentnumber;
        this.editTextPassword= editTextPassword;
        this.uploaded= uploaded;
        this.purchased = purchased;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return editTextEmail;
    }

    public void setEmail(String editTextEmail) {
        this.editTextEmail = editTextEmail;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber= studentnumber;
    }

    public String getPw() {
        return editTextPassword;
    }

    public void setPw(String editTextPassword) {
        this.editTextPassword = editTextPassword;
    }

    public String getUploaded(){return uploaded;}

    public void setUploaded(String uploaded){this.uploaded= uploaded;}

    public String getPurchased(){return purchased;}

    public void setPurchased(String purchased){this.purchased = purchased;}

    public String getProfileimage(){return profileimage;}

    public void setProfileimage(String profileimage){this.profileimage = profileimage;}

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", editTextEmail='" + editTextEmail + '\'' +
                ", editTextPassword='" + editTextPassword + '\'' +
                ", studentnumber='" + studentnumber + '\'' +
                ", uploaded='" + uploaded + '\'' +
                ", purchased='" + purchased + '\'' +
                '}';
    }
}
