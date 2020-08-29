package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperMarketViewParticularOrders extends AppCompatActivity {


    ListView OrdList;

    private ArrayList<ItemsClass> ItemsNames = new ArrayList<>() ;

    ItemsListAdap2 adap ;
    DatabaseReference ItemRef ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_view_particular_orders);





        Intent intent = getIntent();
        final String OId = intent.getExtras().getString("Id" , "");


        OrdList = (ListView)findViewById(R.id.ItemList);
        adap = new ItemsListAdap2(getApplicationContext() , ItemsNames);
        OrdList.setAdapter(adap);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("ID" , "") ;


        ItemRef = FirebaseDatabase.getInstance().getReference("Order/"+ Uid + "/" + OId+ "/Items/" ) ;
        ItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ItemsNames.clear();
                adap.notifyDataSetChanged();

                if(dataSnapshot.exists()){


                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){


                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String MName = dataSnapshot1.child("Email").getValue().toString();
                        String MId = dataSnapshot1.child("Address").getValue().toString();
                        String Id = dataSnapshot1.child("Num").getValue().toString();
                        String Price = dataSnapshot1.child("Price").getValue().toString();

                        ItemsNames.add(new ItemsClass(Name , Id , Price , MName , MId  ));
                        adap.notifyDataSetChanged();





                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }
}
