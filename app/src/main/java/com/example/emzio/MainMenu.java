package com.example.emzio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity {
      public static  User user;
    SharedPreferences share;

    public static TextView titre ;
    BottomNavigationView mv;
    Toast backToast;
    long backPressedTime;
    DatabaseReference mDatabase2;
    public static  ArrayList<Product> x;
    //final Loadsc2 ld = new Loadsc2(MainMenu.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuacti);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        mv = findViewById(R.id.nav_view);


        //user = (User) intent.getSerializableExtra("User");
        share =  getSharedPreferences("MyREF", MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
        ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        startActivity(new Intent(getApplicationContext(), VideTest.class));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_recherche,R.id.navigation_recomnded, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }








}
