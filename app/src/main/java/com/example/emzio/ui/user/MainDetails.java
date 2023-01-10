package com.example.emzio.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.MainLogin;
import com.example.emzio.R;
import com.example.emzio.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainDetails extends AppCompatActivity {
    SharedPreferences share;
    User user;
    private DatabaseReference mDatabase;
    EditText nom, prenom, mail,phone,password_old, password_new;
    RadioButton homme,femme;
    Button valider, valider_password;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_details);
        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));


        nom = findViewById(R.id.details_nom);
        prenom = findViewById(R.id.details_prenom);
        mail = findViewById(R.id.details_mail);
        phone = findViewById(R.id.details_phone);
        password_new = findViewById(R.id.details_password_second);
        password_old = findViewById(R.id.details_password_first);
        homme=findViewById(R.id.details_homme);

        femme=findViewById(R.id.details_femme);

        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        mail.setText(user.getEmail());
        mail.setEnabled(false);
        phone.setText(user.getPhone());

       if(user.getSexe().equals("Homme")){
            homme.setChecked(true);
            femme.setChecked(false);
        }else{
            femme.setChecked(true);
            homme.setChecked(false);
        }

       valider_password = findViewById(R.id.details_password_valider);
       valider = findViewById(R.id.details_valider);

       valider.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               mDatabase = FirebaseDatabase.getInstance().getReference("Users");
               if(!nom.getText().toString().equals(user.getNom()) || !prenom.getText().toString().equals(user.getPrenom()) || !phone.getText().toString().equals(user.getPhone())){
                        if(isValidName(nom.getText().toString()) && isValidName(prenom.getText().toString()) && isValidNumberPhone(phone.getText().toString())){
                            mDatabase.child(user.getID()).child("prenom").setValue(prenom.getText().toString());
                            mDatabase.child(user.getID()).child("nom").setValue(nom.getText().toString());
                            mDatabase.child(user.getID()).child("phone").setValue(phone.getText().toString());

                            User user1 = new User();

                            user1.setID(user.getID());
                            user1.setPhone(phone.getText().toString());
                            user1.setPrenom(prenom.getText().toString());
                            user1.setNom(nom.getText().toString());
                            user1.setSexe(user.getSexe());
                            user1.setEmail(user.getEmail());
                            user1.setPro(user.isPro());
                            user1.setPassword(user.getPassword());

                            updateSharedPrefernces(user1);


                            Toast.makeText(getApplicationContext(),"Inforamtion Changer avec Succes",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"Veuillez Verfier vos informations",Toast.LENGTH_SHORT).show();
                        }

               }else{
                   Toast.makeText(getApplicationContext(),"Vous changez rien",Toast.LENGTH_SHORT).show();
               }


           }
       });

       valider_password.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               changePassword();
           }
       });




    }


    void createUser(User user){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getID()).setValue(user);
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

        updtecurrentUser();


    }

    public void changePassword(){
        if(!password_old.getText().toString().equals("")){
            if(password_old.getText().toString().equals(user.getPassword())){

                if(!password_new.getText().toString().equals(password_old.getText().toString())){
                    if(isValidPassword(password_new.getText().toString())){

                        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                        mDatabase.child(user.getID()).child("password").setValue(password_new.getText().toString());
                        SharedPreferences.Editor editor = share.edit();
                        editor.putString("Password",password_new.getText().toString());
                        editor.commit();
                        editor.apply();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        currentUser.updatePassword(password_new.getText().toString());
                        Toast.makeText(getApplicationContext(),"Mot Pass Changé avec Succes",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Nouveau Mot De Pass INVALIDE",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Le mot de pass est le meme",Toast.LENGTH_SHORT).show();
                }



            }else{
                Toast.makeText(getApplicationContext(),"Votre Ancien Mot de pass est faux",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Le Champ de Ancien Mot de Pass est Vide",Toast.LENGTH_SHORT).show();
        }

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

    public void updtecurrentUser(){

        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));

    }
}
