package com.example.emzio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatuListAdapter extends ArrayAdapter<Product> {
    private static final  String TAG ="StatuListAdapter";
    private Context context;
    private int resource;
    private Commande commande;




    public StatuListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> liste,Commande commande) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
        this.commande = commande;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       /* String id,date;
        int prix;*/
       String marque;
       String name;

        marque = getItem(position).getMarque();
        name = getItem(position).getNom();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);

        /*TextView comamnde_id,commande_price,commande_date;
        comamnde_id = convertView.findViewById(R.id.liste_id_commande);
        commande_date = convertView.findViewById(R.id.liste_date_commande);
        commande_price = convertView.findViewById(R.id.liste_price_commande);

        comamnde_id.setText("Numero de la commande :\n"+id);
        commande_date.setText("date de validation : \n" +date);
        commande_price.setText(String.valueOf(prix)+" Da");
*/
        TextView statu_marque, statu_nom,statu_price,statu_qt;
        ImageView img = convertView.findViewById(R.id.statu_imgpd);
        statu_marque = convertView.findViewById(R.id.statu_marque);
        statu_nom = convertView.findViewById(R.id.statu_nomp);
        statu_price = convertView.findViewById(R.id.statu_price);
        statu_qt = convertView.findViewById(R.id.statu_qt);

        statu_marque.setText(marque);
        statu_nom.setText(name);
        int qt=0;
        statu_price.setText(getItem(position).getPrix()+" Da");

        int i;
        for(i=0;i<commande.getProduits().size();i++){
            if(commande.getProduits().get(i).getProduit_ID().equals(getItem(position).getID())){
                qt = commande.getProduits().get(i).getProdui_qt();
                break;
            }
        }
        statu_qt.setText(String.valueOf(qt));

        Picasso.get().load(getItem(position).getImg()).resize(720,720).into(img);


        notifyDataSetChanged();

        return convertView;
    }
}
