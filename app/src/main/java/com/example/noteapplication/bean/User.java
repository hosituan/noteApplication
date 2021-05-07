package com.example.noteapplication.bean;
import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String passWord;
    private String name = "";

    public User(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }

    public User(String email, String passWord, String name) {
        this.email = email;
        this.passWord = passWord;
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }
    public void setPassWord(String password) {
        this.passWord = password;
    }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() {
        return this.passWord;
    }

}
