package info706.zikub.services;

import java.util.List;

import info706.zikub.models.Music;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface YoutubeService {

    @Headers("Accept: application/json")
    @GET("youtube/search/{query}")
    Call<List<Music>> search(@Header("Authorization") String authorization, @Path("query") String query);
}
