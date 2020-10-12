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

        courseName = getIntent().getStringExtra("courseName");
        courseId = getIntent().getStringExtra("courseId");
        courseDates = getIntent().getStringArrayListExtra("courseDate");

        TextView textView_show_teacher_name = findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + courseName);
        LinearLayout mainLinerLayout = this.findViewById(R.id.layout_teacher_class);

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

    public void _return(View v) {
        TeacherClassDate.this.finish();
    }

    public void gotoPage() {
        dateIntent.setClass(this, TeacherOperation.class);
        startActivity(dateIntent);
    }
}