package com.example.emzio.ui.user;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.emzio.R;
import com.example.emzio.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainAdresse extends AppCompatActivity{

    Spinner wilayaSpinner;
    EditText adresseEdit ,info_supEdit;
    Adresse Useradresse;
    String  adresse, info_sup,wilaya;
    int num_wilaya;
    Button valider;
    User user;
    SharedPreferences share;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adresse);
        wilayaSpinner = findViewById(R.id.adresse_spinner);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.wilayas,android.R.layout.simple_spinner_item);

        adapter_type.setDropDownViewResource(R.layout.my_spinnez);
        wilayaSpinner.setAdapter(adapter_type);

        adresseEdit = findViewById(R.id.adresse_adresse);
        info_supEdit = findViewById(R.id.adresse_info_sup);
        valider = findViewById(R.id.adresse_valider);


        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",0));

        adresseEdit.setText(user.getAdresse().getAdresse());
        info_supEdit.setText(user.getAdresse().getInfoSupp());


        if(user.getAdresse().getNum_wilaya()!=0){
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
                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                if(wilaya!=user.getAdresse().getWilaya() || !adresseEdit.getText().toString().equals(user.getAdresse().getAdresse())
                        || !info_supEdit.getText().toString().equals(user.getAdresse().getInfoSupp())){

                    if(num_wilaya!=0){
                        Adresse x = new Adresse();
                        x.setAdresse(adresseEdit.getText().toString());
                        x.setWilaya(wilaya);
                        x.setInfoSupp(info_supEdit.getText().toString());
                        x.setNum_wilaya(num_wilaya);
                        mDatabase.child(user.getID()).child("adresse").setValue(x);
                        updateShared(x);
                        Toast.makeText(getApplicationContext(),"Adresse changer avec succes!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{

                    }


                }

            }
        });

    }

    public void updateShared(Adresse adresse){

        SharedPreferences.Editor editor = share.edit();
        editor.putString("Adresse_adresse",adresse.getAdresse());
        editor.putString("Adresse_info",adresse.getInfoSupp());
        editor.putString("Adresse_wilaya",adresse.getWilaya());
        editor.putInt("Adresse_wilaya_num",adresse.getNum_wilaya());

        editor.commit();
        editor.apply();
    }

}
