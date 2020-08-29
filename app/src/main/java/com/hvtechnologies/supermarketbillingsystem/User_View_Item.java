package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import static java.lang.Compiler.disable;

public class User_View_Item extends AppCompatActivity {


    ListView OrdList;
    Button Cart ;


    private ArrayList<ItemsClass> ItemsNames = new ArrayList<>() ;

    ItemsListAdapter adap ;
    DatabaseReference CartRef , ItemRef ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__view__item);


        OrdList = (ListView)findViewById(R.id.ItemList);
        Cart = (Button)findViewById(R.id.GotoCart);
        adap = new ItemsListAdapter(getApplicationContext() , ItemsNames);
        OrdList.setAdapter(adap);


        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("Uid" , "") ;

        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent mainIntent = new Intent(User_View_Item.this, Cart.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);





            }
        });


        ItemRef = FirebaseDatabase.getInstance().getReference("Items/" ) ;
        ItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ItemsNames.clear();
                adap.notifyDataSetChanged();

                if(dataSnapshot.exists()){


                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){


                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String MName = dataSnapshot1.child("Market Name").getValue().toString();
                        String MId = dataSnapshot1.child("Market Id").getValue().toString();
                        String Id = dataSnapshot1.getKey();
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



        OrdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String Id = ItemsNames.get(position).getId();
                final String MId = ItemsNames.get(position).getMarketId();
                final String PN = ItemsNames.get(position).getName();
                final String Mn = ItemsNames.get(position).getMarketName();
                final String Price = ItemsNames.get(position).getQty();


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(User_View_Item.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_to_cart, null);

                final EditText Qt = mView.findViewById(R.id.editText4);
                mBuilder.setTitle("Add This Item To Cart ?");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(!Qt.getText().toString().isEmpty()){


                            CartRef = FirebaseDatabase.getInstance().getReference("Cart/"  + Uid + "/" + Id) ;
                            HashMap<String,String> dataMap1 = new HashMap<String, String>();
                            dataMap1.put("Name" , PN);
                            dataMap1.put("Market Name" , Mn);
                            dataMap1.put("Market Id" ,MId );
                            dataMap1.put("Price" ,Price );
                            dataMap1.put("Qty" ,Qt.getText().toString().trim() );
                            CartRef.setValue(dataMap1);
                            dialog.dismiss();

                            Toast.makeText(User_View_Item.this  , "Item Added" , Toast.LENGTH_SHORT).show();


                        }else {

                            Toast.makeText(User_View_Item.this  , "Enter Quantity" , Toast.LENGTH_SHORT).show();


                        }


                    }
                });
                mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();








            }
        });






    }
}
