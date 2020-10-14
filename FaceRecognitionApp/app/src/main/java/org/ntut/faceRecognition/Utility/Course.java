package org.ntut.faceRecognition.Utility;

public class Course {

    private String _className, _classDate, _attendance;

    public Course(String className, String classDate, String attendance) {
        _className = className;
        _classDate = classDate;
        switch (Integer.parseInt(attendance)) {
            case 0:
                _attendance = "準時";
                break;
            case 1:
                _attendance = "遲到";
                break;
            case 2:
                _attendance = "缺席";
                break;
            default:
                throw new RuntimeException("Attendance id error !");
        }
    }

    public String getName() {
        return _className;
    }

    public String getDate() {
        return _classDate;
    }

    public String getAttendance() {
        return _attendance;
    }

}
