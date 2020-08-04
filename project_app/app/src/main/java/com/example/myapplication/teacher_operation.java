package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
public class teacher_operation extends AppCompatActivity {
    String teacher_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation);
        Intent intent = this.getIntent();
        teacher_name = intent.getStringExtra("teacher_name");
        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("歡迎" + teacher_name + "教授");
    }

    public void onclick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.button_take_photo_auto:
                intent.setClass(this , teacher_operation_which_mask_mode.class);
                break;
            case R.id.button_take_photo:
                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_check_absence_record:
                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_add_student_photo:
                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_remove_student_photo:
                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_modify_and_add_seating:
                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_mask_mode:
                intent.setClass(this , teacher_login_new.class);
                break;

        }
        startActivity(intent);
    }
}