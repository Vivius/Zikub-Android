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
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import info706.zikub.adapters.MusicAdapter;
import info706.zikub.components.YoutubePlayer;
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
    private ListView playlist;
    private YoutubePlayer youtubePlayer;

    ImageButton btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
    ImageButton btnNext = (ImageButton)findViewById(R.id.btnNext);
    ImageButton btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log.d("OAUTH_TOKEN", User.getOauthToken(getApplicationContext()));

        playlist = (ListView) findViewById(R.id.playlist);
        youtubePlayer = new YoutubePlayer(this);

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
                Log.d("SUCCESS", "Response success");
                Log.d("CONTENT", response.body().toString());

                ArrayAdapter adapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, response.body().getMusics());
                playlist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable throwable) {
                Log.d("ERROR", "Response error");
                Toast.makeText(getApplicationContext(), "Impossible d'accéder au serveur", Toast.LENGTH_LONG).show();
            }
        });

        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                // Tests
                if(position == 0)
                    youtubePlayer.pause();
                if(position == 1)
                    youtubePlayer.play();
                if(position == 2)
                    youtubePlayer.start("https://www.youtube.com/watch?v=h-T__qXRXXk");
                if(position == 3)
                    startActivity(intent);
            }
        });

        // TEST - Lancement de la lecture d'une musique Youtube dans l'application.
        youtubePlayer.start("https://www.youtube.com/watch?v=_cB3HXVvm0g");


        // boutons lecteur

        // bouton play/pause alterné
        // joue, ou pause, la musique
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: lecture / pause
                //TODO: alterner play/pause selon l'état du bouton, de la lecture...
                btnPlayPause.setImageResource(R.drawable.ic_btnpause);
            }
        });

        // start musique précédente
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start musique précédente
            }
        });

        // start musique suivante
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start musique suivante
            }
        });
    }
}
