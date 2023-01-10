package com.example.emzio.ui.recomnded;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emzio.MainCategorie;
import com.example.emzio.MainProduit;
import com.example.emzio.MyAdapter;
import com.example.emzio.Product;
import com.example.emzio.ProduitHistory;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.Vecteur;

import java.util.ArrayList;

public class RecomendedFragment extends Fragment {

    ArrayList<Product> P = new ArrayList<>();
    ArrayList<Product> P_U = new ArrayList<>();
    ArrayList<String> P_V = new ArrayList<>();
    GridView grid;
    MyAdapter myAdapter;
    ArrayList<Product> myArray = new ArrayList<>();

   ArrayList<Vecteur> vecteur1 = new ArrayList<>();

    ArrayList<Vecteur> vecteur2 = new ArrayList<>();

    private RecomendedViewModel dashboardViewModel;

    CardView cardViewNumber,cardView;
    TextView numberp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(RecomendedViewModel.class);
        View root = inflater.inflate(R.layout.recomnded_acti, container, false);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        cardView = root.findViewById(R.id.rec_card);
        cardViewNumber = root.findViewById(R.id.card_number);
        numberp = root.findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }

        grid = root.findViewById(R.id.liste_reco);
        grid.setAdapter(null);
        P = Things.productsList;
        searchProducts();

        toArray();

        Fonction();
        //Toast.makeText(this.getContext(),String.valueOf(P_V.size()),Toast.LENGTH_SHORT).show();
        searchProducts1();

        if(myArray.isEmpty()){
            cardView.setVisibility(View.GONE);
        }

        myAdapter = new MyAdapter(RecomendedFragment.this.getContext(),myArray);

        ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();
        layoutParams.height = convertDpToPixels(215*((myArray.size()+1)/2)+5,this.getContext()); //this is in pixels
        grid.setLayoutParams(layoutParams);

        grid.setAdapter(myAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getContext(), MainProduit.class);
                Product product = new Product();
                product = myArray.get(position);

                Things.product_act = product;


                startActivity(intent);

            }
        });

        return  root;
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

    void searchProducts(){
        int i,j;
        ArrayList<ProduitHistory> myArrayIds;
        myArrayIds = Things.histo;

        if(!myArrayIds.isEmpty()){
            for(i=0;i<myArrayIds.size();i++){
                for(j=0;j<Things.productsList.size();j++){
                    if(myArrayIds.get(i).getId().equals(Things.productsList.get(j).getID())){
                        P_U.add(Things.productsList.get(j));
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
                           //Toast.makeText(this.getContext(),String.valueOf(distance),Toast.LENGTH_SHORT).show();
                       }
                    }
                }
            }
        }
    }


    public boolean con(String id){

        for(int i= 0 ;i<P_U.size();i++){
            if(P_U.get(i).getID().equals(id)){
                return true ;
            }
        }

        return false;
    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    public void onStart() {
        super.onStart();


        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }


    }
}
