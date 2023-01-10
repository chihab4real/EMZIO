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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.MainMenu;
import com.example.emzio.MainPanier;
import com.example.emzio.Product;
import com.example.emzio.ProduitHistory;
import com.example.emzio.ProduitRechAdapter;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.example.emzio.Welcome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainRechrcheRec extends AppCompatActivity {

    TextView vide;
    Button contini;
    CardView cardView;
    public static GridView grid;
    SharedPreferences share;
   public static  ArrayList<Product> myArray = new ArrayList<>();
    ArrayList<ProduitHistory> myArrayIds = new ArrayList<>();
    ProduitRechAdapter myAdapter;
    public static User user;
    CardView cardViewNumber;
    TextView numberp;
    ImageButton panier;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rechrche_rec);
         cardView = findViewById(R.id.card_vu);


        cardViewNumber = findViewById(R.id.card_number);
        numberp = findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }




        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));



        grid = findViewById(R.id.liste_rech_rec);

        //Toast.makeText(getApplicationContext(),String.valueOf(Things.histo.size()),Toast.LENGTH_SHORT).show();
        myArrayIds = Things.histo;



        searchProducts();

        ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();
        layoutParams.height = convertDpToPixels((myArray.size()*130)+5,getApplicationContext()); //this is in pixels
        grid.setLayoutParams(layoutParams);



        vide = findViewById(R.id.recherche_rec_noProducts);
        contini=findViewById(R.id.recherche_rec_continue);




        grid.setAdapter(null);

        if(myArray.isEmpty()){
            grid.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            contini.setVisibility(View.VISIBLE);
            vide.setVisibility(View.VISIBLE);
        }else{
            grid.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            myAdapter = new ProduitRechAdapter(this,myArray);
            grid.setAdapter(myAdapter);
            contini.setVisibility(View.GONE);
            vide.setVisibility(View.GONE);
        }

        contini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainRechrcheRec.this, MainMenu.class));
                finish();
            }
        });

        panier =findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainRechrcheRec.this, MainPanier.class));
            }
        });
    }





    void searchProducts(){
        int i,j;
        myArray = new ArrayList<>();
        if(!myArrayIds.isEmpty()){
            for(i=0;i<myArrayIds.size();i++){
                for(j=0;j<Things.productsList.size();j++){

                    if(myArrayIds.get(i).getId().equals(Things.productsList.get(j).getID())){
                        if(!myArray.contains(Things.productsList.get(j))){
                            myArray.add(Things.productsList.get(j));
                        }


                    }
                }

            }
        }


    }




    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    @Override
    public void onBackPressed() {
        finish();

    }



    public  void Fonct(){



        Things.histo = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("History").child("Products").child(share.getString("ID",null));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProduitHistory produitHistory = snapshot.getValue(ProduitHistory.class);
                    Things.histo.add(produitHistory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
