package com.example.louise.test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;


public class KeeperinfoActivity extends AppCompatActivity {

    private String keeperid;
    private String keepername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle =this.getIntent().getExtras();
        keeperid = bundle.getString("keeperid");
        keepername = bundle.getString("keepername");

        setTitle(keepername);
        setContentView(R.layout.activity_keeperinfo);


        String re = refresh();
        String classjson = classjson(re);

        String bjson = "";
        String bjson2 = "";
        String bjson3 = "";
        String classification = "";
        String userbadge = userbadge();
        String newname = "";

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        try {
            JSONArray jsonlists = new JSONArray(classjson);
            int size = jsonlists.length();
            for (int ii = 0; ii < size; ii++) {
                classification = jsonlists.get(ii).toString();
                switch (classification) {
                    case "專家":newname = "專家\n條件";break;
                    case "校園尋奇":newname = "成就\n解鎖";break;
                    case "探索":newname = "玩家\n級別";break;
                    default:newname = "樓主\n資格";
                }

                TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab1");

                bjson = bjson(re, classification, userbadge);
                final List<String> listname =new ArrayList<>();
                for (String r: bjson.split("@")){
                    listname.add(r);
                }

                bjson2 = bjson2(re, classification, userbadge);
                final List<String> listdes =new ArrayList<>();
                for (String r: bjson2.split("@")){
                    listdes.add(r);
                }

                bjson3 = bjson3(re, classification, userbadge);
                final List<String> listk =new ArrayList<>();
                for (String r: bjson3.split("@")){
                    listk.add(r);
                }

                final MyAdapter adapter;
                adapter = new MyAdapter(this, listname, listdes, listk);

                spec1.setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        ListView listView01 = new ListView(KeeperinfoActivity.this);
                        listView01.setAdapter(adapter);
                        return listView01;
                    }
                });
                spec1.setIndicator(classification);
                tabHost.addTab(spec1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            badgejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/badge/" + keeperid);

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

    private String bjson(String badgejson, String classification, String userbadge) {
        String keeper = "";
        String keeper2 = "";
        String k = "0";

        try {


            JSONObject keeperlist = new JSONObject(badgejson);
            keeper = keeperlist.getString("badge");

            JSONArray jsonlist = new JSONArray(keeper);
            JSONArray userbadgelist = new JSONArray(userbadge);

            for (int i = 0; i < jsonlist.length(); i++) {
                if (jsonlist.getJSONObject(i).getString("classification").equals(classification)) {
                    k = "0";
                    for (int j = 0; j < userbadgelist.length(); j++) {
                        int w = userbadgelist.getInt(j);
                        int x = jsonlist.getJSONObject(i).getInt("id");
                        if (w == x) {
                            k = w + "取得";
                            break;
                        }
                    }
                    keeper2 = keeper2 + jsonlist.getJSONObject(i).getString("name") + "@";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keeper2;
    }

    private String bjson2(String badgejson, String classification, String userbadge) {
        String keeper = "";
        String keeper2 = "";
        String k = "0";

        try {


            JSONObject keeperlist = new JSONObject(badgejson);
            keeper = keeperlist.getString("badge");

            JSONArray jsonlist = new JSONArray(keeper);
            JSONArray userbadgelist = new JSONArray(userbadge);

            for (int i = 0; i < jsonlist.length(); i++) {
                if (jsonlist.getJSONObject(i).getString("classification").equals(classification)) {
                    k = "0";

                    for (int j = 0; j < userbadgelist.length(); j++) {
                        int w = userbadgelist.getInt(j);
                        int x = jsonlist.getJSONObject(i).getInt("id");
                        if (w == x) {
                            k = w + "取得";
                            break;
                        }
                    }
                    keeper2 = keeper2 + jsonlist.getJSONObject(i).getString("description") + "@";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keeper2;
    }

    private String bjson3(String badgejson, String classification, String userbadge) {
        String keeper = "";
        String keeper2 = "";
        String k = "0";

        try {


            JSONObject keeperlist = new JSONObject(badgejson);
            keeper = keeperlist.getString("badge");

            JSONArray jsonlist = new JSONArray(keeper);
            JSONArray userbadgelist = new JSONArray(userbadge);

            for (int i = 0; i < jsonlist.length(); i++) {
                if (jsonlist.getJSONObject(i).getString("classification").equals(classification)) {
                    k = "0";

                    for (int j = 0; j < userbadgelist.length(); j++) {
                        int w = userbadgelist.getInt(j);
                        int x = jsonlist.getJSONObject(i).getInt("id");
                        if (w == x) {
                            k = w + "取得";
                            break;
                        }
                    }
                    keeper2 = keeper2 + k + "@";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keeper2;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private List<String> na = new ArrayList<>();
        private List<String> des = new ArrayList<>();
        private List<String> k = new ArrayList<>();
        public MyAdapter(Context c, List<String> na, List<String> des, List<String> k) {
            this.na = na;
            this.des = des;
            this.k = k;
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return na.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return na.get(position);
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


            String ans = "";
            if(k.get(position) != "") {
                ans = k.get(position);
                switch (ans) {
                    case "0" : logo.setImageResource(R.drawable.newbie); break;
                    default: logo.setImageResource(R.drawable.nccu);
                }
            }

            name.setText(na.get(position));
            list.setText(des.get(position));

            return convertView;
        }

        public String a(List<String>l) {
            return l.toString();
        }
    }
}











































