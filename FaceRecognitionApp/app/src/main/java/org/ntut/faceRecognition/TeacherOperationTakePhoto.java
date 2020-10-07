package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TeacherOperationTakePhoto extends AppCompatActivity {

    ArrayList<String> student_fake_name = new ArrayList<String>();
    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String className,classDate, take_picture= null;
    private String[] studentName;
    ArrayList<CheckBox> cb_listb = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_take_photo);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            classDate = extras.getString("class_date");
            className = extras.getString("class_name");
            take_picture = extras.getString("take_picture");
            Log.e("aaa",take_picture);
        }
        else
            throw new RuntimeException("Login error ! Cannot find userName");
        if(take_picture.equals("false")){
            Button bt = (Button)findViewById(R.id.button_take_photo);
            bt.setVisibility(View.INVISIBLE);
        }

        findStudent(className);
    }

    synchronized private void findStudent(String class_name) {
        compositeDisposable.add(iMyService.findStudent(class_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonobj = new JSONObject(response);
                        String name = jsonobj.getString("name");
                        Log.e("name", name);
                        String[] items = name.replaceAll("\\[", "")
                                         .replaceAll("\\]", "").split(",");
                        String[] results = new String[items.length];
                        for (int i = 0; i < items.length; i++)
                            results[i] = items[i];
                        Log.e("name", results[0]);
                        Log.e("name", results[1]);

                        studentName = results;
                        setButton();
                    }
                }));
    }
    public void setButton() {


        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.roll_call_layout);
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout show_top_linear = (LinearLayout) this.findViewById(R.id.show_top_linear);

        TextView tx1= new TextView(this);
        tx1.setTextSize(30);
        tx1.setWidth(350);   //設定寬度
        tx1.setHeight(120);
        tx1.setGravity(Gravity.CENTER);
        tx1.setText("姓名");

        TextView tx2= new TextView(this);
        tx2.setTextSize(30);
        tx2.setWidth(235);   //設定寬度
        tx2.setHeight(120);
        tx2.setGravity(Gravity.CENTER);
        tx2.setText("準時");

        TextView tx3= new TextView(this);
        tx3.setTextSize(30);
        tx3.setWidth(235);   //設定寬度
        tx3.setHeight(120);
        tx3.setGravity(Gravity.CENTER);
        tx3.setText("遲到");

        TextView tx4= new TextView(this);
        tx4.setTextSize(30);
        tx4.setWidth(235);   //設定寬度
        tx4.setHeight(120);
        tx4.setGravity(Gravity.CENTER);
        tx4.setText("缺席");
        show_top_linear.addView(tx1);
        show_top_linear.addView(tx2);
        show_top_linear.addView(tx3);
        show_top_linear.addView(tx4);
        Log.e("ne", studentName[0]);

        for (int i = 1;i<=studentName.length;i++){
            LinearLayout li = new LinearLayout(this);
            li.setOrientation(LinearLayout.HORIZONTAL);

            TextView textview=new TextView(this);
            textview.setTextSize(30);
            textview.setWidth(330);   //設定寬度
            textview.setHeight(120);
            textview.setGravity(Gravity.CENTER);
            textview.setText(studentName[i-1].replace("\"", ""));

            CheckBox bt1 = new CheckBox(this);
            bt1.setTextSize(30);
            bt1.setWidth(230);   //設定寬度
            bt1.setHeight(150);
            bt1.setGravity(Gravity.CENTER);
            bt1.setText("+");
            bt1.setId((i*3-2));

            CheckBox bt2 = new CheckBox(this);
            bt2.setTextSize(30);
            bt2.setWidth(230);   //設定寬度
            bt2.setHeight(150);
            bt2.setGravity(Gravity.CENTER);
            bt2.setText("-");
            bt2.setId((i*3-1));

            CheckBox bt3 = new CheckBox(this);
            bt3.setTextSize(30);
            bt3.setWidth(230);   //設定寬度
            bt3.setHeight(150);
            bt3.setGravity(Gravity.CENTER);
            bt3.setText("X");
            bt3.setId((i*3));

            cb_listb.add(bt1);
            cb_listb.add(bt2);
            cb_listb.add(bt3);

            li.addView(textview);
            li.addView(bt1);
            li.addView(bt2);
            li.addView(bt3);

            mainLinerLayout.addView(li);
        }
        for(int i=0;i<cb_listb.size();i++){
            CheckBox cb = cb_listb.get(i);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox delBtn =  (CheckBox)v; //在new 出所按下的按鈕
                    int id = delBtn.getId();//獲取被點擊的按鈕的id
                    Log.e("ss",String.valueOf(id));
                    if(delBtn.isChecked()){
                        check((id));
                    }
                }
            });
        }
    }
    public void check(Integer i) {
        switch (i%3){
            case 0:
                cb_listb.get(i-1-1).setChecked(false);
                cb_listb.get(i-2-1).setChecked(false);
                break;
            case 1:
                cb_listb.get(i+1-1).setChecked(false);
                cb_listb.get(i+2-1).setChecked(false);
                break;
            case 2:
                cb_listb.get(i+1-1).setChecked(false);
                cb_listb.get(i-1-1).setChecked(false);
                break;
            default:
                break;
        }
    }
    public void _finish(View v) {
        Integer count=0;
        for(int i=0;i<cb_listb.size();i++){
            if(cb_listb.get(i).isChecked()){
                count+=1;
            }
        }
        if(count==studentName.length){
            updateRecord();
            TeacherOperationTakePhoto.this.finish();
        }else{
            Toast.makeText(TeacherOperationTakePhoto.this, "還有學生未點到名" , Toast.LENGTH_SHORT).show();
        }
    }
    private  void  updateRecord(){
        ArrayList<Integer> roll = new ArrayList<Integer>();
        for(int i=0;i<studentName.length;i++){
            for(int j=i*3;j<i*3+3;j++){
                if(cb_listb.get(j).isChecked()){
                    roll.add(j%3);
                    // 0 準時
                    // 1 遲到
                    // 2 缺席
                }
            }
        }

//        Log.e("roll_call", roll_call.get(studentName[0]));
//        Log.e("roll_call", roll_call.get(studentName[1]));
        Log.e("roll_call", "roll_call.get(studentName[2])");
        compositeDisposable.add(iMyService.rollCallUpdate(classDate, className, studentName, roll)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
//                        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
//
//                        JSONObject jsonobj = new JSONObject(response);
//                        String name = jsonobj.getString("name");
////                        Log.e("name", name);
//                        String id = jsonobj.getString("id");
//                        userdata.setName(name);
//                        userdata.setId(id);
                    }
                }));
    }

}