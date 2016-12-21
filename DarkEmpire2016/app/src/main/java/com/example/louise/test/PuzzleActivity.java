package com.example.louise.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {

    private String txt_party = "";
    private String txt_user = "";
    private Button relreasesp;
    private TextView superpower;
    private JSONArray sforcelist = null;
    private int s_userid = 0;
    private String s_username = "";
    private int s_camp = 0;
    private int releaseforce = 0;
    private int weaponid = 0;
    private String re = "";
    private TextView title_superpower;
    private Context mContext;
    private List<String> un = new ArrayList<>();
    private List<Integer> uid = new ArrayList<>();
    private List<String> listparty = new ArrayList<>();
    private List<String> releasesp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);


        mContext = getApplicationContext();


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

        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/weapon.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine();
            if (temp != null) {
                weaponid = Integer.parseInt(temp);

            } else {
                weaponid = 1;
            }
        } catch (Exception e) {}


        title_superpower = (TextView) findViewById(R.id.title_superpower);
        superpower = (TextView) findViewById(R.id.superpower);
        String sforceurl = "http://140.119.163.40:8080/GeniusLoci/sforce/app/list/";
        String sforcejson = "";
        try {
            sforcejson = Httpconnect.httpget(sforceurl);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int size = 0;
        try {
            sforcelist = new JSONArray(sforcejson);
            size = sforcelist.length();
//            Toast.makeText(PuzzleActivity.this, size+"", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < size; j++) {
            try {
                String party = "";
                String releaseornot = "";
                s_userid = sforcelist.getJSONObject(j).getInt("userid");
                s_username = sforcelist.getJSONObject(j).getString("username");
                s_camp = sforcelist.getJSONObject(j).getInt("camp");
                releaseforce = sforcelist.getJSONObject(j).getInt("releaseforce");


                if (s_camp == 2) {
                    party = "Sinae";
                } else if (s_camp == 3) {
                    party = "Antayen";
                } else {
                    party = "@@";
                }

                if (releaseforce == 1) {
                    releaseornot = " (已釋放!)";

                } else {
                    releaseornot = " (未釋放!)";
                }

                uid.add(s_userid);
                un.add(s_username);
                listparty.add(party);
                releasesp.add(releaseornot);


            } catch (Exception e) {
                Toast.makeText(PuzzleActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }


        for (int i = 0; i < uid.size(); i++) {

            re += "陣營: " + listparty.get(i) + ", 玩家名稱: " + uid.get(i) + " " + un.get(i) + releasesp.get(i) + "\n";

        }
        superpower.setTextSize(15);
        superpower.setText(re);

        relreasesp = (Button) findViewById(R.id.iv_sp_2);


        if (weaponid == 0) {
            title_superpower.setText("恭喜您是超原力使者!");
            title_superpower.setTextSize(25);
            relreasesp.setOnClickListener(new View.OnClickListener() {
                String re = "";

                public void onClick(View view) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(PuzzleActivity.this);
                    ad.setTitle("是否釋放超原力？");
//                ad.setMessage("");
                    ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            // TODO Auto-generated method stub
                            try {
                                re = Httpconnect.httpget("http://140.119.163.40:8080/GeniusLoci/sforce/app/releaseforce/" + txt_user);
                                if (!re.replace("\n", "").replace(" ", "").equals("false")) {
                                    try {

                                        FileWriter fw = new FileWriter(new File("sdcard/darkempire/weapon.txt"));
                                        final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                                        bw.write("1");
                                        bw.close();
                                    } catch (Exception e) {
                                        Toast.makeText(PuzzleActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    final Toast toast = Toast.makeText(PuzzleActivity.this, "已釋放超原力 ><", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 800);


                                    finish();

                                }
                            } catch (Exception e) {
                                Toast.makeText(PuzzleActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            return;
                        }
                    });
                    ad.show();
                }
            });
        } else {
            relreasesp.setTextColor(Color.parseColor("#D8D8D8"));
            Drawable drawableRed = ContextCompat.getDrawable(mContext, R.drawable.ic_flash_on_24dp1);
            drawableRed.setBounds(
                    0, // left
                    0, // top
                    drawableRed.getIntrinsicWidth(), // right
                    drawableRed.getIntrinsicHeight() // bottom
            );
            relreasesp.setCompoundDrawables(drawableRed ,null, null, null);

            title_superpower.setText("您不是超原力使者喔 @@");
            title_superpower.setTextSize(25);
            relreasesp.setClickable(false);

        }

    }

}
