package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Admin_Activity extends AppCompatActivity {

    Button Add , Logout;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);


        Utils.getDatabase();

        Add = (Button)findViewById(R.id.SupBtn);

        Logout = (Button)findViewById(R.id.LogOut);
        mAuth = FirebaseAuth.getInstance();


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Admin_Activity.this , "Sign Out" , Toast.LENGTH_SHORT ).show();

                mAuth.signOut();
                Intent mainIntent = new Intent(Admin_Activity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);



            }
        });


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(Admin_Activity.this, Admin_Add_Supermarket.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);


            }
        });

    }
}
