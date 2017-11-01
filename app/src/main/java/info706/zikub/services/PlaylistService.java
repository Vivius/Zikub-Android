package info706.zikub.services;

import info706.zikub.models.PlayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface PlaylistService {
    @Headers("Accept: application/json")
    @GET("playlist/get")
    Call<PlayList> get(@Header("Authorization") String authorization);
}
