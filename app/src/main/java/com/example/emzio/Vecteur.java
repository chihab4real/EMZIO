package com.example.emzio;

import java.util.ArrayList;

public class Vecteur {
    private String id;
    private ArrayList<Integer> vec;


    public Vecteur(){

    }

    public Vecteur(String id,ArrayList<Integer> cat){
        this.id = id;
        this.vec = cat;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Integer> getVec() {
        return vec;
    }

    public void setVec(ArrayList<Integer> categorie) {
        this.vec = categorie;
    }


}
