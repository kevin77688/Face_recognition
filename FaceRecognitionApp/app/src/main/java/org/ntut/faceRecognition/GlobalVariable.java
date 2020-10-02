package org.ntut.faceRecognition;

import android.app.Application;

import org.json.JSONObject;

public class GlobalVariable extends Application {
    private String Name, Email,Password, id;     //User 名稱
    private String []class_information;
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
    public void setId(String id){
        this.id = id;
    }
    public void setClassInformation(String []Class){
        this.class_information = Class;
    }
    public String []getClassName() {
        return class_information;
    }
    public String getEmail() {
        return Email;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return Name;
    }
}