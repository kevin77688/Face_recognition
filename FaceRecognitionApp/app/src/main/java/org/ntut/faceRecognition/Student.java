package org.ntut.faceRecognition;

import android.widget.CheckBox;

import java.util.ArrayList;

public class Student {

    private String _studentId, _studentName, _attendance;
    private int _attendanceStatus;
    private ArrayList<CheckBox> _attendanceView;

    public Student(String studentId, String studentName, String attendance) {
        _studentId = studentId;
        _studentName = studentName;
        _attendance = attendance;
        _attendanceStatus = Integer.parseInt(attendance);
    }

    public String getId() {
        return _studentId;
    }

    public String getName() {
        return _studentName;
    }

    public int getAttendanceStatus() {
        return _attendanceStatus;
    }

    public void setAttendanceView(ArrayList<CheckBox> checkBoxes) {
        _attendanceView = checkBoxes;
    }

    public boolean checkAttendanceSet() {
        for (CheckBox checkBox : _attendanceView)
            if (checkBox.isChecked())
                return true;
        return false;
    }

    public boolean setAttendanceCheckBoxChecked(CheckBox checkBox) {
        for (int index = 0; index < _attendanceView.size(); index++)
            if (_attendanceView.get(index) == checkBox) {
                setCheckboxClear();
                _attendanceView.get(index).setChecked(true);
                _attendanceStatus = index;
                return true;
            }
        return false;
    }

    private void setCheckboxClear() {
        for (CheckBox checkBox : _attendanceView)
            checkBox.setChecked(false);
    }
}
