package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Cart extends AppCompatActivity {

    ListView CartList;
    Button Place ;

    private ArrayList<ItemsClass> ItemsNames = new ArrayList<>() ;
    ItemsListAdapter adap ;
    DatabaseReference CartRef , OrderRef , UserRef ;

    String Name , Email , Num , Address ;
    Long OrderNum ;

    Boolean did  = false ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        CartList = (ListView)findViewById(R.id.ItemList);
        Place = (Button)findViewById(R.id.PlaceORDER);
        adap = new ItemsListAdapter(getApplicationContext()  , ItemsNames);
        CartList.setAdapter(adap);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("Uid" , "") ;

        UserRef = FirebaseDatabase.getInstance().getReference("Users/Use/"  + Uid + "/" ) ;
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Name = dataSnapshot.child("Name").getValue().toString();
                    Email = dataSnapshot.child("Email").getValue().toString();
                    Num = dataSnapshot.child("Phone Number").getValue().toString();
                    Address = dataSnapshot.child("Address").getValue().toString();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        CartRef = FirebaseDatabase.getInstance().getReference("Cart/"  + Uid + "/" ) ;
        CartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ItemsNames.clear();
                adap.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String MName = dataSnapshot1.child("Market Name").getValue().toString();
                        String MId = dataSnapshot1.child("Market Id").getValue().toString();
                        String Id = dataSnapshot1.getKey();
                        String Price = dataSnapshot1.child("Price").getValue().toString();
                        String Qty = dataSnapshot1.child("Qty").getValue().toString();

                        double a = Double.parseDouble(Price);
                        double b = Double.parseDouble(Qty);
                        double c = a*b ;

                        ItemsNames.add(new ItemsClass(Name , Id ,Double.toString(c) , MName , MId  ));
                        adap.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        OrderRef = FirebaseDatabase.getInstance().getReference("Order/"  + Uid + "/") ;
        OrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Long a = dataSnapshot.getChildrenCount();
                OrderNum = a ;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date date = new Date();  // to get the date
                SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                final String formattedDate = da.format(date.getTime());




                for (int i = 0 ; i < ItemsNames.size() ; i++){

                    OrderNum++ ;
                    String Name2 = ItemsNames.get(i).getName();
                    String MName = ItemsNames.get(i).getMarketName();
                    String Id = ItemsNames.get(i).getId();
                    String MId = ItemsNames.get(i).getMarketId();
                    String Price = ItemsNames.get(i).getQty();


                    OrderRef = FirebaseDatabase.getInstance().getReference("Order/"  + MId + "/" + formattedDate + "/Items/" + Id) ;
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("Name", Name2);
                    dataMap.put("Price", Price);
                    dataMap.put("User Name", Name);
                    dataMap.put("Email", Email);
                    dataMap.put("Num", Num);
                    dataMap.put("Address", Address);
                    OrderRef.setValue(dataMap);

                    OrderRef = FirebaseDatabase.getInstance().getReference("Order/"  + Uid + "/" + formattedDate + "/Items/" + Id) ;
                    HashMap<String, String> dataMap2 = new HashMap<String, String>();
                    dataMap2.put("Name", Name2);
                    dataMap2.put("Price", Price);
                    dataMap2.put("MName", MName);
                    dataMap2.put("MId", MId);
                    OrderRef.setValue(dataMap2);

                }



                CartRef = FirebaseDatabase.getInstance().getReference("Cart/"  + Uid + "/" );
                CartRef.removeValue();


                Toast.makeText(Cart.this  , "Order Placed" , Toast.LENGTH_SHORT).show();



            }
        });




    }
}
