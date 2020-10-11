package org.ntut.faceRecognition.Retrofit;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("_id") String _id
    );

    @POST("getRollCall")
    @FormUrlEncoded
    Observable<String> getRollCall(
            @Field("class_data") String class_data,
            @Field("class_name") String class_name
    );

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("findStudent")
    @FormUrlEncoded
    Observable<String> findStudent(
            @Field("class_name") String class_name
    );

    @POST("findUserName")
    @FormUrlEncoded
    Observable<String> findName(
            @Field("email") String email
    );

    @POST("findUserClass")
    @FormUrlEncoded
    Observable<String> findClass(
            @Field("id") String id
    );

    @POST("findStudentClass")
    @FormUrlEncoded
    Observable<String> findStudentClass(
            @Field("id") String id
    );

    @POST("findRollCall")
    @FormUrlEncoded
    Observable<String> findRollCall(
            @Field("name") String name
    );

    @POST("findUserClassDate")
    @FormUrlEncoded
    Observable<String> findClassDate(
            @Field("class_name") String class_name
    );

    @Multipart
    @POST("studentUpload")
    Observable<ResponseBody> studentUpload(
            // @Part("user_id") RequestBody id,
            @Part("full_name") RequestBody fullName,
            @Part MultipartBody.Part image
            // @Part("other") RequestBody other
    );

    @POST("rollCallUpdate")
    @FormUrlEncoded
    Observable<String> rollCallUpdate(
            @Field("class_data") String class_data,
            @Field("class_name") String class_name,
            @Field("student_data") String[] student_data,
            @Field("roll_call_data")  ArrayList<Integer>  roll_call_data
    );
}
