package info706.zikub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import info706.zikub.models.PlayList;

public class PlayerActivity extends AppCompatActivity {
    private int playListId;
    private PlayList playList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

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
            playListId = 28;
        }


    }
}
