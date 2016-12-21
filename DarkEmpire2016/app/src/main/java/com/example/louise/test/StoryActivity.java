package com.example.louise.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.ProtocolException;

public class StoryActivity extends AppCompatActivity {

    private String story = "";
    private String mainurl = "http://140.119.163.40:8080/DarkEmpire";
    public static String party = "";
    private ImageView party_s;
    private ImageView party_a;
//    private String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);


            story = "首次登入使用說明\n\n在拳杉堡對抗暗黑勢力的你\n將選擇加入席奈或是安塔雅族\n\n"
                    + "你必須巡邏並淨化神殿\n以捍衛您族人的勢力\n\n暗黑勢力是兩族人共同的敵人\n當所有神殿遭暗黑勢力全面入侵時\n你必須與族人合作\n"
                    + "到大神殿救援拳杉堡\n\n當你成為超原力使者時\n你將負起保護大神殿的任務\n\n";
            TextView textstory = (TextView) findViewById(R.id.story);
            textstory.setText(story);
            textstory.setTextSize(18);
            System.gc();

        party_a = (ImageView) findViewById(R.id.party_a);
        party_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(StoryActivity.this);
                ad.setTitle("我要當安塔雅人囉?");
                ad.setPositiveButton("我要當安塔雅人^^", new DialogInterface.OnClickListener() {//退出按鈕
                    public void onClick(DialogInterface dialog, int i) {
                        // TODO Auto-generated method stub
                        party = "Antayen";
                        String re = "";
//                        /app/ver1.0/user/camp/{user_id}/{camp_id}
//                        String url = "http://140.119.163.40:8080/Spring08/app/usercamp/"+IndexActivity.userid+"/3/";
                        String url = mainurl + "/app/ver1.0/user/camp/" + IndexActivity.userid + "/1";
                        Log.e("url", url);
                        Toast.makeText(StoryActivity.this, url, Toast.LENGTH_SHORT).show();
                        try {

                            re = Httpconnect.httpost(url);
                            Toast.makeText(StoryActivity.this, re.replace("\n", ""), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(StoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }


                        if (re.replace("\n", "").replace(" ", "").equals("success")) {
                            try {
                                String res = IndexActivity.userid + "," + party + ",1";
                                FileWriter fw = new FileWriter(new File("sdcard/darkempire/profile.txt"));
                                final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                                bw.write(res);
                                bw.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(StoryActivity.this, "Not Save!", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent();
                        intent.setClass(StoryActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                ad.setNegativeButton("不要啦我要換><", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        return;
                    }
                });
                ad.show();
            }
        });
        party_s = (ImageView) findViewById(R.id.party_s);
        party_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(StoryActivity.this);
                ad.setTitle("我要當席奈人囉?");
                ad.setPositiveButton("我要當席奈人^^", new DialogInterface.OnClickListener() {//退出按鈕
                    public void onClick(DialogInterface dialog, int i) {
                        // TODO Auto-generated method stub
                        party = "Sinae";
                        String re = "";
//                        String url = "http://140.119.163.40:8080/Spring08/app/usercamp/"+IndexActivity.userid+"/2/";
                        String url = mainurl + "/app/ver1.0/user/camp/" + IndexActivity.userid + "/2";
                        try {
                            re = Httpconnect.httpost(url);
                            Toast.makeText(StoryActivity.this, re.replace("\n", "").replace(" ", ""), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(StoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (re.replace("\n", "").replace(" ", "").equals("success")) {
                            try {
                                String res = IndexActivity.userid + "," + party + ",2";
                                FileWriter fw = new FileWriter(new File("sdcard/darkempire/profile.txt"));
                                final BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                                bw.write(res);
                                bw.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(StoryActivity.this, "Not Save!", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent();
                        intent.setClass(StoryActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                ad.setNegativeButton("不要啦我要換><", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        return;
                    }
                });
                ad.show();
            }
        });


//            Button storybtn = (Button) findViewById(R.id.party);
//            storybtn.setTextColor(0xffffffff);
//            storybtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    AlertDialog.Builder ad = new AlertDialog.Builder(StoryActivity.this);
//                    ad.setTitle("決定好陣營了嗎？");
//                    ad.setPositiveButton("席奈人", new DialogInterface.OnClickListener() {//退出按鈕
//                        public void onClick(DialogInterface dialog, int i) {
//                            // TODO Auto-generated method stub
//                            party = "Sinae";
//                            Intent intent = new Intent();
//                            intent.setClass(StoryActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    ad.setNegativeButton("安塔雅人", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int i) {
//                            party = "Antayen";
//                            Intent intent = new Intent();
//                            intent.setClass(StoryActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    ad.show();
//                }
//            });
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            ConfirmExit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public void ConfirmExit(){
//        AlertDialog.Builder ad=new AlertDialog.Builder(StoryActivity.this);
//        ad.setTitle("退出確認");
//        ad.setMessage("您確定要退出了嗎？");
//        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
//            public void onClick(DialogInterface dialog, int i) {
//                // TODO Auto-generated method stub
//                finish();//關閉activity
//            }
//        });
//        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int i) {
//                return;
//            }
//        });
//        ad.show();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //System.exit(0);
//    }
}
