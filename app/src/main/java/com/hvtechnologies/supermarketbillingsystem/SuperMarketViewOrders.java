package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperMarketViewOrders extends AppCompatActivity {


    ListView OrdList ;
    private ArrayList<String> IdNames = new ArrayList<>() ;

    StringAdapter adapter ;
    DatabaseReference CartRef , OrderRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_view_orders);


        OrdList = (ListView)findViewById(R.id.ItemList);

        adapter = new StringAdapter(getApplicationContext() , IdNames);
        OrdList.setAdapter(adapter);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Id = sharedPrefs.getString("ID" , "") ;

        OrderRef = FirebaseDatabase.getInstance().getReference("Order/" + Id + "/") ;
        OrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                IdNames.clear();
                adapter.notifyDataSetChanged();



                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String OId = dataSnapshot1.getKey();
                        IdNames.add(OId);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        OrdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String Oid = IdNames.get(position);
                Intent mainIntent = new Intent(SuperMarketViewOrders.this, SuperMarketViewParticularOrders.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra("Id" , Oid );
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);







            }
        });













    }
}
