package com.example.emzio;

import android.content.Context;
import android.net.Uri;
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

public class SearchProductListeAdapter extends ArrayAdapter<Product> {

    private static final  String TAG ="CommandeListeAdapter";
    private Context context;
    private int resource;



    public SearchProductListeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> liste) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ImageView image;
        TextView name,price;


        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);
        image = convertView.findViewById(R.id.search_pic);
        name =convertView.findViewById(R.id.search_product_name);
        price= convertView.findViewById(R.id.search_product_price);


        Picasso.get().load(getItem(position).getImg()).resize(480,480).into(image);
        name.setText(getItem(position).getNom());
        price.setText(getItem(position).getPrix()+"Da");




        return convertView;
    }
}
