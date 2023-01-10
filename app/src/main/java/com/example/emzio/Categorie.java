package com.example.emzio;

public class Categorie {
    private String nom_categorie;
    private int importance;

    Categorie(){

    }

    Categorie(String nom, int im){
        this.nom_categorie = nom;
        this.importance = im;
    }

    public String getNom_categorie() {
        return nom_categorie;
    }

    public void setNom_categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }
}
