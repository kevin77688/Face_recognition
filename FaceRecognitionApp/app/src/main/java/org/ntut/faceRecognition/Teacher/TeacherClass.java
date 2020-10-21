package org.ntut.faceRecognition.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;
import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Student.StudentOperation;
import org.ntut.faceRecognition.Utility.Utils;

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
    private Button teacherAddCourseButton;
    private TextView title;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);

        getExtras();
        setupConnection();
        findView();

        setCourseDateView();

        setTeacherAddCourseButton();
        setReturnButton();

        setTitle();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teacherName = getIntent().getStringExtra("username");
            teacherId = getIntent().getStringExtra("userId");
            courses = (HashMap<String, String>) getIntent().getSerializableExtra("courses");
            coursesId = new ArrayList<>(courses.keySet());
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void findView() {
        returnButton = findViewById(R.id.return_button);
        teacherAddCourseButton = findViewById(R.id.teacher_add_course_button);
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
            Log.i("courseId", courseId);
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

    private void setTeacherAddCourseButton() {
        teacherAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View studentJoinCourseLayout = LayoutInflater.from(TeacherClass.this)
                        .inflate(R.layout.activity_teacher_add_course, null);
                MaterialEditText courseId = studentJoinCourseLayout.findViewById(R.id.course_id_text_input);
                MaterialEditText courseName = studentJoinCourseLayout.findViewById(R.id.course_name_text_input);
                MaterialEditText courseCode = studentJoinCourseLayout.findViewById(R.id.course_code_text_input);
                MaterialEditText courseStage = studentJoinCourseLayout.findViewById(R.id.course_stage_text_input);
                MaterialEditText courseCredit = studentJoinCourseLayout.findViewById(R.id.course_credit_text_input);
                MaterialEditText courseTime = studentJoinCourseLayout.findViewById(R.id.course_time_text_input);

                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(TeacherClass.this)
                        .setTitle("新增課程")
                        .setDescription("請輸入課程資訊")
                        .setCustomView(studentJoinCourseLayout)
                        .setNegativeText("取消")
                        .autoDismiss(false)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();
                Button addCourseButton = studentJoinCourseLayout.findViewById((R.id.add_course_button));
                addCourseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isValid = true;
                        if (TextUtils.isEmpty(courseId.getText().toString()) || courseId.getText().toString().length() != 6) {
                            courseId.setError("請輸入6位數課程編號");
                            isValid = false;
                        } else if (TextUtils.isEmpty(courseName.getText().toString())) {
                            courseName.setError("課程名稱不能為空");
                            isValid = false;
                        } else if (TextUtils.isEmpty(courseId.getText().toString()) || courseCode.getText().toString().length() != 7){
                            courseCode.setError("請輸入7位數課程編碼");
                            isValid = false;
                        } else if (TextUtils.isEmpty(courseStage.getText().toString()) || !TextUtils.isDigitsOnly(courseStage.getText().toString())){
                            courseStage.setError("課程階段不能為空且只能為數字");
                            isValid = false;
                        } else if (TextUtils.isEmpty(courseCredit.getText().toString()) || !TextUtils.isDigitsOnly(courseCredit.getText().toString())){
                            courseCredit.setError("課程學分不能為空且只能為數字");
                            isValid = false;
                        } else if (TextUtils.isEmpty(courseTime.getText().toString()) || !TextUtils.isDigitsOnly(courseTime.getText().toString())){
                            courseTime.setError("課程時段不能為空且只能為數字");
                            isValid = false;
                        }
                        if (isValid){
                            compositeDisposable.add(iMyService.teacherAddCourse(teacherId, courseId.getText().toString(), courseName.getText().toString(), courseCode.getText().toString(), Integer.parseInt(courseStage.getText().toString()), Integer.parseInt(courseCredit.getText().toString()), Integer.parseInt(courseTime.getText().toString()))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String response) throws Exception {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int status = jsonObject.getInt("status");
                                            switch (status) {
                                                case 410:
                                                    courseId.setError("課程編號重複");
                                                    break;
                                                case 210:
                                                    Utils.showToast("新增成功", TeacherClass.this);
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    }));
                        }
                    }
                });
            }
        });
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