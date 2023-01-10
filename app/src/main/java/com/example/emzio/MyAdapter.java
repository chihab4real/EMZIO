package com.example.emzio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emzio.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class MyAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<Product> products;


    public MyAdapter() {

    }

    public MyAdapter(Context context, ArrayList<Product> arrayList) {
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

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.produit, null);

        }

        LinearLayout linearLayout = convertView.findViewById(R.id.offi);
        final ImageView img = convertView.findViewById(R.id.imgpd);
        TextView text = convertView.findViewById(R.id.textView);
        TextView text2 = convertView.findViewById(R.id.textView2);

        text.setText(nameSetting(products.get(position).getNom()));

        text2.setText(changeString(products.get(position).getPrix()) + "Da");

        if(isValidate(products.get(position).getID_user())){
            linearLayout.setVisibility(View.VISIBLE);
        }


                Picasso.get().load(products.get(position).getImg()).resize(850, 720).into(img);






        //notifyDataSetChanged();




        return convertView;
    }

public String changeString(String number){

        char[] bytees = new char[number.length()];
        String one="",two="";

        for(int i =0 ; i<number.length();i++){
            bytees[i] = number.charAt(i);
        }

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

public String nameSetting(String name){

        String one="";
        if(name.length()>=25){
            for(int i =0;i<22;i++){
                one+=String.valueOf(name.charAt(i));
            }

            for(int i=22;i<25;i++){
                one+=".";
            }

            return one;
        }

        return name;
}

    public boolean isValidate(String id){

        for(int i = 0;i<Things.myBoutiques.size();i++){
            if(Things.myBoutiques.get(i).getID_chef().equals(id)){
                return true;
            }
        }

        return false;
    }

}