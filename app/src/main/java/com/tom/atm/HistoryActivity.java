package com.tom.atm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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


public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences setting = getSharedPreferences("atm", MODE_PRIVATE);
        String userid = setting.getString("USERID", "");
        String passwd = setting.getString("PASSWD", "");
        new HistoryTask().execute("http://j.snpy.org/atm/h?userid="+userid+"&pw="+passwd);

    }

    class HistoryTask extends AsyncTask<String, Void, String>{
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

            try {
                JSONArray array = new JSONArray(s);
                
                for (int i=0; i <array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    int amount = obj.getInt("amount");
                    String date = obj.getString("date");
                    String userid = obj.getString("userid");
                    Log.d("HIST", amount+"/"+date+"/"+userid);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
