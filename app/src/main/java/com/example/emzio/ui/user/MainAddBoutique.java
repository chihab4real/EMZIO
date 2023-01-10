package com.example.emzio.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.Boutique;
import com.example.emzio.MainMenu;
import com.example.emzio.R;
import com.example.emzio.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainAddBoutique extends AppCompatActivity {

    SharedPreferences share;
    User user;
    Boutique boutique;
    EditText nomBoutique,adresseBoutique,infocBoutique;
    Spinner wilayaSpinner;
    String nom,adresse,infoc,wilaya;
    int num_wilaya;
    Button valider;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_boutique);

        /////////////////////////////////////////////
        share = getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));

        user.setID(share.getString("ID",null));

        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        /////////////////////////////////////////////
        valider = findViewById(R.id.boutique_valider);
        nomBoutique = findViewById(R.id.nombt);
        adresseBoutique = findViewById(R.id.adresse_adresse);
        infocBoutique = findViewById(R.id.adresse_info_sup);


        wilayaSpinner = findViewById(R.id.adresse_spinner);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.wilayas,android.R.layout.simple_spinner_item);

        adapter_type.setDropDownViewResource(R.layout.my_spinnez);
        wilayaSpinner.setAdapter(adapter_type);

        adresseBoutique.setText(user.getAdresse().getAdresse());
        infocBoutique.setText(user.getAdresse().getInfoSupp());

        if(user.getAdresse().getNum_wilaya()!=-1){
            wilayaSpinner.setSelection(user.getAdresse().getNum_wilaya());
        }


        wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wilaya = parent.getItemAtPosition(position).toString();
                num_wilaya = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                if(wilaya!=user.getAdresse().getWilaya() || !adresseBoutique.getText().toString().equals(user.getAdresse().getAdresse())
                        || !infocBoutique.getText().toString().equals(user.getAdresse().getInfoSupp())|| !nomBoutique.getText().toString().isEmpty()){

                    Boutique boutique;
                    Adresse x = new Adresse();
                    x.setAdresse(adresseBoutique.getText().toString());
                    x.setWilaya(wilaya);
                    x.setInfoSupp(infocBoutique.getText().toString());
                    x.setNum_wilaya(num_wilaya);
                    boutique = new Boutique(user.getID(),nomBoutique.getText().toString());
                    boutique.setAdresse(x);
                    mDatabase.child("DemandeBoutique").child(user.getID()).setValue(boutique);
                    Toast.makeText(getApplicationContext(),"Ton Demande a envoyer avec succes",Toast.LENGTH_LONG).show();
                    updateShared(true);
                    startActivity(new Intent(MainAddBoutique.this, MainMenu.class));
                    finish();


                }else{
                    Toast.makeText(getApplicationContext(),"Verfier vos INFO",Toast.LENGTH_SHORT).show();
                    updateShared(false);
                }

            }
        });


    }

    public void updateShared(boolean x){

        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("DemandeEnvoyer",x);
        editor.commit();
        editor.apply();
    }
}