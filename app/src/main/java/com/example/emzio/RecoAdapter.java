package com.example.emzio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecoAdapter extends RecyclerView.Adapter<RecoAdapter.MyViewHolder> {
    private List<Product> productList;
    private OnItemCLickListener onItemCLickListener;

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener;
    }

    public interface  OnItemCLickListener{
        void OnItemClick(int position);


    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prix;
        ImageView img;
        MyViewHolder(View view, final OnItemCLickListener onItemCLickListener) {
            super(view);
            img = view.findViewById(R.id.imgpd_reco);
            nom = view.findViewById(R.id.textView_reco);
            prix = view.findViewById(R.id.textView2_reco);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemCLickListener!=null){
                            int position =  getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION){
                                onItemCLickListener.OnItemClick(position);
                            }
                    }
                }
            });
        }
    }
    public RecoAdapter(List<Product> productList) {
        this.productList = productList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produit_rec, parent, false);
        return new MyViewHolder(itemView,onItemCLickListener);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productList.get(position);
        //holder.title.setText(movie.getTitle());
        Picasso.get().load(product.getImg()).resize(720,720).into(holder.img);
        holder.nom.setText(nameSetting(product.getNom()));
        holder.prix.setText(changeString(product.getPrix())+" Da");
    }
    @Override
    public int getItemCount() {
        return productList.size();
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
    public String nameSetting(String name){

        String one="";
        if(name.length()>=13){
            for(int i =0;i<10;i++){
                one+=String.valueOf(name.charAt(i));
            }

            for(int i=10;i<13;i++){
                one+=".";
            }

            return one;
        }

        return name;
    }
}
