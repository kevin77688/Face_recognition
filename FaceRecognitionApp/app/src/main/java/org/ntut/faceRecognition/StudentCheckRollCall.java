package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ntut.faceRecognition.Retrofit.IMyService;
import org.ntut.faceRecognition.Retrofit.RetrofitClient;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class StudentCheckRollCall extends AppCompatActivity {

    private IMyService iMyService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<String> className, classDate, classStatus;
//    private String[] className,classDate, classStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_roll_call);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

//        findRollCall(userdata.getName());
    }

    synchronized private void findRollCall(String name) {
        compositeDisposable.add(iMyService.findRollCall(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        // TODO classes need to construct as jsonObject
//                        JSONObject jsonObject = new JSONObject(response);
//                        int status = Integer.parseInt(jsonObject.getString("status"));
//
//                        switch(status){
//                            case 203:
//                                break;
//                            case 403:
//                                break;
//                        }
//                        JSONObject classes = jsonObject.getJSONObject("classes");
//                        String _class = jsonobj.getString("class");
//                        String date = jsonobj.getString("date");
//                        String status = jsonobj.getString("status");
//                        className = _class.replaceAll("\\[", "")
//                                .replaceAll("\\]", "").split(",");
//                        classDate = date.replaceAll("\\[", "")
//                                .replaceAll("\\]", "").split(",");
//                        classStatus = status.replaceAll("\\[", "")
//                                .replaceAll("\\]", "").split(",");
//                        setButton();
                    }
                }));
    }
//    private void setButton() {
//        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.roll_call_layout);
//        LinearLayout top = new LinearLayout(this);
//        top.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout show_top_linear = (LinearLayout) this.findViewById(R.id.show_top_linear);
//
//        TextView tx1= new TextView(this);
//        tx1.setText("課程");
//        TextView tx2= new TextView(this);
//        tx2.setText("時間");
//        TextView tx3= new TextView(this);
//        tx3.setText("出席");
//        ArrayList<TextView> textViews = new ArrayList<TextView>(
//                Arrays.asList(tx1, tx2, tx3));
//        for (TextView tx : textViews){
//            tx.setTextSize(30);
//            tx.setWidth(350);   //設定寬度
//            tx.setHeight(120);
//            tx.setGravity(Gravity.CENTER);
//            show_top_linear.addView(tx);
//        }
//
//        for (int i = 0; i < className.length ; i++) {
//            LinearLayout li = new LinearLayout(this);
//            li.setOrientation(LinearLayout.HORIZONTAL);
//
//            TextView tv1= new TextView(this);
//            tx1.setText(className[i].replace("\"", ""));
//            TextView tv2= new TextView(this);
//            tx2.setText(classDate[i].replace("\"", ""));
//            TextView tv3= new TextView(this);
//            tx3.setText("出席");
//            ArrayList<TextView> textViews = new ArrayList<TextView>(
//                    Arrays.asList(tx1, tx2, tx3));
//            for (TextView tx : textViews){
//                tx.setTextSize(30);
//                tx.setWidth(350);   //設定寬度
//                tx.setHeight(120);
//                tx.setGravity(Gravity.CENTER);
//                show_top_linear.addView(tx);
//            }
//            TextView tv1 =new TextView(this);
//            tv1.setTextSize(20);
//            tv1.setWidth(350);   //設定寬度
//            tv1.setHeight(120);
//            tv1.setGravity(Gravity.CENTER);
//            tv1.setText();
////
//            TextView tv2 = new TextView(this);
//            tv2.setTextSize(20);
//            tv2.setWidth(350);   //設定寬度
//            tv2.setHeight(150);
//            tv2.setGravity(Gravity.CENTER);
//            tv2.setText();
////
//            TextView tv3 = new TextView(this);
//            tv3.setTextSize(20);
//            tv3.setWidth(350);   //設定寬度
//            tv3.setHeight(150);
//            tv3.setGravity(Gravity.CENTER);
//            int count = Integer.parseInt(classStatus[i].replace("\"", ""));
//            if(count == 0)
//                tv3.setText("準時");
//            else if(count == 1)
//                tv3.setText("遲到");
//            else
//                tv3.setText("缺席");
//
//            li.addView(tv1);
//            li.addView(tv2);
//            li.addView(tv3);
////
//            mainLinerLayout.addView(li);
//        }
//    }
//    public void _return(View v) {
//        StudentCheckRollCall.this.finish();
//    }
}