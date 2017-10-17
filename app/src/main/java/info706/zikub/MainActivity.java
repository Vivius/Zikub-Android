package info706.zikub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private YoutubePlayer youtubePlayer;
    private PlayList playList;
    private Music selectedMusic = null;
    private int selectedIndex = 0;

    public static final String PLAYLIST_ID_TAG = "PLAYLIST_ID";
    public static final String MUSIC_RANK_TAG = "MUSIC_RANK";

    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageView cover;
    private ListView listView;
    private ViewSwitcher playerViewSwitcher;

    public MainActivity() {
        playList = new PlayList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log.d("OAUTH_TOKEN", User.getOauthToken(getApplicationContext()));

        listView = (ListView) findViewById(R.id.playlist);
        youtubePlayer = new YoutubePlayer(this);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        cover = (ImageView) findViewById(R.id.cover);
        playerViewSwitcher = (ViewSwitcher) findViewById(R.id.playerViewSwitcher);

        playerViewSwitcher.reset();

        // Chargement de la playList de l'utilisateur.
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Setting.API_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        PlaylistService service = retrofit.create(PlaylistService.class);
        Call<PlayList> caller = service.get(User.getOauthToken(getApplicationContext()));

        caller.enqueue(new Callback<PlayList>() {
            @Override
            public void onResponse(Call<PlayList> call, Response<PlayList> response) {
                playList = response.body();

                // Tri de la playlist en fonction des rank.
                Collections.sort(playList.getMusics(), new Comparator<Music>() {
                    @Override
                    public int compare(Music o1, Music o2) {
                        return  o1.getRank() > o2.getRank() ? 1 : -1;
                    }
                });

                ArrayAdapter adapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, playList.getMusics());
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Impossible d'accéder au serveur", Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MUSIC_RANK_TAG, position + 1);
                bundle.putInt(PLAYLIST_ID_TAG, playList.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Evénement appelé lorque la musique est prête (que la lecture commence).
        // Permet d'arrêter l'animation loader.
        youtubePlayer.setYoutubePlayerListener(new YoutubePlayerListener() {
            @Override
            public void onMusicBegin() {
                Log.i("BEGIN", "The music begin !");
                playerViewSwitcher.setDisplayedChild(1);
            }
        });


        // Bontons du lecteur

        // Bouton Play / Pause
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMusic == null)
                    startMusicByIndex(0);

                if(youtubePlayer.isPlaying()) {
                    btnPlayPause.setImageResource(R.drawable.ic_btnplay);
                    youtubePlayer.pause();
                } else {
                    btnPlayPause.setImageResource(R.drawable.ic_btnpause);
                    youtubePlayer.play();
                }
            }
        });

        // Bouton musique suivante
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMusicByIndex(selectedIndex - 1);
            }
        });

        // Bouton musique précédente
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMusicByIndex(selectedIndex + 1);
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

    /**
     * Permet de changer de musique en spécifiant son index (rank).
     * Met à jour l'ensemble de l'interface.
     * Ne fait rien si l'index spécifié n'existe pas.
     *
     * @param index int
     */
    private void startMusicByIndex(int index) {
        if(index >= 0 && index < playList.getMusics().size()) {
            selectedIndex = index;
            selectedMusic = playList.getMusics().get(index);
            youtubePlayer.start(selectedMusic.getUrl());
            Picasso.with(getApplicationContext()).load(selectedMusic.getCover()).resize(200,200).centerCrop().into(cover);
            btnPlayPause.setImageResource(R.drawable.ic_btnpause);
            playerViewSwitcher.setDisplayedChild(0);
        }
    }
}
