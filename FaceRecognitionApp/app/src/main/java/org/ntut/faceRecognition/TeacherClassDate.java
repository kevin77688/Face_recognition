package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TeacherClassDate extends AppCompatActivity {

    private String courseName, courseId;
    private ArrayList<String> courseDates;
    private Intent dateIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_date);

        getExtras();

        setCourseButton();
        setReturnButton();

        setTitle();
    }

    private void getExtras() {
        courseName = getIntent().getStringExtra("courseName");
        courseId = getIntent().getStringExtra("courseId");
        courseDates = getIntent().getStringArrayListExtra("courseDate");
    }

    private void setCourseButton() {
        LinearLayout mainLinerLayout = this.findViewById(R.id.teacher_class_layout);
        for (final String date : courseDates) {
            Button btn = new Button(this);
            btn.setTextSize(30);
            btn.setWidth(250);   //設定寬度
            btn.setHeight(150);
            btn.setGravity(Gravity.CENTER);
            btn.setText(date);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateIntent = new Intent();
                    Button button = (Button) v;
                    dateIntent.putExtra("courseName", courseName);
                    dateIntent.putExtra("courseId", courseId);
                    dateIntent.putExtra("courseDate", date);
                    gotoPage();
                }
            });
            mainLinerLayout.addView(btn);
        }
    }

    private void setReturnButton() {
        Button button = (Button) findViewById(R.id.return_button);
        button.setOnClickListener(Utils.setReturnButton(TeacherClassDate.this));
    }

    private void setTitle() {
        TextView textView_show_teacher_name = findViewById(R.id.title_text);
        textView_show_teacher_name.setText("\n" + courseName);
    }

    private void gotoPage() {
        dateIntent.setClass(this, TeacherOperation.class);
        startActivity(dateIntent);
    }
}