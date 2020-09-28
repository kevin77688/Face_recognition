package org.ntut.faceRecognition.Retrofit;

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
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("_id") String _id
    );

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("findUserClass")
    @FormUrlEncoded
    Observable<String> findClass(
            @Field("id") Integer id
    );

    @Multipart
    @POST("studentUpload")
    Observable<ResponseBody> studentUpload(
            // @Part("user_id") RequestBody id,
            @Part("full_name") RequestBody fullName,
            @Part MultipartBody.Part image
            // @Part("other") RequestBody other
    );
}
