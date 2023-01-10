package com.example.emzio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikeListAdapter extends ArrayAdapter<ProduitLike> {
    private static final  String TAG ="LikeListAdaoter";
    private Context context;
    private int resource;

    //private Commande commande;




    public LikeListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ProduitLike> liste) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
       // this.commande = commande;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String marque;
        String name;
        Product product = new Product();
        int i;
        for(i=0;i<Things.productsList.size();i++){
            if(getItem(position).getID_Produit().equals(Things.productsList.get(i).getID())){
                product =Things.productsList.get(i);
            }
        }

        Things.produitLikes.add(getItem(position));



        marque = product.getMarque();
        name = product.getNom();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);


        TextView statu_marque, statu_nom,statu_price;
        ImageView img = convertView.findViewById(R.id.likep_imgpd);
        statu_marque = convertView.findViewById(R.id.likep_marque);
        statu_nom = convertView.findViewById(R.id.likep_nomp);
        statu_price = convertView.findViewById(R.id.likep_price);


        statu_marque.setText(marque);
        statu_nom.setText(name);
        int qt=0;
        statu_price.setText(product.getPrix()+" Da");
        Picasso.get().load(product.getImg()).resize(720,720).into(img);


        //notifyDataSetChanged();

        return convertView;
    }
}
