package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherOperation extends AppCompatActivity {
    String className, classDate = null;
    private String _username;
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation);

        _username = getIntent().getStringExtra("username");
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            className = extras.getString("class_name");
            classDate = extras.getString("class_date");
        }
        else
            throw new RuntimeException("Login error ! Cannot find userName");

        TextView textView_show_teacher_name = findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + className + "\n" + classDate + "課程");
        LinearLayout mainLinerLayout = this.findViewById(R.id.layout_teacher_class);

    }

    public void onclick(View v) {
        Intent intent = new Intent();
        intent.putExtra("class_name", className);
        intent.putExtra("class_date", classDate);
        switch(v.getId()){
            case R.id.button_take_photo_auto:
                intent.putExtra("take_picture", "true");
                intent.setClass(this , TeacherOperationTakePhoto.class);
                break;
            case R.id.button_take_photo:
                intent.putExtra("take_picture", "false");
                intent.setClass(this , TeacherOperationTakePhoto.class);
                break;
            case R.id.button_check_absence_record:
//                intent.setClass(this , teacher_login_new.class);
                break;
        }
        startActivity(intent);
    }
    public  void  _return(View v){
        TeacherOperation.this.finish();
    }
}