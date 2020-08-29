package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class SuperMarket_View_Items extends AppCompatActivity {


    ListView OrdList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market__view__items);


        OrdList = (ListView)findViewById(R.id.ItemList);




    }
}
