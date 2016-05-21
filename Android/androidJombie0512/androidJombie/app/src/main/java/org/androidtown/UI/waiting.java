package org.androidtown.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.socketclient.ConnectionToServer;

public class waiting extends Activity {
    ScrollView scrollView;
    ConnectionToServer server;
    int userId;
    TextView textViewtimebar;
    TextView textView;
    String scenario="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Calendar c = Calendar.getInstance(); // 현재 시간 따오기
//
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String formattedDate = df.format(c.getTime()); // 년월일 따오기
//
//        textViewtimebar = (TextView) findViewById(R.id.textViewtimebar);
//        textViewtimebar.setText(formattedDate + " " + c.getTime());




        Log.d("MainActivity", "[Info] Watting Activity Start");
        setContentView(R.layout.activity_waiting);
        scrollView = (ScrollView) findViewById(R.id.scrollscnario);

        textView = (TextView) findViewById(R.id.scenario);
        scenario = "일본에서 원전이 폭발한후 방사능의 영향으로 일본 싸이코 좀비 발생, 여기서 큰사건하나가터지는데,  박근혜 정권에서 친일 외교 정권 조약 때문에 핵폐기물 타이어등 전부 눈감아 주고 심지어 싸이코 좀비로 의심되는 환자들은 한국으로 교환학생 명목으로 출입을 허락하게 된다.\n" +
                "\n" +
                "그리하여 싸이코 좀비가 좀비인 듯 아닌 듯 인간인 듯 하면서 비행기를 타고 한류문화를 체험하기 위해한국에 입국 하였고, 그로인해 한국도 일본의 싸이코 좀비로 인한 좀비월드로 변하였다. 하지만 이 좀비는 잠복기가 있어서 본인들도 모르는 사이에 감염되어 있었다.\n" +
                "\n" +
                "시간적 배경은 2016년, 한국, 한양대학교 에리카 어느 곳. 학교생활에 지루함을 느낀 A학생이 점점 미쳐 갔다. 그리고 시험기간이 다가왔을 때, A학생은 이성의 끈을 놓아버렸고, 유전자 변이를 일으켜서 좀비가 되었다. A학생은 다른 학생을 감염 시키지 않으면, 변이된 유전자 때문에 죽게 된다. 그 과정에서 감염되지 않은 학생들은 A학생 즉, 좀비를 피하기 위해 학교 안에서 도망을 다니게 된다. 그리고 이런 상황이 특수부대에 알려져서 지원군이 오기로 되어있는데, 그 시간까지는 좀비를 피하거나 죽이면서 ";
        textView.setText(scenario);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        new waitForGameStart().execute();
    }

    public class waitForGameStart extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... params) {
            try {
                server = (ConnectionToServer) getApplicationContext();
                String input = "";

                Log.d("MainActivity", "[Info] Waiting start...");
                while (!(input = (String) server.read()).equals("GO")) {
                }
                Log.d("MainActivity", "[Info] Starting Google map...");
                //start Map

                Intent startmap = new Intent(waiting.this, map.class);
                startActivity(startmap);

            } catch (Exception e) {
                Log.d("MainActivity", "[Error] waiting class \n" + e.getMessage());
                for (StackTraceElement err : e.getStackTrace()) {
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
            }

            return null;
        }
    }
}