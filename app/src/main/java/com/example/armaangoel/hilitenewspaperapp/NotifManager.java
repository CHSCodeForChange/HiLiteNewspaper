package com.example.armaangoel.hilitenewspaperapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

public class NotifManager extends Service {
    public static Context l;
    private String lastTitle = "";
    public static boolean isRunning;
    private Thread thread;

    private final String CHANNEL_ID = "cfc.dev.androidnotificationchannel.CFCDEV";
    private final String CHANNEL_NAME = "CFC Channel";
    private NotificationManager manager;



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    final class backgroundThread implements Runnable{
        int service_id;
        boolean running;
        Context l;
        String lastTitle;

        public backgroundThread (int service_id, boolean running, Context l) {
            this.service_id = service_id;
            this.running = running;
            this.l = l;
        }


        public void run () {
            synchronized (this){
                while(running) {
                    try {
                        Reader reader = new Reader();
                        reader.execute("https://www.hilite.org?json=get_recent_posts&page=1&count=1&include=posts,title");

                        wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }




    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }


        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        manager.createNotificationChannel(channel);

        thread = new Thread(new backgroundThread(startId, true, l));
        thread.start();
        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
    }



    /**
     * Created by armaangoel on 4/23/18.
     */

    public class Reader extends AsyncTask<String, Void, Void> {

        public String title;

        private Exception exception;

        private ArrayAdapter adapter;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... url) {

            try {
                title = readUrl(url[0]);
            } catch (Exception e) {
                this.exception = e;
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            if (lastTitle.equals("")) {
                lastTitle = title;
            } else if (!(lastTitle.equals(title) || title.equals(""))) {
                Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentText(title)
                        .setContentTitle("HiLite Newspaper")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), Launch.class), PendingIntent.FLAG_UPDATE_CURRENT));

                manager.notify(new Random().nextInt(), builder.build());

                lastTitle = title;
            }
        }




        public String readUrl (String url) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);


            return readMessagesArray(jsonStr);
        }




        public String readMessagesArray(String jsonStr) {
            String title = "";
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray posts = jsonObj.getJSONArray("posts");

                JSONObject c = posts.getJSONObject(0);

                title = c.getString("title");
            } catch (final JSONException e) {
                e.printStackTrace();
            }

            return decode(title);
        }

        public String decode (String html) {
            return Html.fromHtml(html).toString();
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


}
