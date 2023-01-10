package com.example.emzio.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.emzio.Adresse;
import com.example.emzio.Boutique;
import com.example.emzio.CategoriesListe;
import com.example.emzio.Commande;
import com.example.emzio.Loadsc;
import com.example.emzio.Loadsc2;
import com.example.emzio.MainCategorie;
import com.example.emzio.MainLogin;
import com.example.emzio.MainMenu;
import com.example.emzio.MainPanier;
import com.example.emzio.Message;
import com.example.emzio.MyAdapter;
import com.example.emzio.Product;
import com.example.emzio.ProduitCommande;
import com.example.emzio.ProduitHistory;
import com.example.emzio.ProduitLike;
import com.example.emzio.R;
import com.example.emzio.SlideItem;
import com.example.emzio.SliderAdapterExample;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.example.emzio.VideTest;
import com.example.emzio.Welcome;
import com.example.emzio.newMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    Button add;
    private HomeViewModel homeViewModel;
    User user;
    public static CardView cardViewNumber;
    ImageButton panier;
    SearchView searchView;
    Timer timer= new Timer();
    private long backPressedTime;
    private Toast backToast;
    SharedPreferences share;
    Message myMessage;
    TextView pubi;





    LinearLayout xe;
    //GridView grid_allProducts;
    private DatabaseReference mDatabase,mDatabase1,mDatabase2,mDatabase3,mDatabase4;
    ArrayList<Product> myArray = new ArrayList<Product>();
    FirebaseStorage storage;
    StorageReference storageRef;
    GridView categorie_liste;
    ArrayList<ProduitLike> arrayList= new ArrayList<>();
    SwipeRefreshLayout swipe;
    MyAdapter myAdapter;
    ArrayList<String> Categories = new ArrayList<>();
    CategoriesListe myAdapter1;
    public static  TextView numberp;

    SliderView sliderView;
    SliderAdapterExample sliderAdapterExample;

    ArrayList<Commande> myArray2 = new ArrayList<>();

    @SuppressLint("ResourceAsColor")


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home_acti, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();
            }
        });

        pubi = root.findViewById(R.id.pub);

        categorie_liste = root.findViewById(R.id.categories);

        cardViewNumber = root.findViewById(R.id.card_number);
        sliderView = root.findViewById(R.id.imageSlider);
        numberp = root.findViewById(R.id.number_panier);



       //Toast.makeText(getActivity(),String.valueOf(Things.myPubs.size()),Toast.LENGTH_LONG).show();
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }

        if(!Things.myPubs.isEmpty()){

            doIt();


            ((ScrollView) root.findViewById(R.id.scrollView)).smoothScrollTo(0,0);

        }else{
            sliderView.setVisibility(View.GONE);
        }

        Fonct1();


        //Get User Information From SHaredPrefrences////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        share = this.getActivity().getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Things.produitLikes = new ArrayList<>();
        getHistoryPro();
        getDemandeList();

        Categories.add("Composants PC");
        Categories.add("Accessoires ordinateurs");
        Categories.add("Ordinateurs");
        Categories.add("Ordinateur Portables");
        Categories.add("Tablettes");
        Categories.add("Imprimantes et scanners");
        Categories.add("Téléphones");
        Categories.add("Consoles des jeux");
        Categories.add("Appareils photo");
        Categories.add("Sons");

        myAdapter1 = new CategoriesListe(HomeFragment.this.getContext(),Categories);
        swipe = root.findViewById(R.id.swipe);
        categorie_liste.setAdapter(myAdapter1);

        storage = FirebaseStorage.getInstance();

        swipe.setColorSchemeColors(R.color.swipe_1, R.color.swipe_2, R.color.swipe_3);
        storage = FirebaseStorage.getInstance();
        myArray = Things.productsList;






        //Verfier if the user(PRO) Add Products


            if(Things.conf==false){
                Fonct();
                getSearchListe();
                getAll(user);
                Things.conf = true;

            }

            if(Things.ordre.equals("ref")){
                Focntion();
            }






       //Create new Adapter for showing Products


        //the swipe Action for refreshing products///////////////////////////////////////////////////////////////////////////////////////////
       swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                myAdapter = new MyAdapter();
                //grid_allProducts.setAdapter(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Things.add = false;
                        storage = FirebaseStorage.getInstance();
                        //grid_allProducts.setAdapter(null);



                        if(!Things.productsList.isEmpty()){
                            doIt();

                        }

                        Things.conf = false;
                        Focntion();
                        getSearchListe();
                        swipe.setRefreshing(false);

                        //getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();//re-Load Activity

                    }
                }, 1000);
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        categorie_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MainCategorie.class);
                Things.word = Categories.get(position);
                startActivity(intent);
            }
        });
        panier =root.findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainPanier.class));
            }
        });



        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
        return root;

    }


//Get The Principale Image of the Product
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

//this Fonction for getting The Products from FireBase RealTimeDataBase and there Images and put them in ArrayList
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
                    //myAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Things.productsList = myArray;




    }

//Fonction that call the methodes of getting AllImages of Product from FireBase Storage
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

//get ALLImages OF Products from FireBase Storage///////////////////
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

/////////////////////////////////////////////////////////////////////


    void getAll(User user){



Things.produitLikes = new ArrayList<>();
    mDatabase = FirebaseDatabase.getInstance().getReference().child("UserLikedProducts").child(user.getID());

    mDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                ProduitLike produitLike = snapshot.getValue(ProduitLike.class);
                //Toast.makeText(getApplicationContext(),produitLike.getID_Produit(),Toast.LENGTH_SHORT).show();
                Things.produitLikes.add(produitLike);

                //myAdapter.notifyDataSetChanged();

            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}

    @Override
    public void onStart() {
        super.onStart();
        Fonct1();


        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }


        Categories = new ArrayList<>();

        Categories.add("Composants PC");
        Categories.add("Accessoires ordinateurs");
        Categories.add("Ordinateurs");
        Categories.add("Ordinateur Portables");
        Categories.add("Tablettes");
        Categories.add("Imprimantes et scanners");
        Categories.add("Téléphones");
        Categories.add("Consoles des jeux");
        Categories.add("Appareils photo");
        Categories.add("Sons");

        myAdapter1 = new CategoriesListe(HomeFragment.this.getContext(),Categories);

        categorie_liste.setAdapter(myAdapter1);



    }

    public void Fonct(){

        final ArrayList<Boutique> myArray = new ArrayList();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Boutique");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Boutique boutique = snapshot.getValue(Boutique.class);

                    myArray.add(boutique);
                    if(boutique.getID_chef().equals(user.getID())){
                        SharedPreferences.Editor editor = share.edit();
                        editor.putBoolean("Boutique",true);
                        editor.putString("nomBoutique",boutique.getName_Boutique());
                        editor.putString("idBoutique",boutique.getID_Boutique());
                        editor.putString("adresseBoutique",boutique.getAdresse().getAdresse());
                        editor.putString("adresseInfo",boutique.getAdresse().getInfoSupp());
                        editor.putString("adresseWilaya",boutique.getAdresse().getWilaya());
                        editor.commit();
                        editor.apply();


                        //Toast.makeText(getContext(),"YOU ARE BOUTIQUE",Toast.LENGTH_SHORT).show();


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Things.myBoutiques = myArray;


    }

    public void Fonct1(){

        final ArrayList<Message> myArray = new ArrayList();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Message");

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Message message = snapshot.getValue(Message.class);

                        if(message.getId_user().equals(user.getID())){
                            myMessage = message;

                            openDialog();

                        }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void deletex(Message msg){

        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("Message").child(msg.getId_user()).removeValue();

    }

    public void openDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.newmessage,null);

        builder.setView(view);
        final AlertDialog show = builder.show();



        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                show.dismiss();
                deletex(myMessage);
            }
        },7000);




    }


    public void getSearchListe(){
        Things.history = new ArrayList<>();

        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("History").child("Words").child(user.getID());;
       mDatabase3.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String word = snapshot.getValue(String.class);

                Things.history.add(word);



               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    public void doIt(){
        ///////////////////////////////////////////////////////////////slider///////////////////////////////////////////////////////////////////////////////


        if(!Things.myPubs.isEmpty()){
            sliderAdapterExample = new SliderAdapterExample(getActivity());
            SlideItem slideItem ;

            for(int i = 0;i<Things.myPubs.size();i++){
                slideItem = new SlideItem(Things.myPubs.get(i).getNomPub(),Things.myPubs.get(i).getDecsPub(),Things.myPubs.get(i).getImage());
                sliderAdapterExample.addItem(slideItem);
            }





            sliderView.setSliderAdapter(sliderAdapterExample);


            sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            /*sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);*/
            sliderView.setScrollTimeInSec(5);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();




            if(Things.confi==false){
                startActivity(new Intent(getContext(),VideTest.class));
                Things.confi=true;
            }



        }else {
            pubi.setVisibility(View.GONE);
            sliderView.setVisibility(View.GONE);
        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    public void getHistoryPro(){


        mDatabase4 = FirebaseDatabase.getInstance().getReference();

        Things.histo = new ArrayList<>();

        mDatabase4 = FirebaseDatabase.getInstance().getReference().child("History").child("Products").child(user.getID());

        mDatabase4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProduitHistory produitHistory = snapshot.getValue(ProduitHistory.class);
                    if(!Things.histo.contains(produitHistory)){
                        Things.histo.add(produitHistory);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

