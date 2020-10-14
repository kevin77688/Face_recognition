package org.ntut.faceRecognition.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.ntut.faceRecognition.Utility.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class StudentCheckAvatar extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String userId, username;
    private Button returnButton, backButton, nextButton, deleteButton;
    private TextView title;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_avatar);
        setupConnection();
        getExtras();
        getAvatar();
        findView();
        returnButton();
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

    private void findView() {
        returnButton = findViewById(R.id.return_button);
        title = findViewById(R.id.title_text);
        deleteButton = findViewById(R.id.deleterButton);
        imageView = findViewById(R.id.IdProf);
    }

    private void setupConnection() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    private void getAvatar(){

        RequestBody rbUserId = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        compositeDisposable.add(iMyService.studentCheckAvatar(userId)
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

    private void returnButton() {
        returnButton.setOnClickListener(Utils.setReturnButton(StudentCheckAvatar.this));
    }

    private void setTitle() {
        title.setText("\n歡迎" + username + "學生");
    }

}