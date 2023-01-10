package com.example.emzio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emzio.ui.home.HomeFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainModifierProduit extends AppCompatActivity {

    Button modifer;
    EditText id,nom,type,marque,prix,decs,qt;
    ImageView imageView1,imageView2,imageView3,imageView4;

    Product productBefore,productAfter;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_modifier_produit);
        productBefore = Things.productModifier;

        modifer = findViewById(R.id.modifier_et_valider);

        id= findViewById(R.id.modifier_ID);
        nom= findViewById(R.id.modifier_nom);
        type = findViewById(R.id.modifier_type);
        marque = findViewById(R.id.modifier_marque);
        prix = findViewById(R.id.modifier_prix);
        decs = findViewById(R.id.modifier_decs);
        qt = findViewById(R.id.modifier_qt);

        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);

        id.setText(productBefore.getID());
        nom.setText(productBefore.getNom());
        type.setText(productBefore.getType());
        marque.setText(productBefore.getMarque());
        prix.setText(productBefore.getPrix());
        decs.setText(productBefore.getDescription());
        qt.setText(String.valueOf(productBefore.getQuantite()));


       switch (productBefore.getImages().getImages_nb()){
           case 1 :
               Picasso.get().load(productBefore.getImage1()).resize(720,720).into(imageView1);

               findViewById(R.id.card2).setVisibility(View.GONE);
               findViewById(R.id.card3).setVisibility(View.GONE);
               findViewById(R.id.card4).setVisibility(View.GONE);

               break;
           case 2:
               Picasso.get().load(productBefore.getImage1()).resize(720,720).into(imageView1);
               Picasso.get().load(productBefore.getImage2()).resize(720,720).into(imageView2);

               findViewById(R.id.card3).setVisibility(View.GONE);
               findViewById(R.id.card4).setVisibility(View.GONE);
               break;
           case 3:
               Picasso.get().load(productBefore.getImage1()).resize(720,720).into(imageView1);
               Picasso.get().load(productBefore.getImage2()).resize(720,720).into(imageView2);
               Picasso.get().load(productBefore.getImage3()).resize(720,720).into(imageView3);




               findViewById(R.id.card4).setVisibility(View.GONE);

               break;
           case 4 :
               Picasso.get().load(productBefore.getImage1()).resize(720,720).into(imageView1);
               Picasso.get().load(productBefore.getImage2()).resize(720,720).into(imageView2);
               Picasso.get().load(productBefore.getImage3()).resize(720,720).into(imageView3);
               Picasso.get().load(productBefore.getImage4()).resize(720,720).into(imageView4);

               break;
       }

       modifer.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               productAfter = productBefore;
               mDatabase = FirebaseDatabase.getInstance().getReference("Product");
               if(!nom.getText().toString().equals(productBefore.getNom()) || !prix.getText().toString().equals(productBefore.getPrix()) || !decs.getText().toString().equals(productBefore.getDescription())
                       || !qt.getText().toString().equals(String.valueOf(productBefore.getQuantite()))){

                   mDatabase.child(productBefore.getID()).child("nom").setValue(nom.getText().toString());
                   mDatabase.child(productBefore.getID()).child("description").setValue(decs.getText().toString());
                   mDatabase.child(productBefore.getID()).child("prix").setValue(prix.getText().toString());
                   mDatabase.child(productBefore.getID()).child("quantite").setValue(Integer.parseInt(qt.getText().toString()));

                   Toast.makeText(getApplicationContext(),"Produit Modifier Avec Succes!",Toast.LENGTH_LONG).show();


                   Intent intent = new Intent(MainModifierProduit.this,MainMenu.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   Things.ordre = "ref";
                   startActivity(intent);





               }else {
                   Toast.makeText(getApplicationContext(),"Vous n'avez changer rien",Toast.LENGTH_LONG).show();
               }
           }
       });




    }
}