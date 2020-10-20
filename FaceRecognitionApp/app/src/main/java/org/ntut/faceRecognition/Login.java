package org.ntut.faceRecognition;

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

import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Student.StudentOperation;
import org.ntut.faceRecognition.Teacher.TeacherClass;
import org.ntut.faceRecognition.Utility.JsonParser;
import org.ntut.faceRecognition.Utility.Utils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IMyService iMyService;
    private TextView txt_create_account;
    private MaterialEditText edt_login_email, edt_login_password;
    private Button btn_login;
    private String username, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findView();
        setupConnection();

        setLoginButton();
        setRegisterButton();
    }

    private void findView() {
        edt_login_email = findViewById(R.id.email_field);
        edt_login_password = findViewById(R.id.password_field);
        txt_create_account = findViewById(R.id.create_account_text);
        btn_login = findViewById(R.id.login_button);
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void setLoginButton() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if (TextUtils.isEmpty(edt_login_email.getText().toString())) {
                    edt_login_email.setError("Email cannot be null or empty");
                    isValid = false;
                }else
                    edt_login_email.setError(null);

                if (!Utils.isEmailValid(edt_login_email.getText().toString())) {
                    edt_login_email.setError("Email format error");
//                    isValid = false;
                }else
                    edt_login_email.setError(null);
                if (TextUtils.isEmpty(edt_login_password.getText().toString())) {
                    edt_login_password.setError("Password cannot be null or empty");
                    isValid = false;
                }else
                    edt_login_password.setError(null);
                if (isValid)
                    loginUser(edt_login_email.getText().toString(), edt_login_password.getText().toString());
            }
        });
    }

    private void setRegisterButton() {
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(Login.this)
                        .inflate(R.layout.register_layout, null);

                new MaterialStyledDialog.Builder(Login.this)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .autoDismiss(false)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_email = register_layout.findViewById(R.id.email_field);
                                MaterialEditText edt_register_name = register_layout.findViewById(R.id.name_field);
                                MaterialEditText edt_register_password = register_layout.findViewById(R.id.password_field);
                                MaterialEditText edt_register_id = register_layout.findViewById(R.id.id_field);
                                boolean isValid = true;
                                if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    edt_register_email.setError("Email cannot be null or empty");
                                    isValid = false;
                                }else
                                    edt_register_email.setError(null);

                                if (!Utils.isEmailValid(edt_register_email.getText().toString())) {
                                    edt_register_email.setError("Email format error");
                                    isValid = false;
                                }else
                                    edt_register_email.setError(null);

                                if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                                    edt_register_name.setError("Name cannot be null or empty");
                                    isValid = false;
                                }else
                                    edt_register_name.setError(null);

                                if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                                    edt_register_password.setError("Password cannot be null or empty");
                                    isValid = false;
                                }else
                                    edt_register_password.setError(null);

                                if (TextUtils.isEmpty(edt_register_id.getText().toString()) ||
                                        edt_register_id.getText().toString().trim().length() != 9) {
                                    edt_register_id.setError("Id need to be 9 digit");
                                    isValid = false;
                                }else
                                    edt_register_id.setError(null);
                                if (isValid){
                                    registerUser(
                                            edt_register_email.getText().toString(),
                                            edt_register_name.getText().toString(),
                                            edt_register_password.getText().toString(),
                                            edt_register_id.getText().toString()
                                    );
                                    dialog.dismiss();
                                }
                            }
                        }).show();
            }
        });
    }

    synchronized private void registerUser(String email, String name, String password, String _id) {
        compositeDisposable.add(iMyService.registerUser(email, name, password, _id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JsonParser jsonParser = new JsonParser(response);
                        Utils.showToast(jsonParser.getDescription(), Login.this);
                        switch (jsonParser.getStatus()) {
                            case 202:
                            case 401:
                            case 405:
                                break;
                            default:
                                throw new RuntimeException("Status code: " + jsonParser.getStatus() + " error !");
                        }
                    }
                }));
    }

    synchronized private void loginUser(String email, String password) {
        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JsonParser jsonParser = new JsonParser(response);
                        username = jsonParser.getName();
                        userId = jsonParser.getId();
                        Utils.showToast(jsonParser.getDescription(), Login.this);
                        switch (jsonParser.getStatus()) {
                            case 200:
                                goToPage(StudentOperation.class, null);
                                break;
                            case 201:
                                goToPage(TeacherClass.class, jsonParser.getCourses());
                                break;
                            case 400:
                            case 402:
                                break;
                        }
                    }
                }));
    }

    private void goToPage(Class page, HashMap courseList) {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        if (courseList != null)
            intent.putExtra("courses", courseList);
        intent.setClass(Login.this, page);
        startActivity(intent);
    }
}