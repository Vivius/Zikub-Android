package info706.zikub.services;

import info706.zikub.models.Music;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MusicService {
    @Headers("Accept: application/json")
    @DELETE("musics/{id}")
    Call<String> delete(@Header("Authorization") String authorization, @Path("id") int id);

    @Headers("Accept: application/json")
    @POST("musics")
    Call<Music> post(@Header("Authorization") String authorization, @Body Music music);
}
