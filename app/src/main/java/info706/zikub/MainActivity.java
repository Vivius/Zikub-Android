package info706.zikub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import info706.zikub.models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("OAUTH_TOKEN", User.getOauthToken(getApplicationContext()));
    }
}
