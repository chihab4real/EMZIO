package com.example.emzio;

import android.net.Uri;

public class SlideItem {

    private String nomBoutique;
    private Uri image;
    private String adresse;


    public SlideItem(){

    }

    public SlideItem(String name,String adresse,Uri image){
        this.nomBoutique = name;
        this.adresse= adresse;
        this.image=image;
    }
    public String getNomBoutique() {
        return nomBoutique;
    }

    public void setNomBoutique(String nomBoutique) {
        this.nomBoutique = nomBoutique;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
