package com.example.emzio;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.emzio.ui.user.MainMesProduits;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainAdd extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    //StorageTask mUploadTask;
    Button ajouter, upload;
    EditText txt;
    int choice;
    ArrayList<Categorie> categories = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter_marque;
    ImageView img1,img2,img3,img4;
    Button add1,add2,add3,add4;
    Button supp1,supp2,supp3,supp4;
    Uri mImage;
    Images images = new Images();
    User user;
    int condition;
    TextView decstest;
    TextInputEditText decs;
    boolean con1=false,con2=false,con3=false,con4=false;

    Spinner type,marque;
    public static  int number=0;
    private DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String marque_produit, type_produit;

    private static final int PICK_IMAGE_REQUEST = 1;
    int x=0;
    SharedPreferences share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_add_acti);
        decstest = findViewById(R.id.decs_test);
        decs = findViewById(R.id.add_product_description);

        share = getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));

        user.setID(share.getString("ID",null));

        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));

        type=findViewById(R.id.add_product_spinner_type_de_produit);
        marque=findViewById(R.id.add_product_spinner_marque_de_produit);



        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.type_produits,R.layout.my_spinnez);

        adapter_type.setDropDownViewResource(R.layout.my_spinnez);
        type.setAdapter(adapter_type);




        adapter_marque =  ArrayAdapter.createFromResource(this,R.array.type_produits,R.layout.my_spinnez);
        type.setOnItemSelectedListener(this);
        adapter_marque.notifyDataSetChanged();

        marque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                marque_produit = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        upload = findViewById(R.id.add_product_valider);

        add1 = findViewById(R.id.ajouter1);
        add2 = findViewById(R.id.ajouter2);
        add3 = findViewById(R.id.ajouter3);
        add4 = findViewById(R.id.ajouter4);

        supp1 = findViewById(R.id.supprimer1);
        supp2 = findViewById(R.id.supprimer2);
        supp3 = findViewById(R.id.supprimer3);
        supp4 = findViewById(R.id.supprimer4);

        /*ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(x>4){
                    Toast toast = Toast.makeText(MainAdd.this,"4",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    OpenFielChooser();
                }

            }
        });*/

        final Intent intent = new Intent(MainAdd.this, MainMenu.class);
/////////////////////////////////////////////////////////////////////////////
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=1;
                OpenFielChooser();
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=2;
                OpenFielChooser();
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=3;
                OpenFielChooser();
            }
        });

        add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=4;
                OpenFielChooser();
            }
        });

///////////////////////////////////////////////////////////////////////////////

        supp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con1){
                    //Picasso().with(mContext).load(image_array[position]).placeholder(R.drawable.ic_stub).into(imageView);
                    Picasso.get().load(String.valueOf(getResources().getDrawable(R.drawable.ic_add))).into(img1);
                    //img1.setBackground(getResources().getDrawable(R.drawable.ic_add));
                    con1= false;
                }

            }
        });

        supp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con2){

                    Picasso.get().load(String.valueOf(getResources().getDrawable(R.drawable.ic_add))).into(img2);
                    con2=false;
                }

            }
        });

        supp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con3){

                    Picasso.get().load(String.valueOf(getResources().getDrawable(R.drawable.ic_add))).into(img3);
                    con3=false;
                }
            }
        });

        supp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con4){


                    Picasso.get().load(String.valueOf(getResources().getDrawable(R.drawable.ic_add))).into(img4);
                    con4=false;
                }
            }
        });
///////////////////////////////////////////////////////////////////////////////////
        upload.setEnabled(true);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verify()){

                   UploadProduct();



                }else{
                    Toast.makeText(getApplicationContext(),"Veillez Remplire tous les Champs ! ",Toast.LENGTH_SHORT).show();
                }



            }

        });

        decs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                decstest.setVisibility(View.VISIBLE);
                if((decs.getText().toString().length()>=11)){
                    decstest.setText("Description Valide");
                    decstest.setTextColor(Color.rgb(76,175,80));
                }else{
                    decstest.setText("Description invalide");
                    decstest.setTextColor(Color.rgb(255,0,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    void OpenFielChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            mImage = data.getData();

            if(x==1){

                con1=true;
                Picasso.get().load(mImage).resize(720,720).into(img1);
                number=1;


            }


            if(x==2){
                con2 =true;

                Picasso.get().load(mImage).resize(720,720).into(img2);
                number=2;
            }

            if(x==3){

                con3=true;
                Picasso.get().load(mImage).resize(720,720).into(img3);
                number=3;
            }
            //img.setImageURI(mImage);
            if(x==4){
                con4=true;
                Picasso.get().load(mImage).resize(720,720).into(img4);
                number=4;


            }



        }

    }

    public String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void Fonction(ImageView imageView, String name, Product product) {

        StorageReference storageRef = storage.getReference();

        String Folder = "Products/" + user.getID() + "/" + product.getID() + "/";
        product.setFolder(Folder);

        StorageReference mountainsRef = storageRef.child(Folder + name);

        StorageReference mountainImagesRef = storageRef.child(name);


        imageView.setDrawingCacheEnabled(false);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(getApplicationContext(),"Produit Ajouter Acec Succes",Toast.LENGTH_SHORT).show();


            }
        });



    }


    Product getInfo(){

        EditText nom,prix,decs,qt;
        nom = findViewById(R.id.add_product_nom);

        prix = findViewById(R.id.add_product_prix);

        decs = findViewById(R.id.add_product_description);
        qt = findViewById(R.id.add_product_qt);





        return new Product(marque_produit,nom.getText().toString(),type_produit,prix.getText().toString(),user.getID(),decs.getText().toString(),Integer.parseInt(qt.getText().toString()));

    }

    public void addProduct(Product product){

        product.setCategorie(categories);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Product").child(product.getID()).setValue(product);
        Things.refresh=true;


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        type_produit = text;
        choice = position;


        if(position ==0){

            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.composants_pc,R.layout.my_spinnez);
            adapter_marque.setDropDownViewResource(R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);


        }

        if(position ==1){
            adapter_marque= ArrayAdapter.createFromResource(this,
                    R.array.accessoires_ordinateurs, R.layout.my_spinnez);
            methodeX(position);

        }

        if(position ==2){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.ordinateurs, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==3){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.ordianteurs_protables, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==4){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.tablettes, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==5){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.imprimante_scaner,R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==6){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.telephones, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==7){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.games,R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==8){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.appareils_photo, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }

        if(position ==9){
            adapter_marque = ArrayAdapter.createFromResource(this,
                    R.array.sons, R.layout.my_spinnez);
            marque.setAdapter(adapter_marque);
            methodeX(position);

        }



        adapter_marque.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void methodeX(int x){

        categories = new ArrayList<>();
        /*
         <item>Composants PC</item>
        <item>Accessoires ordinateurs</item>
        <item>Ordinateurs</item>
        <item>Ordinateur Portables</item>
        <item>Tablettes</item>
        <item>Imprimantes et scanners</item>
        <item>Téléphones</item>
        <item>Consoles des jeux</item>
        <item>Appareils photo</item>
        <item>Sons</item>
         */
        if(x==0){
            categories.add(new Categorie("Composants PC",5));
            categories.add(new Categorie("Accessoires ordinateurs",4));
            categories.add(new Categorie("Ordinateurs",3));
            categories.add(new Categorie("Ordinateur Portables",2));
            categories.add(new Categorie("Tablettes",1));
            categories.add(new Categorie("Imprimantes et scanners",3));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",1));
            categories.add(new Categorie("Appareils photo",1));
            categories.add(new Categorie("Sons",3));

        }

        if(x==1){

            categories.add(new Categorie("Composants PC",4));
            categories.add(new Categorie("Accessoires ordinateurs",5));
            categories.add(new Categorie("Ordinateurs",3));
            categories.add(new Categorie("Ordinateur Portables",3));
            categories.add(new Categorie("Tablettes",1));
            categories.add(new Categorie("Imprimantes et scanners",4));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",1));
            categories.add(new Categorie("Appareils photo",1));
            categories.add(new Categorie("Sons",3));

        }

        if(x==2){
            categories.add(new Categorie("Composants PC",4));
            categories.add(new Categorie("Accessoires ordinateurs",4));
            categories.add(new Categorie("Ordinateurs",5));
            categories.add(new Categorie("Ordinateur Portables",2));
            categories.add(new Categorie("Tablettes",1));
            categories.add(new Categorie("Imprimantes et scanners",4));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",1));
            categories.add(new Categorie("Appareils photo",1));
            categories.add(new Categorie("Sons",3));
        }

        if(x==3){

            categories.add(new Categorie("Composants PC",1));
            categories.add(new Categorie("Accessoires ordinateurs",4));
            categories.add(new Categorie("Ordinateurs",1));
            categories.add(new Categorie("Ordinateur Portables",5));
            categories.add(new Categorie("Tablettes",0));
            categories.add(new Categorie("Imprimantes et scanners",4));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",1));
            categories.add(new Categorie("Appareils photo",1));
            categories.add(new Categorie("Sons",3));

        }

        if(x==4){
            categories.add(new Categorie("Composants PC",0));
            categories.add(new Categorie("Accessoires ordinateurs",2));
            categories.add(new Categorie("Ordinateurs",0));
            categories.add(new Categorie("Ordinateur Portables",0));
            categories.add(new Categorie("Tablettes",5));
            categories.add(new Categorie("Imprimantes et scanners",3));
            categories.add(new Categorie("Téléphones",3));
            categories.add(new Categorie("Consoles des jeux",0));
            categories.add(new Categorie("Appareils photo",1));
            categories.add(new Categorie("Sons",0));
        }

        if(x==5){
            categories.add(new Categorie("Composants PC",3));
            categories.add(new Categorie("Accessoires ordinateurs",4));
            categories.add(new Categorie("Ordinateurs",3));
            categories.add(new Categorie("Ordinateur Portables",2));
            categories.add(new Categorie("Tablettes",0));
            categories.add(new Categorie("Imprimantes et scanners",5));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",0));
            categories.add(new Categorie("Appareils photo",0));
            categories.add(new Categorie("Sons",0));

        }

        if(x==6){
            categories.add(new Categorie("Composants PC",0));
            categories.add(new Categorie("Accessoires ordinateurs",2));
            categories.add(new Categorie("Ordinateurs",0));
            categories.add(new Categorie("Ordinateur Portables",1));
            categories.add(new Categorie("Tablettes",3));
            categories.add(new Categorie("Imprimantes et scanners",1));
            categories.add(new Categorie("Téléphones",5));
            categories.add(new Categorie("Consoles des jeux",0));
            categories.add(new Categorie("Appareils photo",0));
            categories.add(new Categorie("Sons",0));
        }
        if(x==7){
            categories.add(new Categorie("Composants PC",0));
            categories.add(new Categorie("Accessoires ordinateurs",0));
            categories.add(new Categorie("Ordinateurs",0));
            categories.add(new Categorie("Ordinateur Portables",0));
            categories.add(new Categorie("Tablettes",0));
            categories.add(new Categorie("Imprimantes et scanners",0));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",5));
            categories.add(new Categorie("Appareils photo",0));
            categories.add(new Categorie("Sons",3));

        }
        if(x==8){
            categories.add(new Categorie("Composants PC",0));
            categories.add(new Categorie("Accessoires ordinateurs",0));
            categories.add(new Categorie("Ordinateurs",0));
            categories.add(new Categorie("Ordinateur Portables",0));
            categories.add(new Categorie("Tablettes",0));
            categories.add(new Categorie("Imprimantes et scanners",0));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",0));
            categories.add(new Categorie("Appareils photo",5));
            categories.add(new Categorie("Sons",0));

        }

        if(x==9){
            categories.add(new Categorie("Composants PC",0));
            categories.add(new Categorie("Accessoires ordinateurs",0));
            categories.add(new Categorie("Ordinateurs",0));
            categories.add(new Categorie("Ordinateur Portables",0));
            categories.add(new Categorie("Tablettes",0));
            categories.add(new Categorie("Imprimantes et scanners",0));
            categories.add(new Categorie("Téléphones",0));
            categories.add(new Categorie("Consoles des jeux",0));
            categories.add(new Categorie("Appareils photo",0));
            categories.add(new Categorie("Sons",5));
        }






    }

    public boolean verify(){
        EditText nom,prix,desc,qt;
        nom = findViewById(R.id.add_product_nom);
        prix = findViewById(R.id.add_product_prix);
        desc = findViewById(R.id.add_product_description);
        qt = findViewById(R.id.add_product_qt);

        if(nom.getText().toString().equals("") || prix.getText().toString().equals("") || desc.getText().toString().equals("") || qt.getText().toString().equals("") || qt.getText().toString().equals("0")){

            return  false;
        }

        if(!con1 && !con2 && !con3 && !con4){

            Toast.makeText(getApplicationContext(),"Ajouter minimum une image !",Toast.LENGTH_SHORT).show();
            return  false;
        }

        return true;

    }

    private void UploadProduct(){









        Product product = getInfo();
        String name1 = "image1.jpg";
        String name2 = "image2.jpg";
        String name3 = "image3.jpg";
        String name4 = "image4.jpg";


        Intent intent = new Intent(MainAdd.this,MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Things.ordre = "ref";



        if(con1 && !con2 && !con3 && !con4){
            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);
            images.setImages_nb(1);
            product.setImages(images);

            addProduct(product);


            startActivity(intent);
            finish();
        }


        if(!con1 && con2 && !con3 && !con4){
            Things.add= true;
            Fonction(img2,name1,product);
            images.setImage1(name1);
            images.setImages_nb(1);
            product.setImages(images);

            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(con1 && con2 && !con3 && !con4){
            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img2,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(!con1 && !con2 && con3 && !con4){
            Things.add= true;
            Fonction(img3,name1,product);
            images.setImage1(name1);
            images.setImages_nb(1);
            product.setImages(images);

            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(con1 && !con2 && con3 && !con4){
            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img3,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(!con1 && con2 && con3 && !con4){
            Things.add= true;
            Fonction(img2,name1,product);
            images.setImage1(name1);


            Fonction(img3,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(con1 && con2 && con3 && !con4){
            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img2,name2,product);
            images.setImage2(name2);


            Fonction(img3,name3,product);
            images.setImage3(name3);

            product.setImages(images);
            product.getImages().setImages_nb(3);
            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(!con1 && !con2 && !con3 && con4){
            Things.add= true;
            Fonction(img4,name1,product);
            images.setImage1(name1);
            images.setImages_nb(1);
            product.setImages(images);

            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(con1 && !con2 && !con3 && con4){

            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img4,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(!con1 && con2 && !con3 && con4){
            Things.add= true;
            Fonction(img2,name1,product);
            images.setImage1(name1);


            Fonction(img4,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(con1 && con2 && !con3 && con4){
            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img2,name2,product);
            images.setImage2(name2);


            Fonction(img4,name3,product);
            images.setImage3(name3);

            product.setImages(images);
            product.getImages().setImages_nb(3);
            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(!con1 && !con2 && con3 && con4){

            Things.add= true;
            Fonction(img3,name1,product);
            images.setImage1(name1);


            Fonction(img4,name2,product);
            images.setImage2(name2);


            product.setImages(images);
            product.getImages().setImages_nb(2);
            addProduct(product);
            startActivity(intent);
            finish();
        }

        if(con1 && !con2 && con3 && con4){

            Things.add= true;
            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img3,name2,product);
            images.setImage2(name2);


            Fonction(img4,name3,product);
            images.setImage3(name3);

            product.setImages(images);
            product.getImages().setImages_nb(3);
            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(!con1 && con2 && con3 && con4){
            Things.add= true;
            Fonction(img2,name1,product);
            images.setImage1(name1);


            Fonction(img3,name2,product);
            images.setImage2(name2);


            Fonction(img4,name3,product);
            images.setImage3(name3);

            product.setImages(images);
            product.getImages().setImages_nb(3);
            addProduct(product);
            startActivity(intent);
            finish();

        }

        if(con1 && con2 && con3 && con4){

            Things.add= true;

            Fonction(img1,name1,product);
            images.setImage1(name1);


            Fonction(img2,name2,product);
            images.setImage2(name2);


            Fonction(img3,name3,product);
            images.setImage3(name3);


            Fonction(img4,name4,product);
            images.setImage4(name4);


            product.setImages(images);
            product.getImages().setImages_nb(4);
            addProduct(product);

            startActivity(intent);
            finish();
        }






        /*
                    if(number == 1){
                        Things.add= true;
                        Fonction(img1,name1,product);
                        images.setImage1(name1);
                        images.setImages_nb(1);
                        product.setImages(images);

                        addProduct(product);
                        startActivity(intent);
                        finish();
                    }

                    if(number == 2){
                        Things.add= true;
                        Fonction(img1,name1,product);
                        images.setImage1(name1);


                        Fonction(img2,name2,product);
                        images.setImage2(name2);


                        product.setImages(images);
                        product.getImages().setImages_nb(2);
                        addProduct(product);
                        startActivity(intent);
                        finish();

                    }

                    if(number==3){
                        Things.add= true;
                        Fonction(img1,name1,product);
                        images.setImage1(name1);


                        Fonction(img2,name2,product);
                        images.setImage2(name2);


                        Fonction(img3,name3,product);
                        images.setImage3(name3);

                        product.setImages(images);
                        product.getImages().setImages_nb(3);
                        addProduct(product);
                        startActivity(intent);
                        finish();
                    }

                    if(number==4){
                        Things.add= true;

                        Fonction(img1,name1,product);
                        images.setImage1(name1);


                        Fonction(img2,name2,product);
                        images.setImage2(name2);


                        Fonction(img3,name3,product);
                        images.setImage3(name3);


                        Fonction(img4,name4,product);
                        images.setImage4(name4);


                        product.setImages(images);
                        product.getImages().setImages_nb(4);
                        addProduct(product);


                        startActivity(intent);
                        finish();

                    }


         */


    }

}







