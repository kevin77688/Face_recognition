package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentOperation extends AppCompatActivity {

    private String _username;
    private String _userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);

        // Get Extra
        _username = getIntent().getStringExtra("username");
        _userId = getIntent().getStringExtra("userId");

        TextView upview_text = findViewById(R.id.upview_text);
        upview_text.setText("\n歡迎" + _username + "學生");
    }
    public void onclick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.dcchect_photo_button:
                // TODO show student picture view
                break;
            case R.id.upload_button:
                intent.setClass(this, StudentUpload.class);
                break;
            case R.id.check_presentation_record_button:
                intent.setClass(this , StudentCheckRollCall.class);
                break;
            case R.id.return_button:
                finish();
        }
        intent.putExtra("username", _username);
        intent.putExtra("userId", _userId);
        startActivity(intent);
    }

}