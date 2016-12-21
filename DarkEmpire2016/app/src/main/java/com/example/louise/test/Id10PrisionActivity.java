package com.example.louise.test;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ProtocolException;

public class Id10PrisionActivity extends AppCompatActivity {
    private Integer placeid;
    private String placename;
    private RatingBar ratingBar;
    private float hp_init = 20;
    private String hp = "";
    private String name = "";
    private String username = "";
    private ProgressBar myProgressBar;
    private MediaPlayer soundjump;
    private MediaPlayer soundpunch;
    private String txt_party = "";
    private String txt_user = "";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CheckBox donotshowagain;
    public static final String PREFS_NAME = "place";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id10_prision);
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

        Button waterprison = (Button) findViewById(R.id.waterprison);
        waterprison.setTextSize(20);
        waterprison.setTextColor(Color.WHITE);
        waterprison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Id10PrisionActivity.this, PrisonActivity.class);
                startActivity(intent);
            }
        });

        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        donotshowagain = (CheckBox) eulaLayout.findViewById(R.id.skip);


        AlertDialog.Builder ad = new AlertDialog.Builder(Id10PrisionActivity.this);
        ad.setView(eulaLayout);
        ad.setTitle("遊戲規則");
        ad.setMessage("您點選的是" + "水牢監獄(水岸電梯)" + "\n\n選擇「巡邏」增加馬納值來守護石碑; 當不幸石碑守護者是敵方的時候以「淨化」掠取，但是會減少馬納值。要注意有秒數限制哦！><\n\n開始吧！勇士！\n");
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

        placeid = 10;
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

        String changename = "水牢監獄";
        TextView myTextView5 = (TextView) findViewById(R.id.locationname10);
        myTextView5.setText(changename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView tv = (TextView) findViewById(R.id.belong10);
        switch (name) {
            case "Ruyen":
                tv.setText("屬於:" + camp);
                break;
            default:
                tv.setText("屬於:" + StoryActivity.party + camp);
                break;
        }
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname10);
        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood10);
        switch (name) {
            case "Ruyen":
                myTextView1.setText("敵方的生命值:" + hp);
                break;
            default:
                myTextView1.setText(StoryActivity.party + "方生命值:" + hp);
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

        myProgressBar = (ProgressBar) findViewById(R.id.progressbar10);
        myProgressBar.setProgress(Integer.parseInt(hp) * 5);
        Resources res = getResources();
        switch (name) {
            case "Ruyen":
                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
                break;
            default: {
                if (StoryActivity.party == "Sinae") {
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

        final TextView textview = (TextView) findViewById(R.id.patrolvalue10);
        textview.setText("馬納值:" + votes);


//        final TextView myTextView6 = (TextView) findViewById(R.id.fight10);
//        final MediaPlayer mp = new MediaPlayer();
//        myTextView6.setText("淨化");
//        myTextView6.setTextSize(20);
//        myTextView6.setTextColor(Color.WHITE);
//        myTextView6.setSoundEffectsEnabled(true);
//
//        myTextView6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                myTextView6.setTextColor(Color.RED);
////                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
////                vibrator.hasVibrator();
////                vibrator.vibrate(100);
////                soundpunch = MediaPlayer.create(PlaceSecActivity.this, R.raw.punch);
////                soundpunch.start();
////                soundpunch.seekTo(100);
//
//                String stelejson = "";
//                String stelename = "";
//                String userjson = "";
//                String username = "";
//                String votes = "";
//                try {
//                    stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + placeid);
//                    userjson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/user/" + IndexActivity.userid);
//                    JSONArray stelelist = new JSONArray(stelejson);
//                    JSONArray userlist = new JSONArray(userjson);
//                    username = userlist.getJSONObject(0).getString("name");
//                    stelename = stelelist.getJSONObject(0).getString("name");
//                    votes = userlist.getJSONObject(0).getString("votes");
//
//                    if (stelename.equals(username) || votes.equals("0")) {
//                        final Toast toast = Toast.makeText(Id10PrisionActivity.this, "攻擊對象是自己或\n 已無馬納值哦QQ", Toast.LENGTH_SHORT);
//                        toast.show();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                toast.cancel();
//                            }
//                        }, 800);
//                    } else {
//                        String att = "";
//                        String api = "";
//                        try {
//                            api = "http://140.119.163.40:8080/Spring08/app/stele/attack/" + placeid + "/" + IndexActivity.userid;
//                            att = Httpconnect.httpget(api);
//
//                        } catch (ProtocolException e) {
//                            e.printStackTrace();
//                        }
//                        final Toast toast = Toast.makeText(Id10PrisionActivity.this, "敵方生命值 -1", Toast.LENGTH_SHORT);
//                        toast.show();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                toast.cancel();
//                            }
//                        }, 300);
//                        if (!att.equals("")) myTextView6.setTextColor(Color.WHITE);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });


        final TextView myTextView8 = (TextView) findViewById(R.id.patrol10);
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
//                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//                vibrator.hasVibrator();
//                vibrator.vibrate(100);
//                if(re.equals("success")) {
//                    soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.jump);
//                    soundjump.start();
//                    soundjump.seekTo(300);
//                } else {
//                    soundjump = MediaPlayer.create(PlaceSecActivity.this, R.raw.hax);
//                    soundjump.start();
//                    soundjump.seekTo(700);
//                }
                if (!re.equals("success")) {
                    final Toast toast2 = Toast.makeText(Id10PrisionActivity.this, re, Toast.LENGTH_SHORT);
                    toast2.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast2.cancel();
                        }
                    }, 500);
                } else {
                    final Toast t = Toast.makeText(Id10PrisionActivity.this, "馬納值 +5", Toast.LENGTH_SHORT);
                    t.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            t.cancel();
                        }
                    }, 300);
                }
                if (re != "") myTextView8.setTextColor(Color.WHITE);
            }
        });

        handler.postDelayed(runnable, 2000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private ImageView thirdImage;
    private void findViews() {
        thirdImage = (ImageView) findViewById(R.id.placeimg10);

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

        TextView textview = (TextView) findViewById(R.id.patrolvalue10);
        textview.setText("馬納值:" + vote);
        String changename = "水牢監獄";
        TextView myTextView5 = (TextView) findViewById(R.id.locationname10);
        myTextView5.setText(changename);
        myTextView5.setTextSize(30);
        myTextView5.setTextColor(Color.BLACK);

        TextView tv = (TextView) findViewById(R.id.belong10);
        switch (name) {
            case "Ruyen":
                tv.setText("屬於:" + camp);
                break;
            default:
                tv.setText("屬於:" + StoryActivity.party + camp);
                break;
        }
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);

        TextView myTextView0 = (TextView) findViewById(R.id.lordname10);
        myTextView0.setText("石碑守護者:" + name);
        myTextView0.setTextSize(15);
        myTextView0.setTextColor(Color.BLACK);

        TextView myTextView1 = (TextView) findViewById(R.id.blood10);
        switch (name) {
            case "Ruyen":
                myTextView1.setText("敵方的生命值:" + hp);
                break;
            default:
                myTextView1.setText("您的生命值:" + hp);
                break;
        }
        myTextView1.setTextSize(15);
        myTextView1.setTextColor(Color.BLACK);


        myProgressBar = (ProgressBar) findViewById(R.id.progressbar10);
        myProgressBar.setProgress(Integer.parseInt(hp) * 5);
        Resources res = getResources();
        switch (name) {
            case "Ruyen":
                myProgressBar.setProgressDrawable(res.getDrawable(R.drawable.black_progressbar));
                break;
            default: {
                if (StoryActivity.party == "Sinae") {
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
            refresh();
            handler.postDelayed(this, 2000);
        }
    };

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
//        final Dialog dialog = new Dialog(Id10PrisionActivity.this);
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
}
