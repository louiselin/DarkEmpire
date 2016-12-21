package com.example.louise.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
    private Button play;
    private Button profile;
    private Button manual;
    private Button getcoin;
    private Button nccucontact;
    private Button setting;
    private ImageButton setting_coin;
    private Button award;
    private TextView textView;
    private int test = 0;
    private String condition = "";

    private String txt_party = "";
    private String txt_user = "";
    private MediaPlayer mainbtn;
    private String che_vi, che_me;
    private String switchOn = "ON";
    private String switchOff = "OFF";
    private int length = 2;

    public static final String intent_me="ON";
    public static final String intent_vi="ON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

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

//            if(IndexActivity.userid != "35") {
            String keepername = "";
            String userjson = "";
            try {
                userjson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/" + txt_user);

            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                JSONArray userlist = new JSONArray(userjson);
                keepername = userlist.getJSONObject(0).getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }


//        if (Build.VERSION.SDK_INT >= 23) { // platform version android 6
//            test = runStreamWrapper();
//
//        } else {
//            test = 1;
//
//
//        }

//            textView = (TextView) findViewById(R.id.keeper);
//            textView.setText("攻佔吧！" + keepername + " 爲 " + StoryActivity.party + " 戰鬥吧！");
//            textView.setTextSize(15);
//            textView.setTextColor(Color.BLACK);


            setting_coin = (ImageButton) findViewById(R.id.setting_icon);
            setting_coin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            });
            play = (Button) findViewById(R.id.playgame);
            play.setTextColor(0xffffffff);
            play.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            });

            profile = (Button) findViewById(R.id.profile);
            profile.setTextColor(0xffffffff);
            profile.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ProfileUpdateActivity.class);
                    startActivity(intent);
                }
            });

//                award = (Button) findViewById(R.id.award);
//                award.setOnClickListener(new Button.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent();
//                        intent.setClass(MainActivity.this, AwardActivity.class);
//                        startActivity(intent);
//                    }
//                });

            manual = (Button) findViewById(R.id.manual);
            manual.setTextColor(0xffffffff);
            manual.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ManualActivity.class);
                    startActivity(intent);
                }
            });

            getcoin = (Button) findViewById(R.id.getcoin);
            getcoin.setTextColor(0xffffffff);
//            getcoin.setClickable(false);
            getcoin.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CoinListActivity.class);
                    startActivity(intent);
                }
            });

            nccucontact = (Button) findViewById(R.id.nccucontact);
            nccucontact.setTextColor(0xffffffff);
//            nccucontact.setEnabled(false);
            nccucontact.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NCCUContactActivity.class);
                    startActivity(intent);
                }
            });

//        change to put story in this button now !!!!!
            setting = (Button) findViewById(R.id.setting);
            setting.setTextColor(0xffffffff);
            setting.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_me.equals(che_me)) {
                        mainbtn = MediaPlayer.create(MainActivity.this, R.raw.mainbtn);
                        mainbtn.start();
                        mainbtn.seekTo(200);
                    }
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, StoryOnlyActivity.class);
                    startActivity(intent);
                }
            });
//            }
//        } else {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, IndexActivity.class);
//            finish();
//            startActivity(intent);
//        }

    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0;

    @TargetApi(Build.VERSION_CODES.M)
    private int runStreamWrapper () {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        int tt = PackageManager.PERMISSION_GRANTED;
//        Toast toast2 = Toast.makeText(MainActivity.this, "test1: "+ tt, Toast.LENGTH_SHORT);
//        toast2.show();

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) permissionsNeeded.add("WRITE");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) permissionsNeeded.add("READ");
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            Toast toast = Toast.makeText(MainActivity.this, "show package manager!" + PackageManager.PERMISSION_GRANTED, Toast.LENGTH_SHORT);
//            toast.show();

        } else {
//            switch (condition) {
//                case "setting_coin": {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, SettingActivity.class);
//                    startActivity(intent);
//                } break;
//                case "getcoin": {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, CoinListActivity.class);
//                    startActivity(intent);
//                } break;
//                default: {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, MapsActivity.class);
//                    startActivity(intent);
//                } break;
//            }
        }
//        else {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, MapsActivity.class);
//            startActivity(intent);
//        }
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            int ee = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            Toast toast0 = Toast.makeText(MainActivity.this, "test2: " + ee, Toast.LENGTH_SHORT);
//            toast0.show();
//
//        }
//        else {
//            int ss = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            Toast toast0 = Toast.makeText(MainActivity.this, "test3: " + ss, Toast.LENGTH_SHORT);
//            toast0.show();
//        }
//        int ss = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//        int ee = PackageManager.PERMISSION_GRANTED;
//            Toast toast0 = Toast.makeText(MainActivity.this, "test3: " + ss + " ee " + ee, Toast.LENGTH_SHORT);
//            toast0.show();
//        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
//            permissionsNeeded.add("microphone");
//        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
//            permissionsNeeded.add("camera");

//        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
//                String message = "You need to grant access to the " + permissionsNeeded.get(0);
//                Toast toast1 = Toast.makeText(MainActivity.this, "test1: "+permissionsNeeded.get(0), Toast.LENGTH_SHORT);
//                toast1.show();
//                for (int i = 1; i < permissionsNeeded.size(); i++)
//                    message = message + ", " + permissionsNeeded.get(i);
//                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                    }
//                });
//            }
//            Toast toast2 = Toast.makeText(MainActivity.this, "test2: "+ permissionsList.size(), Toast.LENGTH_SHORT);
//            toast2.show();
//            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//        }

//        runStream();
        return tt;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission (List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if(!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel (String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void runStream() {
//        Toast.makeText(this, "Stream is running", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for(int i = 0; i < permissions.length; i ++)
                    perms.put(permissions[i], grantResults[i]);

                if(perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    runStream();
//                    switch (condition) {
//                        case "setting_coin": {
//                            Intent intent = new Intent();
//                            intent.setClass(MainActivity.this, SettingActivity.class);
//                            startActivity(intent);
//                        } break;
//                        case "getcoin": {
//                            Intent intent = new Intent();
//                            intent.setClass(MainActivity.this, CoinListActivity.class);
//                            startActivity(intent);
//                        } break;
//                        default: {
//                            Intent intent = new Intent();
//                            intent.setClass(MainActivity.this, MapsActivity.class);
//                            startActivity(intent);
//                        } break;
//                    }
                } else {
                    Toast.makeText(this, "Some required permissions have been denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}