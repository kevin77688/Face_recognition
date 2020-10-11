package org.ntut.faceRecognition;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class JsonParser {

    private JSONObject _jsonObject;
    private int _status;
    private String _description, _username, _userId;

    public JsonParser(String response){
        try{
            _jsonObject = new JSONObject(response);
            _status = Integer.parseInt(_jsonObject.getString("status"));
            _description = _jsonObject.getString("description");
            _username = _jsonObject.getString("username");
            _userId = _jsonObject.getString("userId");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public int getStatus(){
        return _status;
    }

    public String getDescription(){
        return _description;
    }

    public String getName(){
        return _username;
    }

    public String getId(){
        return _userId;
    }
}
