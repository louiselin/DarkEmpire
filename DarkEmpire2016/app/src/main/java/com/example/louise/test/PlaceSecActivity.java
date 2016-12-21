package com.example.louise.test;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlaceSecActivity extends AppCompatActivity {

    private ArrayAdapter listAdapter;
    private List<String> listname = new ArrayList<String>();
    private List<String> listcheck = new ArrayList<String>();
    private Integer placeid;
    private String placename;
    private Integer placeidd;
    private String placenamee;
    private RatingBar ratingBar;
    private float hp_init = 20;
    private String hp = "";
    private String name = "";
    private String username = "";
    private ProgressBar myProgressBar;
    private MediaPlayer soundjump;
    private MediaPlayer soundpunch;
    private ImageView weapon;
    private List<Integer> weaponlist = new ArrayList<>();
    private int randomInt;
    private String che_vi, che_me;
    private String switchOn = "ON";
    private String switchOff = "OFF";
    private String txt_party = "";
    private int partyid = 0;
    private String txt_user = "";
    private JSONArray wlist = null;
    private int weaponid = 0;
    private int w = 0;
    private int num = 0;
    private String weaponname = "";
    List<String> wname = new ArrayList<>();
    List<Integer> wid = new ArrayList<>();
    private int powerup = 0;
    private TextView desc_weapon;
    private String re_powerup = "";
    private String re_p = "";



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CheckBox donotshowagain;
    public static final String PREFS_NAME = "place";

    public static final String intent_me="ON";
    public static final String intent_vi="ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_sec);
//        Bundle bundle = this.getIntent().getExtras();
//        placeidd = bundle.getInt("placeid");
//        placenamee = bundle.getString("placename");


//        try {
//            FileWriter fw = new FileWriter(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "placelog.txt"));
//            final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
//            bw.write(placeidd + "," + placenamee);
//            bw.close();
////            Toast.makeText(SettingActivity.this, "save success", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(PlaceSecActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//        }


        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/profile.txt"));
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
        if (txt_party.equals("Antayen")) {
            partyid = 3;
        } else if (txt_party.equals("Sinae")) {
            partyid = 2;
        }

        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/placelog.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine(); //readLine()讀取一整行
//            Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_LONG).show();

            if (temp != null) {
                String[] datas = temp.split(",");
                placename = datas[1];
                placeid = Integer.valueOf(datas[0]);
            }
        } catch (Exception e) {}

//        switch (StoryActivity.party) {
//            case "Sinae": getWindow().setBackgroundDrawableResource(R.drawable.blue); break;
//            default: getWindow().setBackgroundDrawableResource(R.drawable.red); break;
//        }

        // id = 10
//        if(placeid == 10) {
//            Intent intent = new Intent();
//            intent.setClass(PlaceSecActivity.this, Id10PrisionActivity.class);
//            finish();
//            startActivity(intent);
//        }


        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/weapon.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine(); //readLine()讀取一整行
//            Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_LONG).show();

            if (temp != null) {
                weaponid = Integer.parseInt(temp);

            } else {
                weaponid = 1;
            }
        } catch (Exception e) {}

//        Random random = new Random();
//        randomInt = random.nextInt(wlist.length());


        try {
            re_powerup = Httpconnect.httpget("http://140.119.163.40:8080/GeniusLoci/sforce/app/powerup/" + partyid + "/");
            re_p = re_powerup.replace("\n", "").replace(" ", "");
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        int d_fuel = 0;
        int d_power = 0;
        if (weaponid == 10) {
            d_fuel = 1;
            d_power = 100;
        } else if (weaponid == 2) {
            d_fuel = 2;
            d_power = 10;
        } else {
            d_fuel = 1;
            d_power = 1;
        }

        desc_weapon = (TextView) findViewById(R.id.desc_weapon);
        desc_weapon.setText("武器"+weaponid+", 馬納消耗"+d_fuel+"\n攻擊 "+d_power+"(+"+re_p+")");

//        // super power loading
//        if(placeid == 52) {
//            Intent intent = new Intent();
//            intent.setClass(PlaceSecActivity.this, PuzzleActivity.class);
//            finish();
//            startActivity(intent);
//        }



        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        donotshowagain = (CheckBox) eulaLayout.findViewById(R.id.skip);

        AlertDialog.Builder ad = new AlertDialog.Builder(PlaceSecActivity.this);
        ad.setView(eulaLayout);
        ad.setTitle("遊戲規則");

        ad.setMessage("您點選的是" + placename + "\n\n選擇「巡邏」增加馬納值來守護神殿; 當不幸神殿守護者是敵方的時候以「淨化」掠取，但是會減少馬納值。要注意有秒數限制哦！><\n\n開始吧！勇士！\n");
        ad.setNegativeButton("開始遊戲", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String checkBoxResult = "NOT checked";
                if (donotshowagain.isChecked())
                    checkBoxResult = "checked";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("skipMessage", checkBoxResult);
                // Commit the edits!
                editor.commit();

                return;
            }
        });
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("skipMessage", "NOT checked");
        if (!skipMessage.equals("checked"))
            ad.show();
//        ad.show();


        String placejson = "";
        String imagejson = "";
        String stelejson = "";
        try {
            placejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/place/" + placeid);
            imagejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/image/" + placeid);
            stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + placeid);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        String placename = "";
        String image = "";

//            String hp = "";
        String camp = "";
//            String name = "";

        try {
            JSONArray placelist = new JSONArray(placejson);
            JSONArray imagelist = new JSONArray(imagejson);
            JSONArray stelelist = new JSONArray(stelejson);

            placename = placelist.getJSONObject(0).getString("name");
            image = imagelist.getJSONObject(0).getString("image");
            hp = stelelist.getJSONObject(0).getString("hp");
            camp = stelelist.getJSONObject(0).getString("camp");
            name = stelelist.getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(placename.equals("山上蔣公銅像")) {
            placename = "黑馬蔣公";
        }
        TextView myTextView5 = (TextView) findViewById(R.id.locationname);
        myTextView5.setText(placename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView tv = (TextView) findViewById(R.id.belong);
        switch (name) {
            case "Ruyen":
                tv.setText("屬於:" + camp);
                break;
            default:
                tv.setText("屬於:" + txt_party + camp);
                break;
        }
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname);
        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood);
        switch (name) {
            case "Ruyen":
                myTextView1.setText("敵方的生命值:" + hp);
                break;
            default:
                myTextView1.setText(txt_party + "方生命值:" + hp);
                break;
        }
        myTextView1.setTextSize(15);
        myTextView1.setTextColor(Color.BLACK);

//            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//            ratingBar.setRating((Float.parseFloat(hp))/5);
//        Resources res = getResources();
//        switch (name) {
//            case "Ruyen": ratingBar.setProgressDrawable(res.getDrawable(R.drawable.ratingbar_color));break;
//            default: {
//                if(StoryActivity.party == "Sinae") ratingBar.setProgressDrawable(res.getDrawable(R.drawable.ratingbar_color_s));
//                else ratingBar.setProgressDrawable(res.getDrawable(R.drawable.ratingbar_color_a)); break;
//            }
//        }
//            Toast.makeText(PlaceSecActivity.this, String.valueOf((Float.parseFloat(hp))/5) + " " + name, Toast.LENGTH_SHORT).show();

        myProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        myProgressBar.setProgress(Integer.parseInt(hp) * 5);
        Resources res = getResources();
        switch (name) {
            case "Ruyen":
                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
                break;
            default: {
                if (txt_party.equals("Sinae")) {
                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.green_progressbar));
                    break;
                } else {
                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.red_progressbar));
                    break;
                }
            }
        }
        //import image
        findViews();

        thirdImage.setImageDrawable(loadImageFromURL(image));
        image = null;   // important!!

        String url = "http://140.119.163.40:8080/Spring08/app/user/" + txt_user;
        String userjson = "";

        try {
            userjson = Httpconnect.httpget(url);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int votes = 0;

        try {
            JSONArray userlist = new JSONArray(userjson);
            votes = userlist.getJSONObject(0).getInt("votes");
            username = userlist.getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView textview = (TextView) findViewById(R.id.patrolvalue);
        textview.setText("馬納值:" + votes);

        // weapon here
        weapon = (ImageView) findViewById(R.id.weapon);
        weaponlist.add(R.drawable.weapon1);
        weaponlist.add(R.drawable.weapon2);
        weaponlist.add(R.drawable.weapon3);


        weapon.setImageResource(weaponlist.get(weaponid));
        switch (weaponid) {
            case 1: {
                weapon.setImageResource(R.drawable.weapon2);
                break;
            } case 2: {
                weapon.setImageResource(R.drawable.weapon3);
                break;
            } default: {
                weapon.setImageResource(R.drawable.weapon1);
                break;
            }
        }
//        weapon.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        switch (randomInt) {
//                            case 1: {
//                                weapon.setImageResource(0);
//                                weapon.setImageResource(R.drawable.weapon2);
//                                weapon.setRotation(30);
//                                break;
//                            } case 2: {
//                                weapon.setImageResource(0);
//                                weapon.setImageResource(R.drawable.weapon3);
//                                weapon.setRotation(30);
//                                break;
//                            } default: {
//                                weapon.setImageResource(0);
//                                weapon.setImageResource(R.drawable.weapon1);
//                                weapon.setRotation(30);
//                                break;
//                            }
//                        }
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL: {
//                        weapon.setImageResource(0);
//                        weapon.setImageResource(weaponlist.get(randomInt));
//                        weapon.setRotation(0);
//                        break;
//                    }
//                }
//                return true;
//            }
//        });



        final TextView myTextView6 = (TextView) findViewById(R.id.fight);
        final MediaPlayer mp = new MediaPlayer();
        myTextView6.setText("淨化");
        myTextView6.setTextSize(20);
        myTextView6.setTextColor(Color.WHITE);
        myTextView6.setSoundEffectsEnabled(true);


        myTextView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myTextView6.setTextColor(Color.RED);
                // check setting switch button
                try {

                    FileReader fr = new FileReader(new File("sdcard/darkempire/output.txt"));
                    BufferedReader br = new BufferedReader(fr);

                    String temp = br.readLine(); //readLine()讀取一整行
                    if (temp != null) {
                        String[] datas = temp.split(",");
                        che_vi = datas[1];
                        che_me = datas[0];
                    } else {
                        che_vi = che_me = switchOff;
                    }
                } catch (Exception e) {}

                if (intent_vi.equals(che_vi)) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.hasVibrator();
                    vibrator.vibrate(100);
                }

                if (intent_me.equals(che_me)) {
                    soundpunch = MediaPlayer.create(PlaceSecActivity.this, R.raw.punch);
                    soundpunch.start();
                    soundpunch.seekTo(100);
                }
//                Toast.makeText(PlaceSecActivity.this, "me " + intent_me+ " ,vi "+intent_vi, Toast.LENGTH_SHORT).show();


                String stelejson = "";
                String stelename = "";
                String userjson = "";
                String username = "";
                String votes = "";
                try {
                    stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + placeid);
                    userjson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/user/" + txt_user);
                    JSONArray stelelist = new JSONArray(stelejson);
                    JSONArray userlist = new JSONArray(userjson);
                    username = userlist.getJSONObject(0).getString("name");
                    stelename = stelelist.getJSONObject(0).getString("name");
                    votes = userlist.getJSONObject(0).getString("votes");

                    if (stelename.equals(username) || votes.equals("0")) {
                        final Toast toast = Toast.makeText(PlaceSecActivity.this, "攻擊對象是自己或\n 已無馬納值哦QQ", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 800);
                    } else if (!username.equals("Ruyen")){
                        String att = "";
                        String api = "";
                        String fuel = "";




                        try {
                            switch (weaponid) {
                                case 2: {

//                                    Toast.makeText(PlaceSecActivity.this, re_powerup+"", Toast.LENGTH_SHORT).show();
                                    powerup = 10+Integer.parseInt(re_p);
//                                    fuel = "/2/10/";
                                    fuel = "/2/"+String.valueOf(powerup)+"/";
                                    api = "http://140.119.163.40:8080/Spring08/app/stele/attack/" + placeid + "/" + txt_user + fuel;
                                    att = Httpconnect.httpget(api);
                                    final Toast toast = Toast.makeText(PlaceSecActivity.this, "減少敵方生命值", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 300);

                                    break;
                                }
                                case 1: {
//                                    fuel = "/1/1/";
                                    powerup = 1+Integer.parseInt(re_p);
//                                    Toast.makeText(PlaceSecActivity.this, re_powerup+"", Toast.LENGTH_SHORT).show();
                                    fuel = "/1/"+String.valueOf(powerup)+"/";
                                    api = "http://140.119.163.40:8080/Spring08/app/stele/attack/" + placeid + "/" + txt_user + fuel;
                                    att = Httpconnect.httpget(api);
                                    final Toast toast = Toast.makeText(PlaceSecActivity.this, "減少敵方生命值", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 300);

                                    break;
                                }
                                default: {

//                                    if (w == 0) {
                                        api = "http://140.119.163.40:8080/Spring08/app/stele/attack/" + placeid + "/" + txt_user + "/1/100/";
                                        att = Httpconnect.httpget(api);

//                                        Toast.makeText(PlaceSecActivity.this, att, Toast.LENGTH_LONG).show();
                                        if (att.replace("\n", "").replace(" ", "").equals("succes")) {
                                            try {
                                                weapon.setImageResource(0);
                                                weapon.setImageResource(R.drawable.weapon2);
                                                FileWriter fw = new FileWriter(new File("sdcard/darkempire/weapon.txt"));
                                                final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                                                bw.write("1");
                                                bw.close();
                                                final Toast toast = Toast.makeText(PlaceSecActivity.this, "減少敵方生命值", Toast.LENGTH_SHORT);
                                                toast.show();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        toast.cancel();
                                                    }
                                                }, 1000);
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            weapon.setImageResource(0);
                                            weapon.setImageResource(R.drawable.weapon2);
                                            Toast.makeText(PlaceSecActivity.this, "超原力已釋放, 要等下次武器生成! @@", Toast.LENGTH_SHORT).show();
                                        }


//                                        w++;
//                                    } else {
//                                        fuel = "/1/1/";
//                                        api = "http://140.119.163.40:8080/Spring08/app/stele/attack/" + placeid + "/" + txt_user + fuel;
//                                        att = Httpconnect.httpget(api);
//                                    }
//                                    myTextView6.setEnabled(false);

                                    break;
                                }
                            }

                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }


//                        if (!att.equals("")) myTextView6.setTextColor(Color.WHITE);
                    } else {
                        final Toast toast = Toast.makeText(PlaceSecActivity.this, "不可以打同類!!", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 800);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String ree = "";
                int rr = 0;
                int runeid = 0;
                int stone = 0;
                try {
                    Random random = new Random();
                    rr = random.nextInt(2);
                    if (rr == 0) {
                        runeid = 1;
                        stone = 5;
                    } else {
                        runeid = 2;
                        stone = 3;
                    }
                    ree = Httpconnect.httpget("http://140.119.163.40:8080/GeniusLoci/userRuneList/app/get/" + txt_user + "/"+runeid+"/"+stone+"/");
                } catch (Exception e) {
                    Toast.makeText(PlaceSecActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                // result
                if (ree.equals("success\n")) {
                    final Toast toast2 = Toast.makeText(PlaceSecActivity.this, "got "+runeid+" ^^", Toast.LENGTH_SHORT);
                    toast2.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast2.cancel();
                        }
                    }, 200);
                } else {
                    final Toast toast2 = Toast.makeText(PlaceSecActivity.this, "you can't get "+runeid+" ><", Toast.LENGTH_SHORT);
                    toast2.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast2.cancel();
                        }
                    }, 500);
                }
                refresh();
            }
        });


        final TextView myTextView8 = (TextView) findViewById(R.id.patrol);
        myTextView8.setText("巡邏");
        myTextView8.setTextSize(20);
        myTextView8.setTextColor(Color.WHITE);
        myTextView8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myTextView8.setTextColor(Color.RED);

                String re = "";
                try {
                    re = Httpconnect.httpost2("http://140.119.163.40:8080/Spring08/app/checkinList/" + txt_user, "id=0&placeid=" + placeid + "&longitude=121.2&latitude=223.5");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (intent_vi.equals(che_vi)) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.hasVibrator();
                    vibrator.vibrate(100);
                }

                if (intent_me.equals(che_me)) {
                    if (re.equals("success")) {
                        soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.jump);
                        soundjump.start();
                        soundjump.seekTo(300);
                    } else {
                        soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.hax);
                        soundjump.start();
//                        soundjump.seekTo(100);
                    }
                }
//                Toast.makeText(PlaceSecActivity.this, "me " + intent_me+ " ,vi "+intent_vi, Toast.LENGTH_SHORT).show();



                if (!re.equals("success")) {
                    final Toast toast2 = Toast.makeText(PlaceSecActivity.this, re, Toast.LENGTH_SHORT);
                    toast2.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast2.cancel();
                        }
                    }, 500);
                } else {
                    final Toast t = Toast.makeText(PlaceSecActivity.this, "馬納值增加", Toast.LENGTH_SHORT);
                    t.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            t.cancel();
                        }
                    }, 300);
                }
                if (re != "") {
                    myTextView8.setTextColor(Color.WHITE);
                }

                refresh();

            }
        });

//        handler.postDelayed(runnable, 2000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private ImageView thirdImage;

    private void findViews() {
        thirdImage = (ImageView) findViewById(R.id.placeimg);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Drawable loadImageFromURL(String image) {
        try {
            byte[] data = Base64.decode(image, Base64.DEFAULT);
            Drawable draw = new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
            return draw;
        } catch (Exception e) {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
        }
    }


    private void refresh() {
        String placejson = "";
        String userjson = "";
        String stelejson = "";
        try {
            placejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/place/" + placeid);
            userjson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/user/" + txt_user);
            stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + placeid);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        String placename = "";
        int vote = 0;
        String hp = "";
        String camp = "";
        String name = "";

        try {
            JSONArray placelist = new JSONArray(placejson);
            JSONArray userlist = new JSONArray(userjson);
            JSONArray stelelist = new JSONArray(stelejson);


            placename = placelist.getJSONObject(0).getString("name");
            vote = userlist.getJSONObject(0).getInt("votes");
            hp = stelelist.getJSONObject(0).getString("hp");
            camp = stelelist.getJSONObject(0).getString("camp");
            name = stelelist.getJSONObject(0).getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView textview = (TextView) findViewById(R.id.patrolvalue);
        textview.setText("馬納值:" + vote);
        if(placename.equals("山上蔣公銅像")) {
            placename = "黑馬蔣公";
        }
        TextView myTextView5 = (TextView) findViewById(R.id.locationname);
        myTextView5.setText(placename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView tv = (TextView) findViewById(R.id.belong);
        switch (name) {
            case "Ruyen":
                tv.setText("屬於:" + camp);
                break;
            default:
                tv.setText("屬於:" + txt_party + camp);
                break;
        }
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname);
        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood);
        switch (name) {
            case "Ruyen":
                myTextView1.setText("敵方的生命值:" + hp);
                break;
            default:
                myTextView1.setText("我的生命值:" + hp);
                break;
        }
        myTextView1.setTextSize(15);
        myTextView1.setTextColor(Color.BLACK);


        myProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        myProgressBar.setProgress(Integer.parseInt(hp) * 5);
        Resources res = getResources();
        switch (name) {
            case "Ruyen":
                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
                break;
            default: {
                if (txt_party.equals("Sinae")) {
                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.green_progressbar));
//                    occupied();
                    break;
                } else {
                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.red_progressbar));
//                    occupied();
                    break;
                }
            }
        }

//        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        ratingBar.setRating((Float.parseFloat(hp)) / 5);
//        switch (name) {
//            case "Ruyen": ratingBar.setProgressDrawable(getResources().getDrawable(R.drawable.ratingbar_color));break;
//            default: {
//                if(StoryActivity.party == "Sinae") ratingBar.setProgressDrawable(getResources().getDrawable(R.drawable.ratingbar_color_s));
//                else ratingBar.setProgressDrawable(getResources().getDrawable(R.drawable.ratingbar_color_a)); break;
//            }
//        }
//        Toast.makeText(PlaceSecActivity.this,
//                String.valueOf((Float.parseFloat(hp))/5), Toast.LENGTH_SHORT).show();
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情

//            refresh();
//            num++;
//            handler.postDelayed(this, 2000);
//            if (num == 2) {
//                finish();
//            }
        }
    };

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "PlaceSec Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.louise.test/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "PlaceSec Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.louise.test/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void occupied() {
//        final Dialog dialog = new Dialog(PlaceSecActivity.this);
//
//        //setting custom layout to dialog
//        dialog.setContentView(R.layout.custom_dialog_layout);
//        dialog.setTitle("Custom Dialog");
//
//        //adding text dynamically
//        TextView txt = (TextView) dialog.findViewById(R.id.textView);
//        txt.setText("Put your dialog text here.");
//
//        ImageView image = (ImageView)dialog.findViewById(R.id.image);
//        image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
//
//        //adding button click event
//        Button dismissButton = (Button) dialog.findViewById(R.id.button);
//        dismissButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }


//    public void coin_generate() {
//        String re = "";
//        int rr = 0;
//        int runeid = 0;
//        int stone = 0;
//        try {
//            Random random = new Random();
//            rr = random.nextInt(2);
//            if (rr == 0) {
//                runeid = 1;
//                stone = 5;
//            } else {
//                runeid = 2;
//                stone = 3;
//            }
//            re = Httpconnect.httpget("http://140.119.163.40:8080/GeniusLoci/userRuneList/app/get/" + txt_user + "/"+runeid+"/"+stone+"/");
//        } catch (Exception e) {
//            Toast.makeText(PlaceSecActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
//        // result
//        if (re.equals("success\n")) {
//            final Toast toast2 = Toast.makeText(PlaceSecActivity.this, "got "+runeid+" ^^", Toast.LENGTH_SHORT);
//            toast2.show();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    toast2.cancel();
//                }
//            }, 200);
//        } else {
//            final Toast toast2 = Toast.makeText(PlaceSecActivity.this, "you can't get "+runeid+" ><", Toast.LENGTH_SHORT);
//            toast2.show();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    toast2.cancel();
//                }
//            }, 500);
//        }
//    }
}
