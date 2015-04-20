package com.tom.atm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        new MovieTask().execute("http://j.snpy.org/movie.php");
    }

    class MovieTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            String line = null;
            StringBuffer data = new StringBuffer();
             try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(isr);
                line = in.readLine();
                while(line!=null){
                    data.append(line);
                    line = in.readLine();
                }
                Log.d("NET", data.toString());
                Thread.sleep(2000);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            List data = new ArrayList<HashMap<String,String>>();
            try {
                JSONArray array = new JSONArray(s);
                JSONObject theater = array.getJSONObject(0);
                JSONArray movies = theater.getJSONArray("movies");


                for (int i=0 ;i <movies.length(); i++){
                    JSONObject mov = movies.getJSONObject(i);
                    String name = mov.getString("name");
                    String type = mov.getString("type");
                    String plays = mov.getString("plays");
                    Log.d("MOV", name+"/"+type+"/"+plays);
                    Map<String,String> row = new HashMap<String,String>();
                    row.put("name", name);
                    row.put("type", type);
                    row.put("plays", plays);
                    data.add(row);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] from ={"name", "type", "plays"};
            int[] to = {R.id.movie_name, R.id.movie_type, R.id.movie_plays};

            SimpleAdapter adapter = new SimpleAdapter(MovieActivity.this,
                    data, R.layout.movie_row, from, to
            );
            ListView list = (ListView)findViewById(R.id.movie_list);
            list.setAdapter(adapter);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
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
}
