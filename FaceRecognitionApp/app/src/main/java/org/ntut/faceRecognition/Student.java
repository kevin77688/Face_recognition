package org.ntut.faceRecognition;

public class Student {

    private String _studentId, _studentName, _attendance;
    private int _attandanceStatus;

    public Student(String studentId, String studentName, String attendance) {
        _studentId = studentId;
        _studentName = studentName;
        _attendance = attendance;
        _attandanceStatus = Integer.parseInt(attendance);
    }

    public String getId() {
        return _studentId;
    }

    public String getName() {
        return _studentName;
    }

    public int getAttandanceStatus() {
        return _attandanceStatus;
    }
}
