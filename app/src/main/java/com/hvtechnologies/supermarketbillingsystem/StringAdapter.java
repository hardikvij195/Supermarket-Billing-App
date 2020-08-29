package com.hvtechnologies.supermarketbillingsystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class StringAdapter extends BaseAdapter {


    private Context mcontext ;
    private List<String> mList ;


    public StringAdapter(Context mcontext, List<String> mList) {
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


        View v = View.inflate(mcontext , R.layout.stringadaplayoutlist , null);

        TextView PName = (TextView)v.findViewById(R.id.textView9);
        PName.setText("Order Id : " + mList.get(position) );

        return v;

    }
}

