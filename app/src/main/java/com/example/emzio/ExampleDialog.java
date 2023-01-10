package com.example.emzio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.emzio.ui.user.MainMonBoutique;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.emzio.ProduitRechAdapter.convertDpToPixels;

public class ExampleDialog extends AppCompatDialogFragment {
    Product product;

    ExampleDialog(Product product){
        this.product=product;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);

        builder.setTitle("Suppression de produit")
                .setMessage("Voulez vous vraiment supprimer ce produit ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete(product);
                        Toast.makeText(getContext(),"Produit Supprimé avec Succes",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), MainMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Things.ordre = "ref";
                        startActivity(intent);
                    }
                })
        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }


    private void Delete(Product product){
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
                Toast.makeText(getContext(),Things.histo.get(i).getId(),Toast.LENGTH_SHORT).show();
                Things.productsList.remove(product);


            }
        }



        Toast.makeText(getContext(),"Produits Supprimé avec Succes!",Toast.LENGTH_SHORT).show();





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
