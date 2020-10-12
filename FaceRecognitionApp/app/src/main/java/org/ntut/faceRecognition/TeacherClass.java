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

    private String teacherName;
    private String teacherId;
    private HashMap<String, String> courses;
    private Intent courseButtonPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);

        teacherName = getIntent().getStringExtra("username");
        teacherId = getIntent().getStringExtra("userId");
        courses = (HashMap<String, String>) getIntent().getSerializableExtra("courses");

        TextView textView_show_teacher_name = findViewById(R.id.textView_show_teacher_name);
        textView_show_teacher_name.setText("\n歡迎" + teacherName + "教授");

        ArrayList<String> coursesId = new ArrayList<>(courses.keySet());

        LinearLayout mainLinerLayout = this.findViewById(R.id.layout_teacher_class);
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
                    courseButtonPage = new Intent();
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
                                        dates.add((String) jsonObject.get(date.next()));
                                    courseButtonPage.putExtra("dates", dates);
                                    gotoPage(courseName, courseId);
                                }
                            }));
                }
            });
            mainLinerLayout.addView(btn);
        }
    }

    public void _return(View v) {
        TeacherClass.this.finish();
    }

    private void gotoPage(String courseName, String courseId) {
        courseButtonPage.putExtra("courseName", courseName);
        courseButtonPage.putExtra("courseId", courseId);
        courseButtonPage.setClass(this, TeacherClassDate.class);
        startActivity(courseButtonPage);
    }

    synchronized private void getCourseDate(String courseId) {

    }
}