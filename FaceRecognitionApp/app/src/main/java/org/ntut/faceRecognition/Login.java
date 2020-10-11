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

import org.json.JSONArray;
import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    private IMyService iMyService;
    private TextView txt_create_account;
    private MaterialEditText edt_login_email, edt_login_password;
    private Button btn_login;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String username, userId;

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

                                registerUser(
                                        edt_register_email.getText().toString(),
                                        edt_register_name.getText().toString(),
                                        edt_register_password.getText().toString(),
                                        edt_register_id.getText().toString()
                                );
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
                    showToast(jsonParser.getDescription());
                    switch(jsonParser.getStatus()){
                        case 202:
                        case 401:
                            break;
                    }
                }}));
}

    synchronized private void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String response) throws Exception {
                                   JsonParser jsonParser = new JsonParser(response);
                                   userId = jsonParser.getId();
                                   username = jsonParser.getName();
                                   showToast(jsonParser.getDescription());
                                   switch (jsonParser.getStatus()){
                                       case 200:
                                           goToPage(StudentOperation.class);
                                           break;
                                       case 201:
                                           goToPage(TeacherOperation.class);
                                           break;
                                       case 400:
                                       case 402:
                                           break;
                                   }
//                        getClassDate("作業系統");
//                        getClassDate("實務專題(二)");
//                        getClassDate("財務管理");
//                        getClassDate("物件導向程式設計實習");
//                        getClassDate("體育");
//                        getClassDate("設計樣式");
//                        getClassDate("智慧財產權");
//                        getClassDate("人工智慧概論");
//                        getClassDate("雲端應用實務");
//                        getClassDate("職涯進擊講座");
//                        getClassDate("國際觀培養講座");

//                        if("\"Login student\"".equals(response)){
//                            Log.e("同學", userdata.getId());
//                            getStudentInformation(Integer.valueOf(userdata.getId()));
//
//                            goToPage(StudentOperation.class);
//                        }else if("\"Login teacher\"".equals(response)){
//                            getClassInformation(Integer.valueOf(userdata.getId()));
////                            Log.e("走囉", String.valueOf(userdata.class_information.size()));
//                            goToPage(TeacherClass.class);
//                        }

                               }}
                ));
    }



    private void goToPage(Class page) {
        Intent intent = new Intent();
        intent.setClass(this , page);
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showToast(String message){
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    //    synchronized private void findUser(String email) {
//        compositeDisposable.add(iMyService.findName(email)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        JSONObject jsonobj = new JSONObject(response);
//                        String name = jsonobj.getString("name");
//                        String id = jsonobj.getString("id");
//                        userdata.setName(name);
//                        userdata.setId(id);
//                    }
//                }));
//    }

//    synchronized public void getClassInformation(Integer id) {
//        compositeDisposable.add(iMyService.findClass(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
//                        Log.e("json", response);
//                        JSONObject jsonarr = new JSONObject(response);
//                        String jsonOb = jsonarr.getString("class");
//
//                        String[] names = jsonOb.replaceAll("\\[", "")
//                                        .replaceAll("\\]", "").split(",");
//                        ArrayList<String> name = new ArrayList<String>();
//                        for(Integer i =0; i<names.length;i++){
//                            name.add(names[i]);
//                            Log.e("getClassInformation", names[i]);
////                            getClassDate(names[i]);
//                        }
//
//                        userdata.setClassInformation(name);
//                    }
//                }));
//    }
//
//    synchronized public void getClassDate(String class_name) {
//        Log.e("getClassDate", class_name);
//        compositeDisposable.add(iMyService.findClassDate(class_name)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        JSONObject jsonarr = new JSONObject(response);
//                        String jsonOb = jsonarr.getString("date");
//                        String[] names = jsonOb.replaceAll("\\[", "")
//                                .replaceAll("\\]", "").split(",");
//                        Log.e("names", names[1]);
//                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
//                        userdata.setClassDate(jsonarr.getString("name"), names);
//                    }}));
//    }
//
//    synchronized public void getStudentInformation(Integer id) {
//        compositeDisposable.add(iMyService.findStudentClass(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
//                        Log.e("json", response);
//                        JSONObject jsonarr = new JSONObject(response);
//                        String jsonOb = jsonarr.getString("class");
//                        Log.e("getClassInformation", jsonOb);
//                        String[] names = jsonOb.replaceAll("\\[", "")
//                                .replaceAll("\\]", "").split(",");
//                        ArrayList<String> name = new ArrayList<String>();
//                        for(Integer i =0; i<names.length;i++){
//                            name.add(names[i]);
//                            Log.e("getClassInformation", names[i]);
////                            getClassDate(names[i]);
//                        }
//
//                        userdata.setClassInformation(name);
//                    }
//                }));
//    }

}