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

import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import retrofit2.Retrofit;

public class TeacherClassDate extends AppCompatActivity {

    private IMyService iMyService;
    private String className = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_date);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        GlobalVariable userdata = (GlobalVariable)getApplicationContext();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            className = extras.getString("class_name");
        else
            throw new RuntimeException("Login error ! Cannot find userName");
        String [] class_date = userdata.class_date.get(className);
        Log.e("class_date", class_date[0]);

        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + className);
        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.layout_teacher_class);
        for (int i = 0;i<class_date.length;i++){
            Button btn = new Button(this);
            btn.setTextSize(30);
            btn.setWidth(250);   //設定寬度
            btn.setHeight(150);
            btn.setGravity(Gravity.CENTER);
            btn.setText(class_date[i].replace("\"", ""));
            btn.setId((100 + i));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    goToPage((String) button.getText());
                }
            });
            mainLinerLayout.addView(btn);
        }
    }
    private void goToPage(String button_text) {
        Intent intent = new Intent();
        Log.e("goToPage", className);
        intent.putExtra("class_name", className );
        intent.putExtra("class_date", button_text);

        intent.setClass(this , TeacherOperation.class);
        startActivity(intent);
    }
    public  void  _return(View v){
        TeacherClassDate.this.finish();
    }

}