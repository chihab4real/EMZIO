package com.example.emzio;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesListe extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<String> products;


    public CategoriesListe(){

    }

    public CategoriesListe(Context context,ArrayList<String> arrayList){
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView=layoutInflater.inflate(R.layout.categories_home,null);

        }

        TextView text= convertView.findViewById(R.id.cate_nom2);
        ImageView img = convertView.findViewById(R.id.cate_pic);



        if(position ==0){

            img.setBackground(convertView.getResources().getDrawable(R.drawable.composants_pc));

        }

        if(position ==1){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.accesoire_pc));

        }

        if(position ==2){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.ordinateurs));

        }


        if(position ==3){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.laptops));

        }

        if(position ==4){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.tablettes));

        }

        if(position ==5){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.imprimantes_scanner));

        }

        if(position ==6){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.smartphones));

        }

        if(position ==7){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.games));

        }

        if(position ==8){
            img.setBackground(convertView.getResources().getDrawable(R.drawable.photos));

        }

        if(position ==9) {
            img.setBackground(convertView.getResources().getDrawable(R.drawable.sons));
        }



        text.setText(products.get(position));

        //notifyDataSetChanged();



        return convertView;
    }

}
