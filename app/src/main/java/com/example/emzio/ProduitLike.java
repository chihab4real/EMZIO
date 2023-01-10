package com.example.emzio;

public class ProduitLike {
    private String ID_Produit;
    private String ID_user;


    public ProduitLike(){

    }

    public ProduitLike(String idp, String idu){
        this.ID_Produit = idp;
        this.ID_user= idu;
    }


    public String getID_Produit() {
        return ID_Produit;
    }

    public void setID_Produit(String ID_Produit) {
        this.ID_Produit = ID_Produit;
    }

    public String getID_user() {
        return ID_user;
    }

    public void setID_user(String ID_user) {
        this.ID_user = ID_user;
    }
}
