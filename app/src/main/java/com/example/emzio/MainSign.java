package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainSign extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button creat;
    String id;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    SharedPreferences share;
    Spinner spinner;
    boolean pro;

    EditText prenom,nom, mail,password,phone;
    RadioButton homme, femme,accepte;

    TextView valide_nom, valide_prenom, valide_mail, valide_phone, valide_password;

    public static ArrayList<String> mails;
    public static  ArrayList<String> phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signacti);
        creat = findViewById(R.id.button2);
        //spinner********************
        spinner = findViewById(R.id.signin_spinner_type_de_compte);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.users, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner = (Spinner) findViewById(R.id.signin_spinner_type_de_compte);
        spinner.setOnItemSelectedListener(this);

        //*****************
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        share = getSharedPreferences("MyREF",MODE_PRIVATE);

        creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user;
                user= getUserInfoFrmView();

                if(user!=null){
                    CreatUserInAuthSystem(user);
                }

            }
        });

        valide_prenom=findViewById(R.id.signin_prenom_valide);
        valide_nom=findViewById(R.id.signin_nom_valide);
        valide_mail=findViewById(R.id.signin_mail_valide);
        valide_password=findViewById(R.id.signin_password_valide);
        valide_phone=findViewById(R.id.signin_phone_valide);

        prenom = findViewById(R.id.signin_prenom);
        nom = findViewById(R.id.signin_nom);
        mail= findViewById(R.id.signin_mail);
        password=findViewById(R.id.signin_password);
        phone = findViewById(R.id.signin_phone);


        prenom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valide_prenom.setVisibility(View.VISIBLE);
                if(isValidName(prenom.getText().toString())){

                    valide_prenom.setText("Prenom Valide");
                    valide_prenom.setTextColor(Color.rgb(76,175,80));
                }else{
                    valide_prenom.setText("Prenom invalide");
                    valide_prenom.setTextColor(Color.rgb(255,0,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valide_nom.setVisibility(View.VISIBLE);
                if(isValidName(nom.getText().toString())){
                    valide_nom.setText("Nom Valide");
                    valide_nom.setTextColor(Color.rgb(76,175,80));
                }else{
                    valide_nom.setText("Nom invalide");
                    valide_nom.setTextColor(Color.rgb(255,0,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valide_mail.setVisibility(View.VISIBLE);
                if(isValidMail(mail.getText().toString())){


                    if(mails.contains(mail.getText().toString())){
                        valide_mail.setText("Mail deja pret");
                        valide_mail.setTextColor(Color.rgb(255,0,0));
                    }else{
                        valide_mail.setText("Mail Valide");
                        valide_mail.setTextColor(Color.rgb(76,175,80));
                    }

                   /* valide_mail.setText("Mail Valide");
                    valide_mail.setTextColor(Color.rgb(76,175,80));*/
                }else{
                    valide_mail.setText("Mail invalide");
                    valide_mail.setTextColor(Color.rgb(255,0,0));
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valide_password.setVisibility(View.VISIBLE);
                if(isValidPassword(password.getText().toString())){
                    valide_password.setText("Password Valide");
                    valide_password.setTextColor(Color.rgb(76,175,80));
                }else{
                    valide_password.setText("Password invalide");
                    valide_password.setTextColor(Color.rgb(255,0,0));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                valide_phone.setVisibility(View.VISIBLE);

                if(isValidNumberPhone(phone.getText().toString())){

                    if(phones.contains(phone.getText().toString())){
                        valide_phone.setText("Phone deja pret");
                        valide_phone.setTextColor(Color.rgb(255,0,0));
                    }else{
                        valide_phone.setText("Phone Valide");
                        valide_phone.setTextColor(Color.rgb(76,175,80));
                    }

                    /*valide_phone.setText("Phone Valide");
                    valide_phone.setTextColor(Color.rgb(76,175,80));*/
                }else{
                    valide_phone.setText("Phone invalide");
                    valide_phone.setTextColor(Color.rgb(255,0,0));
                }




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    User getUserInfoFrmView(){



        prenom = findViewById(R.id.signin_prenom);
        nom = findViewById(R.id.signin_nom);
        mail= findViewById(R.id.signin_mail);
        password=findViewById(R.id.signin_password);
        phone = findViewById(R.id.signin_phone);

        homme = findViewById(R.id.signin_homme);
        femme = findViewById(R.id.signin_femme);
        accepte = findViewById(R.id.signin_accept);

            boolean p = isValidPassword(password.toString());

            if(accepte.isChecked()) {


                if (isValidName(prenom.getText().toString()) && isValidName(nom.getText().toString()) && isValidMail(mail.getText().toString()) && isValidPassword(password.getText().toString())
                        && isValidNumberPhone(phone.getText().toString())) {

                    if (homme.isChecked() || femme.isChecked()) {
                        if (homme.isChecked()) {
                            return new User(nom.getText().toString(), prenom.getText().toString(), "Homme", mail.getText().toString(), password.getText().toString(), phone.getText().toString(), pro);
                        }

                        return new User(nom.getText().toString(), prenom.getText().toString(), "Femme", mail.getText().toString(), password.getText().toString(), phone.getText().toString(), pro);
                    } else {
                        Toast.makeText(getApplicationContext(), "Selectionner votre Sexe", Toast.LENGTH_SHORT).show();
                        return null;
                    }


                } else {




                    Toast.makeText(getApplicationContext(), "Verfier Vos Informations", Toast.LENGTH_SHORT).show();

                    return null;
                }

            }else{
                Toast.makeText(getApplicationContext(), "Veuillez Accepter les Condition et les termes ", Toast.LENGTH_SHORT).show();
                return null;
            }

    }


    void  CreatUserInAuthSystem(final User user){

        final String TAG = "Sign IN";
        String x;
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user1= mAuth.getCurrentUser();

                            CreatUserInRealTimeDataBase(user,user1);



                            Intent intent = new Intent(MainSign.this,MainWelcomeSign.class);
                            //Toast.makeText(getApplicationContext(), "Authentication avec Succes : Vous Etes maintenant un utilisateur de EMZIO :D.",
                                    //Toast.LENGTH_SHORT).show();
                            updateSharedPrefernces(user);
                            startActivity(intent);
                            finish();

                        } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainSign.this, "Authentication échouer : E-Mail deja pris.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });



    }


    void CreatUserInRealTimeDataBase(User user, FirebaseUser user1){
        final String TAG = "SIGN IN";

            //user1 = mAuth.getCurrentUser();

            user.setID(user1.getUid());
            id= user.getID();


                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users").child(user.getID()).setValue(user);

        updateSharedPrefernces(user);


    }



     public boolean isValidPassword(String password) {


        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[_+.])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);

        return m.matches();
    }


    public boolean isValidName(String name){
        String regex ="(?i)(^[a-z ]+)[a-z .,-]((?! .,-)$){1,25}$";

        Pattern p = Pattern.compile(regex);

        if(name==null){
            return  false;
        }
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public boolean isValidMail(String mail){
        String regex ="([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";

        Pattern p = Pattern.compile(regex);

        if(mail==null){
            return  false;
        }
        Matcher m = p.matcher(mail);
        return m.matches();
    }

    public boolean isValidNumberPhone(String phone){
        String regex ="^(0)([0-9]{9})$";

        Pattern p = Pattern.compile(regex);

        if(phone==null){
            return  false;
        }
        Matcher m = p.matcher(phone);
        return m.matches();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
        if(text.equals("PRO")){
            pro=true;
        }else{
            pro=false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        creat.setEnabled(false);
    }


    void updateSharedPrefernces(User user){

        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("Logged",true);
        editor.putString("Nom",user.getNom());
        editor.putString("Prenom",user.getPrenom());
        editor.putString("Sexe",user.getSexe());
        editor.putString("Email",user.getEmail());
        editor.putString("Password",user.getPassword());
        editor.putString("Phone",user.getPhone());
        editor.putString("ID",user.getID());
        editor.putBoolean("Pro",user.isPro());
        //Toast.makeText(MainLogin.this,user.getEmail(),Toast.LENGTH_SHORT).show();
        editor.commit();
        editor.apply();

        //updtecurrentUser();


    }


}

