package org.ntut.faceRecognition;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariable extends Application {
    private String Name, Email,Password, id;     //User 名稱
    public ArrayList<String>class_information = new ArrayList<String> () ;
    public Map<String, String[]> class_date = new HashMap<String, String[]>();
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
    public void setClassInformation(ArrayList<String> Class){
        this.class_information = Class;
    }
    public void setClassDate(String key, String []ClassDate){
        Log.e(key, ClassDate[0]);
        Log.e(key, ClassDate[1]);
        Log.e(key, ClassDate[2]);
        Log.e(key, ClassDate[3]);
        this.class_date.put(key, ClassDate);
    }
    public ArrayList<String>getClassInformation() {
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