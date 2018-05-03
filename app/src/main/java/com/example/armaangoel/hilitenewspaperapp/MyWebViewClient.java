package com.example.armaangoel.hilitenewspaperapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by armaangoel on 3/18/18.
 */

public class MyWebViewClient extends WebViewClient
{
    ProgressDialog progressDialog;

    public MyWebViewClient(Activity a)
    {
        progressDialog = new ProgressDialog(a);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Story...");
        progressDialog.show();
        // do nothing
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);

        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);

        progressDialog.dismiss();
    }
}