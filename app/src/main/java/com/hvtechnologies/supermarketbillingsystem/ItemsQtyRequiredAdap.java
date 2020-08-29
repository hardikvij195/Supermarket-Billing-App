package com.hvtechnologies.supermarketbillingsystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ItemsQtyRequiredAdap  extends BaseAdapter {


    private Context mcontext ;
    private List<ItemsQtyRequired> mList ;

    public ItemsQtyRequiredAdap(Context mcontext, List<ItemsQtyRequired> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = View.inflate(mcontext , R.layout.itemsorderlayout , null);

        TextView PName = (TextView)v.findViewById(R.id.textView21);
        TextView MName = (TextView)v.findViewById(R.id.textView18);
        TextView Price = (TextView)v.findViewById(R.id.textView22);
        EditText Qty = (EditText)v.findViewById(R.id.editText3);


        PName.setText("Product Name : " + mList.get(position).getName());
        MName.setText("Market Name : " + mList.get(position).getMarketName());
        Price.setText("Price : " + mList.get(position).getPrice());


        return v;

    }
}

