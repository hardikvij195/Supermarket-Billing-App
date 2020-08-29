package com.hvtechnologies.supermarketbillingsystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemsListAdap2 extends BaseAdapter {




    private Context mcontext ;
    private List<ItemsClass> mList ;


    public ItemsListAdap2(Context mcontext, List<ItemsClass> mList) {

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


        View v = View.inflate(mcontext , R.layout.layout4 , null);

        TextView MName = (TextView)v.findViewById(R.id.textView18);

        MName.setText("Buyer Details\nName : " + mList.get(position).getName() + "\nNumber : " + mList.get(position).getId() +
                "\nEmail : " + mList.get(position).getMarketName() + "\nAddress : "+ mList.get(position).getMarketId()+ "\nPrice : " + mList.get(position).getQty());


        return v;

    }
}

