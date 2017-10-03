package info706.zikub.services;

import java.util.List;

import info706.zikub.models.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("users")
    Call<List<User>> findAll();
}
