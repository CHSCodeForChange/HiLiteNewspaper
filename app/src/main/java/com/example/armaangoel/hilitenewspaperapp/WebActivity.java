package com.example.armaangoel.hilitenewspaperapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ShareActionProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebActivity extends AppCompatActivity {

    private WebView web;
    String url;

    public static enum Mode  {
        Normal, Extras
    };

    public static Mode mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        url = getIntent().getExtras().getString("url");

        getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));


        web = (WebView) findViewById(R.id.web);
        web.setWebViewClient(new MyWebViewClient(WebActivity.this));
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        PageViewer viewer = new PageViewer();
        viewer.execute(url);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_story, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Read this story from the CHS HiLite Newspaper:");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share URL"));
        }

        return super.onOptionsItemSelected(item);
    }

    private class PageViewer extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;
        Document doc;
        String url;

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(WebActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading Story...");
            progressDialog.show();
        }


        protected void onPostExecute(Void result) {

            if (doc != null) {
                web.loadData(doc.toString(),"text/html; charset=UTF-8", null);
            } else {
                if (mode == Mode.Normal) {
                    web.loadUrl(url);
                } else {
                    finish();
                }
            }

            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... strings) {
            url = strings[0];
            try {
                if (mode == Mode.Normal) {
                    doc = Jsoup.connect(url).ignoreContentType(true).get();
                    doc.getElementsByAttributeValue("id", "mobile-menu").remove();
                    doc.getElementsByClass("crp_related").remove();
                    doc.getElementsByAttributeValue("id", "sidebar").remove();
                    doc.getElementsByClass("footerwrap").remove();
                } else if (mode == Mode.Extras) {
                    doc = Jsoup.connect(url).ignoreContentType(true).get();
                    doc.getElementsByAttributeValue("id", "mobile-menu").remove();
                    doc.getElementsByAttributeValue("id", "contentleft").remove();
                    doc.getElementsByClass("footerwrap").remove();
                }



            } catch (IOException e) {
                finish();
            }

            return null;
        }
    }

}
