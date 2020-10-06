package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TeacherOperationTakePhoto extends AppCompatActivity {

    ArrayList<String> student_fake_name = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_take_photo);

        student_fake_name.add("陳小一");
        student_fake_name.add("王小二");
        student_fake_name.add("李小三");
        student_fake_name.add("許小四");
        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.roll_call_layout);
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout show_top_linear = (LinearLayout) this.findViewById(R.id.show_top_linear);

        TextView tx1= new TextView(this);
        tx1.setTextSize(30);
        tx1.setWidth(350);   //設定寬度
        tx1.setHeight(120);
        tx1.setGravity(Gravity.CENTER);
        tx1.setText("姓名");

        TextView tx2= new TextView(this);
        tx2.setTextSize(30);
        tx2.setWidth(235);   //設定寬度
        tx2.setHeight(120);
        tx2.setGravity(Gravity.CENTER);
        tx2.setText("準時");

        TextView tx3= new TextView(this);
        tx3.setTextSize(30);
        tx3.setWidth(235);   //設定寬度
        tx3.setHeight(120);
        tx3.setGravity(Gravity.CENTER);
        tx3.setText("遲到");

        TextView tx4= new TextView(this);
        tx4.setTextSize(30);
        tx4.setWidth(235);   //設定寬度
        tx4.setHeight(120);
        tx4.setGravity(Gravity.CENTER);
        tx4.setText("缺席");
        show_top_linear.addView(tx1);
        show_top_linear.addView(tx2);
        show_top_linear.addView(tx3);
        show_top_linear.addView(tx4);
        for (int i = 0;i<50;i++){
            LinearLayout li = new LinearLayout(this);
            li.setOrientation(LinearLayout.HORIZONTAL);

            TextView textview=new TextView(this);
            textview.setTextSize(30);
            textview.setWidth(330);   //設定寬度
            textview.setHeight(120);
            textview.setGravity(Gravity.CENTER);
            textview.setText("你好嗎");

            Button bt1 = new Button(this);
            bt1.setTextSize(30);
            bt1.setWidth(230);   //設定寬度
            bt1.setHeight(150);
            bt1.setGravity(Gravity.CENTER);
            bt1.setText("+");
            bt1.setId((100 + i));
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    check();
                }
            });

            Button bt2 = new Button(this);
            bt2.setTextSize(30);
            bt2.setWidth(230);   //設定寬度
            bt2.setHeight(150);
            bt2.setGravity(Gravity.CENTER);
            bt2.setText("-");
            bt2.setId((100 + i));
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    check();
                }
            });

            Button bt3 = new Button(this);
            bt3.setTextSize(30);
            bt3.setWidth(230);   //設定寬度
            bt3.setHeight(150);
            bt3.setGravity(Gravity.CENTER);
            bt3.setText("X");
            bt3.setId((100 + i));
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    check();
                }
            });

            li.addView(textview);
            li.addView(bt1);
            li.addView(bt2);
            li.addView(bt3);

            mainLinerLayout.addView(li);
        }
    }
    public void check() {
    }
    public void onclick(View v) {
//        Intent intent = new Intent();
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
//        switch(v.getId()){
//
//
//            case R.id.gray_return_button:
//                intent.setClass(this , TeacherOperation.class);
//                break;
//        }
//        startActivity(intent);

    }
    public void _return(View v) {
        TeacherOperationTakePhoto.this.finish();
    }
}