package com.example.emzio;

import android.widget.ArrayAdapter;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class User implements Serializable {

    private String ID;
    private String Nom;
    private String Prenom;
    private String Sexe;
    private String Email;
    private String Password;
    private String Phone;
    private  boolean Pro;
    private Adresse Adresse;

    private boolean boutique;

    public User() {
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getSexe() {
        return Sexe;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public User(String nom,String prenom,String sexe,String email,String password,String phone,boolean pro) {
        this.Nom = nom;
        this.Prenom= prenom;
        this.Sexe = sexe;
        this.Email= email;
        this.Password=password;
        this.Phone=phone;
        this.Pro=pro;

        this.Adresse = new Adresse();
        this.Adresse.setAdresse("Aucune");
        this.Adresse.setInfoSupp("Aucune");
        this.Adresse.setWilaya("Aucune");
        this.Adresse.setNum_wilaya(0);
        this.boutique = false;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isPro() {
        return Pro;
    }

    public void setPro(boolean pro) {
        Pro = pro;
    }

    public Adresse getAdresse() {
        return Adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.Adresse = adresse;
    }

    public boolean isBoutique() {
        return boutique;
    }

    public void setBoutique(boolean boutique) {
        this.boutique = boutique;
    }
}