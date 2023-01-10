package com.example.emzio.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.Boutique;
import com.example.emzio.MainAdd;
import com.example.emzio.MainModifierProduit;
import com.example.emzio.MainMyProductDetails;
import com.example.emzio.MesProduitsAdapter;
import com.example.emzio.Product;
import com.example.emzio.ProduitCommande;
import com.example.emzio.ProduitLike;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainMonBoutique extends AppCompatActivity {

    User user;
    SharedPreferences share;

   public static ListView list;
    CardView cardView;
    Button button;
    TextView textView;
    ArrayList<Product> myArray= new ArrayList<>();
    Boutique boutique;
    private  static int vuesnumber=0,likesnumber = 0;

    TextView nomBoutique,adresseBoutique,infosBoutique,wilayaBoutique;
    ScrollView scrollView;


    MesProduitsAdapter myAdapter;

    DatabaseReference mDatabase1,mDatabase2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Things.productLikesnumber=0;
        Things.productVuesnumber=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mon_boutique);

        list = findViewById(R.id.liste_produits);
        cardView = findViewById(R.id.list_card);
        button = findViewById(R.id.produitd_add);
        textView = findViewById(R.id.produit_noProducts);

        boutique= new Boutique();



        nomBoutique = findViewById(R.id.nom_monboutique);

        adresseBoutique = findViewById(R.id.adresse_monboutique);
        infosBoutique = findViewById(R.id.infos_monboutique);
        wilayaBoutique = findViewById(R.id.wilaya_monboutique);
        scrollView = findViewById(R.id.scrollView);



        ////////////////////////////////////////////////////////////
        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));

        boutique.setName_Boutique((share.getString("nomBoutique","vide")));
        boutique.setAdresse(new Adresse());
        boutique.getAdresse().setAdresse(share.getString("adresseBoutique","vide"));
        boutique.getAdresse().setInfoSupp(share.getString("adresseInfo","vide"));
        boutique.getAdresse().setWilaya(share.getString("adresseWilaya","vide"));
        boutique.setID_Boutique(share.getString("idBoutique","vide"));

        ////////////////////////////////////////////////////////////

        nomBoutique.setText(boutique.getName_Boutique());

        adresseBoutique.setText(boutique.getAdresse().getAdresse());
        infosBoutique.setText(boutique.getAdresse().getInfoSupp());
        wilayaBoutique.setText(boutique.getAdresse().getWilaya());
        scrollView.fullScroll(ScrollView.FOCUS_UP);





        getProducts();

        final Timer timer = new Timer();




        myAdapter = new MesProduitsAdapter(this,R.layout.mes_produits_list,myArray);
        myAdapter.notifyDataSetChanged();
        list.setAdapter(null);




        list.setAdapter(myAdapter);




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Things.productDetails = myArray.get(position);
                getLikes(myArray.get(position));
                getVus(myArray.get(position));
                getDemande(myArray.get(position));

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainMonBoutique.this, MainMyProductDetails.class));

                    }
                },1000);



            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMonBoutique.this, MainAdd.class));

            }
        });

    }


    public void getProducts(){


        for(int i = 0; i< Things.productsList.size(); i++){
            if(Things.productsList.get(i).getID_user().equals(user.getID())){
                myArray.add(Things.productsList.get(i));

                if(myArray.isEmpty()){
                    button.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                }else{



                    button.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }

                ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
                layoutParams.height = convertDpToPixels((myArray.size()*145)+5,getApplicationContext()); //this is in pixels
                list.setLayoutParams(layoutParams);


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

        public  void getLikes(Product product){

        DatabaseReference mDatabase1;
        final ArrayList<ProduitLike> arrayList = new ArrayList<>();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("ProductsLikedUsers").child(product.getID());


        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProduitLike produitLike = snapshot.getValue(ProduitLike.class);
                    arrayList.add(produitLike);
                    Things.productLikesnumber = arrayList.size();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            //Toast.makeText(getApplicationContext(),String.valueOf(Things.productLikesnumber),Toast.LENGTH_SHORT).show();
    }

    public  void getVus(Product product){
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Seen").child(product.getID());
        final ArrayList<ProduitLike> arrayList = new ArrayList<>();
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProduitLike produitLike = snapshot.getValue(ProduitLike.class);
                    arrayList.add(produitLike);
                    Things.productVuesnumber = arrayList.size();
                    //Toast.makeText(context,String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Things.productVuesnumber = arrayList.size();
        Toast.makeText(getApplicationContext(),String.valueOf(Things.productVuesnumber),Toast.LENGTH_SHORT).show();*/

    }


    public  void getDemande(Product product){

        Things.productDemandesnumber=0;
        for(int i =0;i<Things.commandeArrayList.size();i++){
            for(int j=0;j<Things.commandeArrayList.get(i).getProduits().size();j++){
                if(product.getID().equals(Things.commandeArrayList.get(i).getProduits().get(j).getProduit_ID())){
                    Things.productDemandesnumber+=Things.commandeArrayList.get(i).getProduits().get(j).getProdui_qt();
                }
            }
        }



    }


    @Override
    protected void onStart() {
        super.onStart();
        Things.productLikesnumber=0;
        Things.productVuesnumber=0;
    }
}