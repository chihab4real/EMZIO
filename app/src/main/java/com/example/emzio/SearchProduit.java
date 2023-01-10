package com.example.emzio;

public class SearchProduit {
    private String nom;
    private  String id;
    private  String marque;
    private String decs;


    public SearchProduit(){

    }


    public SearchProduit(String nom,String id,String marque,String decs){
        this.nom = nom;
        this.id=id;
        this.marque = marque;
        this.decs= decs;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }
}
