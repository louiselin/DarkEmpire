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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
//    private int weaponid = 0;
    private int w = 0;
    private int num = 0;
    private String weaponname = "";
    List<String> wname = new ArrayList<>();
    List<Integer> wid = new ArrayList<>();
//    private int powerup = 0;
    private TextView desc_weapon;
    private String re_powerup = "";
//    private String re_p = "";
    private int w_item_id = 0;



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

            if (temp != null) {
                String[] datas = temp.split(",");
                placename = datas[1];
                placeid = Integer.valueOf(datas[0]);
            }
        } catch (Exception e) {}



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


        String placejson = "";
        String imagejson = "";
        String stelejson = "";
        String weaponjson = "";

        String n_placejson = "";
        try {
//            String imagetest = Httpconnect.httpget("http://140.119.163.40:8080/GameImg/image/app/" + placeid);
//            Toast.makeText(PlaceSecActivity.this, imagetest, Toast.LENGTH_SHORT).show();
            weaponjson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/item/list");

            placejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/place/" + placeid);
            imagejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/image/" + placeid);
            stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + placeid);

            n_placejson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/list");


        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int camp_id = 0, keeper_id = 0, n_hp = 0;
        try {
            JSONArray n_placelist = new JSONArray(n_placejson);
            int p =  placeid - 1;
            camp_id = n_placelist.getJSONObject(p).getInt("camp_id");
            keeper_id = n_placelist.getJSONObject(p).getInt("keeper_id");
            n_hp = n_placelist.getJSONObject(p).getInt("hp");

            Log.e("placeid info", p+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String placename = "";
        String image = "";
        String camp = "";
        final int weapon_id = 0;
        int weapon_mana = 0;
        String weapon_name = "";
        int weapon_atk = 0;

        desc_weapon = (TextView) findViewById(R.id.desc_weapon);
        weapon = (ImageView) findViewById(R.id.weapon);
        weapon.setImageResource(R.drawable.pill_gray);
        weapon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String weapon_userjson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire//app/ver1.0/userItem/"+txt_user);
                    final JSONArray weapon_list = new JSONArray(weapon_userjson);

                    final String[] weapon_c = {"不用聖水",
                            "紅聖水x"+weapon_list.getJSONObject(0).getInt("quantity"),
                            "黃聖水x"+weapon_list.getJSONObject(2).getInt("quantity"),
                            "藍聖水x"+weapon_list.getJSONObject(3).getInt("quantity")};


                    final Integer[] icons = new Integer[] {R.drawable.pill_gray
                            , R.drawable.pill_red
                            , R.drawable.pill_yell
                            , R.drawable.pill_blue};


                    ListAdapter adapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.weapon_chose, weapon_c) {

                        ViewHolder holder;

                        class ViewHolder {
                            ImageView icon;
                            TextView title;
                        }

                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                    .getSystemService(
                                            Context.LAYOUT_INFLATER_SERVICE);

                            if (convertView == null) {
                                convertView = inflater.inflate(
                                        R.layout.weapon_chose, null);

                                holder = new ViewHolder();
                                holder.icon = (ImageView) convertView.findViewById(R.id.w_icon);
                                holder.title = (TextView) convertView.findViewById(R.id.w_title);
                                convertView.setTag(holder);
                            } else {
                                // view already defined, retrieve view holder
                                holder = (ViewHolder) convertView.getTag();
                            }

                            holder.title.setText(weapon_c[position]);
                            holder.icon.setImageResource(icons[position]);

                            return convertView;
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaceSecActivity.this);
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    w_item_id = item + 1;
//                                    Toast.makeText(PlaceSecActivity.this, "You selected: " + weapon_c[item], Toast.LENGTH_LONG).show();

                                    try {
                                        switch (w_item_id) {
                                            case 2: {
                                                weapon.setImageResource(R.drawable.pill_red);
                                                desc_weapon.setText("[選擇] 紅聖水x" + weapon_list.getJSONObject(0).getInt("quantity"));
                                            }
                                            break;
                                            case 3: {
                                                weapon.setImageResource(R.drawable.pill_yell);
                                                desc_weapon.setText("[選擇] 黃聖水x" + weapon_list.getJSONObject(2).getInt("quantity"));
                                            }
                                            break;
                                            case 4: {
                                                weapon.setImageResource(R.drawable.pill_blue);
                                                desc_weapon.setText("[選擇] 藍聖水x" + weapon_list.getJSONObject(3).getInt("quantity"));
                                            }
                                            break;
                                            default: {
                                                weapon.setImageResource(R.drawable.pill_gray);
                                                desc_weapon.setText("[選擇] 不用聖水");
                                            }
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    Toast.makeText(PlaceSecActivity.this, "You selected: " + w_item_id, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            JSONArray placelist = new JSONArray(placejson);
            JSONArray imagelist = new JSONArray(imagejson);
            JSONArray stelelist = new JSONArray(stelejson);
            JSONArray weaponlist = new JSONArray(weaponjson);





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
//        switch (name) {
//            case "Ruyen":
//                tv.setText("屬於:" + camp);
//                break;
//            default:
//                tv.setText("屬於:" + txt_party + camp);
//                break;
//        }
        tv.setText("屬於:" + camp_id);
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname);
//        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setText("石碑守護者:" + keeper_id);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood);
//        switch (name) {
//            case "Ruyen":
//                myTextView1.setText("敵方的生命值:" + hp);
//                break;
//            default:
//                myTextView1.setText(txt_party + "方生命值:" + hp);
//                break;
//        }
        switch (camp_id) {
            case 3: myTextView1.setText("敵方的生命值:" + n_hp);
                break;
            default: myTextView1.setText(txt_party + "方生命值:" + n_hp);
                break;
        }
        myTextView1.setTextSize(15);
        myTextView1.setTextColor(Color.BLACK);

        myProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        myProgressBar.setProgress(n_hp);
        Resources res = getResources();
//        switch (name) {
//            case "Ruyen":
//                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
//                break;
//            default: {
//                if (txt_party.equals("Sinae")) {
//                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.green_progressbar));
//                    break;
//                } else {
//                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.red_progressbar));
//                    break;
//                }
//            }
//        }
        switch (camp_id) {
            case 3: myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
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

        String url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list/" + txt_user + "/";
        String userjson = "";

        try {
            userjson = Httpconnect.httpget(url);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int votes = 0;

        try {
            JSONArray userlist = new JSONArray(userjson);
            votes = userlist.getJSONObject(0).getInt("mana_now");
            username = userlist.getJSONObject(0).getString("user_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView textview = (TextView) findViewById(R.id.patrolvalue);
        textview.setText("馬納值:" + votes);


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
//                myTextView6.setTextColor(Color.RED);
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



//                /app/ver1.0/checkinPurify/{user_id}/{place_id}/{action}/{longitude}/{latitude}/{item_id}

                String fight = "";
                try {
                    fight = Httpconnect.httpost("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"
                            + txt_user + "/" + placeid + "/2/" + MapsActivity.currlo + "/"
                            + MapsActivity.currla + "/" + w_item_id + "/");
                    Log.e("lo", MapsActivity.currlo+"");
                    Log.e("la", MapsActivity.currla+"");
                    Toast.makeText(PlaceSecActivity.this, fight, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TODO FIGHT RESULT HERE


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
//                myTextView8.setTextColor(Color.RED);

                String re = "";
                try {
//                    re = Httpconnect.httpost2("http://140.119.163.40:8080/Spring08/app/checkinList/" + txt_user, "id=0&placeid=" + placeid + "&longitude=121.2&latitude=223.5");
                    re = Httpconnect.httpost("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"
                            + txt_user + "/" + placeid + "/1/" + MapsActivity.currlo + "/"
                            + MapsActivity.currla + "/" + w_item_id + "/");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (intent_vi.equals(che_vi)) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.hasVibrator();
                    vibrator.vibrate(100);
                }

                if (intent_me.equals(che_me)) {
                    if (re.replace("\n", "").equals("success")) {
                        soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.jump);
                        soundjump.start();
                        soundjump.seekTo(300);
                    } else {
                        soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.hax);
                        soundjump.start();
                    }
                }

                Toast.makeText(PlaceSecActivity.this, re.replace("\n", ""), Toast.LENGTH_LONG).show();

//                if (!re.equals("success")) {
//                    final Toast toast2 = Toast.makeText(PlaceSecActivity.this, re, Toast.LENGTH_SHORT);
//                    toast2.show();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            toast2.cancel();
//                        }
//                    }, 500);
//                } else {
//                    final Toast t = Toast.makeText(PlaceSecActivity.this, "馬納值增加", Toast.LENGTH_SHORT);
//                    t.show();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            t.cancel();
//                        }
//                    }, 300);
//                }


                refresh();

            }
        });
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


        // TODO refresh result here

        String weaponjson = "";
        String n_placejson = "";
        try {
            weaponjson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/item/list");
            n_placejson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/list");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int camp_id = 0, keeper_id = 0, n_hp = 0;
        try {
            JSONArray n_placelist = new JSONArray(n_placejson);
            int p =  placeid - 1;
            camp_id = n_placelist.getJSONObject(p).getInt("camp_id");
            keeper_id = n_placelist.getJSONObject(p).getInt("keeper_id");
            n_hp = n_placelist.getJSONObject(p).getInt("hp");

            Log.e("placeid info", p+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String placename = "";
        String image = "";
        String camp = "";
        final int weapon_id = 0;
        int weapon_mana = 0;
        String weapon_name = "";
        int weapon_atk = 0;

        desc_weapon = (TextView) findViewById(R.id.desc_weapon);
        weapon = (ImageView) findViewById(R.id.weapon);
        weapon.setImageResource(R.drawable.pill_gray);
        weapon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String weapon_userjson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire//app/ver1.0/userItem/"+txt_user);
                    final JSONArray weapon_list = new JSONArray(weapon_userjson);

                    final String[] weapon_c = {"不用聖水",
                            "紅聖水x"+weapon_list.getJSONObject(0).getInt("quantity"),
                            "黃聖水x"+weapon_list.getJSONObject(2).getInt("quantity"),
                            "藍聖水x"+weapon_list.getJSONObject(3).getInt("quantity")};


                    final Integer[] icons = new Integer[] {R.drawable.pill_gray
                            , R.drawable.pill_red
                            , R.drawable.pill_yell
                            , R.drawable.pill_blue};


                    ListAdapter adapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.weapon_chose, weapon_c) {

                        ViewHolder holder;

                        class ViewHolder {
                            ImageView icon;
                            TextView title;
                        }

                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                    .getSystemService(
                                            Context.LAYOUT_INFLATER_SERVICE);

                            if (convertView == null) {
                                convertView = inflater.inflate(
                                        R.layout.weapon_chose, null);

                                holder = new ViewHolder();
                                holder.icon = (ImageView) convertView.findViewById(R.id.w_icon);
                                holder.title = (TextView) convertView.findViewById(R.id.w_title);
                                convertView.setTag(holder);
                            } else {
                                // view already defined, retrieve view holder
                                holder = (ViewHolder) convertView.getTag();
                            }

                            holder.title.setText(weapon_c[position]);
                            holder.icon.setImageResource(icons[position]);

                            return convertView;
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaceSecActivity.this);
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    w_item_id = item + 1;
//                                    Toast.makeText(PlaceSecActivity.this, "You selected: " + weapon_c[item], Toast.LENGTH_LONG).show();

                                    try {
                                        switch (w_item_id) {
                                            case 2: {
                                                weapon.setImageResource(R.drawable.pill_red);
                                                desc_weapon.setText("[選擇] 紅聖水x" + weapon_list.getJSONObject(0).getInt("quantity"));
                                            }
                                            break;
                                            case 3: {
                                                weapon.setImageResource(R.drawable.pill_yell);
                                                desc_weapon.setText("[選擇] 黃聖水x" + weapon_list.getJSONObject(2).getInt("quantity"));
                                            }
                                            break;
                                            case 4: {
                                                weapon.setImageResource(R.drawable.pill_blue);
                                                desc_weapon.setText("[選擇] 藍聖水x" + weapon_list.getJSONObject(3).getInt("quantity"));
                                            }
                                            break;
                                            default: {
                                                weapon.setImageResource(R.drawable.pill_gray);
                                                desc_weapon.setText("[選擇] 不用聖水");
                                            }
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

//                                    Toast.makeText(PlaceSecActivity.this, "You selected: " + w_item_id, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        if(placename.equals("山上蔣公銅像")) {
            placename = "黑馬蔣公";
        }
        TextView myTextView5 = (TextView) findViewById(R.id.locationname);
        myTextView5.setText(placename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView tv = (TextView) findViewById(R.id.belong);
//        switch (name) {
//            case "Ruyen":
//                tv.setText("屬於:" + camp);
//                break;
//            default:
//                tv.setText("屬於:" + txt_party + camp);
//                break;
//        }
        tv.setText("屬於:" + camp_id);
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname);
//        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setText("石碑守護者:" + keeper_id);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood);
//        switch (name) {
//            case "Ruyen":
//                myTextView1.setText("敵方的生命值:" + hp);
//                break;
//            default:
//                myTextView1.setText(txt_party + "方生命值:" + hp);
//                break;
//        }
        switch (camp_id) {
            case 3: myTextView1.setText("敵方的生命值:" + n_hp);
                break;
            default: myTextView1.setText(txt_party + "方生命值:" + n_hp);
                break;
        }
        myTextView1.setTextSize(15);
        myTextView1.setTextColor(Color.BLACK);

        myProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        myProgressBar.setProgress(n_hp);
        Resources res = getResources();
//        switch (name) {
//            case "Ruyen":
//                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
//                break;
//            default: {
//                if (txt_party.equals("Sinae")) {
//                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.green_progressbar));
//                    break;
//                } else {
//                    myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.red_progressbar));
//                    break;
//                }
//            }
//        }
        switch (camp_id) {
            case 3: myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
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
        String url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list/" + txt_user + "/";
        String userjson = "";

        try {
            userjson = Httpconnect.httpget(url);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int votes = 0;

        try {
            JSONArray userlist = new JSONArray(userjson);
            votes = userlist.getJSONObject(0).getInt("mana_now");
            username = userlist.getJSONObject(0).getString("user_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView textview = (TextView) findViewById(R.id.patrolvalue);
        textview.setText("馬納值:" + votes);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PlaceSec Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.louise.test/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PlaceSec Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.louise.test/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
