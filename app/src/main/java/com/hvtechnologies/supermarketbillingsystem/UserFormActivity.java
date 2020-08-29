package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserFormActivity extends AppCompatActivity {


    EditText Name , Email , Number , Address ;
    Button Done ;
    private DatabaseReference ClassRef , UserRef ;

    Boolean Ex = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);


        Done  = (Button)findViewById(R.id.Done);
        Name = (EditText)findViewById(R.id.NameTxt);
        Email = (EditText)findViewById(R.id.EmailTxt);
        Number = (EditText)findViewById(R.id.PhoneTxt);
        Address = (EditText)findViewById(R.id.Address);


        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("Uid" , "");

        ClassRef = FirebaseDatabase.getInstance().getReference("Users/Use/"+ Uid+ "/Address/") ;
        ClassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){

                    Ex = true;
                    UserRef = FirebaseDatabase.getInstance().getReference("Users/Use/"+ Uid+ "/") ;
                    UserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String NameT = dataSnapshot.child("Name").getValue().toString();
                            String NumT = dataSnapshot.child("Phone Number").getValue().toString();
                            String EmailT = dataSnapshot.child("Email").getValue().toString();
                            String AddT = dataSnapshot.child("Address").getValue().toString();


                            Name.setText(NameT);
                            Email.setText(EmailT);
                            Number.setText(NumT);
                            Address.setText(AddT);


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }else {

                    Ex = false;


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!Ex){


                    if(!Name.getText().toString().isEmpty() &&!Address.getText().toString().isEmpty()
                            &&!Number.getText().toString().isEmpty() &&!Email.getText().toString().isEmpty() ){


                        ClassRef = FirebaseDatabase.getInstance().getReference("Users/Use/"+ Uid+ "/") ;
                        HashMap<String,String> dataMap2 = new HashMap<String, String>();
                        dataMap2.put("Phone Number" , Number.getText().toString());
                        dataMap2.put("Uid" , Uid);
                        dataMap2.put("Name" , Name.getText().toString());
                        dataMap2.put("Address" , Address.getText().toString());
                        dataMap2.put("Email" , Email.getText().toString());
                        ClassRef.setValue(dataMap2);

                        Intent m2 = new Intent(UserFormActivity.this , Users.class);
                        startActivity(m2);


                    }else {


                        Toast.makeText(UserFormActivity.this , "Fill All Details" , Toast.LENGTH_SHORT).show();


                    }

                }else {


                    Intent m2 = new Intent(UserFormActivity.this , Users.class);
                    startActivity(m2);




                }







            }
        });


    }
}
