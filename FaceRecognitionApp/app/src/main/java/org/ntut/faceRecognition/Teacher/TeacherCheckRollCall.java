package org.ntut.faceRecognition.Teacher;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Utility.SortStudentList;
import org.ntut.faceRecognition.Utility.Student;
import org.ntut.faceRecognition.Utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TeacherCheckRollCall extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ArrayList<Student> students = new ArrayList<>();
    private LinearLayout titleText, attendanceList;
    private Button confirmButton;
    private String courseName, courseDate, courseId;
    private IMyService iMyService;
    private boolean attendanceRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check_roll_call);

        getExtras();
        setupConnection();
        findView();
        getAttendanceList();
        setConfirmButton();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseName = extras.getString("courseName");
            courseDate = extras.getString("courseDate");
            courseId = extras.getString("courseId");
        } else
            throw new RuntimeException("Transfer extra between activity failed");
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void findView() {
        titleText = findViewById(R.id.title_text);
        attendanceList = findViewById(R.id.attendance_layout);
        confirmButton = findViewById(R.id.confirm_button);
    }

    synchronized private void getAttendanceList() {
        compositeDisposable.add(iMyService.teacherGetCourseAttendance(courseId, courseDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        attendanceRecord = jsonObject.getBoolean("isRecorded");
                        if (attendanceRecord) {
                            JSONObject attendanceJson = jsonObject.getJSONObject("attendance");
                            Iterator<String> studentIds = attendanceJson.keys();
                            while (studentIds.hasNext()) {
                                String studentId = studentIds.next();
                                JSONObject attendance = (JSONObject) attendanceJson.get(studentId);
                                String studentName = attendance.getString("name");
                                String studentAttendance = attendance.getString("attendance");
                                students.add(new Student(studentId, studentName, studentAttendance));
                            }
                        }
                        setView();
                    }
                }));
    }

    private void setView() {
        if (!attendanceRecord) {
            TextView textView = new TextView(this);
            textView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setGravity(Gravity.CENTER);
            textView.setText("本課程還尚未有點名紀錄");
            titleText.addView(textView);
        } else {
            ArrayList<String> titles = new ArrayList<>(
                    Arrays.asList("學號", "姓名", "出缺席紀錄"));
            for (String title : titles) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                textView.setGravity(Gravity.CENTER);
                textView.setText(title);
                titleText.addView(textView);
            }

            sortStudentList();
            for (Student student : students) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Set layout
                ArrayList<String> studentStatus = new ArrayList<>(
                        Arrays.asList(student.getId(), student.getName(), student.getAttendanceStatusString())
                );
                for (String string : studentStatus) {
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(new TableLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(string);
                    linearLayout.addView(textView);
                }
                attendanceList.addView(linearLayout);
            }
        }
    }

    private void sortStudentList() {
        Collections.sort(students, new SortStudentList());
    }

    private void setConfirmButton() {
        confirmButton.setOnClickListener(Utils.setReturnButton(TeacherCheckRollCall.this));
    }


}