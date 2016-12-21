package com.example.louise.test;



import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NCCUContactActivity extends AppCompatActivity implements OnClickListener {

    private ScrollView svChat;
    private Spinner spDraft;
    private EditText etAddress;
    private Spinner spAddress;
    private Button btnConnect;
    private Button btnClose;
    private EditText etDetails;

    private TextView chatid;
    private EditText etName;
    private EditText etMessage;
    private Button btnSend;
    private String uid = "";

    private String mess = "", name = "你";

    private Long targetType = 1l;
    private String target = "";

    private WebSocketClient client;// 连接客户端
    private DraftInfo selectDraft;// 连接协议

    private String localid = "";
    private String localparty = "";
    private Long group = 1l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nccucontact);

        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[]paths = {"全域廣播", "同族群組", "個人對話"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paths);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target = (String) spinner.getItemAtPosition(position);
                Log.v("item", target);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                target = "全域廣播";
            }
        });


        svChat = (ScrollView) findViewById(R.id.svChat);
        spDraft = (Spinner) findViewById(R.id.spDraft);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spAddress = (Spinner) findViewById(R.id.spAddress);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnClose = (Button) findViewById(R.id.btnClose);
        etDetails = (EditText) findViewById(R.id.etDetails);

        chatid = (TextView) findViewById(R.id.chatid);
        etName = (EditText) findViewById(R.id.etName);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btnSend = (Button) findViewById(R.id.btnSend);

        DraftInfo[] draftInfos = {new DraftInfo("WebSocket协议Draft_17", new Draft_17()), new DraftInfo
                ("WebSocket协议Draft_10", new Draft_10()), new DraftInfo("WebSocket协议Draft_76", new Draft_76()), new
                DraftInfo("WebSocket协议Draft_75", new Draft_75())};// 所有连接协议
        selectDraft = draftInfos[0];// 默认选择第一个连接协议

        ArrayAdapter<DraftInfo> draftAdapter = new ArrayAdapter<DraftInfo>(this, android.R.layout
                .simple_spinner_item, draftInfos);
        spDraft.setAdapter(draftAdapter);
        spDraft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDraft = (DraftInfo) spDraft.getItemAtPosition(position);// 选择连接协议

//                etDetails.append("當前連結協議：" + selectDraft.draftName + "\n");

                Log.e("wlf", "選擇連結協議：" + selectDraft.draftName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectDraft = null;// 清空

                Log.e("wlf", "未選擇任何連結協議");
            }
        });


        try {

            FileReader fr = new FileReader(new File("sdcard/darkempire/profile.txt"));
            BufferedReader br = new BufferedReader(fr);

            String temp = br.readLine(); //readLine()讀取一整行
//            Toast.makeText(SettingActivity.this, temp, Toast.LENGTH_LONG).show();

            if (temp != null) {
                String[] datas = temp.split(",");
                localid = datas[0];
                localparty = datas[1];
            }
        } catch (Exception e) {}


        switch (localparty) {
            case "Antayen": group = 60l; break;
            default: group = 30l; break;
        }
        try {
            uid = Httpconnect.httpget("http://140.119.163.40:9000/WebsocketTest/msg/newuser/"+localid+"/"+group).replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatid.setText("id: " + uid);

        ServerInfo[] serverInfos = {new ServerInfo("建立新連線呀", "ws://140.119.163.40:9000/WebsocketTest/ws?uid="+uid), new ServerInfo("连接Java Application后台", "ws://192.168.1" + "" +
                ".104:8887")};// 所有连接后台
        etAddress.setText(serverInfos[0].serverAddress);// 默认选择第一个连接协议

        ArrayAdapter<ServerInfo> serverAdapter = new ArrayAdapter<ServerInfo>(this, android.R.layout
                .simple_spinner_item, serverInfos);
        spAddress.setAdapter(serverAdapter);
        spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ServerInfo selectServerInfo = (ServerInfo) spAddress.getItemAtPosition(position);// 选择连接后台
                etAddress.setText(selectServerInfo.serverAddress);

//                etDetails.append("當前連結後台：" + selectServerInfo.serverName + "\n");

                Log.e("wlf", "當前連結後台：" + selectServerInfo.serverName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectDraft = null;// 清空

                Log.e("wlf", "未選擇任何連結後台");
            }
        });

//        btnConnect.setOnClickListener(this);
//        btnConnect.setPressed(true);


        try {
            if (selectDraft == null) {
                return;
            }
            String address = etAddress.getText().toString().trim();
            if (address.contains("JSR356-WebSocket")) {
                address += etName.getText().toString().trim();
            }
            Log.e("wlf", "連結地址：" + address);
            client = new WebSocketClient(new URI(address), selectDraft.draft) {
                @Override
                public void onOpen(final ServerHandshake serverHandshakeData) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etDetails.setGravity(Gravity.LEFT);
                            etDetails.append("【已經連線...】\n");

                            Log.e("wlf", "已經連結服務器【" + getURI() + "】");

                            // history chat list
                            String chatList;
                            try {
//                                String url = "http://140.119.163.40:9000/WebsocketTest/msglist/app/1480940730431/100";
                                chatList = Httpconnect.httpget("http://140.119.163.40:9000/WebsocketTest/msglist/app/"+uid+"/100");

//                                Log.e("url", url);
                                Log.e("chat", chatList);
                                JSONArray chatArray = new JSONArray(chatList);
                                for(int i=0; i<chatArray.length(); i++){
                                    JSONObject chatObj  = chatArray.getJSONObject(chatArray.length()-i-1);
                                    String c_mes = chatObj.getString("text");
                                    String c_na = chatObj.getString("fromName").replace("\n", "").replace(" ", "");
                                    Long c_other = chatObj.getLong("fromuid");
                                    Long c_touid = chatObj.getLong("touid");

                                    String c_type = "";

                                    if (uid.equals(c_other.toString())) {
                                        c_na = "我";
                                    }
                                    if (c_touid == 1l) {
                                        c_type = "[廣播]" + c_na + ": [";
                                    } else if (c_touid < 100) {
                                        c_type = "[群組]" + c_na + ": [";
                                    } else {
                                        c_type = "[私人]" + c_na + ": [";;
                                    }

                                    etDetails.append(c_type + c_mes + "]\n");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            spDraft.setEnabled(false);
                            etAddress.setEnabled(false);
                            btnConnect.setEnabled(false);
                            etName.setEnabled(false);

                            btnClose.setEnabled(true);
                            btnSend.setEnabled(true);
                        }
                    });
                }



                @Override
                public void onMessage(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            String messjson = message;
                            Log.e("mess", messjson);
                            try {



                                // ON TIME
                                String mes = new JSONObject(messjson).getString("text");
                                String na = new JSONObject(messjson).getString("fromName");
                                String other = new JSONObject(messjson).getString("fromuid");
                                Long touid = new JSONObject(messjson).getLong("touid");

                                String mess_type = "";
                                if (touid == 1f) {
                                    mess_type = "[廣播]";
                                } else if (touid < 100 ) {
                                    mess_type = "[群組]";
                                } else {
                                    mess_type = "[私人]";
                                }

                                if (na.replace(" ", "").equals(uid)) {
//                                            etDetails.setTextColor(Color.BLUE); // self message
//                                            etDetails.setGravity(Gravity.RIGHT);
                                    etDetails.append(mess_type + "我" + ": [" +mes+"]\n");
                                } else {
//                                            etDetails.setGravity(Gravity.LEFT);
//                                            etDetails.setTextColor(Color.GRAY); // other people message
                                    etDetails.append(mess_type + other + ": [" +mes+"]\n");

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("wlf", "獲得服務器訊息【" + message + "】");
                        }
                    });
                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etDetails.append("【斷開服務器連結...】\n");

                            Log.e("wlf", "斷開服務器連結【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");

                            spDraft.setEnabled(true);
                            etAddress.setEnabled(true);
                            btnConnect.setEnabled(true);
                            etName.setEnabled(true);

                            btnClose.setEnabled(false);
                            btnSend.setEnabled(false);
                        }
                    });
                }

                @Override
                public void onError(final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etDetails.append("連結發生異常\n");

                            Log.e("wlf", "连接发生了异常【异常原因：" + e.toString() + "】");

                            spDraft.setEnabled(true);
                            etAddress.setEnabled(true);
                            btnConnect.setEnabled(true);
                            etName.setEnabled(true);

                            btnClose.setEnabled(false);
                            btnSend.setEnabled(false);
                        }
                    });
                }
            };
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (client != null) {
                        mess = etMessage.getText().toString();

                        if (target.replace(" ", "").equals("同族群組")) targetType = group;
                        else if (target.replace(" ", "").equals("個人對話")) {
                            String m = "";
                            m = mess.substring(mess.indexOf("@") + 1, mess.indexOf(" "));
                            Pattern pattern = Pattern.compile("[0-9]*");
                            Matcher isNum = pattern.matcher(m);

                            if (!isNum.matches()) {
                                Toast.makeText(NCCUContactActivity.this, targetType+"These are not digital. Try again!", Toast.LENGTH_SHORT).show();
                                targetType=1l;
                                mess = "";
                            } else targetType = Long.parseLong(m);
                        }

                        Log.e("typeTarget", targetType.toString());
                        String mm = "{'fromuid':"+uid+"," + "'fromName': '"+uid+"'" + ",'touid':"+targetType+"," + "'text':'"+mess+"'}";

//                        String mm = "{'from':1, 'fromname':'aaa', 'to':1, 'text':'helloworld'}";
//                        Toast.makeText(NCCUContactActivity.this, mm.toString(), Toast.LENGTH_LONG).show();
                        client.send(mm);

                        svChat.post(new Runnable() {
                            @Override
                            public void run() {
                                svChat.fullScroll(View.FOCUS_DOWN);
                                etMessage.setText("");
                                etMessage.requestFocus();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        WebSocketImpl.DEBUG = true;
        System.setProperty("java.net.preferIPv6Addresses", "false");
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                try {
                    if (selectDraft == null) {
                        return;
                    }
                    String address = etAddress.getText().toString().trim();
                    if (address.contains("JSR356-WebSocket")) {
                        address += etName.getText().toString().trim();
                    }
                    Log.e("wlf", "連結地址：" + address);
                    client = new WebSocketClient(new URI(address), selectDraft.draft) {
                        @Override
                        public void onOpen(final ServerHandshake serverHandshakeData) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etDetails.setGravity(Gravity.LEFT);
                                    etDetails.append("【已經連線...】\n");

                                    Log.e("wlf", "已經連結服務器【" + getURI() + "】");

                                    spDraft.setEnabled(false);
                                    etAddress.setEnabled(false);
                                    btnConnect.setEnabled(false);
                                    etName.setEnabled(false);

                                    btnClose.setEnabled(true);
                                    btnSend.setEnabled(true);
                                }
                            });
                        }



                        @Override
                        public void onMessage(final String message) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    String messjson = message;
                                    Log.e("mess", messjson);
                                    try {

                                        String mes = new JSONObject(messjson).getString("text");
                                        String na = new JSONObject(messjson).getString("fromName");
                                        String other = new JSONObject(messjson).getString("fromuid");

//                                        etDetails.append(na + ": [" +mess+"]\n");
                                        if (target.replace(" ", "").equals("同族群組")) targetType = group;
                                        else if (target.replace(" ", "").equals("個人對話")) {
                                            String m = "";
                                            m = mess.substring(mess.indexOf("@") + 1, mess.indexOf(" "));
                                            Pattern pattern = Pattern.compile("[0-9]*");
                                            Matcher isNum = pattern.matcher(m);

                                            if (!isNum.matches()) {
                                                Toast.makeText(NCCUContactActivity.this, targetType+"These are not digital. Try again!", Toast.LENGTH_SHORT).show();
                                                targetType=1l;
                                                mess = "";
                                            } else targetType = Long.parseLong(m);
                                        }
//                                        Toast.makeText(NCCUContactActivity.this, na, Toast.LENGTH_LONG).show();
                                        if (na.replace(" ", "").equals(uid)) {
//                                            etDetails.setTextColor(Color.BLUE); // self message
//                                            etDetails.setGravity(Gravity.RIGHT);
                                            etDetails.append("我" + ": [" +mes+"]\n");
                                        } else {
//                                            etDetails.setGravity(Gravity.LEFT);
//                                            etDetails.setTextColor(Color.GRAY); // other people message
                                            etDetails.append(other + ": [" +mes+"]\n");

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Log.e("wlf", "獲得服務器訊息【" + message + "】");
                                }
                            });
                        }

                        @Override
                        public void onClose(final int code, final String reason, final boolean remote) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etDetails.append("【斷開服務器連結...】\n");

                                    Log.e("wlf", "斷開服務器連結【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");

                                    spDraft.setEnabled(true);
                                    etAddress.setEnabled(true);
                                    btnConnect.setEnabled(true);
                                    etName.setEnabled(true);

                                    btnClose.setEnabled(false);
                                    btnSend.setEnabled(false);
                                }
                            });
                        }

                        @Override
                        public void onError(final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etDetails.append("連結發生異常【異常原因: " + e.toString() + "】\n");

                                    Log.e("wlf", "连接发生了异常【异常原因：" + e.toString() + "】");

                                    spDraft.setEnabled(true);
                                    etAddress.setEnabled(true);
                                    btnConnect.setEnabled(true);
                                    etName.setEnabled(true);

                                    btnClose.setEnabled(false);
                                    btnSend.setEnabled(false);
                                }
                            });
                        }
                    };
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnClose:
                if (client != null) {
                    client.close();
                }
                break;
            case R.id.btnSend: // user send message
                try {
                    if (client != null) {
                        mess = etMessage.getText().toString();

                        Log.e("typeTarget", targetType.toString());
                        String mm = "{'fromuid':"+uid+"," + "'fromName': '"+uid+"'" + ",'touid':"+targetType+"," + "'text':'"+mess+"'}";

//                        String mm = "{'from':1, 'fromname':'aaa', 'to':1, 'text':'helloworld'}";
//                        Toast.makeText(NCCUContactActivity.this, mm, Toast.LENGTH_LONG).show();
                        client.send(mm);

                        svChat.post(new Runnable() {
                            @Override
                            public void run() {
                                svChat.fullScroll(View.FOCUS_DOWN);
                                etMessage.setText("");
                                etMessage.requestFocus();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
    }

    private class DraftInfo {

        private final String draftName;
        private final Draft draft;

        public DraftInfo(String draftName, Draft draft) {
            this.draftName = draftName;
            this.draft = draft;
        }

        @Override
        public String toString() {
            return draftName;
        }
    }

    private class ServerInfo {

        private final String serverName;
        private final String serverAddress;

        public ServerInfo(String serverName, String serverAddress) {
            this.serverName = serverName;
            this.serverAddress = serverAddress;
        }

        @Override
        public String toString() {
            return serverName;
        }
    }

}