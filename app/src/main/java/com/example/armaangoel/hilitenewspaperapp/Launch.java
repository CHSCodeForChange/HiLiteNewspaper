package com.example.armaangoel.hilitenewspaperapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Launch extends AppCompatActivity {

    public ListView mListView;
    public ArrayList<Reader.Message> messages = new ArrayList<Reader.Message>();

    private ArrayAdapter adapter;
    public static int page;

    private String title = "HiLite - ";


    public static Launch l;


    public enum Section {
        Recent, Feature, News, StudentSection, Entertainment, Sports, Perspectives, FifteenMinutes
    }

    public static Section section;

    public final String TOP = "https://www.hilite.org?json=get_recent_posts";



    public final String START = "https://www.hilite.org?json=get_category_posts&slug=";
    public final String PAGE = "&page=";
    public final String END = "&count=12&include=posts,title,excerpt,thumbnail,url,modified";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        l = this;

        page = 1;
        section = Section.Recent;

        mainMenu();
    }

    public void mainMenu() {
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);




        mListView = (ListView) findViewById(R.id.listview);


        BottomNavigationView btm = (BottomNavigationView) findViewById(R.id.bottomBar);
        btm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.prev) {
                    if (page > 1) {
                        page--;
                        load();
                    }
                } else if (id == R.id.next) {
                    page++;
                    load();
                } else if (id == R.id.sections) {
                    Intent homeIntent = new Intent(Launch.this, Category.class);
                    startActivity(homeIntent);
                }

                return false;
            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setContentView(R.layout.webview);

                getSupportActionBar().setTitle(messages.get(position).title);

                WebView web = (WebView)findViewById(R.id.web);
                web.setWebViewClient(new MyWebViewClient(Launch.this));
                web.loadUrl(messages.get(position).url);
            }
        });

        load();
    }


    public void load() {
        String url = "";

        if (section == Section.Recent) {
            url = TOP + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Recent");
        }
        else if (section == Section.Feature) {
            url = START + "feature" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Feature");
        }
        else if (section == Section.News) {
            url = START + "news" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "News");
        }
        else if (section == Section.StudentSection) {
            url = START + "student-section" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Student Section");
        }
        else if (section == Section.Entertainment) {
            url = START + "entertainment" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Entertainment");
        }
        else if (section == Section.Sports) {
            url = START + "sports" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Sports");
        }
        else if (section == Section.Perspectives) {
            url = START + "perspectives" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Perspectives");
        }
        else if (section == Section.FifteenMinutes) {
            url = START + "fame" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "15 Minutes");
        }


        Reader r = new Reader(Launch.this);
        r.execute(url);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            mainMenu();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
