package com.imran.mynewsreader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadCallback, AdapterView.OnItemSelectedListener {

    final String apiKey = "&apiKey=ea201ad9975047e1af3e52631573923d";
    final String TAG = "MyNewsreader";
    Button refreshBtn;
    Spinner spinner;

    private List<Article> articles;
    private ArrayAdapter<Article> articleAdapter;
    private ListView articleListView;

    private static final String[] sources = new String[]{
            "ars-technica", "associated-press", "bbc-news", "bbc-sport", "bild", "bloomberg",
            "business-insider", "buzzfeed", "cnbc", "cnn", "daily-mail", "engadget", "entertainment-weekly",
            "espn", "financial-times", "focus", "fortune", "four-four-two", "fox-sports", "google-news",
            "hacker-news", "ign", "independent", "mashable", "metro", "mirror", "national-geographic", "newsweek",
            "new-york-magazine", "polygon", "recode", "reddit", "reuters", "techradar", "the-verge", "the-washington-post"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sources);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        refreshBtn = (Button) findViewById(R.id.button);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJSONData();
            }
        });

        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, R.layout.link_view, articles);
        articleListView = (ListView) findViewById(R.id.link_list);
        articleListView.setAdapter(articleAdapter);

        getJSONData();
    }

    private void getJSONData() {
        getJSONData(sources[spinner.getSelectedItemPosition()]);
    }

    private void getJSONData(String source) {
        articles.clear();
        articleAdapter.notifyDataSetChanged();

        String url = "https://newsapi.org/v1/articles?source="+ source + apiKey;
        if (isConnectedToNetwork())
            new GetJSONTask(this).execute(url);
        else
            Toast.makeText(this, "Not connected to a network", Toast.LENGTH_SHORT).show();
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


    @Override
    public void updateFromDownload(String result) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(result);
        String formattedResult = gson.toJson(element);

        articles.clear();

        try {
            JSONObject obj = new JSONObject(formattedResult);
            JSONArray arr = new JSONArray(obj.getString("articles"));
//        Log.d(TAG, formattedResult);

            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);

                Article article = new Article();
                article.setAuthor(obj.getString("author"));
                article.setTitle(obj.getString("title"));
                article.setDescription(obj.getString("description"));
                article.setUrl(obj.getString("url"));
//                article.setUrlToImage(obj.getString("author"));
//                article.setPublishedAt(obj.getString("published at"));

                articles.add(article);
            }

            articleAdapter.notifyDataSetChanged();
        } catch(Exception e) {
            Log.d(TAG, "JSON error: " + e);
        }
    }

    @Override
    public void finishDownloading(int result) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        getJSONData(sources[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
