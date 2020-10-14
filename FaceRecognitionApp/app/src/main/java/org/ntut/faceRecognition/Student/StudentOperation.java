package org.ntut.faceRecognition.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Utility.Utils;

public class StudentOperation extends AppCompatActivity {

    private String username, userId;
    private TextView title;
    private Button checkPhotoButton, uploadButton, checkAttendanceButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);

        getExtras();
        findView();

        checkPhotoButton();
        uploadPhotoButton();
        checkAttendanceButton();
        returnButton();

        setTitle();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = getIntent().getStringExtra("username");
            userId = getIntent().getStringExtra("userId");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void findView() {
        checkPhotoButton = findViewById(R.id.check_photo_button);
        uploadButton = findViewById(R.id.upload_button);
        checkAttendanceButton = findViewById(R.id.check_attendance_button);
        returnButton = findViewById(R.id.return_button);
        title = findViewById(R.id.title_text);
    }

    private void checkPhotoButton() {
        checkPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO student check photo view
            }
        });
    }

    private void uploadPhotoButton() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Student upload View entry point
                gotoPage(StudentUploadTest.class);
            }
        });
    }

    private void checkAttendanceButton() {
        checkAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentCheckRollCall.class);
            }
        });
    }

    private void returnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(StudentOperation.this));
    }

    private void setTitle() {
        title.setText("\n歡迎" + username + "學生");
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        intent.setClass(this, c);
        startActivity(intent);
    }
}