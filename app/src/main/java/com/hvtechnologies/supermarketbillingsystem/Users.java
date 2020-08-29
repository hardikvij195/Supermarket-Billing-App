package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Users extends AppCompatActivity {


    TextView User;
    Button items , logout , orders , forms ;
    private FirebaseAuth mAuth;

    DatabaseReference UserRef  ;

    String Name , Email , Num , Address ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        items = (Button)findViewById(R.id.ViewItems);
        forms = (Button)findViewById(R.id.Form);
        logout = (Button)findViewById(R.id.LogOut);
        orders = (Button)findViewById(R.id.Orders);
        mAuth = FirebaseAuth.getInstance();

        User = (TextView)findViewById(R.id.textView6);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("Uid" , "") ;


        User.setText("Welcome, " + Name);

        UserRef = FirebaseDatabase.getInstance().getReference("Users/Use/"  + Uid + "/Name/" ) ;
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    UserRef = FirebaseDatabase.getInstance().getReference("Users/Use/"  + Uid + "/" ) ;

                    UserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Name = dataSnapshot.child("Name").getValue().toString();
                            Email = dataSnapshot.child("Email").getValue().toString();
                            Num = dataSnapshot.child("Phone Number").getValue().toString();
                            Address = dataSnapshot.child("Address").getValue().toString();
                            User.setText("Welcome, " + Name);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }else {





                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(Users.this, User_View_Item.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });

        forms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(Users.this, UserFormActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Users.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                mAuth.signOut();
                Intent mainIntent = new Intent(Users.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntentOrrder = new Intent(Users.this, UserOrderHistory.class);
                startActivity(mainIntentOrrder);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });





    }
}
