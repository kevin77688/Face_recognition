package org.ntut.faceRecognition.Retrofit;

import com.google.gson.JsonObject;

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

    //學生上傳圖片
    @Multipart
    @POST("studentUpload")
    Observable<String> studentUpload(
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image
    );

    @POST("uploadAttendanceList")
    @FormUrlEncoded
    Call<String> uploadAttendanceList(
            @Field("studentAttendantList") JsonObject jsonObject
    );

    //學生查看圖片
    @POST("studentCheckAvatar")
    @FormUrlEncoded
    Observable<ResponseBody> studentCheckAvatar(
            @Field("studentId") String studentId,
            @Field("index") int index
    );

    @POST("studentGetTotalAvatar")
    @FormUrlEncoded
    Observable<String> studentGetTotalAvatar(
            @Field("studentId") String studentId
    );

    @POST("studentDeleteAvatar")
    @FormUrlEncoded
    Observable<String> studentDeleteAvatar(
            @Field("studentId") String studentId,
            @Field("index") int index
    );

    @POST("teacherUpload")
    @Multipart
    Observable<String> teacherUpload(
            @Part MultipartBody.Part image,
            @Part("date") RequestBody date
    );

    @POST("studentSearchCourse")
    @FormUrlEncoded
    Observable<String> studentSearchCourse(
            @Field("courseId") String courseId,
            @Field("studentId") String studentId
    );

    @POST("studentAddCourse")
    @FormUrlEncoded
    Call<String> studentAddCourse(
            @Field("courseId") String courseId,
            @Field("studentId") String studentId
    );
}
