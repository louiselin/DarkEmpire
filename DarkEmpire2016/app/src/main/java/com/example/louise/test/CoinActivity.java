package com.example.louise.test;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoinActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Double currla, currlo;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment mFragment;
    GoogleMap mGoogleMap;
    private String txt_party = "";
    private String txt_user = "";
    private JSONArray placelist = null;
    Marker currLocationMarker;
    List<Double> latitude = new ArrayList<>();
    List<Double> longitude = new ArrayList<>();
    List<Integer> bobid = new ArrayList<>();
    List<Integer> runeid_l = new ArrayList<>();
    int list_l = 0;
    private LatLng whole;

    private MediaPlayer coinbtn;
    private MediaPlayer gotchacoin;
    private String che_vi, che_me;
    private String switchOn = "ON";
    private String switchOff = "OFF";

    public static final String intent_me="ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

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

            if (temp != null) {
                String[] datas = temp.split(",");
                txt_party = datas[1];
                txt_user = datas[0];

            } else {
                txt_party = StoryActivity.party;
                txt_user = IndexActivity.userid;
            }
        } catch (Exception e) {}

        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.coinmap);
        mFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        mGoogleMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false); // 縮放手勢
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false); // 捲動 (平移)手勢
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false); // 兩指放在地圖上，然後開始旋轉劃圈
        mGoogleMap.getUiSettings().setCompassEnabled(false); // 左上指南針
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false); // 縮放控制項
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false); // 右下角我的位置
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false); // 地圖工具列
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(false); // 停用傾斜手勢

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
//        Toast.makeText(this,"連線中...",Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        currla = mLastLocation.getLatitude();
        currlo = mLastLocation.getLongitude();
        whole = new LatLng(currla, currlo);
        GroundOverlayOptions n = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.cbg))
                .position(whole, 12000f, 10800f);
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.cbg))
                .position(whole, 100f, 90f);
        mGoogleMap.addGroundOverlay(n);
        mGoogleMap.addGroundOverlay(newarkMap);

        if (mLastLocation != null) {
            final MarkerOptions markerOptions = new MarkerOptions();

            String placejson = "";
            try {
//                placejson = Httpconnect.httpget("http://140.119.163.40:8080/GeniusLoci/runeTransaction/app/search/" + currlo + "/" + currla + "/");

                placejson = Httpconnect.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/runeTradeRecord/search_rune/" + currlo + "/" + currla + "/");
//                Toast.makeText(CoinActivity.this, placejson, Toast.LENGTH_LONG).show();
                Log.e("placejson", placejson);
//                Log.e("0_placejson", o_placejson);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            int runeid = 0;
            int bob = 0;
            Double search_la=0.0, search_lo = 0.0;


            try {
                placelist = new JSONArray(placejson);


            } catch (JSONException e) {
                Toast.makeText(CoinActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < placelist.length(); i++) {
                try {
                    bob = placelist.getJSONObject(i).getInt("id");
                    runeid = placelist.getJSONObject(i).getInt("rune_id");
                    search_la = placelist.getJSONObject(i).getDouble("latitude");
                    search_lo = placelist.getJSONObject(i).getDouble("longitude");
                    bobid.add(bob);
                    runeid_l.add(runeid);
                    longitude.add(search_lo);
                    latitude.add(search_la);
                } catch (Exception e) {
                    Toast.makeText(CoinActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            Log.e("bobid", bobid.toString());
            Log.e("runeid_l", runeid_l.toString());
            Log.e("longitude", longitude.toString());
            Log.e("latitude", latitude.toString());

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(whole, 19));

            int ss = latitude.size();
            for (int l = 0; l < ss; l++) {
//                final Integer listid = new Integer(bobid.get(l));
                list_l = bobid.get(l);
                final LatLng po = new LatLng(latitude.get(l), longitude.get(l));
                markerOptions.position(po);

//                int coin = random.nextInt(2);
                markerOptions.title("" +bobid.get(l));
                if (runeid_l.get(l) == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.coin));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.coin2));
                }

                currLocationMarker = mGoogleMap.addMarker(markerOptions);



                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (intent_me.equals(che_me)) {
                            gotchacoin = MediaPlayer.create(CoinActivity.this, R.raw.gotchacoin);
                            gotchacoin.start();
                            gotchacoin.seekTo(200);
                        }
                        marker.remove();
                        String re = "";
                        try {
//                            Toast.makeText(CoinActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                            re = Httpconnect.httpost("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/runeTradeRecord/get_rune/" + Integer.parseInt(marker.getTitle()) + "/" + txt_user + "/");
                        } catch (Exception e) {
                            Toast.makeText(CoinActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        // result
                        if (re.equals("success\n")) {
                            final Toast toast2 = Toast.makeText(CoinActivity.this, "got it ^^", Toast.LENGTH_SHORT);
                            toast2.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast2.cancel();
                                }
                            }, 200);
                        } else {
                            final Toast toast2 = Toast.makeText(CoinActivity.this, "you can't get it ><", Toast.LENGTH_SHORT);
                            toast2.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast2.cancel();
                                }
                            }, 500);
                        }
                        return true;
                    }
                });
            }
        }

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(3000); //3 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);



    }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

}