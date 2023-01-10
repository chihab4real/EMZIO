package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.ui.home.HomeFragment;
import com.example.emzio.ui.home.HomeViewModel;
import com.example.emzio.ui.user.MainMonBoutique;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainProduit extends AppCompatActivity {


    ArrayList<Product> P = new ArrayList<>();
    ArrayList<Product> P_U = new ArrayList<>();
    ArrayList<String> P_V = new ArrayList<>();
    ArrayList<Product> myArray = new ArrayList<>();

    ArrayList<Vecteur> vecteur1 = new ArrayList<>();

    ArrayList<Vecteur> vecteur2 = new ArrayList<>();


//Product product;
    ImageView imgp,imagepd1,imagepd2,imagepd3,imagepd4;
    TextView nomp,marque, prix, decs;
    LinearLayout images;
    CardView line1,line2,line3,line4;
    Product product;
    ImageView like;
    ImageButton panier;
    DatabaseReference mDatabase,mDatabase1,mDatabase2,mDatabase3;
    User user;
    SharedPreferences share;
    LinearLayout linearLayout;
    boolean liked= false;
    MyAdapter myAdapter;
    Button acheter,ajouter_panier;
    public static int f =0;

    FirebaseStorage storage;
    StorageReference storageRef;
    int index;
    RecyclerView recyclerView;
    RecoAdapter recoAdapter;
    CardView cardViewNumber,cardView;
    TextView numberp;
    boolean isBoutique;
    TextView indispo;




    Button modifier,click;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        product = Things.product_act;




        ////////////////////////////////////////////////////////////
        share = getSharedPreferences("MyREF", MODE_PRIVATE);


        user = new User(share.getString("Nom", null), share.getString("Prenom", null), share.getString("Sexe", null), share.getString("Email", null)
                , share.getString("Password", null), share.getString("Phone", null), share.getBoolean("Pro", false));
        user.setID(share.getString("ID", null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        user.getAdresse().setNum_wilaya(share.getInt("Adresse_wilaya_num",-1));
        ////////////////////////////////////////////////////////////
        addToSeen();

        if(!Things.Produit_click.contains(product.getID())){
            Things.Produit_click.add(product.getID());
            updateProductsClick(product.getID());
        }

        setContentView(R.layout.produit_acti);

        if (!haveNetwork()) {
            Toast.makeText(getApplicationContext(), "Connexion perdu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainProduit.this,MainVerfyInternet.class));

        }
       //Intent intent = getIntent();

        ///////
        recyclerView = findViewById(R.id.recylce_for_produit);
        //recoAdapter = new RecoAdapter(Things.productsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(null);

        ///////








        ////////////////////////////////////////////


        linearLayout = findViewById(R.id.offi);
        click = findViewById(R.id.eva);
        if(isValidate(product.getID_user())){
            linearLayout.setVisibility(View.VISIBLE);
        }

        cardViewNumber = findViewById(R.id.card_number);
        numberp = findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }

        myAdapter = new MyAdapter(this,Things.productsList);

        int i;
        ArrayList<ProduitLike> arrayList= Things.produitLikes;

        for(i=0;i<arrayList.size();i++){
            if(product.getID().equals(arrayList.get(i).getID_Produit())){
                liked = true;
                break;
            }
        }

        P = Things.productsList;
        P_U  = new ArrayList<>();
        P_U.add(product);
        toArray();
        Fonction();
        cardView = findViewById(R.id.card_rec);
        searchProducts1();



        recoAdapter = new RecoAdapter(myArray);
        recyclerView.setAdapter(recoAdapter);
        recoAdapter.setOnItemCLickListener(new RecoAdapter.OnItemCLickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainProduit.this, MainProduit.class);

                Things.product_act = myArray.get(position);
                startActivity(intent);
            }
        });

        //doIt();

        imgp = findViewById(R.id.produit_image_main);
        marque= findViewById(R.id.produit_marqueProduit);
        nomp = findViewById(R.id.produit_nomProduit);
        prix = findViewById(R.id.produit_prixProduit);
        decs = findViewById(R.id.produit_decsriptionProduit);
        imagepd1 = findViewById(R.id.produit_image1);
        imagepd2 = findViewById(R.id.produit_image2);
        imagepd3 = findViewById(R.id.produit_image3);
        imagepd4 = findViewById(R.id.produit_image4);

        line1 =findViewById(R.id.produit_image1_layout);
        line2 =findViewById(R.id.produit_image2_layout);
        line3 =findViewById(R.id.produit_image3_layout);
        line4 =findViewById(R.id.produit_image4_layout);






        imagepd1.setVisibility(View.GONE);
        imagepd2.setVisibility(View.GONE);
        imagepd3.setVisibility(View.GONE);
        imagepd4.setVisibility(View.GONE);


        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        line3.setVisibility(View.GONE);
        line4.setVisibility(View.GONE);




        Picasso.get().load(product.getImg()).resize(1200,1050).into(imgp);

        marque.setText(product.getMarque());
        nomp.setText(product.getNom());
        prix.setText(changeString(product.getPrix())+" Da");
        decs.setText(product.getDescription());

        if(product.getImages().getImages_nb()==1){



            line1.setVisibility(View.VISIBLE);





            imagepd1.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage1()).resize(720,720).into(imagepd1);




        }

        if(product.getImages().getImages_nb()==2){

            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);




            imagepd1.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage1()).resize(720,720).into(imagepd1);

            imagepd2.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage2()).resize(720,720).into(imagepd2);



        }

        if(product.getImages().getImages_nb()==3){



            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.VISIBLE);


            imagepd1.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage1()).resize(720,720).into(imagepd1);

            imagepd2.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage2()).resize(720,720).into(imagepd2);

            imagepd3.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage3()).resize(720,720).into(imagepd3);



        }

        if(product.getImages().getImages_nb()==4){



            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);



            imagepd1.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage1()).resize(720,720).into(imagepd1);

            imagepd2.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage2()).resize(720,720).into(imagepd2);

            imagepd3.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage3()).resize(720,720).into(imagepd3);

            imagepd4.setVisibility(View.VISIBLE);
            Picasso.get().load(product.getImage4()).resize(720,720).into(imagepd4);



        }

        acheter= findViewById(R.id.produit_acheter);
        ajouter_panier = findViewById(R.id.produit_ajouter_panier);
        modifier = findViewById(R.id.produit_modifier);
        indispo = findViewById(R.id.produit_indispo);




        isBoutique = isBoutique(user.getID());

        if(product.getID_user().equals(user.getID())){

            indispo.setVisibility(View.GONE);
            ajouter_panier.setEnabled(false);
            acheter.setEnabled(false);

            if(isBoutique){
                modifier.setVisibility(View.VISIBLE);
                acheter.setVisibility(View.GONE);
                ajouter_panier.setVisibility(View.GONE);
                indispo.setVisibility(View.GONE);


            }else{
                modifier.setVisibility(View.GONE);
                acheter.setVisibility(View.VISIBLE);
                ajouter_panier.setVisibility(View.VISIBLE);

            }

        }else{

            ajouter_panier.setEnabled(true);
            acheter.setEnabled(true);

            if(product.isDisponible()){
                ajouter_panier.setVisibility(View.VISIBLE);
                acheter.setVisibility(View.VISIBLE);
                indispo.setVisibility(View.GONE);
            }else{
                ajouter_panier.setVisibility(View.GONE);
                acheter.setVisibility(View.GONE);
                indispo.setVisibility(View.VISIBLE);
            }
        }






        acheter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Things.number+=1;
                cardViewNumber.setVisibility(View.VISIBLE);
                numberp.setText(String.valueOf(Things.number));
                MainCategorie.cardView.setVisibility(View.VISIBLE);
                MainCategorie.numberp.setText(String.valueOf(Things.number));

                HomeFragment.cardViewNumber.setVisibility(View.VISIBLE);
                HomeFragment.numberp.setText(String.valueOf(Things.number));
                Toast.makeText(getApplicationContext(),"Le Produits a été ajouter au panier",Toast.LENGTH_SHORT).show();
                Things.Panier.add(product);
                startActivity(new Intent(MainProduit.this,MainPanier.class));

            }
        });


        ajouter_panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Things.Panier.contains(product)){
                    Toast.makeText(getApplicationContext(),"Ce Produit est deja dans votre Panier",Toast.LENGTH_SHORT).show();

                }else{
                    cardViewNumber.setVisibility(View.VISIBLE);
                    Things.number+=1;
                    numberp.setText(String.valueOf(Things.number));


                    MainCategorie.cardView.setVisibility(View.VISIBLE);
                    MainCategorie.numberp.setText(String.valueOf(Things.number));



                    HomeFragment.cardViewNumber.setVisibility(View.VISIBLE);
                    HomeFragment.numberp.setText(String.valueOf(Things.number));
                    Things.Panier.add(product);
                    Toast.makeText(getApplicationContext(),"Le Produits a été ajouter au panier",Toast.LENGTH_SHORT).show();
                }




                
            }
        });

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Things.productModifier = product;
                startActivity(new Intent(MainProduit.this,MainModifierProduit.class));
            }
        });

        final Drawable myDrawable = getResources().getDrawable(R.drawable.ic_heart_empty);
        final Drawable myDrawable1 = getResources().getDrawable(R.drawable.ic_heart_full);
        like = findViewById(R.id.like_image);


        like.setImageDrawable(myDrawable);


        if(liked){
            like.setImageDrawable(myDrawable1);

        }else{

            like.setImageDrawable(myDrawable);

        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked){
                    deletFromlike();
                    like.setImageDrawable(myDrawable);
                    liked =false;

                }else{
                    addToLikes();
                    like.setImageDrawable(myDrawable1);
                    liked =true;
                }

            }
        });


        panier =findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainProduit.this,MainPanier.class));
            }
        });

/*

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Timer timer = new Timer();
                MainForume.product = product;
                getAllRatings(product);

                Things.eva_number = MainForume.ratingsList.size();


                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainProduit.this,MainForume.class));
                    }
                },1000);
            }
        });
*/
        }




    public int generateMarque(String marque){


        if(marque.equals("A4tech")){
            return 1;
        }

        if(marque.equals("Acer")){
            return 2;
        }

        if(marque.equals("Adata")){
            return 3;
        }

        if(marque.equals("Aorus")){
            return 4;
        }

        if(marque.equals("Apc")){
            return 5;
        }

        if(marque.equals("Apple")){
            return 6;
        }

        if(marque.equals("Asus")){
            return 7;
        }

        if(marque.equals("Awei")){
            return 8;
        }

        if(marque.equals("Beats Audio")){
            return 9;
        }

        if(marque.equals("Bloody")){
            return 10;
        }

        if(marque.equals("Brandt")){
            return 11;
        }

        if(marque.equals("Brother")){
            return 12;
        }

        if(marque.equals("Canon")){
            return 13;
        }

        if(marque.equals("Canon")){
            return 14;
        }

        if(marque.equals("Capsys")){
            return 15;
        }

        if(marque.equals("Con")){
            return 16;
        }

        if(marque.equals("Condor")){
            return 17;
        }

        if(marque.equals("Cooler Master Ltd")){
            return 18;
        }

        if(marque.equals("Coolermaster")){
            return 19;
        }

        if(marque.equals("D-Link")){
            return 20;
        }

        if(marque.equals("DELL")){
            return 21;
        }

        if(marque.equals("Doro")){
            return 22;
        }

        if(marque.equals("Double M")){
            return 23;
        }

        if(marque.equals("E BLUE")){
            return 24;
        }

        if(marque.equals("Earldom")){
            return 25;
        }

        if(marque.equals("Empty")){
            return 26;
        }

        if(marque.equals("Enigma")){
            return 27;
        }

        if(marque.equals("Enigma")){
            return 28;
        }

        if(marque.equals("Epson")){
            return 29;
        }

        if(marque.equals("Ette")){
            return 30;
        }

        if(marque.equals("Fujifilm")){
            return 31;
        }

        if(marque.equals("Generic")){
            return 32;
        }

        if(marque.equals("Generic")){
            return 33;
        }

        if(marque.equals("Gigabyte")){
            return 34;
        }

        if(marque.equals("Gp")){
            return 35;
        }

        if(marque.equals("Hama")){
            return 36;
        }

        if(marque.equals("Hi Speed")){
            return 37;
        }

        if(marque.equals("Hp")){
            return 38;
        }

        if(marque.equals("Huawei")){
            return 39;
        }

        if(marque.equals("IRIS")){
            return 40;
        }

        if(marque.equals("Infinix")){
            return 41;
        }

        if(marque.equals("JBL")){
            return 42;
        }

        if(marque.equals("Jabra")){
            return 43;
        }

        if(marque.equals("Jeway")){
            return 44;
        }

        if(marque.equals("Kingston")){
            return 45;
        }

        if(marque.equals("Kyocera Ecosys Laser")){
            return 46;
        }

        if(marque.equals("LG")){
            return 47;
        }

        if(marque.equals("Ldnio")){
            return 48;
        }

        if(marque.equals("Lenovo")){
            return 49;
        }

        if(marque.equals("Lexar")){
            return 50;
        }

        if(marque.equals("Logitech")){
            return 51;
        }

        if(marque.equals("Mac Tec")){
            return 52;
        }

        if(marque.equals("Mac Tech")){
            return 53;
        }

        if(marque.equals("Macy")){
            return 54;
        }

        if(marque.equals("Marvo")){
            return 55;
        }

        if(marque.equals("MaxiPower")){
            return 56;
        }

        if(marque.equals("Meetion")){
            return 57;
        }

        if(marque.equals("Microsoft")){
            return 58;
        }

        if(marque.equals("Microsoft-XBOX")){
            return 59;
        }

        if(marque.equals("Msi")){
            return 60;
        }

        if(marque.equals("Nikon")){
            return 61;
        }

        if(marque.equals("Nintendo")){
            return 62;
        }

        if(marque.equals("Novatac")){
            return 63;
        }

        if(marque.equals("Nubwo")){
            return 64;
        }

        if(marque.equals("OPPO")){
            return 65;
        }

        if(marque.equals("Olympus")){
            return 66;
        }

        if(marque.equals("OnePlus")){
            return 67;
        }

        if(marque.equals("PC MAX")){
            return 68;
        }

        if(marque.equals("Panasonic")){
            return 69;
        }

        if(marque.equals("Pny Technologies")){
            return 70;
        }

        if(marque.equals("Rapoo")){
            return 71;
        }

        if(marque.equals("Razer")){
            return 72;
        }

        if(marque.equals("Rivacase")){
            return 73;
        }

        if(marque.equals("SONY")){
            return 74;
        }

        if(marque.equals("Samsung")){
            return 75;
        }

        if(marque.equals("Samsung")){
            return 76;
        }

        if(marque.equals("Scorpion")){
            return 77;
        }

        if(marque.equals("Sharp")){
            return 78;
        }

        if(marque.equals("Sony")){
            return 79;
        }

        if(marque.equals("Sony-Playsattion")){
            return 80;
        }

        if(marque.equals("Spirit Of Gamer")){
            return 81;
        }

        if(marque.equals("Sunlux")){
            return 82;
        }

        if(marque.equals("TPLink")){
            return 83;
        }

        if(marque.equals("Targus")){
            return 84;
        }

        if(marque.equals("Toshiba")){
            return 85;
        }

        if(marque.equals("Uniross")){
            return 86;
        }

        if(marque.equals("Western Digital")){
            return 87;
        }

        if(marque.equals("XIAOMI")){
            return 88;
        }

        if(marque.equals("XOinter")){
            return 89;
        }

        if(marque.equals("ZELOTES")){
            return 90;
        }


        return 0;
    }
    public ArrayList<Integer> transformProductToVector(Product product){

        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i= 0 ; i<product.getCategorie().size();i++){
            arrayList.add(product.getCategorie().get(i).getImportance());
        }

        int x = generateMarque(product.getMarque());
        //arrayList.add(x);

        return arrayList;
    }
    public void toArray(){
        Vecteur v;

        for(int i=0;i<P.size();i++){
            v= new Vecteur();
            v.setId(P.get(i).getID());
            v.setVec(transformProductToVector(P.get(i)));
            vecteur1.add(v);
        }

        v= new Vecteur();

        for(int i=0;i<P_U.size();i++){

            v= new Vecteur();
            v.setId(P_U.get(i).getID());
            v.setVec(transformProductToVector(P_U.get(i)));
            vecteur2.add(v);
        }
    }
    public double cosineSimilarity(ArrayList<Integer> vector1, ArrayList<Integer> vector2) {

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vector1.size(); i++) {
            dotProduct += vector1.get(i) * vector2.get(i);
            normA += Math.pow(vector1.get(i), 2);
            normB += Math.pow(vector2.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    public boolean con(String id){

        for(int i= 0 ;i<P_U.size();i++){
            if(P_U.get(i).getID().equals(id)){
                return true ;
            }
        }

        return false;
    }



    private void Fonction(){

        int i,j;
        double distance;

        for(i=0;i<vecteur1.size();i++){
            for(j=0;j<vecteur2.size();j++){
                distance = cosineSimilarity(vecteur1.get(i).getVec(),vecteur2.get(j).getVec());
                if(distance >0 ){
                    if(!P_V.contains(vecteur1.get(i).getId())){
                        if(!con(vecteur1.get(i).getId())){
                            P_V.add(vecteur1.get(i).getId());
                            //Toast.makeText(getApplicationContext(),String.valueOf(distance),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    void searchProducts1(){
        int i,j;
        ArrayList<String> myArrayIds;
        myArrayIds = P_V;

        if(myArrayIds.isEmpty()){
            cardView.setVisibility(View.GONE);
        }else{
            cardView.setVisibility(View.VISIBLE);
        }

        if(!myArrayIds.isEmpty()){
            for(i=0;i<myArrayIds.size();i++){
                for(j=0;j<Things.productsList.size();j++){
                    if(myArrayIds.get(i).equals(Things.productsList.get(j).getID())){
                        myArray.add(Things.productsList.get(j));
                    }
                }

            }




            Things.produit_recomnde = myArray;
        }


    }


public void addToLikes(){
        ProduitLike produitLike = new ProduitLike(product.getID(),user.getID());

    mDatabase = FirebaseDatabase.getInstance().getReference();
    mDatabase.child("UserLikedProducts").child(user.getID()).child(product.getID()).setValue(produitLike);


    mDatabase1 = FirebaseDatabase.getInstance().getReference();

    mDatabase1.child("ProductsLikedUsers").child(product.getID()).child(user.getID()).setValue(produitLike);

}

    void deletFromlike(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("UserLikedProducts").child(user.getID()).child(product.getID()).removeValue();

        deletx();
    }


    void deletx(){
        ArrayList<ProduitLike> arrayList= Things.produitLikes;



            mDatabase1 = FirebaseDatabase.getInstance().getReference();

            mDatabase1.child("ProductsLikedUsers").child(product.getID()).child(user.getID()).removeValue();




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

    @Override
    protected void onStart() {
        super.onStart();

        cardViewNumber = findViewById(R.id.card_number);
        numberp = findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }


    }

    public boolean isValidate(String id){

        for(int i = 0;i<Things.myBoutiques.size();i++){
            if(Things.myBoutiques.get(i).getID_chef().equals(id)){
                return true;
            }
        }

        return false;
    }

    private boolean haveNetwork(){

        ConnectivityManager cm =
                (ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();



        return isConnected;
    }

    public void updateProductsClick(String id){


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        ProduitHistory produitHistory = new ProduitHistory();
        produitHistory.setId(id);
        produitHistory.setDate(formatter.format(date));

        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("History").child("Products").child(user.getID()).child(id).setValue(produitHistory);


    }

    public  void addToSeen(){
        mDatabase3 = FirebaseDatabase.getInstance().getReference();
        ProduitLike produitLike = new ProduitLike();
        produitLike.setID_Produit(product.getID());
        produitLike.setID_user(user.getID());
        mDatabase3.child("Seen").child(product.getID()).child(user.getID()).setValue(produitLike);
    }

    public boolean isBoutique(String id){

        for(int i=0;i<Things.myBoutiques.size();i++){
            if(Things.myBoutiques.get(i).getID_chef().equals(id)){
                return true;
            }
        }

        return  false;
    }










}

