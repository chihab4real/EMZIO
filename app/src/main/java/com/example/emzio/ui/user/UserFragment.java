package com.example.emzio.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emzio.Adresse;
import com.example.emzio.MainAdd;
import com.example.emzio.MainLogin;
import com.example.emzio.MainMenu;
import com.example.emzio.MainPanier;
import com.example.emzio.ProduitHistory;
import com.example.emzio.R;
import com.example.emzio.Things;
import com.example.emzio.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {

    private UserViewModel notificationsViewModel;
    User user;
    ImageView panier;
    SharedPreferences share;
    LinearLayout logout;
    FloatingActionButton add;

    ArrayList <String> option = new ArrayList<>();
    RecyclerView moncom;
    TextView titre;
    LinearLayout details,carnet_adresse, mes_commandes,article_aime,rech_rec, myProd,boutique,monBoutique;

    CardView cardViewNumber;
    CardView cardViewPro;
    TextView numberp;
    boolean demande;
    boolean isboutique;

    DatabaseReference mDatabase2;
    CardView cardView;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        notificationsViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.user_acti, container, false);
        final TextView id,nom,prenom,sexe,email,password,phone,pro;
        cardView = root.findViewById(R.id.point_adresse);

        cardViewPro = root.findViewById(R.id.monpro);
        cardViewNumber = root.findViewById(R.id.card_number);
        numberp = root.findViewById(R.id.number_panier);
        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }



        logout = root.findViewById(R.id.logout);
        details = root.findViewById(R.id.details);
        carnet_adresse = root.findViewById(R.id.carnet_adresse);
        mes_commandes = root.findViewById(R.id.mes_commandes);
        article_aime = root.findViewById(R.id.article_aime);
        rech_rec = root.findViewById(R.id.rechercher_recement);
        myProd = root.findViewById(R.id.my_stuff);
        boutique=root.findViewById(R.id.add_bouti);
        monBoutique= root.findViewById(R.id.monboutique);


        email = root.findViewById(R.id.useremail);

        share = this.getActivity().getSharedPreferences("MyREF",MODE_PRIVATE);
        user = new User(share.getString("Nom",null),share.getString("Prenom",null),share.getString("Sexe",null),share.getString("Email",null)
                ,share.getString("Password",null),share.getString("Phone",null),share.getBoolean("Pro",false));
        user.setID(share.getString("ID",null));
        user.setAdresse(new Adresse(share.getString("Adresse_adresse",null),share.getString("Adresse_wilaya",null),share.getString("Adresse_info",null)));
        demande = share.getBoolean("DemandeEnvoyer",false);
        email.setText("Bonjour,\n"+user.getPrenom());



        isboutique = isBoutique(user.getID());

        if(isboutique==true){
            monBoutique.setVisibility(View.VISIBLE);
            boutique.setVisibility(View.GONE);
            myProd.setVisibility(View.GONE);
        }else{
            monBoutique.setVisibility(View.GONE);
            boutique.setVisibility(View.VISIBLE);
            myProd.setVisibility(View.VISIBLE);
        }

        add = root.findViewById(R.id.add);
        if(user.isPro()==false){
            add.setVisibility(View.GONE);
            cardViewPro.setVisibility(View.GONE);
        }

       /* if(demande == true){
            boutique.setVisibility(View.GONE);
        }*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAdd.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        panier =root.findViewById(R.id.panier_vector);

        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainPanier.class));
            }
        });



        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainDetails.class));
            }
        });


        carnet_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainAdresse.class));
            }
        });

        mes_commandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainMesCommandes.class));
            }
        });


        article_aime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainLikes.class));
            }
        });

        rech_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainRechrcheRec.class));
            }
        });

        myProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainMesProduits.class));

            }
        });

        boutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainAddBoutique.class));
            }
        });


        monBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainMonBoutique.class));
            }
        });
        return root;
    }


    void Logout(){

        SharedPreferences.Editor editor = share.edit();
        editor.clear();
        editor.commit();
        editor.apply();
        Toast.makeText(UserFragment.this.getContext(),"Deconnexion avec Succes",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserFragment.this.getContext(), MainLogin.class));
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();

    }

    public void onStart() {
        super.onStart();


        if(Things.number==0){
            cardViewNumber.setVisibility(View.GONE);
        }else{
            cardViewNumber.setVisibility(View.VISIBLE);
            numberp.setText(String.valueOf(Things.number));

        }

        if(share.getString("Adresse_wilaya","Aucun").equals("Aucune")){
            cardView.setVisibility(View.VISIBLE);
        }else{
            cardView.setVisibility(View.GONE);
        }

    }

    public boolean isBoutique(String id){

        for(int i=0;i<Things.myBoutiques.size();i++){
            if(Things.myBoutiques.get(i).getID_chef().equals(id)){
                return true;
            }
        }

        return  false;
    }




}
