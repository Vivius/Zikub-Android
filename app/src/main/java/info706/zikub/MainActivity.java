package info706.zikub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info706.zikub.adapters.MusicAdapter;
import info706.zikub.adapters.MusicEditionAdapter;
import info706.zikub.adapters.MusicEditionAdapterListener;
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
    public static final String PLAYLIST_ID_TAG = "PLAYLIST_ID";
    public static final String MUSIC_RANK_TAG = "MUSIC_RANK";
    public static final String IS_EDITION_MODE_TAG = "IS_EDITION_MODE";

    // Vars
    private YoutubePlayer youtubePlayer;
    private PlayList playList;
    private Music selectedMusic = null;
    private int selectedIndex = 0;
    private Boolean isEditionMode = false;

    // Layout's components
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageView cover;
    private ListView listView;
    private ViewSwitcher playerViewSwitcher;
    private Toolbar toolbar;
    private LinearLayout lecteur;
    private Button btnAddMusic;

    // Adapters
    private MusicEditionAdapter editionAdapter;
    private MusicAdapter readAdapter;

    public MainActivity() {
        playList = new PlayList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Ma Playliste");

        // Log.d("OAUTH_TOKEN", User.getOauthToken(getApplicationContext()));
        youtubePlayer = new YoutubePlayer(this);

        listView = (ListView) findViewById(R.id.playlist);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        cover = (ImageView) findViewById(R.id.cover);
        lecteur = (LinearLayout)findViewById(R.id.lecteur);
        btnAddMusic = (Button)findViewById(R.id.btnAddMusic);

        playerViewSwitcher = (ViewSwitcher) findViewById(R.id.playerViewSwitcher);
        playerViewSwitcher.reset();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                // Création des adapters
                readAdapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, playList.getMusics());
                editionAdapter = new MusicEditionAdapter(getApplicationContext(), R.layout.music_edition_item, playList.getMusics());
                editionAdapter.setListener(new MusicEditionAdapterListener() {
                    @Override
                    public void onEditMusic(Music music) {
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(MUSIC_RANK_TAG, music.getRank());
                        bundle.putInt(PLAYLIST_ID_TAG, playList.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteMusic(Music music) {
                        playList.getMusics().remove(music);
                        editionAdapter.notifyDataSetChanged();
                        refreshDisplayMode();
                    }
                });

                // Récupération de paramètres optionnels.
                Bundle bundle  = getIntent().getExtras();
                if(bundle != null) {
                    isEditionMode = bundle.getBoolean(IS_EDITION_MODE_TAG, false);
                }

                // Affichage des résultats selon le mode (lecture / modification).
                refreshDisplayMode();
            }

            @Override
            public void onFailure(Call<PlayList> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Le serveur ne répond pas", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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
     * Permet de modifier les items disponibles dans le menu de la toolbar.
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        for(int i=0; i<menu.size(); ++i) {
            menu.getItem(i).setVisible(true);
        }
        for(int i=0; i<menu.size(); ++i) {
            int id = menu.getItem(i).getItemId();
            if(isEditionMode) {
                if(id == R.id.action_share || id == R.id.action_edit)
                    menu.getItem(i).setVisible(false);
            } else {
                if(id == R.id.action_check)
                    menu.getItem(i).setVisible(false);
            }
        }
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
            case R.id.action_edit:
                isEditionMode = true;
                refreshDisplayMode();
                return true;
            case R.id.action_check:
                isEditionMode = false;
                refreshDisplayMode();
                return true;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "Partage de la playliste", Toast.LENGTH_LONG).show();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Test de l'intent");
                startActivity(Intent.createChooser(share, "Partage de la playliste"));
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

    /**
     * Affiche l'affichage pour le mode edition ou lecture en fonction de la propriété 'isEdtionMode'.
     */
    private void refreshDisplayMode()
    {
        if(isEditionMode) {
            listView.setAdapter(editionAdapter);
            lecteur.setVisibility(LinearLayout.GONE);
            if(playList.getMusics().size() < 5)
                btnAddMusic.setVisibility(Button.VISIBLE);
        } else {
            listView.setAdapter(readAdapter);
            lecteur.setVisibility(LinearLayout.VISIBLE);
            btnAddMusic.setVisibility(Button.GONE);
        }
        invalidateOptionsMenu();
    }
}
