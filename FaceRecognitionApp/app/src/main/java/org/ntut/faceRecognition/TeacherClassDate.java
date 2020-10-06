package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import retrofit2.Retrofit;

public class TeacherClassDate extends AppCompatActivity {

    private IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_date);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        GlobalVariable userdata = (GlobalVariable)getApplicationContext();

        String className = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            className = extras.getString("class_name");
        else
            throw new RuntimeException("Login error ! Cannot find userName");
        Log.e("name", userdata.getName());
        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + className);
    }
    public  void  _return(View v){
        TeacherClassDate.this.finish();
    }

}