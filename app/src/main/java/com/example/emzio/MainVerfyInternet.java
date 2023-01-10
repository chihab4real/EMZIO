package com.example.emzio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainVerfyInternet extends AppCompatActivity {

    LinearLayout tryagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_verfy_internet);
        tryagain = findViewById(R.id.try_again);

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if(haveNetwork()){
                    MainVerfyInternet.super.onBackPressed();
                }

            }
        });




    }


    private boolean haveNetwork(){

        ConnectivityManager cm =
                (ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        /*boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();*/
        /*
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }*/

        return isConnected;
    }

    @Override
    public void onBackPressed() {
        if(haveNetwork()){
            Toast.makeText(getApplicationContext(),"Vous Etes Connecter a nouveau",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }else{
            Toast.makeText(getApplicationContext(),"Y'a Pas de Connexion Internet!",Toast.LENGTH_SHORT).show();
        }

    }
}