package info706.zikub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info706.zikub.adapters.MusicAdapter;
import info706.zikub.models.Music;
import info706.zikub.models.PlayList;
import info706.zikub.models.Setting;
import info706.zikub.models.User;
import info706.zikub.services.MusicService;
import info706.zikub.services.PlaylistService;
import info706.zikub.services.YoutubeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private int musicRank;
    private int playlistId;
    private List<Music> searchResults;

    private ListView listview;
    private EditText search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Recherche YouTube");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle  = getIntent().getExtras();
        musicRank = bundle.getInt(MainActivity.MUSIC_RANK_TAG);
        playlistId = bundle.getInt(MainActivity.PLAYLIST_ID_TAG);

        listview = (ListView) findViewById(R.id.listview);
        search = (EditText) findViewById(R.id.search);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Setting.API_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        final YoutubeService youtube = retrofit.create(YoutubeService.class);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Call<List<Music>> caller = youtube.search(User.getOauthToken(getApplicationContext()), search.getText().toString());
                search.clearFocus();
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(search.getWindowToken(), 0);

                caller.enqueue(new Callback<List<Music>>() {
                    @Override
                    public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                        if(response.isSuccessful()) {
                            searchResults = response.body();
                            ArrayAdapter adapter = new MusicAdapter(getApplicationContext(), R.layout.music_item, searchResults);
                            listview.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Impossible d'effectuer une recherche sur Youtube pour le moment...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Music>> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Le serveur ne répond pas", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = searchResults.get(position);
                music.setRank(musicRank);
                music.setPlaylist_id(playlistId);

                Log.i("MUSIC", music.toString());

                Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Setting.API_HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                MusicService service = retrofit.create(MusicService.class);
                Call<Music> caller = service.post(User.getOauthToken(getApplicationContext()), music);

                caller.enqueue(new Callback<Music>() {
                    @Override
                    public void onResponse(Call<Music> call, Response<Music> response) {
                        if(response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(MainActivity.IS_EDITION_MODE_TAG, true);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Le serveur ne répond pas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Music> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Le serveur ne répond pas", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MainActivity.IS_EDITION_MODE_TAG, true);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
