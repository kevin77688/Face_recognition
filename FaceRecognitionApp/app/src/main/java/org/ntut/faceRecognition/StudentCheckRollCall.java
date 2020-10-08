package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class StudentCheckRollCall extends AppCompatActivity {

    ArrayList<String> student_fake_name = new ArrayList<String>();
    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String className,classDate, take_picture= null;
    private String[] studentName;
    ArrayList<CheckBox> cb_listb = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_roll_call);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    }

    public void _return(View v) {
        StudentCheckRollCall.this.finish();
    }
}