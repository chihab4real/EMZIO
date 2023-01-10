package com.example.emzio.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.Adresse;
import com.example.emzio.CommandeListeAdapter;
import com.example.emzio.MainAdd;
import com.example.emzio.MainMenu;
import com.example.emzio.MainModifierProduit;
import com.example.emzio.MainMyProductDetails;
import com.example.emzio.MesProduitsAdapter;
import com.example.emzio.Product;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.emzio.ProduitRechAdapter.convertDpToPixels;

public class MainMesProduits extends AppCompatActivity {
    User user;
    SharedPreferences share;
    public  static  ListView list;
    CardView cardView;
    Button button;
    TextView textView;
    ArrayList<Product> myArray= new ArrayList<>();

    MesProduitsAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mes_produits);
        list = findViewById(R.id.liste_produits);
        cardView = findViewById(R.id.list_card);
        button = findViewById(R.id.produitd_add);
        textView = findViewById(R.id.produit_noProducts);
        ////////////////////////////////////////////////////////////
        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));
        ////////////////////////////////////////////////////////////





        getProducts();



        if(myArray.isEmpty()){
            cardView.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.VISIBLE);
        }


        myAdapter = new MesProduitsAdapter(this,R.layout.mes_produits_list,myArray);
        myAdapter.notifyDataSetChanged();
        list.setAdapter(null);




        list.setAdapter(myAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(myArray.get(position));
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMesProduits.this, MainAdd.class));

            }
        });


    }


    public void getProducts(){


        for(int i = 0; i< Things.productsList.size();i++){
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
                layoutParams.height = convertDpToPixels((myArray.size()*140)+5,getApplicationContext()); //this is in pixels
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

    public void openDialog(final Product product) {
        final Dialog dialog = new Dialog(MainMesProduits.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.deleteconfirmation);


        dialog.show();

        Button oui,non,supp,mod;
        final LinearLayout lin1,lin2;
        lin1 = dialog.findViewById(R.id.opendlin1);
        lin2 = dialog.findViewById(R.id.opendlin2);
        supp = dialog.findViewById(R.id.supp);
        mod = dialog.findViewById(R.id.modd);
        oui = dialog.findViewById(R.id.oui_supp);
        non = dialog.findViewById(R.id.non_supp);


        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
            }
        });

        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Things.productModifier = product;
                dialog.dismiss();
                startActivity(new Intent(MainMesProduits.this,MainModifierProduit.class));
            }
        });

        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Supprimer(product);
                Intent intent = new Intent(MainMesProduits.this,MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Things.ordre = "ref";
                startActivity(intent);
                finish();
            }
        });


        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

    }

    private void Supprimer(Product product){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();



// Create a reference to the file to delete
        if(product.getImages().getImages_nb()==1){

            DeleteImage(product,"image1.jpg");
        }


        if(product.getImages().getImages_nb()==2){
            DeleteImage(product,"image1.jpg");
            DeleteImage(product,"image2.jpg");
        }

        if(product.getImages().getImages_nb()==3){

            DeleteImage(product,"image1.jpg");
            DeleteImage(product,"image2.jpg");
            DeleteImage(product,"image3.jpg");

        }


        if(product.getImages().getImages_nb()==4){
            DeleteImage(product,"image1.jpg");
            DeleteImage(product,"image2.jpg");
            DeleteImage(product,"image3.jpg");
            DeleteImage(product,"image4.jpg");


        }




        mDatabase.child("Product").child(product.getID()).removeValue();





        for(int i=0;i<Things.histo.size();i++){
            if(Things.productsList.get(i).getID().equals(product.getID())){
                //Toast.makeText(getApplicationContext(),Things.histo.get(i).getId(),Toast.LENGTH_SHORT).show();
                Things.productsList.remove(product);


            }
        }



        Toast.makeText(getApplicationContext(),"Produits Supprimé avec Succes!",Toast.LENGTH_SHORT).show();




    }


    private void DeleteImage(Product product,String name){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


// Delete the file
        StorageReference desertRef = storageRef.child("Products/"+product.getID_user()+"/"+product.getID()+"/"+name);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });


    }
}