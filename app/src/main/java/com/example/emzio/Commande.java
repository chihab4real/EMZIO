package com.example.emzio;

import java.util.ArrayList;
import java.util.Date;

public class Commande {
    private String ID_commande;

    private String ID_client;
    private int prix;
    private String date_validation;
    private  Adresse Adresse;
    private  String type_de_payement;
    private  String type_de_livraison;

    private ArrayList<ProduitCommande> produits;

    Commande(){

    }

    Commande(String idc){

        this.ID_client=idc;
        this.ID_commande = generateID();
        this.produits = new ArrayList<>();


    }

    public String getID_commande() {
        return ID_commande;
    }

    public void setID_commande(String ID_commande) {
        this.ID_commande = ID_commande;
    }



    public String getID_client() {
        return ID_client;
    }

    public void setID_client(String ID_client) {
        this.ID_client = ID_client;
    }

    public ArrayList<ProduitCommande> getProduits() {
        return produits;
    }

    public void setProduits(ArrayList<ProduitCommande> produits) {
        this.produits = produits;
    }


    public String generateID(){
        return ""+System.currentTimeMillis()+"";
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDate_validation() {
        return date_validation;
    }

    public void setDate_validation(String date_validation) {
        this.date_validation = date_validation;
    }

    public Adresse getAdresse() {
        return Adresse;
    }

    public void setAdresse(Adresse adresse) {
        Adresse = adresse;
    }

    public String getType_de_payement() {
        return type_de_payement;
    }

    public void setType_de_payement(String type_de_payement) {
        this.type_de_payement = type_de_payement;
    }

    public String getType_de_livraison() {
        return type_de_livraison;
    }

    public void setType_de_livraison(String type_de_livraison) {
        this.type_de_livraison = type_de_livraison;
    }
}
