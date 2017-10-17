package info706.zikub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info706.zikub.adapters.MusicAdapter;
import info706.zikub.components.YoutubePlayer;
import info706.zikub.components.YoutubePlayerListener;
import info706.zikub.models.Music;
import info706.zikub.models.PlayList;
import info706.zikub.models.Setting;
import info706.zikub.models.User;
import info706.zikub.services.PlaylistService;
import info706.zikub.services.YoutubeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private YoutubePlayer youtubePlayer;
    private int playlistId;
    private List<Music> musics;

    private int selectedMusic = -1;

    public static final String PLAYLIST_ID_TAG = "PLAYLIST_ID";
    public static final String MUSIC_RANK_TAG = "MUSIC_RANK";

    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageView cover;
    private ListView playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log.d("OAUTH_TOKEN", User.getOauthToken(getApplicationContext()));

        playlist = (ListView) findViewById(R.id.playlist);
        youtubePlayer = new YoutubePlayer(this);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        cover = (ImageView) findViewById(R.id.cover);

        // Chargement de la playlist de l'utilisateur.
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Setting.API_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        PlaylistService service = retrofit.create(PlaylistService.class);
        Call<PlayList> caller = service.get(User.getOauthToken(getApplicationContext()));

        caller.enqueue(new Callback<PlayList>() {
            @Override
            public void onResponse(Call<PlayList> call, Response<PlayList> response) {
                playlistId = response.body().getId();
                musics = response.body().getMusics();

                Collections.sort(musics, new Comparator<Music>() {
                    @Override
                    public int compare(Music o1, Music o2) {
                        return  o1.getRank() > o2.getRank() ? 1 : -1;
                    }
                });

                ArrayAdapter adapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, musics);
                playlist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Impossible d'accéder au serveur", Toast.LENGTH_LONG).show();
            }
        });

        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MUSIC_RANK_TAG, position + 1);
                bundle.putInt(PLAYLIST_ID_TAG, playlistId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // TEST - Lancement de la lecture d'une musique Youtube dans l'application.
        // youtubePlayer.start("https://www.youtube.com/watch?v=_cB3HXVvm0g");

        youtubePlayer.setYoutubePlayerListener(new YoutubePlayerListener() {
            @Override
            public void onMusicBegin() {
                Log.i("BEGIN", "The music begin !");
            }
        });


        // Bontons du lecteur
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMusic == -1) {
                    selectedMusic = 0;
                    youtubePlayer.start(musics.get(selectedMusic).getUrl());
                    Picasso.with(getApplicationContext()).load(musics.get(selectedMusic).getCover()).resize(200,200).centerCrop().into(cover);
                }

                if(youtubePlayer.isPlaying()) {
                    btnPlayPause.setImageResource(R.drawable.ic_btnplay);
                    youtubePlayer.pause();
                } else {
                    btnPlayPause.setImageResource(R.drawable.ic_btnpause);
                    youtubePlayer.play();
                }
            }
        });

        // start musique précédente
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMusic > 0) {
                    selectedMusic--;
                    youtubePlayer.start(musics.get(selectedMusic).getUrl());
                    Picasso.with(getApplicationContext()).load(musics.get(selectedMusic).getCover()).resize(200,200).centerCrop().into(cover);
                    btnPlayPause.setImageResource(R.drawable.ic_btnpause);
                }
            }
        });

        // start musique suivante
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMusic < 5) {
                    selectedMusic++;
                    youtubePlayer.start(musics.get(selectedMusic).getUrl());
                    Picasso.with(getApplicationContext()).load(musics.get(selectedMusic).getCover()).resize(200,200).centerCrop().into(cover);
                    btnPlayPause.setImageResource(R.drawable.ic_btnpause);
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop le lecteur si on change d'activité.
        youtubePlayer.pause();
        btnPlayPause.setImageResource(R.drawable.ic_btnplay);
    }
}
