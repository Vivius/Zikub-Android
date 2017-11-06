package info706.zikub;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import info706.zikub.models.PlayList;
import info706.zikub.models.Setting;
import info706.zikub.models.User;
import info706.zikub.services.PlaylistService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShareActivity extends AppCompatActivity {
    private static final int SHARE_RESULT_CODE = 1;

    private Toolbar toolbar;
    private ImageButton btnShare;
    private TextInputEditText txtName;
    private TextInputEditText txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        btnShare = (ImageButton)findViewById(R.id.btnShare);
        txtName = (TextInputEditText)findViewById(R.id.txtName);
        txtDescription = (TextInputEditText)findViewById(R.id.txtDescription);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Partage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération de la playliste actuelle et mise à jour du nom et de la description.
                Bundle bundle  = getIntent().getExtras();
                int playlistId = bundle.getInt(MainActivity.PLAYLIST_ID_TAG, 0);
                PlayList playList = new PlayList(txtName.getText().toString(), txtDescription.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Setting.API_HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                PlaylistService service = retrofit.create(PlaylistService.class);

                // Mise à jour du nom et de la description de la playliste actuelle.
                Call<PlayList> updator = service.update(User.getOauthToken(getApplicationContext()), playlistId, playList);
                updator.enqueue(new Callback<PlayList>() {
                    @Override
                    public void onResponse(Call<PlayList> call, Response<PlayList> response) {}
                    @Override
                    public void onFailure(Call<PlayList> call, Throwable t) {}
                });

                // Création d'une nouvelle playliste que l'utilisateur pourra modifer par la suite.
                PlayList newPlaylist = new PlayList("NEW PLAYLIST", "...");
                Call<PlayList> creator = service.create(User.getOauthToken(getApplicationContext()), newPlaylist);
                creator.enqueue(new Callback<PlayList>() {
                    @Override
                    public void onResponse(Call<PlayList> call, Response<PlayList> response) {}
                    @Override
                    public void onFailure(Call<PlayList> call, Throwable t) {}
                });

                // Partage du lien de la playliste.
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, "Partage de ma playlist Zikub");
                share.putExtra(Intent.EXTRA_TEXT, "http://www.zikub.com/playlist/" + playlistId);
                startActivityForResult(Intent.createChooser(share, "Partage de ma playlist"), SHARE_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SHARE_RESULT_CODE) {
            Toast.makeText(getApplicationContext(), "Partage effectué! Retour vers votre nouvelle playlist...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
