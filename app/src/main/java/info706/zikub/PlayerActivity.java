package info706.zikub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

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

public class PlayerActivity extends AppCompatActivity {
    private int playListId;
    private int selectedIndex = 0;
    private Music selectedMusic;
    private YoutubePlayer youtubePlayer;
    private PlayList playList;

    private Toolbar toolbar;
    private TextView txtDescription;
    private ListView listView;
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageView cover;
    private LinearLayout lecteur;
    private ViewSwitcher playerViewSwitcher;

    private MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        txtDescription = (TextView)findViewById(R.id.txtDescription);
        listView = (ListView)findViewById(R.id.playlist);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        cover = (ImageView) findViewById(R.id.cover);
        lecteur = (LinearLayout)findViewById(R.id.lecteur);

        playerViewSwitcher = (ViewSwitcher) findViewById(R.id.playerViewSwitcher);
        playerViewSwitcher.reset();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        youtubePlayer = new YoutubePlayer(this);

        // Traite l'intent
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals("android.intent.action.VIEW")) {
            Uri data = intent.getData();
            String url = data.toString();
            String[] parts = url.split("/");
            String id = parts[parts.length-1];
            playListId = Integer.parseInt(id);
            Log.i("ID", playListId + "");
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            playListId = 28;
        }

        // Chargement de la playliste.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Setting.API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlaylistService service = retrofit.create(PlaylistService.class);

        Call<PlayList> updator = service.get(User.getOauthToken(getApplicationContext()), playListId);
        updator.enqueue(new Callback<PlayList>() {
            @Override
            public void onResponse(Call<PlayList> call, Response<PlayList> response) {
                if(response.isSuccessful()) {
                    playList = response.body();
                    setTitle("[" + playList.getUser().getName() + "] " + playList.getName());
                    txtDescription.setText(playList.getDescription());
                    adapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, playList.getMusics());
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Vous n'êtes plus connecté", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Impossible de récupérer la playlist", Toast.LENGTH_LONG).show();
            }
        });

        // Gestion des clicks sur les items de la playliste.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startMusicByIndex(position);
            }
        });

        // Gestion des événements du YoutubePlayer.
        youtubePlayer.setYoutubePlayerListener(new YoutubePlayerListener() {
            // Evénement appelé lorque la musique est prête (quand la lecture commence).
            // Permet d'arrêter l'animation loader.
            @Override
            public void onMusicBegin() {
                Log.i("MUSIC PLAYER", "START");
                playerViewSwitcher.setDisplayedChild(1);
            }
            // Evénement appelé quand la lecture de la musique est terminé.
            // Permet de passer au morceau suivant.
            @Override
            public void onMusicEnd() {
                Log.i("MUSIC PLAYER", "STOP");
                startMusicByIndex(selectedIndex + 1);
            }
        });

        ////////////////////////
        // Bontons du lecteur //
        ////////////////////////

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

    /**
     * Arrête le lecteur lorsque l'on quitte l'activité.
     */
    @Override
    public void onStop() {
        super.onStop();
        // Stop le lecteur si on change d'activité.
        youtubePlayer.pause();
        btnPlayPause.setImageResource(R.drawable.ic_btnplay);
    }

    /**
     * Crée le menu de la toolbar.
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_player, menu);
        return true;
    }

    /**
     * Gère les différentes actions disponibles dans la toolbar de l'activité.
     *
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
