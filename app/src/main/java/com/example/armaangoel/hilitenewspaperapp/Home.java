package com.example.armaangoel.hilitenewspaperapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {


    public final int timeout = 400;


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