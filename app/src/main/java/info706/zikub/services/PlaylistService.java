package info706.zikub.services;

import info706.zikub.models.PlayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlaylistService {
    @Headers("Accept: application/json")
    @GET("playlists/current")
    Call<PlayList> getCurrent(@Header("Authorization") String authorization);

    @Headers("Accept: application/json")
    @GET("playlists/{id}")
    Call<PlayList> get(@Header("Authorization") String authorization, @Path("id") int id);

    @Headers("Accept: application/json")
    @PUT("playlists/{id}")
    Call<PlayList> update(@Header("Authorization") String authorization, @Path("id") int id, @Body PlayList playList);

    @Headers("Accept: application/json")
    @POST("playlists")
    Call<PlayList> create(@Header("Authorization") String authorization, @Body PlayList playList);
}
