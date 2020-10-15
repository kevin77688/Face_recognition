package org.ntut.faceRecognition.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Utility.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class StudentCheckAvatar extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String userId, username, imageName;
    private Button returnButton, backButton, nextButton, deleteButton;
    private TextView title;
    private ImageView imageView;
    private int totalAvatar, currentAvatar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_avatar);
        setupConnection();
        findView();
        getExtras();
        getTotalAvatar();
        setNextButton();
        setBackButton();
        setDeleteButton();
        returnButton();
        update();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = getIntent().getStringExtra("username");
            userId = getIntent().getStringExtra("userId");
        } else
            throw new RuntimeException("Passing extras between activity failed !");
    }

    private void findView() {
        returnButton = findViewById(R.id.return_button);
        title = findViewById(R.id.title_text);
        deleteButton = findViewById(R.id.deleterButton);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        imageView = findViewById(R.id.IdProf);
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void getTotalAvatar(){
        compositeDisposable.add(iMyService.studentGetTotalAvatar(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        totalAvatar = Integer.parseInt(jsonObject.getString("amount"));
                        Log.i("totalAvatar", String.valueOf(totalAvatar));
                        update();
                        getAvatar();
                    }
                }));
    }

    private void getAvatar(){
        if (totalAvatar != 0 || totalAvatar > currentAvatar){
            compositeDisposable.add(iMyService.studentCheckAvatar(userId, currentAvatar)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            Bitmap bmp = BitmapFactory.decodeStream(responseBody.byteStream());
                            imageView.setImageBitmap(bmp);
                        }
                    }));
        }
    }

    private void setDeleteButton(){
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (totalAvatar != 0 || totalAvatar > currentAvatar) {
                    compositeDisposable.add(iMyService.studentDeleteAvatar(userId, currentAvatar)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String responseBody) throws Exception {
                                    totalAvatar--;
                                    if (currentAvatar == totalAvatar && totalAvatar != 0){
                                        currentAvatar--;
                                    }
                                    update();
                                    imageView.setImageResource(0);
                                    getAvatar();
                                }
                            }));
                }
            }
        });
    }

    private void setNextButton(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Student upload View entry point
                currentAvatar++;
                getAvatar();
                update();
            }
        });
    }

    private void setBackButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Student upload View entry point
                currentAvatar--;
                getAvatar();
                update();
            }
        });
    }

    private void update(){
        if (currentAvatar >= (totalAvatar - 1)){
            nextButton.setVisibility(View.INVISIBLE);
        }
        else {
            nextButton.setVisibility(View.VISIBLE);
        }

        if (currentAvatar == 0){
            backButton.setVisibility(View.INVISIBLE);
        }
        else{
            backButton.setVisibility(View.VISIBLE);
        }
        setTitle();
    }

    private void returnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(StudentCheckAvatar.this));
    }

    private void setTitle() {
        if (totalAvatar == 0){
            title.setText("\n歡迎" + username + "學生" + "\n無照片");
        } else {
            title.setText("\n歡迎" + username + "學生" + "\n照片" + (currentAvatar + 1) + "/" + totalAvatar);
        }
    }

}