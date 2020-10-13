package org.ntut.faceRecognition;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class TeacherOperationManualCheckAttendance extends AppCompatActivity {

    ArrayList<CheckBox> cb_listb = new ArrayList<CheckBox>();
    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String courseName, courseDate, courseId;
    private ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_manual_check_attendance);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseName = extras.getString("courseName");
            courseDate = extras.getString("courseDate");
            courseId = extras.getString("courseId");
        } else
            throw new RuntimeException("Transfer extra between activity failed");
        students = new ArrayList<>();
        getStudentList();
    }

    synchronized private void getStudentList() {
        compositeDisposable.add(iMyService.teacherGetCourseAttendance(courseId, courseDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        
                        Iterator<String> studentIds = jsonObject.keys();
                        while (studentIds.hasNext()) {
                            String studentId = studentIds.next();
                            String studentName = jsonObject.getJSONObject(studentId).getString("name");
                            String studentAttendance = jsonObject.getJSONObject(studentId).getString("attendance");
                            students.add(new Student(studentId, studentName, studentAttendance));
                        }
                        setButton();
                    }
                }));
    }

    private void setButton() {
        LinearLayout mainLinerLayout = (LinearLayout) findViewById(R.id.roll_call_layout);
        LinearLayout show_top_linear = (LinearLayout) findViewById(R.id.show_top_linear);

        ArrayList<String> titles = new ArrayList<>(
                Arrays.asList("姓名", "準時", "遲到", "缺席"));
        for (String title : titles) {
            TextView textView = new TextView(this);
            textView.setTextSize(30);
            textView.setWidth(350);   //設定寬度
            textView.setHeight(120);
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            show_top_linear.addView(textView);
        }


//        int studentCount = 0;
        for (final Student student : students) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Set student name
            TextView textView = new TextView(this);
            textView.setTextSize(30);
            textView.setWidth(330);   //設定寬度
            textView.setHeight(120);
            textView.setGravity(Gravity.CENTER);
            textView.setText(student.getName());
            linearLayout.addView(textView);

            ArrayList<CheckBox> checkBoxesColumn = new ArrayList();
            // Set checkbox
            for (int i = 0; i < 3; i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setTextSize(30);
                checkBox.setWidth(230);   //設定寬度
                checkBox.setHeight(150);
                checkBox.setGravity(Gravity.CENTER);
                checkBox.setChecked(false);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Student st : students) {
                            if (st.setAttendanceCheckBoxChecked((CheckBox) v)) ;
                            return;
                        }
                        throw new RuntimeException("Checkbox find parent failed !");
                    }
                });
                checkBoxesColumn.add(checkBox);
                linearLayout.addView(checkBox);
            }
            if (student.getAttandanceStatus() != -1)
                checkBoxesColumn.get(student.getAttandanceStatus()).setChecked(true);
            student.setAttendanceView(checkBoxesColumn);
            mainLinerLayout.addView(linearLayout);
        }
    }

    public void _finish(View v) {
        for (Student student : students)
            if (!student.checkAttendanceSet()) {
                Toast.makeText(TeacherOperationManualCheckAttendance.this, "還有學生未點到名", Toast.LENGTH_SHORT).show();
                return;
            }
        updateRecord();
        finish();
    }

    private void updateRecord() {
        // TODO update record need to reformat
//        for (ArrayList<CheckBox> checkBoxArrayList : checkBoxesRow){
//
//        }
//
//
//
//        ArrayList<Integer> roll = new ArrayList<Integer>();
//        for(int i=0;i<studentName.length;i++){
//            for(int j=i*3;j<i*3+3;j++){
//                if(cb_listb.get(j).isChecked()){
//                    roll.add(j%3);
//                    // 0 準時
//                    // 1 遲到
//                    // 2 缺席
//                }
//            }
//        }
//
////        Log.e("roll_call", roll_call.get(studentName[0]));
////        Log.e("roll_call", roll_call.get(studentName[1]));
//        Log.e("roll_call", "roll_call.get(studentName[2])");
//        compositeDisposable.add(iMyService.rollCallUpdate(classDate, className, studentName, roll)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                    }
//                }));
    }
}