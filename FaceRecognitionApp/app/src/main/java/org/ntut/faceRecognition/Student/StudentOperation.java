package org.ntut.faceRecognition.Student;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import org.ntut.faceRecognition.Teacher.TeacherOperationManualCheckAttendance;
import org.ntut.faceRecognition.Utility.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentOperation extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String username, userId, currentCourseId;
    private TextView title, courseIdText, courseNameText, courseCodeText, courseStageText, courseCreditText;
    private Button checkPhotoButton, uploadButton, checkAttendanceButton, returnButton, joinCourseButton, addCourseButton;
    private IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);

        getExtras();
        setupConnection();
        findView();

        checkPhotoButton();
        uploadPhotoButton();
        checkAttendanceButton();
        returnButton();
        setJoinCourseButton();
        setTitle();
        setAddCourseButton();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = getIntent().getStringExtra("username");
            userId = getIntent().getStringExtra("userId");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void findView() {
        checkPhotoButton = findViewById(R.id.check_photo_button);
        uploadButton = findViewById(R.id.upload_button);
        checkAttendanceButton = findViewById(R.id.check_attendance_button);
        returnButton = findViewById(R.id.return_button);
        joinCourseButton = findViewById(R.id.join_course_button);
        title = findViewById(R.id.title_text);

        View addCourseLayout = LayoutInflater.from(this).inflate(R.layout.activity_student_join_course,null);
        courseIdText = addCourseLayout.findViewById(R.id.course_id_text);
        courseNameText = addCourseLayout.findViewById(R.id.course_name_text);
        courseCodeText = addCourseLayout.findViewById(R.id.course_code_text);
        courseStageText = addCourseLayout.findViewById(R.id.course_stage_text);
        courseCreditText = addCourseLayout.findViewById(R.id.course_credit_text);
        addCourseButton = addCourseLayout.findViewById(R.id.student_confirm_course_button);
    }

    private void checkPhotoButton() {
        checkPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentCheckAvatar.class);
            }
        });
    }

    private void uploadPhotoButton() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentUploadTest.class);
            }
        });
    }

    private void checkAttendanceButton() {
        checkAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(StudentCheckRollCall.class);
            }
        });
    }

    private void returnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(StudentOperation.this));
    }

    private void setJoinCourseButton() {
        joinCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View studentJoinCourseLayout = LayoutInflater.from(StudentOperation.this)
                        .inflate(R.layout.activity_student_join_course, null);
                MaterialEditText courseId = studentJoinCourseLayout.findViewById(R.id.course_id_text_input);

                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(StudentOperation.this)
                        .setTitle("加入課程")
                        .setDescription("請輸入課號")
                        .setCustomView(studentJoinCourseLayout)
                        .setNegativeText("取消")
                        .autoDismiss(false)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();
                Button searchButton = studentJoinCourseLayout.findViewById((R.id.student_search_button));
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isValid = true;
                        addCourseButton.setEnabled(false);
                        if (TextUtils.isEmpty(courseId.getText().toString()) || courseId.getText().toString().length() != 6) {
                            courseId.setError("請輸入6位數課程編號");
                            isValid = false;
                        }else
                            courseId.setError(null);
                        if (isValid){
                            compositeDisposable.add(iMyService.studentSearchCourse(courseId.getText().toString(), userId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String response) throws Exception {
                                            JSONObject jsonObject = new JSONObject(response);
                                            int status = jsonObject.getInt("status");
                                            switch (status) {
                                                case 407:
                                                    courseId.setError("查無此課程");
                                                    break;
                                                case 206:
                                                    courseId.setError("已選此課程");
                                                    break;
                                                case 207:
                                                    Utils.showToast("找到課程", StudentOperation.this);
                                                    JSONObject courseObject = jsonObject.getJSONObject("course");
                                                    String id = courseObject.getString("_id");
                                                    String name = courseObject.getString("name");
                                                    String code = courseObject.getString("code");
                                                    String stage = courseObject.getString("stage");
                                                    String credit = courseObject.getString("credits");
                                                    currentCourseId = id;
                                                    setAddCourseView(id, name, code, stage, credit);
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

    private void setAddCourseView(String id, String courseName, String code, String stage, String credit){
        courseIdText.setText("課程編號 : " + id);
        courseNameText.setText("課程名稱 : " + courseName);
        courseCodeText.setText("課程代碼 : " + code);
        courseStageText.setText("階段 : " + stage);
        courseCreditText.setText("學分 : " + credit);
        addCourseButton.setEnabled(true);
    }

    private void setAddCourseButton(){
        addCourseButton.setOnClickListener(v -> {
            Call<String> call = iMyService.studentAddCourse(currentCourseId, userId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Utils.showToast("加選成功", StudentOperation.this);
                    addCourseButton.setEnabled(false);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utils.showToast("加選失敗", StudentOperation.this);
                }
            });
        });
    }

    private void setTitle() {
        title.setText("\n歡迎" + username + "學生");
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        intent.setClass(this, c);
        startActivity(intent);
    }
}