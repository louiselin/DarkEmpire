package com.example.louise.test;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class Tab3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);

//        listView01 = (ListView) findViewById(R.id.levellist);

        String re = refresh();
        String classjson = classjson(re); //[專家,校園尋奇,探索,樓主]

        String bjson = "";
        String classification = "探索";
        String userbadge = userbadge();
        String keeper = "";
        String keeper2 = "";
        String k = "0";
        String res = "";
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
                            res = jsonlist.getJSONObject(i).getString("name");

                            break;
                        }
                    }
//                        keeper2 = keeper2 + "是否取得:" + k + "ID:" + jsonlist.getJSONObject(i).getInt("id")
//                            + " 名稱:" + jsonlist.getJSONObject(i).getString("name")
//                            + "  條件:" + jsonlist.getJSONObject(i).getString("description") + "\n";
                }
            }
            TextView myTextView0 = (TextView) findViewById(R.id.playerlevel);
            myTextView0.setText(res);
            ImageView iv = (ImageView) findViewById(R.id.imglevel);
            if(res!=""){
                switch (res) {
                    case "UCCU":  iv.setImageResource(R.drawable.uccu); break;
                    case "NewCCU":  iv.setImageResource(R.drawable.newccu); break;
                    case "NCCU":  iv.setImageResource(R.drawable.nccu); break;
                    default: iv.setImageResource(R.drawable.newbie);
                }
            }


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

}