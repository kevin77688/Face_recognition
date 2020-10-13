package org.ntut.faceRecognition.Retrofit;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMyService {

    // 註冊
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("_id") String _id
    );

    // 登入
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    // 學生查詢出缺席
    @POST("studentCheckAttendance")
    @FormUrlEncoded
    Observable<String> studentCheckAttendance(
            @Field("studentId") String studentId
    );

    // 教授點課程時回傳日期
    @POST("teacherFindCourseDate")
    @FormUrlEncoded
    Observable<String> findCourseDate(
            @Field("courseId") String courseId
    );

    // 教授點擊課程時間回傳點名單
    @POST("teacherGetCourseAttendance")
    @FormUrlEncoded
    Observable<String> teacherGetCourseAttendance(
            @Field("courseId") String courseId,
            @Field("courseDate") String courseDate
    );

    @POST("findStudentClass")
    @FormUrlEncoded
    Observable<String> findStudentClass(
            @Field("id") String id
    );


    @Multipart
    @POST("studentUpload")
    Observable<ResponseBody> studentUpload(
            // @Part("user_id") RequestBody id,
            @Part("full_name") RequestBody fullName,
            @Part MultipartBody.Part image
            // @Part("other") RequestBody other
    );

    @POST
    @FormUrlEncoded
    Observable<String> attendanceUpdate(
            @Field("courseId") String class_data,
            @Field("student_data") String[] student_data,
            @Field("roll_call_data") ArrayList<Integer> roll_call_data
    );

    @POST("rollCallUpdate")
    @FormUrlEncoded
    Observable<String> rollCallUpdate(
            @Field("class_data") String class_data,
            @Field("class_name") String class_name,
            @Field("student_data") String[] student_data,
            @Field("roll_call_data") ArrayList<Integer> roll_call_data
    );

    @POST("uploadAttendanceList")
    @FormUrlEncoded
    Call uploadAttendanceList(
            @Field("studentAttendantList") JSONObject jsonObject
    );
}
