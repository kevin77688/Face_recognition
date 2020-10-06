package org.ntut.faceRecognition;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariable extends Application {
    private String Name, Email,Password, id;     //User 名稱
    public String []class_information;
    private Map<String, String[]> class_date = new HashMap<String, String[]>();
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
    public void setClassDate(String key, String []ClassDate){
        this.class_date.put(key, ClassDate);
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
    public  Map<String, String[]> getClassDate(){
        return  class_date;
    }
}