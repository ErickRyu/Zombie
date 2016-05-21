package org.androidtown.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Endgame extends AppCompatActivity {

    Button OK;

    Button kill;
    Button name;
    Button death;
    Button me;
    Button user1;
    Button user2;
    Button kill1;
    Button kill2;
    Button kill3;
    Button death1;
    Button death2;
    Button death3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        name = (Button) findViewById(R.id.name);
        death = (Button) findViewById(R.id.death);
        kill = (Button) findViewById(R.id.kill);
        me = (Button) findViewById(R.id.myName);
        user1 = (Button) findViewById(R.id.name_1);
        user2 = (Button) findViewById(R.id.name_2);
        kill1 = (Button) findViewById(R.id.kill_1);
        kill2 = (Button) findViewById(R.id.kill_2);
        kill3 = (Button) findViewById(R.id.kill_3);
        death1 = (Button) findViewById(R.id.death_1);
        death2 = (Button) findViewById(R.id.death_2);
        death3 = (Button) findViewById(R.id.death_3);
        OK = (Button) findViewById(R.id.OK);

        name.setText("name");
        death.setText("death");
        kill.setText("kill");
        me.setText("me");
        user1.setText("user1");
        user2.setText("user2");
        kill1.setText("1");
        kill2.setText("1");
        kill3.setText("1");
        death1.setText("1");
        death2.setText("1");
        death3.setText("1");
        OK.setText("OK");


        OK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent OKOK = new Intent(Endgame.this, waiting.class);
                startActivity(OKOK);

            }
        });



    }

}