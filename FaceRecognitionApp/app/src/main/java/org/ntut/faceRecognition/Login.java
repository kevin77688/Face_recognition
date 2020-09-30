package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    public static String user_email;
    private IMyService iMyService;
    private TextView txt_create_account;
    private MaterialEditText edt_login_email, edt_login_password;
    private Button btn_login;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init Services
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // Init view
        edt_login_email = findViewById(R.id.edt_email);
        edt_login_password = findViewById(R.id.edt_password);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginUser(edt_login_email.getText().toString(), edt_login_password.getText().toString());
            }
        });

        txt_create_account = findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(Login.this)
                        .inflate(R.layout.register_layout, null);

                new MaterialStyledDialog.Builder(Login.this)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
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
                                final boolean[] count = {false};
                                MaterialEditText edt_register_email = register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_name = register_layout.findViewById(R.id.edt_name);
                                MaterialEditText edt_register_password = register_layout.findViewById(R.id.edt_password);
                                MaterialEditText edt_register_id = register_layout.findViewById(R.id.edt_id);
                                String edt_register_identification;

                                if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    Toast.makeText(Login.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                                    Toast.makeText(Login.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                                    Toast.makeText(Login.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(edt_register_id.getText().toString())) {
                                    Toast.makeText(Login.this, "ID cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                edt_register_identification = edt_register_name.getText().toString();
                                if("a".equals(edt_register_identification.substring(0, 1)))
                                    edt_register_identification = "teacher";
                                else
                                    edt_register_identification = "student";

                                registerUser(
                                        edt_register_email.getText().toString(),
                                        edt_register_name.getText().toString(),
                                        edt_register_password.getText().toString(),
                                        edt_register_identification,
                                        edt_register_id.getText().toString()
                                );
                            }
                        }).show();
            }
        });

    }

    private void findUser(String email) {
        compositeDisposable.add(iMyService.findName(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();

                        JSONObject jsonobj = new JSONObject(response);
                        String name = jsonobj.getString("name");
//                        Log.e("name", name);
                        String id = jsonobj.getString("id");
                        userdata.setName(name);
                        userdata.setId(id);
                    }
                }));
    }

    private void registerUser(String email, String name, String password, String identification, String _id) {
        compositeDisposable.add(iMyService.registerUser(email, name, password, identification, _id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(Login.this, "" + response, Toast.LENGTH_SHORT).show();
                    }
                }));
        goToPage(StudentOperation.class);
    }

    private void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //交使用者資料儲存成全域變數
        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
        userdata.setEmail(email);
        userdata.setPassword(password);
        findUser(email);

        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(Login.this, "" + response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", response);
                        if("\"Login student\"".equals(response)){
                            goToPage(StudentOperation.class);
                        }else if("\"Login teacher\"".equals(response)){
                            goToPage(TeacherClass.class);
                        }

                    }
                }));
    }
    private void goToPage(Class page) {
        Intent intent = new Intent();
        intent.setClass(this , page);
        startActivity(intent);
    }
}