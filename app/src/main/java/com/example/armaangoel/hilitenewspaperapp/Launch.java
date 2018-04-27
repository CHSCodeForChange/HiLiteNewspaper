package com.example.armaangoel.hilitenewspaperapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;

public class Launch extends AppCompatActivity {

    ListView mListView;
    ArrayList<Reader.Message> messages = new ArrayList<Reader.Message>();

    public static Context c;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainMenu();
    }

    public void mainMenu() {
        setContentView(R.layout.activity_home);

        c = Launch.this;

        AsyncTask<String, Void, ArrayList<Reader.Message>> reader = new Reader().execute("https://www.hilite.org?json=get_recent_posts&page=1&count=12&include=posts,title,excerpt,thumbnail,url,modified");
        Reader r = (Reader) reader;

        while (r.finishedMessages == null) {}

        messages = r.finishedMessages;
        mListView = (ListView) findViewById(R.id.listview);
        myAdapter adapter = new myAdapter(Launch.this, messages);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            mainMenu();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
