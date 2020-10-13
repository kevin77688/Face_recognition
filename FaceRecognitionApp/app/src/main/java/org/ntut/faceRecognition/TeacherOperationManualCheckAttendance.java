package org.ntut.faceRecognition;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TeacherOperationManualCheckAttendance extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String courseName, courseDate, courseId;
    private ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_manual_check_attendance);

        getExtras();
        setupConnection();

        students = new ArrayList<>();
        getStudentList();
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

    synchronized private void getStudentList() {
        compositeDisposable.add(iMyService.teacherGetCourseAttendance(courseId, courseDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject attendanceJson = jsonObject.getJSONObject("attendance");
                        Iterator<String> studentIds = attendanceJson.keys();
                        while (studentIds.hasNext()) {
                            String studentId = studentIds.next();
                            JSONObject attendance = (JSONObject) attendanceJson.get(studentId);
                            String studentName = attendance.getString("name");
                            String studentAttendance = attendance.getString("attendance");
                            students.add(new Student(studentId, studentName, studentAttendance));
                        }
                        setView();
                    }
                }));
    }

    private void setView() {
        LinearLayout mainLinerLayout = (LinearLayout) findViewById(R.id.attendance_layout);
        LinearLayout show_top_linear = (LinearLayout) findViewById(R.id.title_text);

        ArrayList<String> titles = new ArrayList<>(
                Arrays.asList("姓名", "準時", "遲到", "缺席"));
        for (String title : titles) {
            TextView textView = new TextView(this);
            textView.setTextSize(25);
            textView.setWidth(300);   //設定寬度
            textView.setHeight(120);
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            show_top_linear.addView(textView);
        }


        for (Student student : students) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Set student name
            TextView textView = new TextView(this);
            textView.setTextSize(30);
            textView.setWidth(330);   //設定寬度
            textView.setHeight(120);
            textView.setGravity(Gravity.CENTER);
            textView.setText(student.getName());
            linearLayout.addView(textView);

            ArrayList<CheckBox> checkBoxesColumn = new ArrayList();
            // Set checkbox
            for (int i = 0; i < 3; i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setTextSize(30);
                checkBox.setWidth(230);   //設定寬度
                checkBox.setHeight(150);
                checkBox.setGravity(Gravity.CENTER);
                checkBox.setChecked(false);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Student st : students)
                            if (st.setAttendanceCheckBoxChecked((CheckBox) v))
                                return;
                        throw new RuntimeException("Checkbox find parent failed !");
                    }
                });
                checkBoxesColumn.add(checkBox);
                linearLayout.addView(checkBox);
            }
            if (student.getAttendanceStatus() != -1)
                checkBoxesColumn.get(student.getAttendanceStatus()).setChecked(true);
            student.setAttendanceView(checkBoxesColumn);
            mainLinerLayout.addView(linearLayout);
        }
    }

    private void setConfirmButton() {
        Button button = (Button) findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Student student : students)
                    if (!student.checkAttendanceSet()) {
                        Utils.showToast("還有學生未點到名", TeacherOperationManualCheckAttendance.this);
                        return;
                    }
                updateRecord();
                finish();
            }
        });
    }

    private void updateRecord() {
        JsonObject responseData = new JsonObject();
        try {
            responseData.addProperty("courseId", courseId);
            responseData.addProperty("courseDate", courseDate);
            JsonObject studentsJson = new JsonObject();
            for (Student student : students) {
//                JsonObject studentJson = new JsonObject();
//                studentJson.addProperty("userId", student.getId());
//                studentJson.addProperty("attendance", student.getAttendanceStatus());
//                studentsJson.add("student", studentJson);
                studentsJson.addProperty(student.getId(), student.getAttendanceStatus());
            }
            Log.e("Total student : ", Integer.toString(students.size()));
            responseData.add("students", studentsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<String> call = iMyService.uploadAttendanceList(responseData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", "successful");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Response", "error");
            }
        });
    }

}