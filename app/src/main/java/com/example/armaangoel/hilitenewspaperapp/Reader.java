package com.example.armaangoel.hilitenewspaperapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by armaangoel on 4/23/18.
 */

public class Reader extends AsyncTask<String, Void, Void> {


    public ArrayList<Message> finishedMessages;
    public boolean isFinished = false;
    private Exception exception;
    private ProgressDialog pd;

    private ArrayAdapter adapter;



    private Launch l = new Launch();

    public Reader (Launch l) {
        this.l = l;
        pd = new ProgressDialog(l);
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd.setMessage("Loading Stories...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Void doInBackground(String... url) {

        try {
            finishedMessages = readUrl(url[0]);
            isFinished = true;
        } catch (Exception e) {
            this.exception = e;
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        adapter = new myAdapter(l, finishedMessages);
        l.mListView.setAdapter(adapter);
        l.messages = finishedMessages;
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }




    public ArrayList<Message> readUrl (String url) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        //System.out.println(jsonStr.toString());

        return readMessagesArray(jsonStr);
    }




    public ArrayList<Message> readMessagesArray(String jsonStr) {
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray posts = jsonObj.getJSONArray("posts");

            // looping through All Contacts
            for (int i = 0; i < posts.length(); i++) {
                JSONObject c = posts.getJSONObject(i);

                String title = c.getString("title");
                String excerpt = c.getString("excerpt");
                String date = c.getString("modified");
                String link = c.getString("url");



                if (c.has("thumbnail_images")) {
                    String thumb = c.getJSONObject("thumbnail_images").getJSONObject("medium" +
                            "").getString("url");
                    URL url = new URL(thumb);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    messages.add(new Message(title, excerpt, bmp, date, link));
                } else {
                    messages.add(new Message(title, excerpt, date, link));

                }
            }
        } catch (final JSONException e) {
        } catch (IOException e) {
        }

        return messages;
    }

    public class Message {
        String title;
        String excerpt;
        Bitmap thumbnail;
        String url;
        String date;

        public Message(String text, String excerpt, Bitmap thumbnail, String date, String url) {
            this.title = text;
            this.excerpt = excerpt;
            this.thumbnail = thumbnail;
            this.url = url;
            this.date = date;
        }


        public Message(String text, String excerpt, String date, String url) {
            this.title = text;
            this.excerpt = excerpt;
            this.url = url;
            this.date = date;
        }

    }


    public class HttpHandler {

        private static final String TAG = "HttpHandler";

        public HttpHandler() {
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

}


