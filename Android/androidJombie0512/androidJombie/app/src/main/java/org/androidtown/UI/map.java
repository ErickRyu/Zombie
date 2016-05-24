package org.androidtown.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socketclient.ConnectionToServer;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import Model.User;

public class map extends AppCompatActivity {
    static final int ENEMY_COLOR = Color.RED;
    static final int FRIEND_COLOR = Color.GREEN;


    Button status;
    TextView textview;
    GoogleMap map;
    LocationManager manager;
    MyLocationListener listener;
    SensorManager sensorManager;
    MySensorListener sensorListener;

    static int userId;
    static Location currentLocation;
    ConcurrentHashMap<String, User> userMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "[Info] In map class, starting");
        setContentView(R.layout.activity_map);
        status = (Button) findViewById(R.id.status);
        textview = (TextView) findViewById(R.id.textView3);

        status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent endgame = new Intent(map.this, Endgame.class);
                startActivity(endgame);


            }
        });

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map); //fragment를 찾아야하기 때문에
        map = fragment.getMap(); //구글맵 가져오기

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 시스템에서 호출
        listener = new MyLocationListener(); // 위치정보를 원할때만 받을 수 있는 리스너 생성

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //센서관리자 시스템에서 호출
        sensorListener = new MySensorListener(); // 센서리스너
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Log.d("MainActivity", "[Info] Game starting...");
        new gameStart().execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.androidtown.socket/http/host/path")
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
                "map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.androidtown.socket/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void onButton2Clicked(View v) {

    }

    public class gameStart extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... params) {
            ConcurrentHashMap<Integer, User> userMap = new ConcurrentHashMap<>();
            double latitude;
            double longitude;
            User me;

            try {
                ConnectionToServer server = (ConnectionToServer) getApplicationContext();
                int userId = (Integer) ConnectionToServer.userId;
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 시스템에서 호출

                Location lastLocation;
                String input = "";
                while (true) {
                    Log.d("MainActivity", "[Info] Waiting command from server");
                    input = (String) server.read();
                    Log.d("MainActivity", "[Info] got message : " + input);
                    if (input.equals("giveLocation")) {
                        if(userMap != null)
                            userMap.clear();

                        lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d("MainActivity", "[Info] lastLocation : " + lastLocation);
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();

                        server.write(userId + ":" + latitude + " " + longitude);
                        Log.d("MainActivity", "[Info] Sended my Location");
                        System.out.println("\n");
                        System.out.println("*********************************");
                        userMap = (ConcurrentHashMap<Integer, User>) server.read();
                        me = userMap.get(userId);


                        for (final User user : userMap.values()) {

                            String name = user.getUserName();
                            latitude = user.getLatitude();
                            longitude = user.getLongitude();
                            int hp = user.getHP();
                            boolean isZombie = user.getisZombie();


                            final double finalLatitude = latitude;
                            final double finalLongitude = longitude;
                            final User finalMe = me;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if(finalMe != user) {
                                        map.addCircle(new CircleOptions()
                                                .center(new LatLng(finalLatitude, finalLongitude))
                                                .radius(1)
                                                .strokeColor((finalMe.getisZombie() ^ user.getisZombie() ? ENEMY_COLOR : FRIEND_COLOR))
                                                .fillColor((finalMe.getisZombie() ^ user.getisZombie() ? ENEMY_COLOR : FRIEND_COLOR)));
                                    }
                                }
                            });


                            Log.d("MainActivity", "[Info] userMap contents : ----------\n" +
                                    name + "\n"
                                    + "(" + latitude + ", " + longitude + ")\n"
                                    + "HP : " + hp + "\n"
                                    + (isZombie ? "Zombie" : "Human"));
                        }
                    } else if (input.equals("EXIT")) {
                        HashMap<Integer, User> passUserMap = new HashMap<Integer, User>(userMap);
                        ConnectionToServer.userMap = passUserMap;
                        Intent exit = new Intent(map.this, Endgame.class);


                        startActivity(exit);
                        break;
                    }
                }
            } catch (SecurityException e) {
                Log.d("MainActivity", "[Error] GiveLocation SecurityException : " + e.getMessage());
                for (StackTraceElement err : e.getStackTrace()) {
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
            } catch (Exception e) {
                Log.d("MainActivity", "[Error] GiveLocation Error : " + e.getMessage());
                for (StackTraceElement err : e.getStackTrace()) {
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onButton1Clicked(View v) { //버튼 클릭시 정보 보내고 받기
        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 시스템에서 호출

            long minTime = 1000; //ms단위 1초당
            float minDistance = 0; // 항상 업데이트 하기

            MyLocationListener listener = new MyLocationListener(); // 위치정보를 원할때만 받을 수 있는 리스너 생성
            Location lastLocation;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime, minDistance, listener); // 업데이트된 정보를 리스너에게 보내는 메소드

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 건물안에 있을때는 gps가 정상 작동 안되므로 기지국 거리를 이용한 네트워크 위치기반 사용
                    minTime, minDistance, listener);
            lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();


            textview.setText("가장 최근의 내 위치 : " + latitude + ", " + longitude);
            textview.invalidate();
        } catch (SecurityException e) {
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            map.setMyLocationEnabled(false); // 위치 찍기

            if (manager != null) {
                manager.removeUpdates(listener); // pause가 뒤로가기 눌렀을때 반응하는 메소드니까 지속적으로 업데이트하는것을 꺼줘야함
            }

            sensorManager.unregisterListener(sensorListener); //
        } catch (SecurityException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            map.setMyLocationEnabled(true); // 내 위치 보여주기

            requestMyLocation();

            sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
            Log.d("MainActivity", "[Info] on reusme success");
        } catch (SecurityException e) {

        }


    }

    public void requestMyLocation() {

        long minTime = 1000; //ms단위 1초당
        float minDistance = 0; // 항상 업데이트 하기
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime, minDistance, listener); // 업데이트된 정보를 리스너에게 보내는 메소드

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 건물안에 있을때는 gps가 정상 작동 안되므로 기지국 거리를 이용한 네트워크 위치기반 사용
                    minTime, minDistance, listener);
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Double latitude = 0.;
            Double longitude = 0.;

            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                Log.d("mainActivity", "가장 최근의 내 위치 : " + latitude + ", " + longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            }
        } catch (SecurityException e) {

        }
    }

    private void showCurrentMap(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);

        //             map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.clear();

        MarkerOptions marker = new MarkerOptions();
    }

    class MySensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) { // 센서의 값을 여기서 전달받음 센서의 방향을 받아서 이미지를 돌려주면 됨
            Log.d("MySensorListener", "sensor #0 : " + event.values[0]);
        }
    }

    class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {  // 위치정보가 갱신될 때 호출되는 메소드

            Double latitude = location.getLatitude();  // 위도와 경도를 받아오는 방법
            Double longitude = location.getLongitude();

            textview.setText("내 위치 : " + latitude + "," + longitude);
            textview.invalidate();


            showCurrentMap(latitude, longitude);


        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
