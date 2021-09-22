package com.assissoft.canif;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenCanif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(SplashScreenCanif.this, ActivityCanif.class);
        startActivity(i);
        finish();

    }
}
