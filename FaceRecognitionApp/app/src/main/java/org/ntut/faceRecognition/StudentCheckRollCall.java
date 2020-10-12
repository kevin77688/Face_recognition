package org.ntut.faceRecognition;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import retrofit2.Retrofit;

public class StudentCheckRollCall extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_roll_call);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        courses = new ArrayList<>();
        findRollCall(getIntent().getStringExtra("userId"));
    }

    synchronized private void findRollCall(String userId) {
        compositeDisposable.add(iMyService.studentCheckAttendance(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        JSONObject jsonObject = new JSONObject(response);
                        int status = Integer.parseInt(jsonObject.getString("status"));

                        switch (status) {
                            case 203:
                                JSONObject classes = jsonObject.getJSONObject("classes");
                                Iterator<String> iter_class = classes.keys();
                                while (iter_class.hasNext()) {
                                    JSONObject oneClass = (JSONObject) classes.get(iter_class.next());
                                    String className = oneClass.getString("name");
                                    String classDate = oneClass.getString("date");
                                    String classAttendance = oneClass.getString("attendance");
                                    courses.add(new Course(className, classDate, classAttendance));
                                }
                                setButton();
                                break;
                            case 403:
                                break;
                        }
                    }
                }));
    }

    private void setButton() {
        LinearLayout mainLinerLayout = this.findViewById(R.id.roll_call_layout);
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout show_top_linear = this.findViewById(R.id.show_top_linear);

        TextView tx1 = new TextView(this);
        tx1.setText("課程");
        TextView tx2 = new TextView(this);
        tx2.setText("時間");
        TextView tx3 = new TextView(this);
        tx3.setText("出席");
        ArrayList<TextView> textViews = new ArrayList<TextView>(
                Arrays.asList(tx1, tx2, tx3));
        for (TextView tx : textViews) {
            tx.setTextSize(30);
            tx.setWidth(350);   //設定寬度
            tx.setHeight(120);
            tx.setGravity(Gravity.CENTER);
            show_top_linear.addView(tx);
        }

        for (Course course : courses) {
            LinearLayout li = new LinearLayout(this);
            li.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1 = new TextView(this);
            tv1.setText(course.getName());
            TextView tv2 = new TextView(this);
            tv2.setText(course.getDate());
            TextView tv3 = new TextView(this);
            tv3.setText(course.getAttendance());
            ArrayList<TextView> tvs = new ArrayList<TextView>(
                    Arrays.asList(tx1, tx2, tx3));
            for (TextView tv : tvs) {
                tv.setTextSize(20);
                tv.setWidth(350);   //設定寬度
                tv.setHeight(150);
                tv.setGravity(Gravity.CENTER);
                li.addView(tv);
            }
            mainLinerLayout.addView(li);
        }

    }
}