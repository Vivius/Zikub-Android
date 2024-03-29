package info706.zikub.services;

import info706.zikub.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface UserService {
    @Headers("Accept: application/json")
    @GET("current")
    Call<User> getCurrent(@Header("Authorization") String authorization);
}
