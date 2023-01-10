package com.example.emzio;

import android.net.Uri;

public class Publicite {
    private  String idPub;
    private String nomPub;
    private  String decsPub;
    private  String idImage;
    private String folder;
    private Uri image;


    Publicite(){

    }

    Publicite(String nom, String decs){
        this.nomPub = nom;
        this.decsPub = decs;
        this.idPub = generateID();
        this.idImage = this.idPub;
    }


    public String getNomPub() {
        return nomPub;
    }

    public void setNomPub(String nomPub) {
        this.nomPub = nomPub;
    }

    public String getDecsPub() {
        return decsPub;
    }

    public void setDecsPub(String decsPub) {
        this.decsPub = decsPub;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getIdPub() {
        return idPub;
    }

    public void setIdPub(String idPub) {
        this.idPub = idPub;
    }

    public String generateID(){
        return System.currentTimeMillis()+"_"+this.nomPub;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
