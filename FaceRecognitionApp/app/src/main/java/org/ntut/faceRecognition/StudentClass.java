package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);

//        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
//        textView_show_teacher_name.setText("\n歡迎" + userdata.getName() + "同學");
//        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.layout_teacher_class);
//        String [] class_names = userdata.getClassInformation().toArray(new String[0]);
//        for (int i = 0;i<class_names.length;i++){
//            Button btn = new Button(this);
//            btn.setTextSize(30);
//            btn.setWidth(250);   //設定寬度
//            btn.setHeight(150);
//            btn.setGravity(Gravity.CENTER);
//            btn.setText(class_names[i].replace("\"", ""));
//            btn.setId((100 + i));
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Button button = (Button) v;
//                    goToPage((String) button.getText());
//                }
//            });
//            mainLinerLayout.addView(btn);
//        }
    }
    private void goToPage(String button_text) {
        Intent intent = new Intent();
        intent.putExtra("class_name", button_text);
        intent.setClass(this , StudentCheckRollCall.class);
        startActivity(intent);
    }
    public void _return(View v) {
        StudentClass.this.finish();
    }

}