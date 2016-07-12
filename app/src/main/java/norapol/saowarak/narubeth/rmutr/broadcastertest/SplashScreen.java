package norapol.saowarak.narubeth.rmutr.broadcastertest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import norapol.saowarak.narubeth.rmutr.broadcastertest.utility.SessionManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.logoutUser();

        Handler objHandler = new Handler();
        objHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 5000);
        MediaPlayer introPlayer = MediaPlayer.create(getBaseContext(), R.raw.soundtitle);
        introPlayer.start();
    }   // Main Method


}   // Main Class
