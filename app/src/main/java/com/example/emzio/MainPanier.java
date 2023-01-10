package com.example.emzio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.ui.home.HomeFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainPanier extends AppCompatActivity {

    ArrayList<Product> P = new ArrayList<>();
    ArrayList<Product> P_U = new ArrayList<>();
    ArrayList<String> P_V = new ArrayList<>();
    ArrayList<Product> myArray = new ArrayList<>();

    ArrayList<Vecteur> vecteur1 = new ArrayList<>();

    ArrayList<Vecteur> vecteur2 = new ArrayList<>();


    MyAdapterPanier myAdapter;
    GridView grid;
    static CardView cardView,cardView2;
    static TextView noProductsText;
    static Button continueAchats,valider;
    RelativeLayout priceLayout;
    public static TextView price_total;
    ArrayList<Product> myArray_AllApp_Prodcuts;
    ArrayList<ProduitPanier> myArray_Allpanier_Products;
    public static int prixx=0;
    SharedPreferences share;
    User user;

    RecyclerView recyclerView;
    RecoAdapter recoAdapter;

    Commande commande;
    private DatabaseReference mDatabase,mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prixx=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panier_acti);
        cardView = findViewById(R.id.card_panier);
        cardView2 = findViewById(R.id.card_rec);


        ///////
        recyclerView = findViewById(R.id.recylce_for_produit);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(null);

        ///////




        share = getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));






        continueAchats = findViewById(R.id.panier_continue);

        price_total = findViewById(R.id.panier_price_total);


        noProductsText=findViewById(R.id.panier_noProducts);
        valider = findViewById(R.id.panier_valider_commande);
        myArray_AllApp_Prodcuts = Things.Panier;
        int i;
        myArray_Allpanier_Products =new ArrayList<>();

        for(i=0;i<myArray_AllApp_Prodcuts.size();i++){
            myArray_Allpanier_Products.add(new ProduitPanier(myArray_AllApp_Prodcuts.get(i)));
        }
        P = Things.productsList;
        P_U = Things.Panier;
        toArray();
        Fonction();
        searchProducts1();

        if(myArray.isEmpty()){
            cardView2.setVisibility(View.GONE);
        }else{
            cardView2.setVisibility(View.VISIBLE);
        }

        recoAdapter = new RecoAdapter(myArray);
        recyclerView.setAdapter(recoAdapter);

        recoAdapter.setOnItemCLickListener(new RecoAdapter.OnItemCLickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainPanier.this, MainProduit.class);

                Things.product_act = myArray.get(position);
                startActivity(intent);
            }
        });



        int j;
        myAdapter = new MyAdapterPanier(MainPanier.this, myArray_Allpanier_Products);

        grid = findViewById(R.id.panier_grid_products);
        grid.setAdapter(null);

        if(myArray_Allpanier_Products.size()!=0){


            ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();
            layoutParams.height = convertDpToPixels((myArray_Allpanier_Products.size()*137)+5,getApplicationContext()); //this is in pixels
            grid.setLayoutParams(layoutParams);

            for(j=0;j<myArray_Allpanier_Products.size();j++){
                prixx = prixx + myArray_Allpanier_Products.get(j).getQuantit()* Integer.parseInt(myArray_Allpanier_Products.get(j).getProduct().getPrix());
                price_total.setText(String.valueOf(prixx)+" Da");

            }


        //price_total.setText(String.valueOf(33));
            continueAchats.setVisibility(View.GONE);

                noProductsText.setVisibility(View.GONE);

            cardView.setVisibility(View.VISIBLE);

        }else{
            continueAchats.setVisibility(View.VISIBLE);
            noProductsText.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }


        grid.setAdapter(myAdapter);


        continueAchats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*commande = new Commande(user.getID());
                commande.setPrix(prixx);
                 methode();
                 Things.Panier.clear();
                myArray_Allpanier_Products.clear();
                 grid.setAdapter(null);
                 myAdapter.notifyDataSetChanged();
                continueAchats.setVisibility(View.VISIBLE);
                noProductsText.setVisibility(View.VISIBLE);
                valider.setVisibility(View.GONE);
                price_total.setVisibility(View.GONE);
                priceLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Votre Commande a été Valider avec Succes",Toast.LENGTH_SHORT).show();
                addCommande(commande);*/

                Things.price = prixx;
                Things.inPanier = myArray_Allpanier_Products;
                startActivity(new Intent(MainPanier.this,MainValidation.class));

            }
        });




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


    public void addCommande(Commande commande){


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        commande.setDate_validation(formatter.format((date)));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Commande").child(user.getID()).child(commande.getID_commande()).setValue(commande);


        Things.refresh=true;

    }

    void methode(){
        int k=0;
        ArrayList<ProduitCommande> produit_dans_commande = new ArrayList<>();
        ProduitCommande object;

        for(k=0;k<myArray_Allpanier_Products.size();k++){
            object = new ProduitCommande();
            object.setProduit_ID(myArray_Allpanier_Products.get(k).getProduct().getID());
            object.setProdui_qt(myArray_Allpanier_Products.get(k).getQuantit());

            produit_dans_commande.add(object);
        }

        commande.setProduits(produit_dans_commande);

    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Things.Panier.size()!=0){

            //price_total.setText(String.valueOf(33));
            continueAchats.setVisibility(View.GONE);

            noProductsText.setVisibility(View.GONE);

            cardView.setVisibility(View.VISIBLE);

        }else{
            continueAchats.setVisibility(View.VISIBLE);
            noProductsText.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }


    }
}
