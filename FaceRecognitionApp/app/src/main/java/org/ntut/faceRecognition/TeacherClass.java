package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TeacherClass extends AppCompatActivity {

    private String teacherName, teacherId;
    private HashMap<String, String> courses;
    private ArrayList<String> coursesId;
    private Button returnButton;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);

        getExtras();
        coursesId = new ArrayList<>(courses.keySet());

        findView();
        setTitle();
        setCourseDateView();
        setReturnButton();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teacherName = getIntent().getStringExtra("username");
            teacherId = getIntent().getStringExtra("userId");
            courses = (HashMap<String, String>) getIntent().getSerializableExtra("courses");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void findView() {
        returnButton = findViewById(R.id.return_button);
        title = findViewById(R.id.title_text);
    }

    private void setReturnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(TeacherClass.this));
    }

    private void setTitle() {
        title.setText("\n歡迎" + teacherName + "教授");
    }

    private void setCourseDateView() {
        LinearLayout mainLinerLayout = this.findViewById(R.id.teacher_class_layout);
        for (final String courseId : coursesId) {
            final String courseName = courses.get(courseId);
            Button btn = new Button(this);
            btn.setTextSize(30);
            btn.setWidth(250);   //設定寬度
            btn.setHeight(150);
            btn.setGravity(Gravity.CENTER);
            btn.setText(courseName);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    // Init Services
                    Retrofit retrofitClient = RetrofitClient.getInstance();
                    IMyService iMyService = retrofitClient.create(IMyService.class);
                    compositeDisposable.add(iMyService.findCourseDate(courseId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String response) throws Exception {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ArrayList<String> dates = new ArrayList<>();
                                    Iterator<String> date = jsonObject.getJSONObject("dates").keys();
                                    while (date.hasNext())
                                        dates.add((String) jsonObject.getJSONObject("dates").get(date.next()));
                                    gotoPage(courseName, courseId, dates);
                                }
                            }));
                }
            });
            mainLinerLayout.addView(btn);
        }
    }

    private void gotoPage(String courseName, String courseId, ArrayList<String> courseDates) {
        Intent intent = new Intent();
        intent.putExtra("courseName", courseName);
        intent.putExtra("courseId", courseId);
        intent.putExtra("courseDate", courseDates);
        intent.setClass(this, TeacherClassDate.class);
        startActivity(intent);
    }
}