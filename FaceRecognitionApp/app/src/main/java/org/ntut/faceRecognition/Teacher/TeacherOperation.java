package org.ntut.faceRecognition.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Utility.Utils;

public class TeacherOperation extends AppCompatActivity {
    private String courseName, courseId, courseDate;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation);

        getExtras();

        setAutoCheckAttendanceButton();
        setManualCheckAttendanceButton();
        setAttendanceListButton();
        setReturnButton();

        setTitle();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseName = extras.getString("courseName");
            courseId = extras.getString("courseId");
            courseDate = extras.getString("courseDate");
        } else
            throw new RuntimeException("pass data between activity error");
    }

    private void setTitle() {
        TextView textView_show_teacher_name = findViewById(R.id.title_text);
        textView_show_teacher_name.setText("\n" + courseName + "\n" + courseDate + "課程");
    }

    private void setAutoCheckAttendanceButton() {
        Button button = findViewById(R.id.auto_check_attendance_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO auto check attendance action
            }
        });
    }

    private void setManualCheckAttendanceButton() {
        Button button = findViewById(R.id.manual_check_attendance_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(TeacherOperationManualCheckAttendance.class);
            }
        });
    }

    private void setAttendanceListButton() {
        Button button = findViewById(R.id.check_attendance_list_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(TeacherCheckRollCall.class);
            }
        });
    }


    private void setReturnButton() {
        Button button = findViewById(R.id.return_button);
        button.setOnClickListener(Utils.setReturnButton(TeacherOperation.this));
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.putExtra("courseName", courseName);
        intent.putExtra("courseId", courseId);
        intent.putExtra("courseDate", courseDate);
        intent.setClass(TeacherOperation.this, c);
        startActivity(intent);
    }

}