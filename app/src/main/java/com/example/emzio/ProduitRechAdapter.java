package com.example.emzio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.emzio.ui.user.MainRechrcheRec;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProduitRechAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<Product> products;


    public ProduitRechAdapter(){

    }

    public ProduitRechAdapter(Context context,ArrayList<Product> arrayList){
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
            convertView=layoutInflater.inflate(R.layout.produit_rech,null);

        }

        ImageView img = convertView.findViewById(R.id.rech_img);
        TextView nom= convertView.findViewById(R.id.rech_nom);
        TextView marque = convertView.findViewById(R.id.rech_marqu);
        final TextView prix = convertView.findViewById(R.id.rech_prix);
        RelativeLayout relativeLayout = convertView.findViewById(R.id.rech_relati);
        Button oui = convertView.findViewById(R.id.rech_oui),non= convertView.findViewById(R.id.rech_non);
        final CardView cardView = convertView.findViewById(R.id.supp);





        nom.setText(products.get(position).getNom());
        prix.setText(products.get(position).getPrix()+" Da");
        marque.setText(products.get(position).getMarque());

        Picasso.get().load(products.get(position).getImg()).resize(720,720).into(img);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardView.getVisibility()==View.GONE){
                    cardView.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = MainRechrcheRec.grid.getLayoutParams();
                    layoutParams.height = layoutParams.height+convertDpToPixels(90); //this is in pixels
                    MainRechrcheRec.grid.setLayoutParams(layoutParams);

                }else{
                    cardView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = MainRechrcheRec.grid.getLayoutParams();
                    layoutParams.height = layoutParams.height-convertDpToPixels(90); //this is in pixels
                    MainRechrcheRec.grid.setLayoutParams(layoutParams);
                }
            }
        });

        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = MainRechrcheRec.grid.getLayoutParams();
                layoutParams.height = layoutParams.height-convertDpToPixels(90); //this is in pixels
                MainRechrcheRec.grid.setLayoutParams(layoutParams);
            }
        });

        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("History").child("Products").child(MainRechrcheRec.user.getID()).child(products.get(position).getID()).removeValue();
                cardView.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = MainRechrcheRec.grid.getLayoutParams();
                layoutParams.height = layoutParams.height-convertDpToPixels(220); //this is in pixels
                MainRechrcheRec.grid.setLayoutParams(layoutParams);


                for(int i=0;i<Things.histo.size();i++){
                    if(Things.histo.get(i).getId().equals(products.get(position).getID())){

                        Things.histo.remove(Things.histo.get(i));
                        //Things.histo.notify();

                    }
                }



                products.remove(products.get(position));
                notifyDataSetChanged();



            }
        });





        return convertView;
    }

    public static int convertDpToPixels(float dp){
        Resources resources = Resources.getSystem();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }





}
