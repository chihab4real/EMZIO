package com.example.emzio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPasswordForget extends AppCompatActivity {

    Button next1,next2,nextx;
    CardView passwords;
    EditText mail,oldPassword,newPassword,phone;
    TextInputLayout textInputLayout;

    TextView errorx,error1,error2,errorphone;
    String password;

    ArrayList<User> myArray = new ArrayList<>();

    DatabaseReference mDatabase,mDatabase1;
    String id;
    static  boolean x =false;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_password_forget);

        next1 = findViewById(R.id.next1);
        next2 = findViewById(R.id.next2);

        passwords = findViewById(R.id.passwords);

        mail = findViewById(R.id.change_mail);
        oldPassword = findViewById(R.id.pass_old);
        newPassword = findViewById(R.id.pass_new);

        errorx = findViewById(R.id.errorx);
        error1 = findViewById(R.id.error1);
        error2 = findViewById(R.id.error2);

        nextx = findViewById(R.id.nextx);
        phone = findViewById(R.id.change_phone);
        errorphone = findViewById(R.id.errorphone);
        textInputLayout = findViewById(R.id.phone_input);

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwords.setVisibility(View.GONE);
                errorx.setVisibility(View.GONE);
                textInputLayout.setVisibility(View.GONE);
                nextx.setVisibility(View.GONE);
                errorphone.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidMail(mail.getText().toString())){
                    ifItexisite(mail.getText().toString());


                }else{
                    errorx.setText("E-Mail inValide");
                    errorx.setVisibility(View.VISIBLE);
                }

            }
        });


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorphone.setVisibility(View.VISIBLE);
                passwords.setVisibility(View.GONE);
                if(isValidNumberPhone(phone.getText().toString())){
                    errorphone.setText("Phone  Valide");
                    errorphone.setTextColor(Color.rgb(76,175,80));
                }else{
                    errorphone.setText("Phone invalide");
                    errorphone.setTextColor(Color.rgb(255,0,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidNumberPhone(phone.getText().toString())){
                    if(myArray.get(0).getPhone().equals(phone.getText().toString())){

                        passwords.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                error1.setVisibility(View.VISIBLE);
                if(isValidPassword(oldPassword.getText().toString())){
                    error1.setText("Password Valide");
                    error1.setTextColor(Color.rgb(76,175,80));
                }else{
                    error1.setText("Password invalide");
                    error1.setTextColor(Color.rgb(255,0,0));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                error2.setVisibility(View.VISIBLE);
                if(newPassword.getText().toString().equals(oldPassword.getText().toString())){
                    error2.setText("Password Valide");
                    error2.setTextColor(Color.rgb(76,175,80));

                }else{
                    error2.setText("Password invalide");
                    error2.setTextColor(Color.rgb(255,0,0));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMail(mail.getText().toString()) && isValidPassword(oldPassword.getText().toString()) && newPassword.getText().toString().equals(oldPassword.getText().toString()) &&
                        isValidNumberPhone(phone.getText().toString())
                ){

                    ChangeIt(mail.getText().toString(),newPassword.getText().toString());
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainPasswordForget.this,MainLogin.class));
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Verifier Vos Inforamtions",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean isValidMail(String mail){
        String regex ="([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";

        Pattern p = Pattern.compile(regex);

        if(mail==null){
            return  false;
        }
        Matcher m = p.matcher(mail);
        return m.matches();
    }


    public boolean ifItexisite(final String mail){



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if(user.getEmail().equals(mail)){

                        id = user.getID();
                        myArray.add(user);

                        errorx.setText("Mail Valide");
                        errorx.setTextColor(Color.rgb(76,175,80));
                        Login(mail,user.getPassword());
                        textInputLayout.setVisibility(View.VISIBLE);
                        nextx.setVisibility(View.VISIBLE);
                        //passwords.setVisibility(View.VISIBLE);


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return x;
    }

    public  void Login(String mail,String password){

        final String TAG = "SIGN IN";

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            currentUser = mAuth.getCurrentUser();

                            //Toast.makeText(getApplicationContext(),user1.getUid(),Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                           // Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();

                        }

                        // ...

                    }
                });

    }

    public boolean isValidPassword(String password) {


        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[_+.])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);

        return m.matches();
    }

    public void ChangeIt(String mail,String password){

        mDatabase1 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase1.child(myArray.get(0).getID()).child("password").setValue(password);


        currentUser.updatePassword(password);


        Toast.makeText(getApplicationContext(),"Mot Pass Changé avec Succes",Toast.LENGTH_SHORT).show();
    }

    public boolean isValidNumberPhone(String phone){
        String regex ="^(0)([0-9]{9})$";

        Pattern p = Pattern.compile(regex);

        if(phone==null){
            return  false;
        }
        Matcher m = p.matcher(phone);
        return m.matches();

    }

}