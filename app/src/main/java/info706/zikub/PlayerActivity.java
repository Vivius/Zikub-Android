package info706.zikub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import info706.zikub.models.PlayList;

public class PlayerActivity extends AppCompatActivity {
    private int playListId;
    private PlayList playList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
