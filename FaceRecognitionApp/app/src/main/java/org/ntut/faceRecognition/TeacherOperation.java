package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    String teacher_name;
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

        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
        String className = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            className = extras.getString("class_name");
        else
            throw new RuntimeException("Login error ! Cannot find userName");

        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n" + className + "課程");
    }

    public void onclick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.button_take_photo_auto:
                intent.setClass(this , TeacherOperationTakePhoto.class);
                break;
            case R.id.button_take_photo:
                break;
            case R.id.button_check_absence_record:
//                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_add_student_photo:
//                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.button_remove_student_photo:
            case R.id.button_modify_and_add_seating:
                break;

        }
        startActivity(intent);
    }
    public  void  _return(View v){
        TeacherOperation.this.finish();
    }
}