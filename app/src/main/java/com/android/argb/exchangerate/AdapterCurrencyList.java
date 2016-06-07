package com.android.argb.exchangerate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class AdapterCurrencyList extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Currency> data;

    public AdapterCurrencyList(Context context, List<Currency> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_rate, null);

        vi.setVisibility(View.VISIBLE);
        TextView value = (TextView) vi.findViewById(R.id.rowCurrencyValue);
        value.setText(String.format("%s", data.get(position).getValue()));

        TextView rowCurrencyName = (TextView) vi.findViewById(R.id.rowCurrencyName);
        rowCurrencyName.setText(String.format("%s", data.get(position).getCurrencyName()));

        TextView rowCurrencyCode = (TextView) vi.findViewById(R.id.rowCurrencyCode);
        rowCurrencyCode.setText(String.format("%s", data.get(position).getCurrencyCode()));
        return vi;
    }
}