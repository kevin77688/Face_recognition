package org.ntut.faceRecognition.Utility;

import java.util.Comparator;

public class SortStudentList implements Comparator<Student> {
    @Override
    public int compare(Student student1, Student student2) {
        return (student1.getAttendanceStatus() <= student2.getAttendanceStatus()) ? 1 : -1;
    }
}
