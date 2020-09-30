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

public class TeacherClass extends AppCompatActivity {

    String teacher_name, class_name;
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
        setContentView(R.layout.activity_teacher_class);
        // Init Services
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
        teacher_name = userdata.getName();
        Log.e("name", userdata.getName());
        TextView textView_show_teacher_name = (TextView)findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n歡迎" + userdata.getName() + "教授");
        Log.e("class", "class_name");
        getClassInformation(1);
    }

    private void getClassInformation(Integer id) {
//        final String[] class_name = new String[1];
        compositeDisposable.add(iMyService.findClass(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();

                        JSONObject jsonobj = new JSONObject(response);
                        Log.e("class", jsonobj.getString("name"));
//                        class_name[0] = jsonobj.getString("name");
                        class_name =  jsonobj.getString("name");
                    }
                }));
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
            case R.id.button_return:
                intent.setClass(this , Login.class);
                break;

        }
        startActivity(intent);
    }
}