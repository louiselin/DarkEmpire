package com.example.louise.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ProtocolException;


public class ProfileUpdateActivity extends AppCompatActivity {
    private String url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/"+IndexActivity.userid;
    private String stuid="";
    private String name="";
    private String email="";
    private int level=0;
//    private int exp=0;
//    private int votes = 0;
    private Long user_id = 1l;
    private int count = 0;
    private String txt_party = "";
    private String txt_user = "";
    private String userjson="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
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

        refresh();

        final Button alerchange = (Button) findViewById(R.id.alertchange);
        alerchange.setOnClickListener(new View.OnClickListener() {
            String updatejson = "";
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(ProfileUpdateActivity.this);
                final View v = inflater.inflate(R.layout.porfileupdate, null);
                final EditText nametext = (EditText) (v.findViewById(R.id.a_nametext));
                final EditText emailtext = (EditText) (v.findViewById(R.id.a_emailtext));
                nametext.setText(name);
                emailtext.setText(email);
                //語法一：new AlertDialog.Builder(主程式類別).XXX.XXX.XXX;
                new AlertDialog.Builder(ProfileUpdateActivity.this)
                        .setView(v)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    String name = nametext.getText().toString(); // update name
                                    String email = emailtext.getText().toString(); // update email
                                    String re = "";


                                    // create connection to post update data to web api
                                    try {

                                        updatejson = "["+Httpconnect.httpget(url)+"]";
                                        JSONArray userlist = new JSONArray(updatejson);

                                        String user_id = userlist.getJSONObject(0).getString("user_id");
                                        if(user_id.equals(txt_user)) {

                                            re = Httpconnect.httpost2(url, "user_name=" + name + "&email=" + email);
                                            Toast.makeText(ProfileUpdateActivity.this, re, Toast.LENGTH_SHORT).show();
                                            refresh();

                                        } else {
                                            re = "failed";
                                            Toast.makeText(ProfileUpdateActivity.this, re, Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (Exception e) {
                                    }



                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
            }
        });


    }

    private void refresh() {
        url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/"+txt_user;
        try {
            userjson = "["+Httpconnect.httpget(url)+"]";

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        try {
            JSONArray userlist = new JSONArray(userjson);

            stuid= userlist.getJSONObject(0).getString("student_id");
            name= userlist.getJSONObject(0).getString("user_name");
            email= userlist.getJSONObject(0).getString("email");
            level= userlist.getJSONObject(0).getInt("level");
            user_id = userlist.getJSONObject(0).getLong("user_id");
            count = userlist.getJSONObject(0).getInt("count");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView teamname = (TextView) findViewById(R.id.teamname);
        teamname.setText("I am a "+txt_party +" player.");

        final TextView myTextView5 = (TextView)findViewById(R.id.keepername);
        myTextView5.setText("玩家匿稱：" + name );

        final TextView myTextView6 = (TextView)findViewById(R.id.upemail);
        myTextView6.setText("玩家信箱：" + email);

        final TextView myTextView8 = (TextView)findViewById(R.id.keeperlevel);
        myTextView8.setText("遊戲等級：" + level);


        final TextView myTextView9 = (TextView)findViewById(R.id.keepervotes);
        myTextView9.setText("使用者編號：" + user_id);

        final TextView sum_checkin = (TextView)findViewById(R.id.sum_checkin);
        sum_checkin.setText("累積巡邏次數：" + count);

    }


}
