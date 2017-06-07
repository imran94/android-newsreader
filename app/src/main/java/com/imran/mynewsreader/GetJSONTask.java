package com.imran.mynewsreader;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 14-Jan-17.
 */
public class GetJSONTask extends AsyncTask<String, Void, String> {

    DownloadCallback callback;

    GetJSONTask(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder response = new StringBuilder();
        try {
            URL website = new URL(urls[0]);
            URLConnection connection = website.openConnection();

            connection.getInputStream();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();
        } catch (Exception e) {
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if (s.equals("")) {
            callback.finishDownloading(callback.ERROR);
        } else {
            callback.finishDownloading(callback.CONNECT_SUCCESS);
            callback.updateFromDownload(s);
        }
    }


}