package hr.fer.rpp.classificationapp.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hr.fer.rpp.classificationapp.models.Image;
import hr.fer.rpp.classificationapp.models.Record;
import hr.fer.rpp.classificationapp.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    //@FormUrlEncoded
    @POST("register")
    Call<ResponseBody> createUser(
            @Body HashMap<String, String> body
    );

    @POST("perform-login")
    Call<ResponseBody> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("user/loggedin")
    Call<User> getLoggedUser(
            @Header("Authorization") String authHeader
    );

    @GET("logout")
    Call<ResponseBody> logout(
            @Header("Authorization") String authHeader
    );

    @Multipart
    @POST("record/create")
    Call<Record> uploadImage(
            //@Header("Authorization") String authHeader,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part image
    );

    @GET("record/get")
    Call<Set<Record>> getRecords(
            @Query("username") String username
    );

    /*
    @GET("record/get/id")
    Call<Image> getRecordById(
            @Query("id") Long id
    );

            //@HeaderMap Map<String, String> token,

    @POST("record/create")
    Call<ResponseBody> uploadImage(
            @Header("Authorization") String authHeader,
            @Body HashMap<String, byte[]> body
    );
     */

}