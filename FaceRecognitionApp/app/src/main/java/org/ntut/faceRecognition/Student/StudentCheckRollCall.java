package org.ntut.faceRecognition.Student;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Utility.Course;
import org.ntut.faceRecognition.Utility.Utils;

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
    private String userId;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_roll_call);

        courseList = new ArrayList<>();
        getExtras();
        findView();
        setupConnection();

        setConfirmButton();
        getAttendanceList();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("userId");
        }
    }

    private void findView() {
        confirmButton = findViewById(R.id.confirm_button);
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void setConfirmButton() {
        confirmButton.setOnClickListener(Utils.setReturnButton(StudentCheckRollCall.this));
    }

    synchronized private void getAttendanceList() {
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
        LinearLayout mainLinerLayout = findViewById(R.id.attendance_layout);
        LinearLayout show_top_linear = findViewById(R.id.title_text);
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