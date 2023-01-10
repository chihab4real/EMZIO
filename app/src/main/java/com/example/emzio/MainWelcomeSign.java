package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainWelcomeSign extends AppCompatActivity {

    FirebaseStorage storage, storage1;
    StorageReference storageRef, storageRef1;
    DatabaseReference mDatabase, mDatabase1, mDatabase2,mDatabase3;


    ArrayList<Product> myArray = new ArrayList();
    ArrayList<Commande> myArray2 = new ArrayList();
    ArrayList<Publicite> myArray1 = new ArrayList();

    Timer timer;
    TextView welc;
    SharedPreferences share;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome_sign);

        share =  getSharedPreferences("MyREF", MODE_PRIVATE);
        welc = findViewById(R.id.hello_user);
        welc.setText("Bienvenu sur Emzio "+share.getString("Prenom",null)+" !");
        timer = new Timer();


        Focntion();
        getDemandeList();
        //Fonct();
        FocntionPub();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainWelcomeSign.this,MainMenu.class));
                finish();
            }
        },5000);
    }


    void Focntion() {

        storage = FirebaseStorage.getInstance();
        myArray = new ArrayList<Product>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Product");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    getImage(product);
                    getAllresteImages(product);
                    myArray.add(product);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Things.productsList = myArray;


    }

    void getImage(final Product product) {

        storage = FirebaseStorage.getInstance();
        String Folder = "Products/" + product.getID_user() + "/" + product.getID() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        storageRef.child(Folder + "image1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                product.setImg(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    void getAllresteImages(Product product) {
        int x = product.getImages().getImages_nb();

        int index = myArray.indexOf(product);

        if (x == 1) {

            getImage1(product, index);


        }

        if (x == 2) {


            getImage1(product, index);
            getImage2(product, index);


        }

        if (x == 3) {
            getImage1(product, index);
            getImage2(product, index);
            getImage3(product, index);

        }

        if (x == 4) {
            getImage1(product, index);
            getImage2(product, index);
            getImage3(product, index);
            getImage4(product, index);


        }


    }

    void getImage1(final Product product, final int index) {
        storage = FirebaseStorage.getInstance();
        String Folder = "Products/" + product.getID_user() + "/" + product.getID() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        storageRef.child(Folder + "image1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                product.setImage1(uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }////

    void getImage2(final Product product, final int index) {
        storage = FirebaseStorage.getInstance();
        String Folder = "Products/" + product.getID_user() + "/" + product.getID() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        storageRef.child(Folder + "image2.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                product.setImage2(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }////

    void getImage3(final Product product, final int index) {
        storage = FirebaseStorage.getInstance();
        String Folder = "Products/" + product.getID_user() + "/" + product.getID() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        storageRef.child(Folder + "image3.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                product.setImage3(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    } ////

    void getImage4(final Product product, final int index) {
        storage = FirebaseStorage.getInstance();
        String Folder = "Products/" + product.getID_user() + "/" + product.getID() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        storageRef.child(Folder + "image4.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                product.setImage4(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    } ////


    void FocntionPub() {

        storage1 = FirebaseStorage.getInstance();
        myArray1 = new ArrayList<Publicite>();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Pub");

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Publicite publicite = snapshot.getValue(Publicite.class);

                    getImagePub(publicite);

                    myArray1.add(publicite);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Things.myPubs = myArray1;


    }

    void getImagePub(final Publicite publicite) {

        storage = FirebaseStorage.getInstance();
        String Folder = "Pub/" + publicite.getIdPub() + "/";
        //StorageReference islandRef = storageRef.child(Folder+"image1.jpg");
        storageRef = storage.getReference();
        String x = publicite.getIdImage() + ".jpg";
        storageRef.child(publicite.getFolder() + x).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                publicite.setImage(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    void getDemandeList(){


        myArray2 = new ArrayList<Commande>();
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Commande");

        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Commande commande = snapshot.getValue(Commande.class);


                    myArray2.add(commande);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Things.commandeArrayList = myArray2;

    }

}