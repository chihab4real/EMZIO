package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.ui.user.MainMesProduits;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainMyProductDetails extends AppCompatActivity {

    Button ok,modifier,supprimer,dispo;
    TextView nom,prix,marque,type,decs,id,qt;
    TextView title,likes,vus,demande;
    ImageView image1,image2,image3,image4,imageP;
    Product product;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        product = Things.productDetails;



        setContentView(R.layout.activity_main_my_product_details);

        /////////////////////////////////////////////////////

        ok = findViewById(R.id.ok);
        modifier = findViewById(R.id.modifier);
        supprimer = findViewById(R.id.supprimer);
        dispo = findViewById(R.id.indispo);

        nom = findViewById(R.id.details_p_nom);
        prix = findViewById(R.id.details_p_prix);
        marque = findViewById(R.id.details_p_marque);
        type = findViewById(R.id.details_p_type);
        decs = findViewById(R.id.details_p_decs);
        id = findViewById(R.id.details_p_id);
        qt= findViewById(R.id.details_p_qt);

        title = findViewById(R.id.title);
        likes = findViewById(R.id.likes_number);
        vus = findViewById(R.id.seen_number);
        demande = findViewById(R.id.demande_number);

        imageP = findViewById(R.id.image_p);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        /////////////////////////////////////////////////////////

        nom.setText(product.getNom());
        prix.setText(product.getPrix()+" Da");
        marque.setText(product.getMarque());
        type.setText(product.getType());
        decs.setText(product.getDescription());
        id.setText(product.getID());
        title.setText(product.getNom());
        qt.setText(String.valueOf(product.getQuantite()));


        if(product.isDisponible()){
            dispo.setText("Marqué comme indisponible");
        }else{
            dispo.setText("Marqué comme disponible");
        }

        Picasso.get().load(product.getImage1()).resize(720,720).into(imageP);

        switch (product.getImages().getImages_nb()){
            case 1 :
                Picasso.get().load(product.getImage1()).resize(720,720).into(image1);

                findViewById(R.id.card2).setVisibility(View.GONE);
                findViewById(R.id.card3).setVisibility(View.GONE);
                findViewById(R.id.card4).setVisibility(View.GONE);

                break;
            case 2:
                Picasso.get().load(product.getImage1()).resize(720,720).into(image1);
                Picasso.get().load(product.getImage2()).resize(720,720).into(image2);

                findViewById(R.id.card3).setVisibility(View.GONE);
                findViewById(R.id.card4).setVisibility(View.GONE);
                break;
            case 3:
                Picasso.get().load(product.getImage1()).resize(720,720).into(image1);
                Picasso.get().load(product.getImage2()).resize(720,720).into(image2);
                Picasso.get().load(product.getImage3()).resize(720,720).into(image3);




                findViewById(R.id.card4).setVisibility(View.GONE);

                break;
            case 4 :
                Picasso.get().load(product.getImage1()).resize(720,720).into(image1);
                Picasso.get().load(product.getImage2()).resize(720,720).into(image2);
                Picasso.get().load(product.getImage3()).resize(720,720).into(image3);
                Picasso.get().load(product.getImage4()).resize(720,720).into(image4);

                break;
        }

        likes.setText(String.valueOf(Things.productLikesnumber)+" Likes");
        vus.setText(String.valueOf(Things.productVuesnumber)+" Vus");
        demande.setText(String.valueOf(Things.productDemandesnumber)+ " Commandes");

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Modifier",Toast.LENGTH_SHORT).show();
                Things.productModifier = product;
                startActivity(new Intent(MainMyProductDetails.this,MainModifierProduit.class));
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(product);
            }
        });


        dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Product");
                if(product.isDisponible()){
                    mDatabase.child(product.getID()).child("disponible").setValue(false);
                    product.setDisponible(false);
                    dispo.setText("Marqué comme disponible");
                }else{
                    mDatabase.child(product.getID()).child("disponible").setValue(true);
                    product.setDisponible(true);
                    dispo.setText("Marqué comme indisponible");
                }
            }
        });



    }

    public void openDialog(final Product product) {
        final Dialog dialog = new Dialog(MainMyProductDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.deleteconfirmation);


        dialog.show();

        Button oui,non;
        LinearLayout lin1,lin2;
        lin1 = dialog.findViewById(R.id.opendlin1);
        lin2 = dialog.findViewById(R.id.opendlin2);
        oui = dialog.findViewById(R.id.oui_supp);
        non = dialog.findViewById(R.id.non_supp);

        lin1.setVisibility(View.GONE);
        lin2.setVisibility(View.VISIBLE);

        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Supprimer(product);

                Intent intent = new Intent(MainMyProductDetails.this,MainMenu.class);
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