package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainLogin extends AppCompatActivity {
    SharedPreferences share;

    Button connecter;
    static  User user = null;
    TextView create_acounte,forget;
    EditText mail, pass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    final Loadsc ld = new Loadsc(MainLogin.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.loginacti);

        share = getSharedPreferences("MyREF", MODE_PRIVATE);

        if(share.contains("Logged")){
            boolean tru = share.getBoolean("Logged",false);
            if(tru == true){
                //AutoLogin(share.getString("Email",null),share.getString("Password",null));
            }

        }

        connecter= findViewById(R.id.login_connexion);
        create_acounte=findViewById(R.id.login_create_account);
        forget = findViewById(R.id.login_password_forger);
        mail = findViewById(R.id.login_mail);
        pass = findViewById(R.id.login_password);

        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mail.getText().toString().isEmpty() ||pass.getText().toString().isEmpty()){

                }else{
                    ld.LoadingAlertDiaolg();
                    signin(mail.getText().toString(),pass.getText().toString());
                }


            }
        });

        create_acounte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainLogin.this, MainSign.class);
                getAllUsers();

                Timer timer = new Timer();

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        startActivity(intent);
                        finish();
                    }

                }, 700);

            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainLogin.this,MainPasswordForget.class));
            }
        });





    }



    void signin(final String email, final String password){
        final String TAG = "SIGN IN";


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user1 = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Coennecter!",Toast.LENGTH_SHORT).show();


                            //getCurrentUser();
                            getUser(user1.getUid());







                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            ld.dissmiss();

                        }

                        // ...

                    }
                });

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
                            Intent intent = new Intent(MainLogin.this,MainMenu.class);

                            startActivity(intent);
                            finish();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }
                        ld.dissmiss();
                        // ...

                    }
                });
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


                    startActivity(new Intent(MainLogin.this,Welcome.class));
                    ld.dissmiss();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();

                }


                }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getAllUsers(){
        MainSign.mails = new ArrayList<>();
        MainSign.phones = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user  = snapshot.getValue(User.class);


                    MainSign.mails.add(user.getEmail());
                    MainSign.phones.add(user.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
