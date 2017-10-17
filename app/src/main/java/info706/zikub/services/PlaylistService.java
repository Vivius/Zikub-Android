package info706.zikub.services;

import info706.zikub.models.Music;
import info706.zikub.models.PlayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;

public interface PlaylistService {

    @Headers("Accept: application/json")
    @GET("playlist/get")
    Call<PlayList> get(@Header("Authorization") String authorization);

    @Headers("Accept: application/json")
    @PATCH("playlist/update/list")
    Call<Music> updateList(@Header("Authorization") String authorization, @Body Music music);
}
