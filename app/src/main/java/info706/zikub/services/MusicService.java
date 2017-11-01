package info706.zikub.services;

import java.util.List;

import info706.zikub.models.Music;
import info706.zikub.models.User;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MusicService {
    @Headers("Accept: application/json")
    @DELETE("musics/{id}")
    Call<String> delete(@Header("Authorization") String authorization, @Path("id") int id);
}
