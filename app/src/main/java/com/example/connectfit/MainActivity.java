package com.example.connectfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMER = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainFragment())
                .commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Substituir o fragmento atual pelo próximo fragmento
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new LoginScreenFragment())
                        .commit();
            }
        }, SPLASH_TIMER);
    }
}
