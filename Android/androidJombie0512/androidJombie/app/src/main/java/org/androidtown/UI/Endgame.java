package org.androidtown.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socketclient.ConnectionToServer;

import java.util.HashMap;

import Model.User;

public class Endgame extends AppCompatActivity {

    Button OK;
    User myUser;
    int userNum;
    int userId;
    HashMap<Integer, User> userMap;
    TextView killDeath;
    TextView isZombie;
    String iszom;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

//            userMap = (HashMap<Integer, User>)getIntent().getSerializableExtra("userMap");

            setContentView(R.layout.activity_endgame);
            new showEnding().start();
        }catch(Exception e){
            Log.d("MainActivity", "[Error] oncreate err " + e);
            for (StackTraceElement err : e.getStackTrace()) {
                Log.d("MainActivity", "[Error]" + err + "\n");
            }
        }
    }
    public class showEnding extends Thread{
        public showEnding(){}
        @Override
        public void run(){
            try {

                userMap = (HashMap)ConnectionToServer.userMap;
                userId = (int)ConnectionToServer.userId; // 내꺼임

                Log.d("MainActivity", "[Info] userMap is : " + userMap);
                userNum = userMap.size();
                myUser = userMap.get(userId);
                Log.d("MainActivity", "[Info] Load complete");

                OK = (Button) findViewById(R.id.OK);
                killDeath = (TextView) findViewById(R.id.killDeath);
                isZombie = (TextView) findViewById(R.id.isZombie);
                result = (TextView) findViewById(R.id.result);

                result.setTextColor(Color.RED);
                OK.setTextColor(Color.RED);
                isZombie.setTextColor(Color.RED);
                killDeath.setTextSize(20);
                killDeath.setTextColor(Color.RED);

                Log.d("MainActivity", "[Info] Button, textView ok");
                if (myUser.getisZombie() == true)
                    iszom = "Zombie";
                else
                    iszom = "Human";
                result.setText("결과 화면");
                isZombie.setText(isZombie.getText() + iszom);
                OK.setText("OK");
                String info = String.format("%-25s%-5s%-5s\n", "Name", "Kill", "Death");
                killDeath.setText(info);
                info = String.format("%-20s%-5d%-5d\n", myUser.getUserName(), myUser.getKill(), myUser.getDeath());
                killDeath.append(info);
                Log.d("MainActivity", "[Info]");
                for (User user : userMap.values()) {
                    if (user != myUser) {
                        info = String.format("%-20s%-5d%-5d\n", user.getUserName(), user.getKill(), user.getDeath());
                        killDeath.append(info);
                    }

                }

                OK.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent OKOK = new Intent(Endgame.this, waiting.class);
                        startActivity(OKOK);

                    }
                });
            }catch(Exception e){
                Log.d("MainActivity", "[Error] endgame error " + e);
                for (StackTraceElement err : e.getStackTrace()) {
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
            }
        }
    }

}