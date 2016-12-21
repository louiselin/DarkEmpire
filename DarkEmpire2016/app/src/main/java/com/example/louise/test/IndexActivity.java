package com.example.louise.test;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;



public class IndexActivity extends AppCompatActivity {
    private WebView myBrowser;
    public String htmlSourceCode = "";
//    public  String myURL = "http://140.119.163.40:8080/Spring08/app/login";
    public  String myURL = "http://140.119.163.40:8080/DarkEmpire/app/login";

    public static String url;
    public static String userid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        myBrowser = (WebView) findViewById(R.id.loginview);

        myBrowser.getSettings().setJavaScriptEnabled(true);// 支持javascript
        myBrowser.addJavascriptInterface(new Handler(),"handler");  // 相當于在網頁的js中增加一個handler類，實現java與WebView的js交互


        myBrowser.loadUrl(myURL);
        myBrowser.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url2, Bitmap favicon) {
                if (url2.length() > 59) {
                    if (url2.substring(0, 54).equals("http://140.119.163.40:8080/DarkEmpire/app/authenticate")) {

                        String json = "";
                        try {
                            json = Httpconnect.httpget(url2).replace("\n", "").replace(" ", "");

                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }


                        int start = json.indexOf(":")+2;
                        int end = json.indexOf("}")-1;
                        userid = json.substring(start,end);
                        Toast.makeText(IndexActivity.this, userid, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.setClass(IndexActivity.this, StoryActivity.class);
                        IndexActivity.this.finish();
                        startActivity(intent);

                    }
                }
                url2 = null;
                favicon = null;
                view.clearCache(true);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                // 通過內部類定義的方法獲取html頁面加載的內容，這個需要添加在webview加載完成後的回調中
                // view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
                url = null;
                view.clearCache(true);

                //view.loadUrl("javascript:window.handler.show('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                // super.onPageFinished(view, url);
            }
        });
    }


    final class Handler {

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void show(String data) {

            htmlSourceCode = data;
            // Toast.makeText(IndexActivity.this, "htmlSourceCode", Toast.LENGTH_SHORT).show();
            //  new AlertDialog.Builder(IndexActivity.this).setMessage(data).create().show();
        }
    }
}