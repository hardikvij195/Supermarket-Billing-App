package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {



    public static  int SPLASH = 1000 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent h = new Intent(MainActivity.this , ShowPage.class);
                startActivity(h);
                finish();


            }
        },SPLASH);








    }
}
