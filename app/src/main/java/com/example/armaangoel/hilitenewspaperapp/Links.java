package com.example.armaangoel.hilitenewspaperapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

public class Links extends AppCompatActivity {

    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pages);


        grid = (GridLayout) findViewById(R.id.pagesGrid);

        for (int i = 0; i < grid.getChildCount(); i++) {
            CardView card = (CardView)grid.getChildAt(i);

            final int finalI = i;
            card.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent browserIntent = null;

                    if (finalI == 0) {
                        browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/HiLite-Online/86051497611"));
                    } else if (finalI == 1) {
                        browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/hilitenews"));
                    } else if (finalI == 2) {
                        browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/hilitenews/"));
                    } else if (finalI == 3) {
                        browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/HiLiteOnline"));
                    }

                    if (browserIntent != null) {
                        startActivity(browserIntent);
                        finish();
                    }
                }
            });
        }
    }
}
