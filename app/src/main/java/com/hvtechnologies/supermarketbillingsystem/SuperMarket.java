package com.hvtechnologies.supermarketbillingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SuperMarket extends AppCompatActivity {


    Button add , del , view , LogOut , orders ;
    private DatabaseReference ClassRef , ClassRef2 ;

    FirebaseAuth mAuth ;
    String MarketName , MarketId ;

    TextView txt ;

    ArrayList<ItemsClass> mList = new ArrayList<>();
    ItemsSpinnerAdapter adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market);

        add = (Button)findViewById(R.id.AddItem);
        del = (Button)findViewById(R.id.DelItem);
        view = (Button)findViewById(R.id.ViewItem);
        orders = (Button)findViewById(R.id.Orders);

        LogOut = (Button)findViewById(R.id.LogOut);
        txt = (TextView)findViewById(R.id.textView5);

        mAuth = FirebaseAuth.getInstance();

        adapter = new ItemsSpinnerAdapter(getApplicationContext() , mList);

        String Uid = mAuth.getCurrentUser().getUid();
        ClassRef = FirebaseDatabase.getInstance().getReference("Items/") ;
        ClassRef2 = FirebaseDatabase.getInstance().getReference("Users/Supermarket/" + Uid ) ;

        ClassRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MarketName = dataSnapshot.child("Name").getValue(String.class);
                MarketId = dataSnapshot.child("Id").getValue(String.class);
                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPrefs.edit();
                edit.putString("ID" , MarketId );
                edit.putString("NAME" , MarketName );
                edit.apply();

                txt.setText(MarketName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ClassRef = FirebaseDatabase.getInstance().getReference("Items/") ;
        ClassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                mList.clear();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String MId = dataSnapshot1.child("Market Id").getValue().toString();
                        if(MId.equals(MarketId)){


                            String Id = dataSnapshot1.getKey();
                            String Name = dataSnapshot1.child("Name").getValue().toString();
                            String Qty = dataSnapshot1.child("Price").getValue().toString();
                            String BaseName = dataSnapshot1.child("Base Name").getValue().toString();

                            mList.add(new ItemsClass(Name , Id , Qty , BaseName ,"" ));

                        }




                    }

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(SuperMarket.this, SuperMarketViewOrders.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);



            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SuperMarket.this , "Sign Out" , Toast.LENGTH_SHORT ).show();

                mAuth.signOut();
                Intent mainIntent = new Intent(SuperMarket.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);



            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SuperMarket.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_item, null);
                final EditText Name = (EditText) mView.findViewById(R.id.editTextItemName);
                final EditText Qty = (EditText) mView.findViewById(R.id.editTextItemQty);
                final EditText Base = (EditText) mView.findViewById(R.id.editTextBaseUnit);


                mBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if( !Name.getText().toString().isEmpty() && !Qty.getText().toString().isEmpty()){

                            Date date = new Date();  // to get the date
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                            final String Id = df.format(date.getTime());

                            ClassRef = FirebaseDatabase.getInstance().getReference("Items/" + Id + "/" ) ;
                            HashMap<String,String> dataMap = new HashMap<String, String>();
                            dataMap.put("Price" , Qty.getText().toString().trim() );
                            dataMap.put("Name" , Name.getText().toString().trim().toUpperCase() );
                            dataMap.put("Base Name" , Base.getText().toString().trim() );
                            dataMap.put("Market Name" , MarketName );
                            dataMap.put("Market Id" , MarketId );
                            ClassRef.setValue(dataMap);

                            Toast.makeText( SuperMarket.this , "Item : " + Name.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();


                        }else{

                            Toast.makeText( SuperMarket.this , "No Item Added" , Toast.LENGTH_SHORT).show();

                        }


                    }
                });


                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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



        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SuperMarket.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_item, null);
                final TextView BoxName = (TextView) mView.findViewById(R.id.Box_Name);
                final TextView Info = (TextView) mView.findViewById(R.id.textView7);
                final Spinner Sp = (Spinner) mView.findViewById(R.id.SpinnerItems);

                BoxName.setText("Delete Items");
                Sp.setAdapter(adapter);

                Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String Name = mList.get(position).getName();
                        String Q = mList.get(position).getQty();
                        String B = mList.get(position).getMarketName();

                        Info.setText("Name : " + Name + "\nPrice : " + Q + "/" + B);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        int ps = Sp.getSelectedItemPosition();
                        String Id = mList.get(ps).getId();
                        ClassRef = FirebaseDatabase.getInstance().getReference("Items/" + Id + "/" ) ;
                        ClassRef.removeValue();


                    }
                });


                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SuperMarket.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_item, null);
                final TextView BoxName = (TextView) mView.findViewById(R.id.Box_Name);
                final TextView Info = (TextView) mView.findViewById(R.id.textView7);
                final Spinner Sp = (Spinner) mView.findViewById(R.id.SpinnerItems);


                BoxName.setText("View Items");
                Sp.setAdapter(adapter);

                Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String Name = mList.get(position).getName();
                        String Q = mList.get(position).getQty();
                        String B = mList.get(position).getMarketName();

                        Info.setText("Name : " + Name + "\nPrice : " + Q + "/" + B);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                mBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
