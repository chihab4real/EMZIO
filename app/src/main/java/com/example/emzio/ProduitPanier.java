package com.example.emzio;

public class ProduitPanier {

    private Product product;
    private int quantit;


     ProduitPanier(){

    }

    ProduitPanier(Product product){
         this.product = product;
         this.quantit=1;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantit() {
        return quantit;
    }

    public void setQuantit(int quantit) {
        this.quantit = quantit;
    }
}
