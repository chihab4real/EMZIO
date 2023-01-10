package com.example.emzio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterPanier extends BaseAdapter{
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<ProduitPanier> products;
    public Button delet;


    public MyAdapterPanier(){

    }

    public MyAdapterPanier(Context context, ArrayList<ProduitPanier> arrayList){
        super();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.products = arrayList;
    }



    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       if(layoutInflater==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView=layoutInflater.inflate(R.layout.produit_panier,null);

        }

        ImageView img = convertView.findViewById(R.id.panier_produit_img);
        TextView marque = convertView.findViewById(R.id.panier_produit_marque);
        TextView nom= convertView.findViewById(R.id.panier_produit_nom);
        TextView prix = convertView.findViewById(R.id.panier_produit_prix);
        final TextView quantite = convertView.findViewById(R.id.panier_produit_quantite);
        Button add, moins;
        add = convertView.findViewById(R.id.panier_button_plus);
        moins = convertView.findViewById(R.id.panier_button_moins);
        delet = convertView.findViewById(R.id.panier_button_delet);
        quantite.setText(String.valueOf(products.get(position).getQuantit()));
        nom.setText(products.get(position).getProduct().getNom());
        prix.setText(products.get(position).getProduct().getPrix());

        marque.setText(products.get(position).getProduct().getMarque());



        Picasso.get().load(products.get(position).getProduct().getImg()).resize(720,720).into(img);


        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainPanier.prixx=MainPanier.prixx - (products.get(position).getQuantit()*Integer.parseInt(products.get(position).getProduct().getPrix()));
                MainPanier.price_total.setText(String.valueOf(MainPanier.prixx));
                products.remove(position);
                Things.Panier.remove(position);
                Things.number-=1;

                if(Things.Panier.size()==0){
                    MainPanier.noProductsText.setVisibility(View.VISIBLE);
                    MainPanier.valider.setVisibility(View.GONE);
                    MainPanier.continueAchats.setVisibility(View.VISIBLE);
                    MainPanier.cardView.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPanier.prixx=MainPanier.prixx + Integer.parseInt(products.get(position).getProduct().getPrix());
                MainPanier.price_total.setText(String.valueOf(MainPanier.prixx)+"Da");
                products.get(position).setQuantit(products.get(position).getQuantit()+1);
                quantite.setText(String.valueOf(products.get(position).getQuantit()));
                notifyDataSetChanged();

            }
        });


        moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(products.get(position).getQuantit()>1){
                    MainPanier.prixx=MainPanier.prixx - Integer.parseInt(products.get(position).getProduct().getPrix());
                    MainPanier.price_total.setText("Prix Total : "+String.valueOf(MainPanier.prixx)+"Da");
                    products.get(position).setQuantit(products.get(position).getQuantit()-1);
                    quantite.setText(String.valueOf(products.get(position).getQuantit()));
                    notifyDataSetChanged();
                }


            }
        });


       // notifyDataSetChanged();



        return convertView;
    }



}
