package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TeacherOperation extends AppCompatActivity {
    String className, classDate = null;
    private String _username;
    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation);
        // Init Services
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

//        GlobalVariable userdata = (GlobalVariable)getApplicationContext();

        _username = getIntent().getStringExtra("username");
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            className = extras.getString("class_name");
            classDate = extras.getString("class_date");
        }
        else
            throw new RuntimeException("Login error ! Cannot find userName");

        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + _username + "\n" + className + "課程");

        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.layout_teacher_class);

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