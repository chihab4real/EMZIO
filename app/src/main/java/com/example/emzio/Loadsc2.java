package com.example.emzio;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

public class Loadsc2 extends AppCompatActivity {
    Activity activity;
    AlertDialog dialog;
        Loadsc2(Activity myActivity){

            activity = myActivity;

        }

        void LoadingAlertDiaolg(){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater =  activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.load2,null));
            builder.setCancelable(false);
            dialog=builder.create();
            dialog.show();

        }

        void dissmiss(){
            dialog.dismiss();
        }
}
