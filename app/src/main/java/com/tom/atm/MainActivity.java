package com.tom.atm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    private EditText userid;
    private EditText passwd;
    private CheckBox saveUserid;
    private boolean save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        save = getSharedPreferences("atm", MODE_PRIVATE).getBoolean("SAVE_USERID",false);
        saveUserid.setChecked(save);
        saveUserid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CHECK", isChecked+"");
                getSharedPreferences("atm", MODE_PRIVATE)
                        .edit()
                        .putBoolean("SAVE_USERID", isChecked)
                        .commit();
            }
        });


        getPreferences(MODE_PRIVATE).edit().putString("TEST", "haha").commit();

        //String uid = getSharedPreferences("atm", MODE_PRIVATE).getString("PREF_USERID", "");
        if (save) {
            userid.setText(
                    getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE).getString("PREF_USERID", "")
            );
        }

    }

    void findViews(){
        userid = (EditText) findViewById(R.id.userid);
        passwd = (EditText) findViewById(R.id.passwd);
        saveUserid = (CheckBox) findViewById(R.id.cb_save_userid);
    }

    public void login(View v){
        String uid = userid.getText().toString();
        String pw = passwd.getText().toString();
        new LoginTask().execute("http://j.snpy.org:8080/atm/login?userid="+uid+"&pw="+pw);

        //HttpURLConnection = url.openConnection();



        /*if (uid.equals("jack") && pw.equals("1234")){
            if (save) {
                getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE).edit()
                        .putString("PREF_USERID", uid)
                        .commit();
            }
            Intent intent = new Intent(this, FuncActivity.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this)
                    .setMessage("登入失敗")
                    .setPositiveButton("OK", null)
                    .show();
        }*/
    }

    class LoginTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            int data = 0;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                data = is.read();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer==49){
                Intent intent = new Intent(MainActivity.this, FuncActivity.class);
                startActivity(intent);
            }else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("登入訊息")
                        .setMessage("登入失敗")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }

    public void exit(View v){
        new AlertDialog.Builder(this)
                .setTitle("確認對話框")
                .setMessage("確定要離開嗎?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("取消", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();;
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
}
