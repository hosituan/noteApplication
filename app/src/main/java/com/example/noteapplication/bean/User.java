package com.example.noteapplication.bean;
import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String passWord;

    public User(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassWord(String password) {
        this.passWord = password;
    }

    public String getPassword() {
        return this.passWord;
    }

}
