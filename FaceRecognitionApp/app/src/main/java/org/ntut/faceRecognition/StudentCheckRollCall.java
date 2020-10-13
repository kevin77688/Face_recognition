package org.ntut.faceRecognition;

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
import java.util.Arrays;
import java.util.Iterator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class StudentCheckRollCall extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_roll_call);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        courseList = new ArrayList<>();
        findRollCall(getIntent().getStringExtra("userId"));
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    synchronized private void findRollCall(String userId) {
        compositeDisposable.add(iMyService.studentCheckAttendance(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        int status = Integer.parseInt(jsonObject.getString("status"));

                        switch (status) {
                            case 203:
                                JSONObject courses = jsonObject.getJSONObject("courses");
                                Iterator<String> iter_class = courses.keys();
                                while (iter_class.hasNext()) {
                                    JSONObject course = (JSONObject) courses.get(iter_class.next());
                                    String className = course.getString("name");
                                    String classDate = course.getString("date");
                                    String classAttendance = course.getString("attendance");
                                    courseList.add(new Course(className, classDate, classAttendance));
                                }
                                setButton();
                                break;
                            case 403:
                                break;
                        }
                    }
                }));
    }

    private void setButton() {
        LinearLayout mainLinerLayout = this.findViewById(R.id.roll_call_layout);
        LinearLayout show_top_linear = this.findViewById(R.id.show_top_linear);
        ArrayList<String> titles = new ArrayList<>(Arrays.asList("課程", "時間", "出席"));
        for (String title : titles) {
            TextView tx = new TextView(this);
            tx.setTextSize(30);
            tx.setWidth(350);   //設定寬度
            tx.setHeight(120);
            tx.setGravity(Gravity.CENTER);
            tx.setText(title);
            show_top_linear.addView(tx);
        }

        for (Course course : courseList) {
            LinearLayout li = new LinearLayout(this);
            li.setOrientation(LinearLayout.HORIZONTAL);
            ArrayList<String> attendanceData = new ArrayList<>(Arrays.asList(course.getName(), course.getDate(), course.getAttendance()));
            for (String data : attendanceData) {
                TextView tx = new TextView(this);
                tx.setTextSize(20);
                tx.setWidth(350);   //設定寬度
                tx.setHeight(150);
                tx.setGravity(Gravity.CENTER);
                tx.setText(data);
                li.addView(tx);
            }
            mainLinerLayout.addView(li);
        }

    }
}