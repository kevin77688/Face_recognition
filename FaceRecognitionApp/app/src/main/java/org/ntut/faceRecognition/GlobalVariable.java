package org.ntut.faceRecognition;

import android.app.Application;

public class GlobalVariable extends Application {
    private String Name, Email,Password, id;     //User 名稱
    private String Class_name;
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
    public void setClassName(String Class){
        this.Class_name = Class;
    }
    public String getClassName() {
        return Class_name;
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