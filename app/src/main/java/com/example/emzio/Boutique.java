package com.example.emzio;

public class Boutique {

    private String ID_Boutique;
    private String ID_chef;
    private String Name_Boutique;
    private Adresse adresse;

    public Boutique(){

    }

    public Boutique(String idc,String name){

        this.ID_chef = idc;
        this.Name_Boutique = name;
        this.adresse = new Adresse();
        this.ID_Boutique = generateID();
    }

    public String getID_Boutique() {
        return ID_Boutique;
    }

    public void setID_Boutique(String ID_Boutique) {
        this.ID_Boutique = ID_Boutique;
    }

    public String getID_chef() {
        return ID_chef;
    }

    public void setID_chef(String ID_chef) {
        this.ID_chef = ID_chef;
    }

    public String getName_Boutique() {
        return Name_Boutique;
    }

    public void setName_Boutique(String name_Boutique) {
        Name_Boutique = name_Boutique;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String generateID(){
        return System.currentTimeMillis()+"_"+this.Name_Boutique;
    }
}
