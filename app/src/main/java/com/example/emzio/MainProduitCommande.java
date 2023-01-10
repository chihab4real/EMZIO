package com.example.emzio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainProduitCommande extends AppCompatActivity {
    GridView list;
    SharedPreferences share;
    User user;
    Commande commande;
    ArrayList<Product> myArray;
    DatabaseReference mDatabase;
    TextView prix,id;
    StatuListAdapter myAdapter;
    CardView cardViewNumber;
    TextView numberp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_produit_commande);

        prix = findViewById(R.id.prix_commande);
        id = findViewById(R.id.id_commande);


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

        list = findViewById(R.id.list_produit_mes_commandes);


        commande = Things.commande;

        prix.setText(String.valueOf(commande.getPrix()));
        id.setText(commande.getID_commande());


        getAllProductsinCommande(commande);

        myAdapter = new StatuListAdapter(this,R.layout.produit_mes_commandes,myArray,commande);
        myAdapter.notifyDataSetChanged();
        list.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();



    }

    void getAllProductsinCommande(Commande commande){

        ArrayList<Product> products = Things.productsList;
        myArray = new ArrayList<>();
        int i,j;

        for(i=0;i<commande.getProduits().size();i++){
            for(j=0;j<products.size();j++){
                if(commande.getProduits().get(i).getProduit_ID().equals(products.get(j).getID())){

                    myArray.add(products.get(j));
                    ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
                    layoutParams.height = convertDpToPixels(126*(myArray.size())+5,this.getApplicationContext()); //this is in pixels
                    list.setLayoutParams(layoutParams);
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
}
