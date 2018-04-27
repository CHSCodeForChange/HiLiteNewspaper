package com.example.armaangoel.hilitenewspaperapp;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Home extends AppCompatActivity {


    public final int timeout = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startup);


        new Handler().postDelayed(new Runnable() {
            @Override
            public String toString() {
                return "$classname{}";
            }

            @Override
            public void run() {
                Intent homeIntent = new Intent(Home.this, Launch.class);
                startActivity(homeIntent);
                finish();

            }
        }, timeout);



    }
}