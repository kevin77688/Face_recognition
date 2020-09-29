package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentOperation extends AppCompatActivity {
    //    private String email;
    private String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);

        // get user name
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            userName = extras.getString("name");
        else
            throw new RuntimeException("Login error ! Cannot find userName");

        TextView upview_text = (TextView) findViewById(R.id.upview_text);
        upview_text.setText("\n歡迎" + userName + "學生");
    }

    public void onclick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dcchect_photo_button:
                intent.setClass(this, Login.class);
                break;
            case R.id.upload_button:
                Log.e("sssss", "快跳轉");
                intent.setClass(this, StudentUpload.class);
                break;
            case R.id.check_presentation_record_button:
//                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.return_button:
                intent.setClass(this, Login.class);
                break;
        }
        startActivity(intent);
    }

}