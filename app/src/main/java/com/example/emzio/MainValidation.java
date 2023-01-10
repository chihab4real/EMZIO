package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emzio.ui.user.MainAdresse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainValidation extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    User user;
    Adresse adresse = new Adresse();
    String boutique_name;
    int prixLiv = 1200;
    int prixx;
    String type_paiment,type_livraison = "A domicile";
    Commande commande;
    NestedScrollView scrollView;
    SharedPreferences share;

    TextView modfier_adresse;
    Timer timer;
    private DatabaseReference mDatabase,mDatabase2,mDatabase3;

    TextView livraison_username,livraison_adresse,livraison_infosup,livraison_wilaya,livraison_phone;


    RadioButton radioButton_domicile,radioButton_boutique;
    Spinner nosBoutiques;

    TextView livraison_prix,livraison_prixLivraison,livraison_prixtotal;

    Button passer_aupaiement,passer_validation,valider;

    RelativeLayout paiment,validation;

    CardView cardViewPaiement,cardViewValidation;


    RadioButton radioButtonPaiement;

    TextView validation_username,validation_livraison,validation_paiement,validation_prix;

    ArrayList<String> boutiques = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_validation);
        scrollView = findViewById(R.id.scrollx);
        timer = new Timer();



        share = this.getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));



        adresse = user.getAdresse();

        paiment = findViewById(R.id.paiement);
        validation = findViewById(R.id.validation);

        cardViewPaiement = findViewById(R.id.card_paiment);
        cardViewValidation = findViewById(R.id.card_validation);


        modfier_adresse = findViewById(R.id.modifier_adresse);


        livraison_username= findViewById(R.id.livraison_username);
        livraison_adresse= findViewById(R.id.livraison_adresse);
        livraison_infosup= findViewById(R.id.livraison_infosup);
        livraison_wilaya= findViewById(R.id.livraison_wilaya);
        livraison_phone= findViewById(R.id.livraison_phone);

        radioButton_boutique = findViewById(R.id.livraison_radio_boutique);
        radioButton_domicile = findViewById(R.id.livraison_radio_domi);

        nosBoutiques = findViewById(R.id.spinner_nosboutique);

        livraison_prix = findViewById(R.id.livraison_prix);
        livraison_prixLivraison = findViewById(R.id.livraison_prixlivraison);
        livraison_prixtotal = findViewById(R.id.livraison_prixtotal);

        passer_aupaiement = findViewById(R.id.livraison_button_passer);



        passer_validation = findViewById(R.id.paiement_button_passer);
        valider = findViewById(R.id.valider);

        radioButtonPaiement = findViewById(R.id.paiement_radio_type);

        validation_username = findViewById(R.id.validation_username);
        validation_livraison = findViewById(R.id.validation_typeliv);
        validation_paiement = findViewById(R.id.validation_typepai);
        validation_prix = findViewById(R.id.validation_price);


        /////////////////////////////livraison
        methode();
        livraison_username.setText(user.getPrenom()+" "+user.getNom());
        livraison_adresse.setText(user.getAdresse().getAdresse());
        livraison_infosup.setText(user.getAdresse().getInfoSupp());
        livraison_wilaya.setText(user.getAdresse().getWilaya());
        livraison_phone.setText(user.getPhone());

        radioButton_domicile.setChecked(true);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, boutiques);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nosBoutiques.setAdapter(adapter);
        nosBoutiques.setEnabled(false);
        prixx = Things.price+prixLiv;

        radioButton_domicile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nosBoutiques.setEnabled(false);
                prixLiv = 1200;
                type_livraison = "A domicile";
                validation_livraison.setText(type_livraison);
                livraison_prixLivraison.setText((String.valueOf(prixLiv)+"Da"));
                livraison_prixtotal.setText(String.valueOf(Things.price+prixLiv)+"Da");
                validation_prix.setText(String.valueOf(Things.price+prixLiv)+"Da");
                prixx = Things.price+prixLiv;

                adresse = user.getAdresse();

            }
        });


        radioButton_boutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nosBoutiques.setEnabled(true);
                prixLiv=800;
                type_livraison = "Boutique Officielle";
                validation_livraison.setText(type_livraison);
                livraison_prixLivraison.setText((String.valueOf(prixLiv)+"Da"));
                livraison_prixtotal.setText(String.valueOf(Things.price+prixLiv)+"Da");
                validation_prix.setText(String.valueOf(Things.price+prixLiv)+"Da");
                prixx = Things.price+prixLiv;




            }
        });

        modfier_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainValidation.this, MainAdresse.class));

            }
        });


        livraison_prix.setText(String.valueOf(Things.price)+"Da");
        livraison_prixLivraison.setText((String.valueOf(prixLiv)+"Da"));
        livraison_prixtotal.setText(String.valueOf(Things.price+prixLiv)+"Da");


        passer_aupaiement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type_livraison.equals("A domicile")){
                    if(adresse.getWilaya().equals("Aucune") || adresse.getAdresse().equals("Aucune")){
                        Toast.makeText(getApplicationContext(),"Votre Adresse a domicile n'est pas valide",Toast.LENGTH_LONG).show();
                    }else{
                        paiment.setVisibility(View.VISIBLE);
                        cardViewPaiement.setVisibility(View.VISIBLE);

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                scrollView.scrollTo(600,600);

                            }
                        },200);

                    }
                }else{
                    paiment.setVisibility(View.VISIBLE);
                    cardViewPaiement.setVisibility(View.VISIBLE);

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollView.scrollTo(600,600);

                        }
                    },200);
                }


            }
        });


        ///////////////////////////////////////Paiement


        radioButtonPaiement.setChecked(true);
        type_paiment = "Paiement à la livraison";
        passer_validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation.setVisibility(View.VISIBLE);
                cardViewValidation.setVisibility(View.VISIBLE);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        scrollView.scrollTo(900,900);

                    }
                },200);

            }
        });

     ////////////////////////////////Validation

        validation_username.setText(user.getPrenom()+" "+user.getNom());
        validation_livraison.setText(type_livraison);
        validation_paiement.setText(type_paiment);
        validation_prix.setText(String.valueOf(Things.price+prixLiv)+"Da");

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commande = new Commande(user.getID());
                commande.setPrix(prixx);
                commande.setType_de_livraison(type_livraison);
                commande.setType_de_payement(type_paiment);
                commande.setAdresse(adresse);
                 methodeX();
                 Things.Panier.clear();
                 Things.inPanier.clear();
                 Things.number=0;

                valider.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),"Votre Commande a été Valider avec Succes",Toast.LENGTH_SHORT).show();

                addCommande(commande);

                startActivity(new Intent(MainValidation.this,MainMenu.class));
                finish();

            }
        });




    }


    public void methode(){
        boutiques = new ArrayList<>();

        for(int i=0;i<Things.myBoutiques.size();i++){
            boutiques.add(Things.myBoutiques.get(i).getName_Boutique()+" - "+Things.myBoutiques.get(i).getAdresse().getWilaya());
        }

        if(boutiques.isEmpty()){
            radioButton_boutique.setEnabled(false);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        boutique_name = parent.getItemAtPosition(position).toString();
        methodeY(adresse,boutique_name);



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void methodeX(){
        int k=0;
        ArrayList<ProduitCommande> produit_dans_commande = new ArrayList<>();
        ProduitCommande object;

        for(k=0;k<Things.inPanier.size();k++){
            object = new ProduitCommande();
            object.setProduit_ID(Things.inPanier.get(k).getProduct().getID());
            object.setProdui_qt(Things.inPanier.get(k).getQuantit());

            produit_dans_commande.add(object);
        }

        commande.setProduits(produit_dans_commande);

    }

    public void addCommande(Commande commande){


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        commande.setDate_validation(formatter.format((date)));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Commande").child(commande.getID_commande()).setValue(commande);


        Things.refresh=true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        share = this.getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));

        livraison_adresse.setText(user.getAdresse().getAdresse());
        livraison_infosup.setText(user.getAdresse().getInfoSupp());
        livraison_wilaya.setText(user.getAdresse().getWilaya());
        adresse= user.getAdresse();

    }

    public void methodeY(Adresse adresse,String bt){

        adresse = new Adresse();

        for(int i =0; i< Things.myBoutiques.size();i++){
            if(Things.myBoutiques.get(i).getName_Boutique().equals(bt)){
                adresse = Things.myBoutiques.get(i).getAdresse();
                break;
            }
        }


    }





}