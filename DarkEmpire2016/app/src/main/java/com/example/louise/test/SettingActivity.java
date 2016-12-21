package com.example.louise.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    Switch switchButton, switchButton2;
    private String switchOn = "ON";
    private String switchOff = "OFF";
    private Button settingclick;
    private Button explainbtn;
    private Boolean re_vi=false;
    private Boolean re_me=false;
//    private String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "output.txt";//擷取手機的/sdcard路徑並給定檔案名稱


    private String res="OFF,OFF";
    String che_vi = "";
    String che_me = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/output.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine(); //readLine()讀取一整行
//            Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_LONG).show();

            if (temp != null) {
                String[] datas = temp.split(",");
                che_vi = datas[1];
                che_me = datas[0];
                res = che_me + "," + che_vi;
//                Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_SHORT).show();

                if (che_vi.equals(switchOn)) re_vi = true;
                else re_vi = false;
                if (che_me.equals(switchOn)) re_me = true;
                else re_me = false;
            } else {
                che_vi = che_me = switchOff;
            }
        } catch (Exception e) {}


        explainbtn = (Button) findViewById(R.id.explain);
        explainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, ExplainAvtivity.class);
                startActivity(intent);
            }
        });
            // For first switch button
            switchButton = (Switch) findViewById(R.id.switchButton);
            switchButton.setChecked(re_me);
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked) {
                        che_me = switchOn;
                        Toast.makeText(SettingActivity.this, "change to: " + che_me, Toast.LENGTH_SHORT).show();
                    } else {
                        che_me = switchOff;
                        Toast.makeText(SettingActivity.this, "change to: " + che_me, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // for second switch button
            switchButton2 = (Switch) findViewById(R.id.switchButton2);
            switchButton2.setChecked(re_vi);
            switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                    if (bChecked || switchButton2.isChecked()) {
                        che_vi = switchOn;
                        Toast.makeText(SettingActivity.this, "change to: " + che_vi, Toast.LENGTH_SHORT).show();
                    } else {
                        che_vi = switchOff;
                        Toast.makeText(SettingActivity.this, "change to: " + che_vi, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            settingclick = (Button) findViewById(R.id.settingclick);
            settingclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (switchButton.isChecked() && switchButton2.isChecked()) {
                            res = switchOn + "," + switchOn;
                        } else if (switchButton.isChecked() && !switchButton2.isChecked()){
                            res = switchOn + "," + switchOff;
                        } else if (!switchButton.isChecked() && switchButton2.isChecked()){
                            res = switchOff + "," + switchOn;
                        } else {
                            res = switchOff + "," + switchOff;
                        }
//                        Toast.makeText(SettingActivity.this, "res = "+res, Toast.LENGTH_SHORT).show();
                        FileWriter fw = new FileWriter(new File("sdcard/darkempire/output.txt"));
                        final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                        bw.write(res);
                        bw.close();
                        Toast.makeText(SettingActivity.this, "save success", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }


}


