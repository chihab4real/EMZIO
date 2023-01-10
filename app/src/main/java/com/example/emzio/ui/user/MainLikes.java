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
import com.example.emzio.LikeListAdapter;
import com.example.emzio.MainCategorie;
import com.example.emzio.MainMenu;
import com.example.emzio.MainPanier;
import com.example.emzio.MainProduit;
import com.example.emzio.Product;
import com.example.emzio.ProduitLike;
import com.example.emzio.R;
import com.example.emzio.StatuListAdapter;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainLikes extends AppCompatActivity {
    ListView listView;
    LikeListAdapter myAdapter;
    DatabaseReference mDatabase, mDatabase1;
    ArrayList<ProduitLike> likesArray = new ArrayList<>();
    SharedPreferences share;
    Button delet_all,conti;
    User user;
    CardView cardView;
    TextView vs_aim,noLikes;
    CardView cardViewNumber;
    TextView numberp;
    ImageButton panier;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_likes);

        cardView = findViewById(R.id.card_likes);

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
        delet_all = findViewById(R.id.like_delet_all);
        noLikes = findViewById(R.id.likes_noLikes);
        conti = findViewById(R.id.likes_continue);
        vs_aim = findViewById(R.id.vous_aimez);
        Things.produitLikes= new ArrayList<>();

        getAll(user);
        listView = findViewById(R.id.list_likes);
        listView.setAdapter(null);


        if(likesArray.isEmpty()){
            cardView.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.VISIBLE);
        }


        myAdapter = new LikeListAdapter(this,R.layout.likeproduit,likesArray);



        myAdapter.notifyDataSetChanged();

        listView.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainProduit.class);
                Product product = new Product();
                product = getTheProduct(likesArray.get(position));
                Things.product_act = product;
                startActivity(intent);
            }
        });


        delet_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Things.produitLikes = new ArrayList<>();
                Delet_all();
                noLikes.setVisibility(View.VISIBLE);
                conti.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
                delet_all.setVisibility(View.GONE);
                vs_aim.setVisibility(View.GONE);

            }
        });


        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainLikes.this, MainMenu.class));
                finish();
            }
        });

        panier =findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainLikes.this, MainPanier.class));
            }
        });

    }

    void getAll(User user){


        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserLikedProducts").child(user.getID());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProduitLike produitLike = snapshot.getValue(ProduitLike.class);
                    //Toast.makeText(getApplicationContext(),produitLike.getID_Produit(),Toast.LENGTH_SHORT).show();
                    likesArray.add(produitLike);

                    if(likesArray.isEmpty()){
                        noLikes.setVisibility(View.VISIBLE);
                        conti.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);
                        delet_all.setVisibility(View.GONE);
                        vs_aim.setVisibility(View.GONE);


                    }else{
                        noLikes.setVisibility(View.GONE);
                        conti.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        delet_all.setVisibility(View.VISIBLE);
                        vs_aim.setVisibility(View.VISIBLE);

                    }

                    ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                    layoutParams.height = convertDpToPixels((likesArray.size()*126)+5,getApplicationContext()); //this is in pixels
                    listView.setLayoutParams(layoutParams);

                    myAdapter.notifyDataSetChanged();


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    void Delet_all(){
        ArrayList<ProduitLike> arrayList =likesArray;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("UserLikedProducts").child(user.getID()).removeValue();
        for(int i=0;i<arrayList.size();i++){
            deletx(arrayList.get(i));
        }

        listView.setAdapter(null);

    }

    void deletx(ProduitLike produitLike){

        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase1.child("ProductsLikedUsers").child(produitLike.getID_Produit()).child(user.getID()).removeValue();




    }
    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private Product getTheProduct(ProduitLike produitLike){
        for(int i=0;i<Things.productsList.size();i++){
            if(produitLike.getID_Produit().equals(Things.productsList.get(i).getID())){
                return Things.productsList.get(i);
            }
        }
        return Things.productsList.get(0);
    }
}

