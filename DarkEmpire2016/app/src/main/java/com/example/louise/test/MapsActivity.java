package com.example.louise.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ProtocolException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private JSONArray placelist = null;
    private String url = "";
    public static String[] list;
    public static List<Integer> plistid = new ArrayList<Integer>();
    public static List<String> plistname = new ArrayList<String>();
    public static List<Double> plistla = new ArrayList<Double>();
    public static List<Double> plistlo = new ArrayList<Double>();
    private String userid;
    private int check = 0;
    private Location currentLocation;
    private Marker currentMarker, itemMarker;
    private LatLng nccu;
    private CheckBox donotshowagain;
    public static final String PREFS_NAME = "map";
    LocationRequest mLocationRequest;
    public static Double currla, currlo;
    private Double latitude, longitude;
    private float distance, d;
    private LatLng po;

    private MediaPlayer placebtn;
    private String che_vi, che_me;
    private String switchOn = "ON";
    private String switchOff = "OFF";

    public static final String intent_me="ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(MapsActivity.this, "載入中...", Toast.LENGTH_SHORT).show();
        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        donotshowagain = (CheckBox) eulaLayout.findViewById(R.id.skip);

        AlertDialog.Builder ad = new AlertDialog.Builder(MapsActivity.this);
        ad.setView(eulaLayout);
        ad.setTitle("攻塔攻略");
        ad.setMessage("點選您要攻下的神殿地名字！進入神殿所在準備攻擊嘍！\n");
        ad.setNegativeButton("開始探索", new DialogInterface.OnClickListener() {
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

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(this,"API 建立完成",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {


        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        currla = mLastLocation.getLatitude();
        currlo = mLastLocation.getLongitude();
        nccu = new LatLng(currla, currlo);


//        String placeurl = " http://140.119.163.40:8080/Spring08/app/place ";
        String placeurl = " http://140.119.163.40:8080/DarkEmpire/app/ver1.0/place ";
        String json2 = "";
        try {
            json2 = Httpconnect.httpget(placeurl);


        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int size = 0;
        try {
            placelist = new JSONArray(json2);
            size = placelist.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < size; i++) {
            try {
                if (placelist.getJSONObject(i).getInt("main_id") == 0) {

                    latitude = placelist.getJSONObject(i).getDouble("latitude");
                    longitude = placelist.getJSONObject(i).getDouble("longitude");

                    po = new LatLng(latitude, longitude);
                    options.position(po);
                    options.title(placelist.getJSONObject(i).getString("name"));
//                    修改水滴的圖示
                    String stelejson = "";
                    try {
//                        int id = placelist.getJSONObject(i).getInt("id");
//                        stelejson = Httpconnect.httpget("http://140.119.163.40:8080/Spring08/app/stele/" + id);
                        int id = placelist.getJSONObject(i).getInt("place_id");
                        stelejson = "[" + Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/" + id) + "]";

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    int camp = 0;
                    try {
                        JSONArray stelelist = new JSONArray(stelejson);
                        camp = stelelist.getJSONObject(0).getInt("camp_id");
                        int pid = stelelist.getJSONObject(0).getInt("place_id");
                        if (pid == 52) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.yellowflag);
                            options.icon(icon);
                        } else if (camp == 2) { // Sinae
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blueflag);
                            options.icon(icon);
                        } else if (camp == 1) { // A
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.redflag);
                            options.icon(icon);
                        } else if (camp == 3) {
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blackflag);
                            options.icon(icon);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(options);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nccu, 17));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//
//                return true;
//            }
//        });

                //maker infowindows click event
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        int placeid = 0;
                        String placename = "";
                        double placela = 0;
                        double placelo = 0;


                        if (intent_me.equals(che_me)) {
                            placebtn = MediaPlayer.create(MapsActivity.this, R.raw.placebtn);
                            placebtn.start();
                            placebtn.seekTo(200);
                        }
                        //find maker's id
                        for (int i = 0; i < placelist.length(); i++) {
                            try {
                                if (placelist.getJSONObject(i).getString("name").equals(marker.getTitle())) {
                                    placeid = placelist.getJSONObject(i).getInt("place_id");
                                    placename = placelist.getJSONObject(i).getString("name");
                                    placela = placelist.getJSONObject(i).getDouble("latitude");
                                    placelo = placelist.getJSONObject(i).getDouble("longitude");
                                    distance = (float) Math.sqrt(((placelo - currlo) * (placelo - currlo)) + ((placela - currla) * (placela - currla)));
                                    d = distance * 100000;
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //find subplace
                        plistid.clear();
                        plistname.clear();
                        plistid.add(placeid);
                        plistname.add(placename);
                        plistla.add(placela);
                        plistlo.add(placelo);

                        for (int i = 0; i < placelist.length(); i++) {
                            try {
                                if (placelist.getJSONObject(i).getInt("main_id") == placeid) {
                                    plistid.add(placelist.getJSONObject(i).getInt("place_id"));
                                    plistname.add(placelist.getJSONObject(i).getString("name"));
                                    plistla.add(placelist.getJSONObject(i).getDouble("latitude"));
                                    plistlo.add(placelist.getJSONObject(i).getDouble("longitude"));
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (d > 1000) {
//                            Toast.makeText(MapsActivity.this, currla + "," + currlo + "," + d, Toast.LENGTH_LONG).show();
                            Toast.makeText(MapsActivity.this, "距離太遠了喔>< 動起來!!", Toast.LENGTH_SHORT).show();
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.hasVibrator();
                            vibrator.vibrate(100);
                        } else {
//                            Toast.makeText(MapsActivity.this, currla + "," + currlo + "," + d, Toast.LENGTH_LONG).show();
//                            Toast.makeText(MapsActivity.this, "OK", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(MapsActivity.this, PlacelistActivity.class);
                            startActivity(intent);
                        }



                    }
                });
    }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
            }


        }