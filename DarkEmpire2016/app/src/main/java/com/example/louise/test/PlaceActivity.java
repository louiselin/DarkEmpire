package com.example.louise.test;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    private ListView listView01;
    private ArrayAdapter listAdapter;
    private List<String> listname =new ArrayList<String>();
    private List<String> listcheck =new ArrayList<String>();
    private  Integer placeid;
    public String keeperid = "";
    String keepername = "";
    String keeperlevel = "";
    String keepervotes = "";
    String keeperexp = "";
    private String txt_party = "";
    private String txt_user = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        try {

            FileReader fr = new FileReader(new File("sdcard/profile.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine(); //readLine()讀取一整行
//            Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_LONG).show();

            if (temp != null) {
                String[] datas = temp.split(",");
                txt_party = datas[1];
                txt_user = datas[0];

            } else {
                txt_party = StoryActivity.party;
                txt_user = IndexActivity.userid;
            }
        } catch (Exception e) {}

        Bundle bundle =this.getIntent().getExtras();
        placeid = bundle.getInt("placeid");


        String placejson="";
        String keeperjson="";
        String checkinlistjson="";
        String imagejson="";
        try {
            placejson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/place/"+placeid);
            keeperjson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/keeperlist/"+placeid);
            checkinlistjson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/checkin/place/"+placeid);
            imagejson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/image/" + placeid);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        String keeper="";
        String placename="";
        int brick=0;
        int attack=0;
        int defense=0;
        String image="";
        try {
            JSONArray placelist = new JSONArray(placejson);
            JSONArray keeperlist = new JSONArray(keeperjson);
            JSONArray imagelist = new JSONArray(imagejson);

            keeper= keeperlist.getJSONObject(0).getString("name");
            placename= placelist.getJSONObject(0).getString("name");
            brick=keeperlist.getJSONObject(0).getInt("count");
            attack=placelist.getJSONObject(0).getInt("attack");
            defense=placelist.getJSONObject(0).getInt("defense");
            image=imagelist.getJSONObject(0).getString("image");


            JSONArray checkinlist = new JSONArray(checkinlistjson);
            for(int i=0;i< checkinlist.length();i++)
            {
                String test = checkinlist.getJSONObject(i).getString("userid");
                String checkname = checkinlist.getJSONObject(i).getString("name");
                String checkcount = checkinlist.getJSONObject(i).getString("count");
                listname.add(checkname+"結了"+checkcount+"緣分");
                listcheck.add(test);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView myTextView5 = (TextView)findViewById(R.id.placeName);
        myTextView5.setText(placename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView)findViewById(R.id.keeper);
        myTextView0.setText("領主:" + keeper);
        myTextView0.setTextSize(20);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView)findViewById(R.id.totalBrick);
        myTextView1.setText("磚頭:" + brick);
        myTextView1.setTextSize(20);
        myTextView1.setTextColor(Color.BLACK);

        TextView myTextView2 = (TextView)findViewById(R.id.attackBrick);
        myTextView2.setText("被破壞:" + attack);
        myTextView2.setTextSize(20);
        myTextView2.setTextColor(Color.BLACK);

        TextView myTextView3 = (TextView)findViewById(R.id.defenseBrick);
        myTextView3.setText("被增加:" + defense);
        myTextView3.setTextSize(20);
        myTextView3.setTextColor(Color.BLACK);



        listView01 = (ListView)findViewById(R.id.listView);
        //把show_text放進來，讓ListView直接用陣列的值
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1, listname);
        listView01.setAdapter(listAdapter);


        //import image
        findViews();
        thirdImage.setImageDrawable(loadImageFromURL(image));

        String url = "http://140.119.163.40:8080/Spring08/app/user/"+txt_user;
        String userjson="";



        try {
            userjson = Httpconnect.httpget(url);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int votes = 0;

        try {
            JSONArray userlist = new JSONArray(userjson);

            votes= userlist.getJSONObject(0).getInt("votes");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView textview = (TextView) findViewById(R.id.keepervotes);
        textview.setText("信任度："+votes);


        final TextView myTextView6 = (TextView)findViewById(R.id.attack);
        myTextView6.setText("減損");
        myTextView6.setTextSize(20);
        myTextView6.setTextColor(Color.BLUE);

        final TextView myTextView7 = (TextView)findViewById(R.id.defense);
        myTextView7.setText("強化");
        myTextView7.setTextSize(20);
        myTextView7.setTextColor(Color.BLUE);

        final TextView myTextView8 = (TextView)findViewById(R.id.checkin);
        myTextView8.setText("簽契約");
        myTextView8.setTextSize(20);
        myTextView8.setTextColor(Color.BLUE);

        myTextView6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myTextView6.setTextColor(Color.RED);
                String vote = "";
                try {

                    // vote=Httpconnect.httpost("http://140.119.163.40:8080/Spring08/app/votes/"+placeid+"/0/"+MapsActivity.userid);
                    vote = Httpconnect.httpost("http://140.119.163.40:8080/Spring08/app/votes/" + placeid + "/0/" + txt_user);
                    // vote=Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/votes/" + placeid + "/0/" + IndexActivity.userid);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                //   refresh();
               /* Toast toast2 = Toast.makeText(PlaceActivity.this,vote+IndexActivity.userid , Toast.LENGTH_SHORT);
                toast2.show();*/

            }
        });

        myTextView7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myTextView7.setTextColor(Color.RED);
                String vote="";
                try {
//                    // vote=Httpconnect.httpost("http://140.119.163.40:8080/Spring08/app/votes/"+placeid+"/0/"+MapsActivity.userid);
                    vote=Httpconnect.httpost("http://140.119.163.40:8080/Spring08/app/votes/" + placeid + "/1/" + txt_user);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                // refresh();

            }
        });


        myTextView8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myTextView8.setTextColor(Color.RED);
                String re="";
                try {
                    re=Httpconnect.httpost2("http://140.119.163.40:8080/Spring08/app/checkinList/"+ txt_user,"id=0&placeid="+placeid+"&longitude=121.2&latitude=223.5");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  refresh();
                Toast toast2 = Toast.makeText(PlaceActivity.this, re, Toast.LENGTH_SHORT);
                toast2.show();


                //DO you work here
            }
        });




        handler.postDelayed(runnable, 2000);

    }


    private ImageView thirdImage;

    private void findViews() {
        thirdImage = (ImageView) findViewById(R.id.imageView);


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Drawable loadImageFromURL(String image){
        try{
            byte[] data = Base64.decode(image, Base64.DEFAULT);


            Drawable draw = new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));


            return draw;
        }catch (Exception e) {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
        }
    }



    private void refresh()
    {


        String placejson="";
        String keeperjson="";
        String checkinlistjson="";
        String imagejson="";
        String userjson="";
        try {
            placejson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/place/"+placeid);
            keeperjson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/keeperlist/"+placeid);
            checkinlistjson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/checkin/place/"+placeid);
            imagejson=  Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/image/"+placeid);
            userjson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/user/"+IndexActivity.userid);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        String keeper="";
        String placename="";
        int brick=0;
        int attack=0;
        int defense=0;
        int vote=0;
        String image="";

        try {
            JSONArray placelist = new JSONArray(placejson);
            JSONArray keeperlist = new JSONArray(keeperjson);
            JSONArray imagelist = new JSONArray(imagejson);
            JSONArray userlist = new JSONArray(userjson);

            keeper= keeperlist.getJSONObject(0).getString("name");
            placename= placelist.getJSONObject(0).getString("name");
            brick=keeperlist.getJSONObject(0).getInt("count");
            attack=placelist.getJSONObject(0).getInt("attack");
            defense=placelist.getJSONObject(0).getInt("defense");
            vote=userlist.getJSONObject(0).getInt("votes");


            JSONArray checkinlist = new JSONArray(checkinlistjson);
            listname.clear();
            for(int i=0;i< checkinlist.length();i++)
            {
//                listname.add(checkinlist.getJSONObject(i).getString("name")+checkinlist.getJSONObject(i).getString("userid")+"結了"+checkinlist.getJSONObject(i).getString("count")+"緣分");
                String test = checkinlist.getJSONObject(i).getString("userid");
                String checkname = checkinlist.getJSONObject(i).getString("name");
                String checkcount = checkinlist.getJSONObject(i).getString("count");
                listname.add(checkname+"結了"+checkcount+"緣分");
                listcheck.add(test);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

/*
        Toast toast2 = Toast.makeText(PlaceActivity.this,keeper +"" , Toast.LENGTH_LONG);
        toast2.show();
*/

        TextView textview = (TextView) findViewById(R.id.keepervotes);
        textview.setText("信任度："+ vote);

        TextView myTextView5 = (TextView)findViewById(R.id.placeName);
        myTextView5.setText(placename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView)findViewById(R.id.keeper);
        myTextView0.setText("領主:" + keeper);
        myTextView0.setTextSize(20);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView)findViewById(R.id.totalBrick);
        myTextView1.setText("磚頭:" + brick);
        myTextView1.setTextSize(20);
        myTextView1.setTextColor(Color.BLACK);

        TextView myTextView2 = (TextView)findViewById(R.id.attackBrick);
        myTextView2.setText("被破壞:" + attack);
        myTextView2.setTextSize(20);
        myTextView2.setTextColor(Color.BLACK);

        TextView myTextView3 = (TextView)findViewById(R.id.defenseBrick);
        myTextView3.setText("被增加:" + defense);
        myTextView3.setTextSize(20);
        myTextView3.setTextColor(Color.BLACK);


        listView01.setAdapter(null);
        //把show_text放進來，讓ListView直接用陣列的值
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1, listname);
        listView01.setAdapter(listAdapter);

        listView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                String keeperidjson = "";
                keeperid = listcheck.get(position);

                try {
                    keeperidjson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/user/"+keeperid);
                } catch (Exception e) {}

                try {

                    JSONArray keeperidlist = new JSONArray(keeperidjson);

                    keepername = keeperidlist.getJSONObject(0).getString("name");
                    keeperexp = keeperidlist.getJSONObject(0).getString("exp");
                    keeperlevel = keeperidlist.getJSONObject(0).getString("level");
                    keepervotes = keeperidlist.getJSONObject(0).getString("votes");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ConfirmExit();

//                Toast.makeText(getApplicationContext(),
//                        "點選的是" + keepername + " " + keeperlevel + " " + keepervotes + " " + keeperexp,
//                        //+ position, //postition是指點選到的index
//                        Toast.LENGTH_SHORT).show();

            } //end onItemClick
        });
    }

    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(PlaceActivity.this);
        ad.setTitle("簡略敵人戰績");
        ad.setMessage("昵稱：" + keepername + "\n"
                + "種子數：" + keepervotes + "\n"
                + "等級：" + keeperlevel + "\n"
                + "經驗值：" + keeperexp + "\n"
                + "是否要完整查看敵人戰績勳章？");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(PlaceActivity.this, KeeperinfoActivity.class);

                //將Bundle物件assign給intent
                Bundle bundle = new Bundle();
                bundle.putString("keeperid", keeperid);
                bundle.putString("keepername", keepername);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                return;
            }
        });
        ad.show();
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            refresh();
            handler.postDelayed(this, 2000);
        }
    };

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            Intent intent = new Intent();
//            intent.setClass(PlaceActivity.this, MapsActivity.class);
//            finish();
//            startActivity(intent);
//            return false;
//        }
//        return false;
//    }

}
