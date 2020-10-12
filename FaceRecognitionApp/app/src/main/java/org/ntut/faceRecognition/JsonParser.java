package org.ntut.faceRecognition;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonParser {

    private JSONObject _jsonObject;
    private int _status;
    private String _description, _username, _userId;

    public JsonParser(String response) {
        try {
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

    public String getName() {
        return _username;
    }

    public String getId() {
        return _userId;
    }

    public HashMap<String, String> getCourses() {
        HashMap courses = new HashMap();
        try {
            JSONObject jsonCourses = _jsonObject.getJSONObject("courses");
            Iterator<String> coursesId = jsonCourses.keys();
            while (coursesId.hasNext()) {
                String courseId = coursesId.next();
                courses.put(courseId, jsonCourses.get(courseId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }
}
