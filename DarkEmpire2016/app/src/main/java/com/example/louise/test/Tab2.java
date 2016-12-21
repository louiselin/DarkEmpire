package com.example.louise.test;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tab2 extends Activity {

    private ListView list;
    private TextView show;

    int[] logos = new int[] { R.drawable.newbie, R.drawable.newccu,
            R.drawable.nccu };

    String[] names = new String[] { };
    String[] lists = new String[] { };

    MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        String re = refresh();
        String classjson = classjson(re); //[專家,校園尋奇,探索,樓主]

        String bjson = "";
        String classification = "校園尋奇";
        String userbadge = userbadge();
        String keeper = "";
        String keeper2 = "";
        String k = "0";
        String resname = "";
        String resdes = "";
        String noname = "";
        String nodes = "";
        int add = 0;


        try {

            JSONObject keeperlist = new JSONObject(re);
            keeper = keeperlist.getString("badge");

            JSONArray jsonlist = new JSONArray(keeper);
            JSONArray userbadgelist = new JSONArray(userbadge);

            for (int i = 0; i < jsonlist.length(); i++) {
                if (jsonlist.getJSONObject(i).getString("classification").equals(classification)) {
                    // keeper2 = keeper2 + jsonlist.getJSONObject(i).toString() + "\n";
                    k = "0";
                    for (int j = 0; j < userbadgelist.length(); j++) {
                        // String w=  userbadgelist.getJSONObject(j).toString();
                        int w = userbadgelist.getInt(j);
                        int x = jsonlist.getJSONObject(i).getInt("id");
                        if (w == x) {
                            k = w + "取得";
                            resname =  jsonlist.getJSONObject(i).getString("name") + "&";
                            resdes = jsonlist.getJSONObject(i).getString("description") + "&";
                            break;
                        }
                    }


                    names = resname.split("&");
                    lists = resdes.split("&");
//                        keeper2 = keeper2 + "是否取得:" + k + "ID:" + jsonlist.getJSONObject(i).getInt("id")
//                            + " 名稱:" + jsonlist.getJSONObject(i).getString("name")
//                            + "  條件:" + jsonlist.getJSONObject(i).getString("description") + "\n";
                }


            }


//            if(res == "") {
//                res = "AHHHH,HA!! Newbie!";
//                TextView myTextView0 = (TextView) findViewById(R.id.tab2text);
//                myTextView0.setText(res);
//            } else {
//                TextView myTextView0 = (TextView) findViewById(R.id.tab2text);
//                myTextView0.setText(" 名稱 / 條件:" + res);
//            }

//            ImageView iv = (ImageView) findViewById(R.id.imglevel);
//            if(res!=""){
//                switch (res) {
//                    case "UCCU":  iv.setImageResource(R.drawable.uccu); break;
//                    case "NewCCU":  iv.setImageResource(R.drawable.newccu); break;
//                    case "NCCU":  iv.setImageResource(R.drawable.nccu); break;
//                    default: iv.setImageResource(R.drawable.newbie);
//                }
//            }
            list = (ListView) findViewById(R.id.list);
            adapter = new MyAdapter(this);
            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        Toast toast2 = Toast.makeText(Tab3.this, re, Toast.LENGTH_SHORT);  //  all badge datas
//        toast2.show();
//
//        Toast toast3 = Toast.makeText(Tab3.this, bjson, Toast.LENGTH_SHORT); // data = classification status
//        toast3.show();

    }


    private String refresh() {


        String badgejson = "";

        try {

            badgejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/badge");

        } catch (ProtocolException e) {
            e.printStackTrace();
        }


        return badgejson;

    }

    private String userbadge() {


        String badgejson = "";

        try {

            badgejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/badge/" + IndexActivity.userid);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }


        return badgejson;

    }


    private String classjson(String badgejson) {
        String keeper = "";
        try {
            JSONObject keeperlist = new JSONObject(badgejson);
            keeper = keeperlist.getString("classification");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keeper;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.tab2xml, null);

            ImageView logo = (ImageView) convertView.findViewById(R.id.imglogo);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView list = (TextView) convertView.findViewById(R.id.description);

//            logo.setImageResource(logos[position]);
            logo.setImageResource(R.drawable.newbie);
            name.setText(names[position]);
            list.setText(lists[position]);

            return convertView;
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
}