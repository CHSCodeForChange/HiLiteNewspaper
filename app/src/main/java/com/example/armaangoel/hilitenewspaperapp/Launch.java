package com.example.armaangoel.hilitenewspaperapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import java.util.ArrayList;

public class Launch extends AppCompatActivity {

    public ListView mListView;
    public ArrayList<Reader.Message> messages = new ArrayList<Reader.Message>();

    private ArrayAdapter adapter;
    public static int page;

    private String title = "HiLite - ";


    public static Launch l;


    public enum Section {
        Recent, Feature, News, StudentSection, Entertainment, Sports, Perspectives, FifteenMinutes, Jam, Online, Search
    }

    public static Section section;

    public final String TOP = "https://www.hilite.org?json=get_recent_posts";



    public final String START = "https://www.hilite.org?json=get_category_posts&slug=";
    public final String PAGE = "&page=";
    public final String END = "&count=12&include=posts,title,excerpt,thumbnail,url,modified";

    public final String SEARCHSTART = "https://hilite.org/?json=get_search_results&search=";
    public String search = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotifManager.l = this;
        if (!NotifManager.isRunning) {
            startService(new Intent(getBaseContext(), NotifManager.class));
        }


        l = this;

        page = 1;
        section = Section.Recent;


        mainMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);

        final MenuItem search = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                page = 1;
                setSearch(s);
                section = Section.Search;

                searchView.clearFocus();
                searchView.setFocusable(false);
                search.collapseActionView();
                searchView.setIconified(true);

                load();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.links) {
            Intent links = new Intent(Launch.this, Links.class);
            startActivity(links);
        }
        return super.onOptionsItemSelected(item);
    }

    public void mainMenu() {
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);




        mListView = (ListView) findViewById(R.id.listview);


        BottomNavigationView btm = (BottomNavigationView) findViewById(R.id.bottomBar);
        btm.setSelectedItemId(R.id.sections);
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
                    Intent homeIntent = new Intent(Launch.this, Sections.class);
                    startActivity(homeIntent);
                }

                return false;
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                WebActivity.mode = WebActivity.Mode.Normal;

                Intent web = new Intent(Launch.this, WebActivity.class);
                Bundle data = new Bundle();
                data.putString("url", messages.get(position).url);
                data.putString("title", messages.get(position).title);

                web.putExtras(data);
                startActivity(web);

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
        else if (section == Section.Jam) {
            url = START + "just-a-minute" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Just A Minute");
        }
        else if (section == Section.Online) {
            url = START + "onlineonly" + PAGE + page + END;
            getSupportActionBar().setTitle(title + "Online Only");
        } else if (section == Section.Search) {
            url = SEARCHSTART + search.replace(' ', '+') + PAGE + page + END;
            getSupportActionBar().setTitle("Results for: \"" + search + "\"");
        }


        Reader r = new Reader(Launch.this);
        r.execute(url);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (section == Section.Search) {
                section = Section.Recent;
                page = 1;
            }

            mainMenu();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
