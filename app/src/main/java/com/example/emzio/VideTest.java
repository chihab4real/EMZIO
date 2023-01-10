package com.example.emzio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class VideTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vide);

        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {


                finish();
            }

        }, 500);
*/
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();


    }


}