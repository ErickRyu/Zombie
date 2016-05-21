package org.androidtown.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socketclient.ConnectionToServer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 안드로이드에서 소켓 클라이언트로 연결하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends Activity implements View.OnClickListener {
//    Map<String, User> userMap = new HashMap<String, User>();


    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    ConnectionToServer server;
    static int userId;

    ////
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b0;
    private Button b_back;
    private Button b_OK;


    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Connect To Server */


        b1 = (Button) findViewById(R.id.button_1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button_2);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.button_3);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.button_4);
        b4.setOnClickListener(this);
        b5 = (Button) findViewById(R.id.button_5);
        b5.setOnClickListener(this);
        b6 = (Button) findViewById(R.id.button_6);
        b6.setOnClickListener(this);
        b7 = (Button) findViewById(R.id.button_7);
        b7.setOnClickListener(this);
        b8 = (Button) findViewById(R.id.button_8);
        b8.setOnClickListener(this);
        b9 = (Button) findViewById(R.id.button_9);
        b9.setOnClickListener(this);
        b0 = (Button) findViewById(R.id.button_0);
        b0.setOnClickListener(this);
        b_back = (Button) findViewById(R.id.button_back);
        b_back.setOnClickListener(this);
        b_OK = (Button) findViewById(R.id.button_OK);
        b_OK.setOnClickListener(this);
        result = (TextView) findViewById(R.id.text_result);
        b_OK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Runnable connRunnable = new connectserver();
                new Thread(connRunnable).start();

            }
        });
    }
    public class InitUser implements Runnable{
        int userId;
        public InitUser( int userId){
            this.userId = userId;
        }
        public void run(){
            boolean matched = initUser(userId);
            try {
                if (matched) {
                    ConnectionToServer.userId = userId;
                    Intent waitingIntent = new Intent(MainActivity.this, waiting.class);
                    startActivity(waitingIntent);
                } else {
                    socket.close();
                    Log.d("MainActivity", "[Info] Close socket");
                }
            }catch(Exception e){
                Log.d("MainActivity", "[Error] Call waiting intent\n" + e.getMessage() + "\n");
                for(StackTraceElement err : e.getStackTrace()){
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
                e.printStackTrace();
            }
        }
        public boolean initUser(int userId) {
            // Login
            try {
                Log.d("MainActivity", "[Info] Writing userId...");
                server.write(userId);
                Log.d("MainActivity", "[Info] wrtied userId");
                boolean matched = (boolean) server.read();
                return matched;
            }catch(Exception e){
                Log.d("MainActivity", "[Error] initUser : " + e.getMessage() + "\n");
                for(StackTraceElement err : e.getStackTrace()){
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
                return false;
            }
        }
    }


    class connectserver implements Runnable {
        public void run() {
            try {
            /*Toast toview = new Toast(getApplicationContext());*/
//                Log.d("MainActivity", "[Info] Connecting server");
                socket = new Socket("192.168.0.7", 5000);
//                Log.d("MainActivity", "[Info] Connecting...");
                server = new ConnectionToServer(socket);
                Log.d("MainActivity", "[Info] Connected");
                int inputUserId = Integer.parseInt(result.getText().toString());
                Log.d("MainActivity", "[Info] got id : " + inputUserId);
                Runnable initRunnable = new InitUser(inputUserId);
                new Thread(initRunnable).start();
            } catch (Exception e) {
                Log.d("MainActivity", "[Error] MessgaeHandler Error");
                for(StackTraceElement err : e.getStackTrace()){
                    Log.d("MainActivity", "[Error]" + err + "\n");
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(b1)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("1");
            } else {
                result.setText(result.getText() + "1");
            }
        } else if (v.equals(b2)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("2");
            } else {
                result.setText(result.getText() + "2");
            }
        } else if (v.equals(b3)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("3");
            } else {
                result.setText(result.getText() + "3");
            }
        } else if (v.equals(b4)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("4");
            } else {
                result.setText(result.getText() + "4");
            }
        } else if (v.equals(b5)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("5");
            } else {
                result.setText(result.getText() + "5");
            }
        } else if (v.equals(b6)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("6");
            } else {
                result.setText(result.getText() + "6");
            }
        } else if (v.equals(b7)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("7");
            } else {
                result.setText(result.getText() + "7");
            }
        } else if (v.equals(b8)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("8");
            } else {
                result.setText(result.getText() + "8");
            }
        } else if (v.equals(b9)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("9");
            } else {
                result.setText(result.getText() + "9");
            }
        } else if (v.equals(b0)) {
            if (result.getText().length() == 1 && "0".equals(result.getText())) {
                result.setText("0");
            } else {
                result.setText(result.getText() + "0");
            }
        } else if (v.equals(b_back)) {
            if (result.getText().length() != 0) {
                result.setText(result.getText().subSequence(0, result.getText().length() - 1));
            }
        } else if (v.equals(b_OK)) {

        }
    }
}