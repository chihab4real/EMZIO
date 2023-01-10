package com.example.emzio;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties

public class Product implements Parcelable {
    private String Marque;
    private String Nom;
    private String Type;
    private String Prix;
    private String ID_user;
    private Images images;
    private  String description;
    private String ID;
    private String Folder;
    private Uri img;
    private Uri image1;
    private  Uri image2;
    private Uri image3;
    private Uri image4;
    private  ArrayList<Categorie> categorie;
    private int quantite;
    private  boolean disponible;


    public Product() {
    }


    protected Product(Parcel in) {
        Marque = in.readString();
        Nom = in.readString();
        Type = in.readString();
        Prix = in.readString();
        ID_user = in.readString();
        ID = in.readString();
        Folder = in.readString();
        img = in.readParcelable(Uri.class.getClassLoader());


    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getMarque() {
        return Marque;
    }

    public void setMarque(String marque) {
        Marque = marque;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String prix) {
        Prix = prix;
    }

    public String getID_user() {
        return ID_user;
    }

    public void setID_user(String ID_user) {
        this.ID_user = ID_user;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }


    public String getFolder() {
        return Folder;
    }

    public void setFolder(String folder) {
        Folder = folder;
    }




    public  Product(String marque,String nom,String type,String prix,String ids,String decs,int qt){
        this.Marque=marque;
        this.Nom=nom;
        this.Type=type;
        this.Prix=prix;
        this.ID_user=ids;
        this.ID = generateID();
        this.Folder=null;
        this.description=decs;
        this.categorie = new ArrayList<>();
        this.quantite = qt;
        this.disponible = true;

    }

    public String generateID(){
        return System.currentTimeMillis()+"_"+this.Nom;
    }


    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Marque);
        dest.writeString(Nom);
        dest.writeString(Type);
        dest.writeString(Prix);
        dest.writeString(ID_user);
        dest.writeString(ID);
        dest.writeString(Folder);
        dest.writeParcelable(img, flags);
    }

    public Uri getImage1() {
        return image1;
    }

    public void setImage1(Uri image1) {
        this.image1 = image1;
    }

    public Uri getImage2() {
        return image2;
    }

    public void setImage2(Uri image2) {
        this.image2 = image2;
    }

    public Uri getImage3() {
        return image3;
    }

    public void setImage3(Uri image3) {
        this.image3 = image3;
    }

    public Uri getImage4() {
        return image4;
    }

    public void setImage4(Uri image4) {
        this.image4 = image4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }







    public ArrayList<com.example.emzio.Categorie> getCategorie() {
        return categorie;
    }

    public void setCategorie(ArrayList<com.example.emzio.Categorie> categorie) {
        this.categorie = categorie;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
