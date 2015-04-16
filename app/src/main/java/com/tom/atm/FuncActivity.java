package com.tom.atm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class FuncActivity extends Activity implements AdapterView.OnItemClickListener {

    private GridView grid;
    private String[] names;
    int[] icons = {R.drawable.func_balance, R.drawable.func_history,R.drawable.func_03,R.drawable.func_google,R.drawable.func_05,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);
        findViews();
        names = new String[]{"餘額查詢", "交易明細","轉帳","Google","離開"};

//        ArrayAdapter adapter = new ArrayAdapter(this,
//               android.R.layout.simple_list_item_1, names);
        grid = (GridView) findViewById(R.id.grid);
        IconAdapter adapter = new IconAdapter();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Log.d("ITEM", position+"/"+id);
        switch((int) id){
            case R.drawable.func_balance:
                Intent balace = new Intent(this, BalanceActivity.class);
                startActivity(balace);
                break;
            case R.drawable.func_history:
                Intent history = new Intent(this, HistoryActivity.class);
                startActivity(history);
                break;
            case R.drawable.func_google:
                Intent google = new Intent(this, GoogleActivity.class);
                startActivity(google);
                break;
        }

    }

    private void findViews() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_func, menu);
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


    public class IconAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return icons[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
//                TextView tv = new TextView(FuncActivity.this);
//                tv.setText(names[position]);
//                convertView = tv;
//                ImageView iv = new ImageView(FuncActivity.this);
//                iv.setImageResource(icons[position]);
//                convertView = iv;
                View v = getLayoutInflater().inflate(R.layout.icon,null);
                ImageView iv = (ImageView) v.findViewById(R.id.icon_image);
                TextView tv = (TextView) v.findViewById(R.id.icon_name);
                iv.setImageResource(icons[position]);
                tv.setText(names[position]);
                convertView = v;

            }
            return convertView;
        }
    }
}
