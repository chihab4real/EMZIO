package com.example.emzio;

public class ProduitCommande {
    private String produit_ID;
    private int produi_qt;


    ProduitCommande(){

    }

    ProduitCommande(String id,int qt){
        this.produit_ID=id;
        this.produi_qt=qt;
    }


    public String getProduit_ID() {
        return produit_ID;
    }

    public void setProduit_ID(String produit_ID) {
        this.produit_ID = produit_ID;
    }

    public int getProdui_qt() {
        return produi_qt;
    }

    public void setProdui_qt(int produi_qt) {
        this.produi_qt = produi_qt;
    }
}
