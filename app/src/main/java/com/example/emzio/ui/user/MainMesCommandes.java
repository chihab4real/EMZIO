package com.example.emzio.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.Commande;
import com.example.emzio.CommandeListeAdapter;
import com.example.emzio.MainCategorie;
import com.example.emzio.MainMenu;
import com.example.emzio.MainPanier;
import com.example.emzio.MainProduitCommande;
import com.example.emzio.Product;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.example.emzio.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainMesCommandes extends AppCompatActivity {
    ListView commandes_liste;
    CardView cardView;
    SharedPreferences share;
    FirebaseStorage storage;
    ArrayList<Commande> myArray = new ArrayList<>();
    DatabaseReference mDatabase;
    CommandeListeAdapter myAdapter;
    TextView noCommande;
    ArrayList<String> arrayList= new ArrayList<>();
    Button conti;
    int number;
    User user;
    CardView cardViewNumber;
    ImageButton panier;
    TextView numberp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mes_commandes);
        cardView = findViewById(R.id.list_card);


        cardViewNumber = findViewById(R.id.card_number);
        numberp = findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }


        ////////////////////////////////////////////////////////////
        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));
        ////////////////////////////////////////////////////////////





        noCommande = findViewById(R.id.commande_noProducts);
        conti = findViewById(R.id.commande_continue);
        commandes_liste = findViewById(R.id.liste_commande);

        getAllCommandes(user);
        int size = myArray.size();

        if(myArray.isEmpty()){
            noCommande.setVisibility(View.VISIBLE);
            conti.setVisibility(View.VISIBLE);
            commandes_liste.setVisibility(View.GONE);

            cardView.setVisibility(View.GONE);

        }




        myAdapter = new CommandeListeAdapter(this,R.layout.commande_list,myArray);
        myAdapter.notifyDataSetChanged();
        commandes_liste.setAdapter(null);




        commandes_liste.setAdapter(myAdapter);









        commandes_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Things.commande = myArray.get(position);
                    startActivity(new Intent(MainMesCommandes.this, MainProduitCommande.class));
            }
        });

        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMesCommandes.this, MainMenu.class));
                finish();
            }
        });

        panier =findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMesCommandes.this, MainPanier.class));
            }
        });

    }


    void getAllCommandes(final User user){

        storage = FirebaseStorage.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Commande");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Commande commande = snapshot.getValue(Commande.class);

                    if(commande.getID_client().equals(user.getID())){
                        myArray.add(commande);
                        number++;
                        //oast.makeText(getApplicationContext(),String.valueOf(number),Toast.LENGTH_SHORT).show();



                        if(myArray.isEmpty()){
                            noCommande.setVisibility(View.VISIBLE);
                            conti.setVisibility(View.VISIBLE);
                            commandes_liste.setVisibility(View.GONE);

                            cardView.setVisibility(View.GONE);







                        }else{
                            noCommande.setVisibility(View.GONE);
                            conti.setVisibility(View.GONE);
                            commandes_liste.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);


                            ViewGroup.LayoutParams layoutParams = commandes_liste.getLayoutParams();
                            layoutParams.height = convertDpToPixels((number*87)+5,getApplicationContext()); //this is in pixels
                            commandes_liste.setLayoutParams(layoutParams);

                        }


                        myAdapter.notifyDataSetChanged();
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });





    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    public void onStart() {
        super.onStart();


        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }


    }
}
