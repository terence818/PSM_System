package com.example.psmsystem;

public class User {


    public User(String username, String fullname, String email , String type) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.type = type;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private String username;
    private String fullname;
    private String email;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public User() {
        this.username = "";
        this.email = "";
        this.fullname = "";
        this.type="";


    }

}
