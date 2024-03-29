package info706.zikub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info706.zikub.models.User;

public class HomeActivity extends AppCompatActivity {

    // TODO: connexion réseaux sociaux
    // facebook     https://blog.rolandl.fr/2014-04-26-android-mettre-en-place-le-facebook-connect-dans-une-application.html
    // twitter      https://blog.rolandl.fr/2015-01-25-mettre-en-place-le-twitter-oauth-dans-une-application-android.html
    // google       https://blog.rolandl.fr/2015-02-15-mettre-en-place-la-connexion-google-plus-dans-une-application-android.html
    //              https://developers.google.com/identity/sign-in/android/start-integrating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button btnConnection = (Button)findViewById(R.id.btnConnexion);
        final Button btnRegister = (Button)findViewById(R.id.btnInscription);

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Redirige directement l'utilisateur sur l'activité main si il possède déjà un token.
        if(User.getOauthToken(getApplicationContext()) != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
