package com.appsol.login;

/**
 * Created by bendev on 1/15/15.
 */
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.appsol.apps.devotional.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class CountriesListAdapter extends BaseAdapter implements Filterable{
    private final Context context;
    private   String[] values;
    private   String[] newvalues;
    private  List<String> data;
    private final ArrayList<String> origData;
    int m=0;


    ValueFilter valueFilter;



    public CountriesListAdapter(Context context, String[] values) {
       // super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
        this.newvalues = values;
         m = 0;
        this.data = Arrays.asList(values);
        this.origData = new ArrayList<String>(this.data);

    }


    @Override
    public int getCount() {
        return newvalues.length;
    }

    @Override
    public Object getItem(int position) {
        return newvalues[position];
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView =  inflater.inflate(R.layout.country_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.txtViewCountryName);
       ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewFlag);
        String[] g;
        g = newvalues[position].split(",");
        textView.setText(GetCountryZipCode(g[1]).trim());
         String pngName = g[1].trim().toLowerCase();
         imageView.setImageResource(context.getResources().getIdentifier("drawable/" + pngName, null, context.getPackageName()));

         return rowView;
    }

    private String GetCountryZipCode(String ssid){
        Locale loc = new Locale("", ssid);

        return loc.getDisplayCountry().trim();
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String[] list;
            list = new String[values.length];
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> cb = new ArrayList<String>();

               int j =0 ;
         //       String[] filterc = values;


                for(int i = 0 ; i< values.length; i++) {
                    String[] g = values[i].split(",");
                    list[i] =GetCountryZipCode(g[1]).trim();
                                  }
                for(int i = 0; i < values.length; i++){
                    if(list[i].toUpperCase().contains(constraint.toString().toUpperCase())){
                        //filterc[j] = values[i];
                       cb.add(values[i]);
                        j++;
                    }
                }
                //String[] filterc = new String[cb.size()];
                String[] filterc = cb.toArray(new String[cb.size()]);
                //filterc = cb.toArray(filterc);


                results.count = cb.size();
                results.values = filterc;
                FragmentCountries.updateList(filterc);
            } else {
                results.count = list.length;
                results.values = values;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            newvalues = (String[]) results.values;
            notifyDataSetChanged();
        }

    }








}