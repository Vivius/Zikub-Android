package info706.zikub.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    // Returns a token if authentificated.
    @FormUrlEncoded
    @POST("login")
    Call<String> login(@Field("email") String email, @Field("password") String password);

    // Returns a token if registered.
    @FormUrlEncoded
    @POST("register")
    Call<String> register();
}
