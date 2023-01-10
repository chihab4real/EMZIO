package com.example.emzio;

public class Adresse {
    private String adresse;
    private String wilaya;
    private String infoSupp;
    private  int num_wilaya;

    public Adresse(){

    }


    public Adresse(String ad, String wi,String info){

        this.adresse = ad;
        this.wilaya = wi;
        this.infoSupp = info;

    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getInfoSupp() {
        return infoSupp;
    }

    public void setInfoSupp(String infoSupp) {
        this.infoSupp = infoSupp;
    }

    public int getNum_wilaya() {
        return num_wilaya;
    }

    public void setNum_wilaya(int num_wilaya) {
        this.num_wilaya = num_wilaya;
    }
}
