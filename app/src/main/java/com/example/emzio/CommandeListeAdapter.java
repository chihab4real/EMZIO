package com.example.emzio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandeListeAdapter extends ArrayAdapter<Commande> {
    private static final  String TAG ="CommandeListeAdapter";
    private Context context;
    private int resource;




    public CommandeListeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Commande> liste) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id,date;
        int prix;

        prix = getItem(position).getPrix();
        id = getItem(position).getID_commande();
        date = getItem(position).getDate_validation();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);

        TextView comamnde_id,commande_price,commande_date;
        comamnde_id = convertView.findViewById(R.id.liste_id_commande);
        commande_date = convertView.findViewById(R.id.liste_date_commande);
        commande_price = convertView.findViewById(R.id.liste_price_commande);

        comamnde_id.setText("Numero de la commande :\n"+id);
        commande_date.setText("date de validation : \n" +date);
        commande_price.setText((changeString(String.valueOf(prix)))+" Da");

        //notifyDataSetChanged();

        return convertView;
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
}
