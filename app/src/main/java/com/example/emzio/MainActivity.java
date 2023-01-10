package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.emzio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    SharedPreferences share;
    private FirebaseAuth mAuth;
    ProgressBar prg;
    DatabaseReference mDatabase;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       if (!haveNetwork()) {
            Toast.makeText(MainActivity.this, "Connexion perdu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,MainVerfyInternet.class));

        }else{


           DisplayMetrics metrics = new DisplayMetrics();
           getWindowManager().getDefaultDisplay().getMetrics(metrics);

           int height = metrics.heightPixels;
           int width = metrics.widthPixels;

           Things.h = height;
           Things.w = width;
        mAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(MainActivity.this,MainLogin.class);
        Things.conf=false;

        share =  getSharedPreferences("MyREF", MODE_PRIVATE);

        if(share.contains("Logged")){

            AutoLogin(share.getString("Email",null),share.getString("Password",null));

        }else{
            startActivity(intent);
            finish();

        }


       }

    }



    void AutoLogin(final String email, final String password){
        final String TAG = "SIGN IN";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            User s = new User("","","",email,password,"",false);
                            getUser(user1.getUid());
                            Intent intent = new Intent(MainActivity.this,Welcome.class);
                            //intent.putExtra("User",s);
                            startActivity(intent);
                            finish();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                            Intent intent = new Intent(MainActivity.this,MainLogin.class);
                            startActivity(intent);
                            finish();
                        }

                        // ...

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
    protected void onStart() {
        super.onStart();
        if (!haveNetwork()) {
            Toast.makeText(MainActivity.this, "Connexion perdu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,MainVerfyInternet.class));

        }else{



            mAuth = FirebaseAuth.getInstance();
            Intent intent = new Intent(MainActivity.this,MainLogin.class);
            Things.conf=false;

            share =  getSharedPreferences("MyREF", MODE_PRIVATE);
            if(share.contains("Logged")){

                AutoLogin(share.getString("Email",null),share.getString("Password",null));

            }else{
                startActivity(intent);
                finish();

            }


        }

    }

    public  void getUser(String id){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user1 = dataSnapshot.getValue(User.class);

                if(user1 !=null){
                    SharedPreferences.Editor editor = share.edit();
                    user = user1;
                    editor.putBoolean("Logged",true);
                    editor.putString("Nom",user.getNom());
                    editor.putString("Prenom",user.getPrenom());
                    editor.putString("Sexe",user.getSexe());
                    editor.putString("Email",user.getEmail());
                    editor.putString("Password",user.getPassword());
                    editor.putString("Phone",user.getPhone());
                    editor.putString("ID",user.getID());
                    editor.putBoolean("Pro",user.isPro());
                    editor.putString("Adresse_adresse",user.getAdresse().getAdresse());
                    editor.putString("Adresse_info",user.getAdresse().getInfoSupp());
                    editor.putString("Adresse_wilaya",user.getAdresse().getWilaya());
                    editor.putInt("Adresse_wilaya_num",user.getAdresse().getNum_wilaya());

                    editor.commit();
                    editor.apply();





                }else{
                    Toast.makeText(getApplicationContext(),"EROR",Toast.LENGTH_LONG).show();
                }


            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
