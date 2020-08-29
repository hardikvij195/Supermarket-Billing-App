package com.hvtechnologies.supermarketbillingsystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemsSpinnerAdapter extends BaseAdapter {


    private Context mcontext ;
    private List<ItemsClass> mList ;


    public ItemsSpinnerAdapter(Context mcontext, List<ItemsClass> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View v = View.inflate(mcontext , R.layout.spinnernamesadapter, null);
        TextView Name = (TextView)v.findViewById(R.id.textView19);
        Name.setText(mList.get(i).getName());
        return v;




    }
}
