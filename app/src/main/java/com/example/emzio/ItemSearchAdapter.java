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

public class ItemSearchAdapter extends ArrayAdapter<String> {

    private static final  String TAG ="itemSearchAdapter";
    private Context context;
    private int resource;

    public ItemSearchAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> liste) {
        super(context, resource, liste);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String word= getItem(position);


        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(this.resource,parent,false);

        TextView word_c;
        word_c = convertView.findViewById(R.id.word);

        word_c.setText(word);


        //notifyDataSetChanged();

        return convertView;
    }


}
