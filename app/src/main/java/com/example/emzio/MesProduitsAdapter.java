package com.example.emzio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emzio.ui.user.MainMesProduits;
import com.example.emzio.ui.user.MainMonBoutique;
import com.example.emzio.ui.user.MainRechrcheRec;
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

import java.util.ArrayList;

import static com.example.emzio.ProduitRechAdapter.convertDpToPixels;

public class MesProduitsAdapter extends ArrayAdapter<Product> {
    private static final  String TAG ="MesProduitsAdapter";
    private Context context;
    private int resource;
    private  ArrayList<Product> products;
    public   int likesnumber=0,vuesnumber=0;
    public ArrayList<ProduitLike> myArray= new ArrayList<>();




    public MesProduitsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> liste) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
        this.products = liste;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);


        TextView id,prix,nom,marque,type;


        prix= convertView.findViewById(R.id.prix_myproduit);
        nom= convertView.findViewById(R.id.nom_myproduit);
        marque= convertView.findViewById(R.id.marque_myproduit);
        type= convertView.findViewById(R.id.type_myproduit);
        ImageView imageView =convertView.findViewById(R.id.mesproduits_image);
        Button modifier,supprimer;



        prix.setText(changeString(products.get(position).getPrix())+"Da");
        nom.setText(products.get(position).getNom());
        marque.setText(products.get(position).getMarque());
        type.setText(products.get(position).getType());

        Picasso.get().load(products.get(position).getImg()).resize(720,720).into(imageView);

        /*

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Modifier",Toast.LENGTH_SHORT).show();
                Things.productModifier = products.get(position);
                context.startActivity(new Intent(context,MainModifierProduit.class));
            }
        });

        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Supprimer(products.get(position));
                Toast.makeText(context,"Produit Supprimé avec Succes",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Things.ordre = "ref";
                context.startActivity(intent);

            }
        });

*/

        //notifyDataSetChanged();

        return convertView;
    }

    public String changeString(String number){

        char[] bytees = new char[number.length()];
        String one="",two="";

        for(int i =0 ; i<number.length();i++){
            bytees[i] = number.charAt(i);
        }
/*
        if(number.length()==4){
            one = one+String.valueOf(bytees[0])+" ";
            one = one+String.valueOf(bytees[1]);
            one = one+String.valueOf(bytees[2]);
            one = one+String.valueOf(bytees[3]);
            return one;


        }
         if(number.length()==5){
             one = one+String.valueOf(bytees[0]);
             one = one+String.valueOf(bytees[1])+" ";
             one = one+String.valueOf(bytees[2]);
             one = one+String.valueOf(bytees[3]);
             one = one+String.valueOf(bytees[4]);
             return one;

          }

            if(number.length()==6){
                one = one+String.valueOf(bytees[0]);
                one = one+String.valueOf(bytees[1]);
                one = one+String.valueOf(bytees[2])+" ";
                one = one+String.valueOf(bytees[3]);
                one = one+String.valueOf(bytees[4]);
                one = one+String.valueOf(bytees[5]);
                return one;

           }
*/
        if(number.length()>=4 && number.length()<=6){
            for(int i = 0;i<number.length();i++){
                one = one+String.valueOf(bytees[i]);
                if(number.length()-4==i){
                    one = one+" ";
                }
            }
            return one;
        }




        return number;
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

        ViewGroup.LayoutParams layoutParams =  MainMonBoutique.list.getLayoutParams();
        layoutParams.height = layoutParams.height-convertDpToPixels(220); //this is in pixels
        MainMonBoutique.list.setLayoutParams(layoutParams);


        /*ViewGroup.LayoutParams layoutParams1 =  MainMesProduits.list.getLayoutParams();
        layoutParams.height = layoutParams.height-convertDpToPixels(220); //this is in pixels
        MainMesProduits.list.setLayoutParams(layoutParams1);*/


        for(int i=0;i<Things.histo.size();i++){
            if(Things.productsList.get(i).getID().equals(product.getID())){
                Toast.makeText(context,Things.histo.get(i).getId(),Toast.LENGTH_SHORT).show();
                Things.productsList.remove(product);


            }
        }



        Toast.makeText(context,"Produits Supprimé avec Succes!",Toast.LENGTH_SHORT).show();
        products.remove(product);
        notifyDataSetChanged();



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

  /*  public  void getLikes(Product product){

        DatabaseReference mDatabase1;
        final ArrayList<ProduitLike> arrayList = new ArrayList<>();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("ProductsLikedUsers").child(product.getID());


        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProduitLike produitLike = snapshot.getValue(ProduitLike.class);
                    arrayList.add(produitLike);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        likesnumber = arrayList.size();
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
                    Toast.makeText(context,String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myArray = arrayList;
        //Toast.makeText(context,String.valueOf(vuesnumber),Toast.LENGTH_SHORT).show();

    }*/

}
