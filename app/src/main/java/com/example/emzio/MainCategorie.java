package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

public class MainCategorie extends AppCompatActivity {

    ArrayList<Product> P = new ArrayList<>();

    ArrayList<Product> P_U = new ArrayList<>();
    ArrayList<String> P_V = new ArrayList<>();
    ArrayList<Product> myArrayRec = new ArrayList<>();

    ArrayList<Vecteur> vecteur1 = new ArrayList<>();

    ArrayList<Vecteur> vecteur2 = new ArrayList<>();
    ArrayList<String> all_marques = new ArrayList<>();
    ImageButton panier;

    Spinner marque,trier;
    String m = "Toutes";
    int t = 0;

    TextView txt;
    ArrayList<Product> myArray = new ArrayList<>();
    ArrayList<Product> myArray_4 = new ArrayList<>();
    ArrayList<Product> myArray_reste = new ArrayList<>();
    MyAdapter myAdapter,myAdapter2;
    GridView grid_first, grid_all;
    RecyclerView recyclerView;
    RecoAdapter recoAdapter;

    public static CardView cardView;
    static CardView cardViewNumber;
    public static TextView numberp;
    ImageView imageView;

    LinearLayout linearLayoutRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categorie);

        imageView = findViewById(R.id.myvector);
        linearLayoutRec = findViewById(R.id.linear_rec);
      methode(Things.word);

      marque = findViewById(R.id.spinner_cat_marque);
      trier = findViewById(R.id.spinner_cat_trier);



        cardViewNumber = findViewById(R.id.card_number);
        numberp = findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }

        ///////
        recyclerView = findViewById(R.id.recylce_for);
        //recoAdapter = new RecoAdapter(Things.productsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(null);

        ///////
        txt = findViewById(R.id.categroei_name);
        getProducts(Things.word);

        //spiners
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, all_marques);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marque.setAdapter(arrayAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trier, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trier.setAdapter(adapter);
        //

        grid_first = findViewById(R.id.products_categorie);
        cardView = findViewById(R.id.card);
        grid_all = findViewById(R.id.products_reste);
        grid_all.setAdapter(null);
        grid_first.setAdapter(null);

        txt.setText(Things.word);

        /*

        searchProducts();
        P = myArray;

        toArray();
        Fonction();
        searchProducts1();

        if(myArrayRec.isEmpty()){
           linearLayoutRec.setVisibility(View.GONE);
        }else{
            linearLayoutRec.setVisibility(View.VISIBLE);
            recoAdapter = new RecoAdapter(myArrayRec);
            recyclerView.setAdapter(recoAdapter);
            recoAdapter.setOnItemCLickListener(new RecoAdapter.OnItemCLickListener() {
                @Override
                public void OnItemClick(int position) {
                    Intent intent = new Intent(MainCategorie.this, MainProduit.class);

                    Things.product_act = myArrayRec.get(position);
                    startActivity(intent);
                }
            });
        }
*/
        onStart();


        if(myArray.size()<=2){

            myArray_4 = myArray;
                cardView.setVisibility(View.GONE);
                grid_all.setVisibility(View.GONE);



            myAdapter=new MyAdapter(this,myArray_4);




            ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
            layoutParams.height = convertDpToPixels(210*((myArray.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
            grid_first.setLayoutParams(layoutParams);
            grid_first.setAdapter(myAdapter);



            myAdapter.notifyDataSetChanged();






        }else{
            divise();

            myAdapter=new MyAdapter(this,myArray_4);
            /*here*/   myAdapter2=new MyAdapter(this,myArray_reste);



            ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
            layoutParams.height = convertDpToPixels(210*((myArray_4.size()+1)/2)+5,this.getApplicationContext()); //this is in pixels
            grid_first.setLayoutParams(layoutParams);
            grid_first.setAdapter(myAdapter);



            myAdapter.notifyDataSetChanged();




            layoutParams = grid_all.getLayoutParams();
         /*here*/   layoutParams.height = convertDpToPixels(210*((myArray_reste.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
            grid_all.setLayoutParams(layoutParams);
            grid_all.setAdapter(myAdapter2);



            myAdapter2.notifyDataSetChanged();


        }




        marque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trierMarque(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        trier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trierListe(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        grid_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent intent = new Intent(MainCategorie.this, MainProduit.class);
                Product product = new Product();
                product = myArray_4.get(position);
                Things.product_act = product;


                        startActivity(intent);


            }
        });


        grid_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final Intent intent = new Intent(MainCategorie.this, MainProduit.class);
                Product product = new Product();
                product = myArray_reste.get(position);

                Things.product_act = product;


               startActivity(intent);


            }
        });

        panier =findViewById(R.id.panier_vector);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainCategorie.this,MainPanier.class));
            }
        });



    }

    public  void getProducts(String word){
        myArray = new ArrayList<>();
        all_marques = new ArrayList<>();
        all_marques.add("Toutes");

        int i;
        for(i=0;i<Things.productsList.size();i++){
            if(Things.productsList.get(i).getType().equals(word)){
                if(!all_marques.contains(Things.productsList.get(i).getMarque())){
                    all_marques.add(Things.productsList.get(i).getMarque());
                }
                myArray.add(Things.productsList.get(i));

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


    public void divise(){
        myArray_4 = new ArrayList<>();
        myArray_reste = new ArrayList<>();


        myArray_4.add(myArray.get(0));
        myArray_4.add(myArray.get(1));


        int i;
        for(i=2;i<myArray.size();i++){
            myArray_reste.add(myArray.get(i));
        }


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


        P = new ArrayList<>();
        P_U = new ArrayList<>();
        P_V = new ArrayList<>();
        vecteur1 = new ArrayList<>();
        vecteur2 = new ArrayList<>();
        myArrayRec = new ArrayList<>();

        searchProducts();
        P = myArray;

        toArray();
        Fonction();
        searchProducts1();

        if(myArrayRec.isEmpty()){
            linearLayoutRec.setVisibility(View.GONE);
        }else{
            linearLayoutRec.setVisibility(View.VISIBLE);
            recoAdapter = new RecoAdapter(myArrayRec);
            recyclerView.setAdapter(recoAdapter);

            recoAdapter.setOnItemCLickListener(new RecoAdapter.OnItemCLickListener() {
                @Override
                public void OnItemClick(int position) {
                    Intent intent = new Intent(MainCategorie.this, MainProduit.class);
                    //here
                    Things.product_act = myArrayRec.get(position);
                    startActivity(intent);
                }
            });
        }


    }



    void searchProducts(){
        int i,j;
        ArrayList<String> myArrayIds = new ArrayList<>();
        for(int k = 0;k<Things.histo.size();k++){
            myArrayIds.add(Things.histo.get(k).getId());

        }


        if(!myArrayIds.isEmpty()){
            for(i=0;i<myArrayIds.size();i++){
                for(j=0;j<Things.productsList.size();j++){
                    if(myArrayIds.get(i).equals(Things.productsList.get(j).getID())){
                        P_U.add(Things.productsList.get(j));
                    }
                }

            }
        }


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
                        myArrayRec.add(Things.productsList.get(j));
                    }
                }

            }

            Things.produit_recomnde = myArray;
        }


    }



public void methode(String x){

    if(x.equals("Composants PC")){

        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_compo));

    }
    if(x.equals("Accessoires ordinateurs")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_acc));
    }
    if(x.equals("Ordinateurs")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_pc));
    }
    if(x.equals("Ordinateur Portables")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_laptop));
    }
    if(x.equals("Tablettes")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_tablet));
    }
    if(x.equals("Imprimantes et scanners")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_printer));
    }
    if(x.equals("Téléphones")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_phones));
    }
    if(x.equals("Consoles des jeux")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_games));
    }
    if(x.equals("Appareils photo")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_camera));
    }
    if(x.equals("Sons")){
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_cat_son));
    }
}


public void trierMarque(String marque){
        m = marque;

        if(marque.equals("Toutes")){
            cardView.setVisibility(View.VISIBLE);
            findViewById(R.id.card_rec).setVisibility(View.VISIBLE);
            getProducts(Things.word);

            if(t==1){
                sort(myArray);

            }

            if(t==2){
                sortrevers(myArray);


            }

            if(myArray.size()<=2){

                myArray_4 = myArray;
                cardView.setVisibility(View.GONE);
                grid_all.setVisibility(View.GONE);



                myAdapter=new MyAdapter(this,myArray_4);




                ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
                layoutParams.height = convertDpToPixels(210*((myArray.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
                grid_first.setLayoutParams(layoutParams);
                grid_first.setAdapter(myAdapter);



                myAdapter.notifyDataSetChanged();






            }else{
                divise();

                myAdapter=new MyAdapter(this,myArray_4);
                /*here*/   myAdapter2=new MyAdapter(this,myArray_reste);



                ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
                layoutParams.height = convertDpToPixels(210*((myArray_4.size()+1)/2)+5,this.getApplicationContext()); //this is in pixels
                grid_first.setLayoutParams(layoutParams);
                grid_first.setAdapter(myAdapter);



                myAdapter.notifyDataSetChanged();




                layoutParams = grid_all.getLayoutParams();
                /*here*/   layoutParams.height = convertDpToPixels(210*((myArray_reste.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
                grid_all.setLayoutParams(layoutParams);
                grid_all.setAdapter(myAdapter2);



                myAdapter2.notifyDataSetChanged();


            }


        }else{

            cardView.setVisibility(View.GONE);
            findViewById(R.id.card_rec).setVisibility(View.GONE);

            myArray_4 = new ArrayList<>();

            for(int i=0;i<myArray.size();i++){
                if(myArray.get(i).getMarque().equals(marque)){
                    myArray_4.add(myArray.get(i));
                }
            }


            if(t==1){
                sort(myArray_4);
            }

            if(t==2){
             sortrevers(myArray_4);
            }


            myAdapter=new MyAdapter(this,myArray_4);


            ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
            layoutParams.height = convertDpToPixels(210*((myArray_4.size()+1)/2)+5,this.getApplicationContext()); //this is in pixels
            grid_first.setLayoutParams(layoutParams);
            grid_first.setAdapter(myAdapter);



            myAdapter.notifyDataSetChanged();


        }

}


public void trierListe(int position){
        t= position;

    if(m.equals("Toutes")){
        cardView.setVisibility(View.VISIBLE);
        findViewById(R.id.card_rec).setVisibility(View.VISIBLE);
        getProducts(Things.word);

        if(t==1){
            sort(myArray);

        }

        if(t==2){
            sortrevers(myArray);


        }

        if(myArray.size()<=2){

            myArray_4 = myArray;
            cardView.setVisibility(View.GONE);
            grid_all.setVisibility(View.GONE);



            myAdapter=new MyAdapter(this,myArray_4);




            ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
            layoutParams.height = convertDpToPixels(210*((myArray.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
            grid_first.setLayoutParams(layoutParams);
            grid_first.setAdapter(myAdapter);



            myAdapter.notifyDataSetChanged();






        }else{
            divise();

            myAdapter=new MyAdapter(this,myArray_4);
            /*here*/   myAdapter2=new MyAdapter(this,myArray_reste);



            ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
            layoutParams.height = convertDpToPixels(210*((myArray_4.size()+1)/2)+5,this.getApplicationContext()); //this is in pixels
            grid_first.setLayoutParams(layoutParams);
            grid_first.setAdapter(myAdapter);



            myAdapter.notifyDataSetChanged();




            layoutParams = grid_all.getLayoutParams();
            /*here*/   layoutParams.height = convertDpToPixels(210*((myArray_reste.size()+1)/2)+15,this.getApplicationContext()); //this is in pixels
            grid_all.setLayoutParams(layoutParams);
            grid_all.setAdapter(myAdapter2);



            myAdapter2.notifyDataSetChanged();


        }


    }else{

        cardView.setVisibility(View.GONE);
        findViewById(R.id.card_rec).setVisibility(View.GONE);

        myArray_4 = new ArrayList<>();

        for(int i=0;i<myArray.size();i++){
            if(myArray.get(i).getMarque().equals(m)){
                myArray_4.add(myArray.get(i));
            }
        }


        if(position==1){
            sort(myArray_4);
        }

        if(position==2){
            sortrevers(myArray_4);
        }


        myAdapter=new MyAdapter(this,myArray_4);


        ViewGroup.LayoutParams layoutParams = grid_first.getLayoutParams();
        layoutParams.height = convertDpToPixels(210*((myArray_4.size()+1)/2)+5,this.getApplicationContext()); //this is in pixels
        grid_first.setLayoutParams(layoutParams);
        grid_first.setAdapter(myAdapter);



        myAdapter.notifyDataSetChanged();


    }


}

    public void sort(ArrayList<Product> arrayList){
        int i,j,indice_min;
        Product temp;

        for(i=0;i<arrayList.size();i++){
            indice_min = i;
            for(j=i+1;j<arrayList.size();j++){
                if(Integer.parseInt(arrayList.get(indice_min).getPrix())>Integer.parseInt(arrayList.get(j).getPrix())){
                    indice_min = j;
                }
            }

            temp = arrayList.get(i);
            arrayList.set(i,arrayList.get(indice_min));
            arrayList.set(indice_min,temp);
        }
    }

    public void sortrevers(ArrayList<Product> arrayList){
        int i,j,indice_min;
        Product temp;

        for(i=0;i<arrayList.size();i++){
            indice_min = i;
            for(j=i+1;j<arrayList.size();j++){
                if(Integer.parseInt(arrayList.get(indice_min).getPrix())<Integer.parseInt(arrayList.get(j).getPrix())){
                    indice_min = j;
                }
            }

            temp = arrayList.get(i);
            arrayList.set(i,arrayList.get(indice_min));
            arrayList.set(indice_min,temp);
        }
    }




}
