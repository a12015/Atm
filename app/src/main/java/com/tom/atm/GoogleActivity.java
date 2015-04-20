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


public class GoogleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        //http://finance.google.com/finance/info?client=ig&q=TPE:2317,TPE:2330,TPE:3008,TPE:2454
        new GoogleTask().execute("http://finance.google.com/finance/info?client=ig&q=TPE:2317,TPE:2330,TPE:3008");
    }

    class GoogleTask extends AsyncTask<String, Void, String> {

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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s = s.substring(3);
            Log.d("PROC", s);
            List data = new ArrayList<HashMap<String,String>>();
            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    String stockNo = obj.getString("t");
                    String current = obj.getString("l_cur");
                    String change = obj.getString("c");
                    String changePercent = obj.getString("cp");
                    Log.d("STOCK", stockNo+"/"+current+"/"+change+"/"+changePercent);
                    Map<String,String> row = new HashMap<String,String>();
                    row.put("symbol", stockNo);
                    row.put("current", current);
                    row.put("change", change);
                    row.put("percent", changePercent);
                    data.add(row);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] from ={"symbol", "current", "change", "percent"};
            int[] to = {R.id.google_symbol, R.id.google_current, R.id.google_change, R.id.google_percent};

            SimpleAdapter adapter = new SimpleAdapter(GoogleActivity.this,
                    data, R.layout.google_row, from, to
                    );

            ListView list = (ListView)findViewById(R.id.googe_list);
            list.setAdapter(adapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google, menu);
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
