package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentOperation extends AppCompatActivity {

    private String _username, _userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);

        getExtras();


        TextView upview_text = findViewById(R.id.title_text);
        upview_text.setText("\n歡迎" + _username + "學生");

        checkPhotoButton();
        uploadPhotoButton();
        checkAttendanceButton();
        returnButton();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _username = getIntent().getStringExtra("username");
            _userId = getIntent().getStringExtra("userId");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void checkPhotoButton() {
        Button button = (Button) findViewById(R.id.check_photo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO student check photo view
            }
        });
    }

    private void uploadPhotoButton() {
        Button button = (Button) findViewById(R.id.upload_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentUpload.class);
            }
        });
    }

    private void checkAttendanceButton() {
        Button button = (Button) findViewById(R.id.check_attendance_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentCheckRollCall.class);
            }
        });
    }

    private void returnButton() {
        Button button = (Button) findViewById(R.id.manual_check_attendance_button);
        button.setOnClickListener(Utils.setReturnButton(StudentOperation.this));
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.putExtra("username", _username);
        intent.putExtra("userId", _userId);
        intent.setClass(this, c);
        startActivity(intent);
    }
}