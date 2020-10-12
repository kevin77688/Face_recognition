package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseName = extras.getString("courseName");
            courseId = extras.getString("courseId");
            courseDate = extras.getString("courseDate");
        } else
            throw new RuntimeException("pass data between activity error");

        TextView textView_show_teacher_name = findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + courseName + "\n" + courseDate + "課程");

        findViewById(R.id.button_take_photo_auto).setOnClickListener(onClickListener());
        findViewById(R.id.button_take_photo).setOnClickListener(onClickListener());
        findViewById(R.id.button_check_absence_record).setOnClickListener(onClickListener());

    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("courseName", courseName);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseDate", courseDate);
                switch (v.getId()) {
                    case R.id.button_take_photo_auto:
                        intent.putExtra("take_picture", "true");
                        intent.setClass(TeacherOperation.this, TeacherOperationTakePhoto.class);
                        break;
                    case R.id.button_take_photo:
                        intent.putExtra("take_picture", "false");
                        intent.setClass(TeacherOperation.this, TeacherOperationTakePhoto.class);
                        break;
                    case R.id.button_check_absence_record:
                        break;
                }
                startActivity(intent);
            }
        };
    }

    public void _return(View v) {
        TeacherOperation.this.finish();
    }
}