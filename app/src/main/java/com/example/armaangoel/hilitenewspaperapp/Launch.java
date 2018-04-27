package com.example.armaangoel.hilitenewspaperapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

public class Launch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView mListView;
    ArrayList<Reader.Message> messages = new ArrayList<Reader.Message>();

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ArrayAdapter adapter;
    private int page;

    private Button b1, b2;

    private ProgressDialog progressDialog;



    public static Context c;

    public enum Section {
        Top, Feature, News, StudentSection, Entertainment, Sports
    }

    public static Section section;

    public final String TOP = "https://www.hilite.org?json=get_recent_posts";



    public final String START = "https://www.hilite.org?json=get_category_posts&slug=";
    public final String PAGE = "&page=";
    public final String END = "&count=12&include=posts,title,excerpt,thumbnail,url,modified";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainMenu();
    }

    public void mainMenu() {
        setContentView(R.layout.activity_home);

        page = 1;
        c = Launch.this;
        section = Section.Top;


        drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b1 = (Button) findViewById(R.id.back);
        b2 = (Button) findViewById(R.id.forward);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page > 1) {
                    page--;
                    load();
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                load();
            }
        });



        mListView = (ListView) findViewById(R.id.listview);

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        AsyncTask<String, Void, ArrayList<Reader.Message>> reader = new Reader().execute(TOP);


        Reader r = (Reader) reader;

        while (r.finishedMessages == null) {}

        messages = r.finishedMessages;
        adapter = new myAdapter(Launch.this, messages);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setContentView(R.layout.webview);

                WebView web = (WebView)findViewById(R.id.web);
                web.setWebViewClient(new MyWebViewClient(Launch.this));
                web.loadUrl(messages.get(position).url);
            }
        });
    }


    public void load() {
        drawer.closeDrawers();
        toggle.syncState();

        String url = "";

        if (section == Section.Top) url = TOP + PAGE + page + END ;
        else if (section == Section.Feature) url = START + "feature" + PAGE + page + END;
        else if (section == Section.News) url = START + "news" + PAGE + page + END;
        else if (section == Section.StudentSection) url = START + "student-section" + PAGE + page + END;
        else if (section == Section.Entertainment) url = START + "entertainment" + PAGE + page + END;
        else if (section == Section.Sports) url = START + "sports" + PAGE + page + END;

        AsyncTask<String, Void, ArrayList<Reader.Message>> reader = new Reader().execute(url);


        Reader r = (Reader) reader;
        while (!r.isFinished) {}

        messages = r.finishedMessages;
        adapter = new myAdapter(Launch.this, messages);
        mListView.setAdapter(adapter);
      //  progressDialog.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){

            return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        progressDialog.show();
        int id  = item.getItemId();

        page = 1;

        if (id == R.id.home){
            section = Section.Top;
            load();
        } else if (id == R.id.home){
            section = Section.Top;
            load();
        } else if (id == R.id.news){
            section = Section.News;
            load();
        } else if (id == R.id.feature){
            section = Section.Feature;
            load();
        } else if (id == R.id.studentsection){
            section = Section.StudentSection;
            load();
        } else if (id == R.id.entertainment){
            section = Section.Entertainment;
            load();
        } else if (id == R.id.sports){
            section = Section.Sports;
            load();
        }

        return false;

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
