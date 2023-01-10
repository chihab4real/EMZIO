package com.example.emzio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainAfterSearch extends AppCompatActivity {


    GridView grid;
    ArrayList<Product> myArray;
    MyAdapter myAdapter;
    TextView text;
    String filter_marque = "-1";
    int filter_trier = -1;
    Spinner spinner_marque,spinner_trier;
    ArrayList<String> marque = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_after_search);
        text = findViewById(R.id.textx);
        spinner_marque = findViewById(R.id.spinner_marque);
        spinner_trier = findViewById(R.id.spinner_trier);

        text.setText(Things.search_word);
        grid = findViewById(R.id.grid_after);
        grid.setAdapter(null);

        myArray=new ArrayList<>();
        myArray = Things.produitSearch;

        myAdapter = new MyAdapter(MainAfterSearch.this, myArray);//Create new Adapter for showing Products
        grid.setAdapter(myAdapter);
        marque.add("Tous Les Marque");
        allMarque();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ArrayAdapter<String> adapter_marque = new ArrayAdapter<>(MainAfterSearch.this, android.R.layout.simple_spinner_item, marque);

        adapter_marque.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_marque.setAdapter(adapter_marque);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayAdapter<CharSequence> adapter_trier = ArrayAdapter.createFromResource(this,R.array.trier,android.R.layout.simple_spinner_item);

        adapter_trier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_trier.setAdapter(adapter_trier);




        spinner_marque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position ==0){
                    filter_marque="-1";
                    myArray = Things.produitSearch;
                    myAdapter = new MyAdapter(MainAfterSearch.this, myArray);//Create new Adapter for showing Products
                    grid.setAdapter(myAdapter);
                }else{
                    filter_marque = parent.getItemAtPosition(position).toString();
                    Filtrer_Marque(parent.getItemAtPosition(position).toString());
                }

                if(filter_trier !=-1){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_trier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position ==0){
                    myArray = Things.produitSearch;
                    myAdapter = new MyAdapter(MainAfterSearch.this, myArray);//Create new Adapter for showing Products
                    grid.setAdapter(myAdapter);
                }else{


                    filter_trier = position;

                }

                if(!filter_marque.equals("-1")){
                    Filtrer_Marque(filter_marque);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void allMarque(){
        int i;

        for(i=0;i<myArray.size();i++){
            if(!marque.contains(myArray.get(i).getMarque())){
                marque.add(myArray.get(i).getMarque());
            }
        }



    }


    public void Filtrer_Marque(String marque){
        int i;
        ArrayList<Product> newListe = new ArrayList<>();
        for(i=0;i<myArray.size();i++){
            if(myArray.get(i).getMarque().equals(marque)){
                newListe.add(myArray.get(i));
            }
        }

        myAdapter = new MyAdapter(MainAfterSearch.this, newListe);//Create new Adapter for showing Products
        grid.setAdapter(myAdapter);

    }





}
