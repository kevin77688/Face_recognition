package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.layout_teacher_class);
        String [] class_names = userdata.getClassName();
        Log.e("json", String.valueOf(class_names.length));
        for (int i = 0;i<class_names.length;i++){
            Button btn = new Button(this);
            btn.setTextSize(30);
            btn.setWidth(250);   //設定寬度
            btn.setHeight(150);
            btn.setGravity(Gravity.CENTER);
            btn.setText(class_names[i].replace("\"", ""));
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
    public void _return(View v) {
        TeacherClass.this.finish();
    }
    private void goToPage(String button_text) {
        Intent intent = new Intent();
        intent.putExtra("class_name", button_text);
        intent.setClass(this , TeacherOperation.class);
        startActivity(intent);
    }
}