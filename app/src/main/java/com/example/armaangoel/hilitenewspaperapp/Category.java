package com.example.armaangoel.hilitenewspaperapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

public class Category extends AppCompatActivity {

    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sections);

        final Launch l = Launch.l;

        grid = (GridLayout) findViewById(R.id.grid);

        for (int i = 0; i < grid.getChildCount(); i++) {
            CardView card = (CardView)grid.getChildAt(i);

            final int finalI = i;
            card.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    if (finalI == 0) {
                        l.section = Launch.Section.Recent;
                    } else if (finalI == 1) {
                        l.section = Launch.Section.News;
                    } else if (finalI == 2) {
                        l.section = Launch.Section.Feature;
                    } else if (finalI == 3) {
                        l.section = Launch.Section.StudentSection;
                    } else if (finalI == 4) {
                        l.section = Launch.Section.Entertainment;
                    } else if (finalI == 5) {
                        l.section = Launch.Section.Sports;
                    } else if (finalI == 6) {
                        l.section = Launch.Section.Perspectives;
                    } else if (finalI == 7) {
                        l.section = Launch.Section.FifteenMinutes;
                    }

                    l.page = 1;
                    l.mainMenu();
                    finish();
                }
            });
        }


    }
}
