package com.example.emzio;

import android.media.Rating;
import android.net.Uri;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Vector;

public class Things {

    public static ArrayList<Product> productsList = null;

    public static boolean conf = false;
    public static boolean refresh= false;
    public static boolean add = false;
    public static  Product product_act;
    public static  Product product_act_pre;
    public static  Commande commande;
    public static  User user = null;
    public static  int number=0;


    public static  ArrayList<Product> Panier=new ArrayList<>();


    public static  ArrayList<ProduitLike> produitLikes=new ArrayList<>();

    public static ArrayList<Product> produitSearch = new ArrayList<>();

    public static  String search_word;

    public static  String word;


    public static  ArrayList<String> Produit_click = new ArrayList<>();


    public static  ArrayList<Product> produit_recomnde = new ArrayList<>();

    public static  ArrayList<Boutique> myBoutiques = new ArrayList<>();



    public static ArrayList<ProduitPanier> inPanier = new ArrayList<>();

    public static int price =0;

    public static  ArrayList<String> history = new ArrayList<>();


    public static  ArrayList<Publicite> myPubs = new ArrayList<>();

    public  static  int h = 0;
    public  static  int w = 0;
    public static  SliderAdapterExample sliderAdapterExample;

    public static ArrayList<ProduitHistory> histo;

    public static  boolean confi = false;

    public  static  Product productModifier = new Product();
    public  static  String ordre = "no";

    public static Product productDetails = new Product();

    public  static  int productLikesnumber = 0;
    public  static  int productVuesnumber = 0;
    public  static  int productDemandesnumber = 0;

    public static  ArrayList<Commande> commandeArrayList = new ArrayList<>();
    public  static int eva_number = 0;



    public static int numberdemande = 0;













}


