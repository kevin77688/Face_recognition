package org.ntut.faceRecognition.Student;

import android.content.Intent;
import android.os.Bundle;
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
import org.ntut.faceRecognition.Utility.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class StudentOperation extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String username, userId;
    private TextView title;
    private Button checkPhotoButton, uploadButton, checkAttendanceButton, returnButton, joinCourseButton;
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
                MaterialEditText courseId = studentJoinCourseLayout.findViewById(R.id.course_id_text);

                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(StudentOperation.this)
                        .setTitle("加入課程")
                        .setDescription("請輸入課號")
                        .setCustomView(studentJoinCourseLayout)
                        .setNegativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("加入")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final boolean[] count = {false};
                            }
                        }).show();
                Button searchButton = studentJoinCourseLayout.findViewById((R.id.student_search_button));
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (courseId.getText().toString().length() != 6) {
                            Utils.showToast("請輸入6位數課程編號", StudentOperation.this);
                            return;
                        } else {
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
                                                    Utils.showToast("查無此課程", StudentOperation.this);
                                                    break;
                                                case 206:
                                                    Utils.showToast("已選此課程", StudentOperation.this);
                                                    break;
                                                case 207:
                                                    Utils.showToast("找到課程", StudentOperation.this);
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