package com.example.myapplication;

import android.app.Application;

public class GlobalVariable extends Application {
    private String Name, Email,Password;     //User 名稱

    public void setPassword(String Password){
        this.Password = Password;
    }
    public String getPassword() {
        return Password;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }
    public void setName(String Name){
        this.Name = Name;
    }
    public String getEmail() {
        return Email;
    }
    public String getName() {
        return Name;
    }
}