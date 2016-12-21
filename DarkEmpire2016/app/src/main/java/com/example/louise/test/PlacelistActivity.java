package com.example.louise.test;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlacelistActivity extends AppCompatActivity {
    private ListView listView01;
    private ArrayAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelist); //把activity_placelist.xml 顯示出來
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
//        switch (StoryActivity.party) {
//            case "Sinae": getWindow().setBackgroundDrawableResource(R.drawable.blue); break;
//            default: getWindow().setBackgroundDrawableResource(R.drawable.red); break;
//        }

//        listView01 = new ListView(PlacelistActivity.this);
        listView01 = (ListView)findViewById(R.id.placelistview);

        final MyAdapter adapter;
        adapter = new MyAdapter(this, MapsActivity.plistname);
        listView01.setAdapter(adapter);

//        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1, MapsActivity.plistname);
//        listView01.setAdapter(listAdapter);

        //ListView的onClick
        listView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {


                if (MapsActivity.plistid.get(position) == 52) {
                    Intent intent = new Intent();
                    intent.setClass(PlacelistActivity.this, PuzzleActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(PlacelistActivity.this, PlaceSecActivity.class);

                    //傳送點選到的index
//                Bundle bundle = new Bundle();
//                bundle.putInt("placeid", MapsActivity.plistid.get(position));
//                bundle.putString("placename", MapsActivity.plistname.get(position));
//                //將Bundle物件assign給intent
//                intent.putExtras(bundle);
                    try {
                        FileWriter fw = new FileWriter(new File("sdcard/darkempire/placelog.txt"));
                        final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                        bw.write(MapsActivity.plistid.get(position) + "," + MapsActivity.plistname.get(position));
                        bw.close();
//            Toast.makeText(SettingActivity.this, "save success", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(PlacelistActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }


                    startActivity(intent);
                }

//                Toast.makeText(getApplicationContext(),
//                        "點選的是" + MapsActivity.plistname.get(position) ,
//                                //+ position, //postition是指點選到的index
//                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private List<String> pname = new ArrayList<>();
        private String name = "";
        private List<String> psign = new ArrayList<>();

        public MyAdapter(Context c, List<String> pname) {
            this.pname = pname;
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pname.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return pname.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.sign_raw, null);

            final TextView placelistname = (TextView) convertView.findViewById(R.id.placelistname);
//            final TextView placelistsign = (TextView) convertView.findViewById(R.id.placelistsign);



//            SpannableString content = new SpannableString(pname.get(position));
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            placelistname.setText("請選擇：" + pname.get(position));
//            placelistname.setText(pname.get(position));
//            placelistsign.setText(" ");
//            placelistsign.setTextColor(Color.BLUE);
//            placelistsign.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    placelistsign.setTextColor(Color.RED);
//                    String re = "";
//                    try {
//                        re = Httpconnect.httpost2("http://140.119.163.40:8080/Spring08/app/checkinList/" + IndexActivity.userid, "id=0&placeid=" + MapsActivity.plistid.get(position) + "&longitude=121.2&latitude=223.5");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    //  refresh();
//                    Toast toast2 = Toast.makeText(PlacelistActivity.this, re, Toast.LENGTH_SHORT);
//                    toast2.show();
//
//
//                    //DO you work here
//                }
//            });


            return convertView;
        }

    }
}
