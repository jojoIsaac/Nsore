package com.appsol.login;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.appsol.apps.devotional.R;


/**
 * Created by bendev on 1/17/15.
 */

public class FragmentCountries extends ActionBarActivity implements SearchView.OnQueryTextListener {
     CountriesListAdapter dataAdapter;
    public View row;
     ListView listview;
    String temp[] = {"Gplus","Facebook","Instagram","Linkdin","Pintrest","Twitter","Snapchat","Skype"};
    String[] resourceList;
    private static String[] rlist;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getActivity().requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //       showActionBar();

        //View v = inflater.inflate(R.layout.countries,container,false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.countries);
        resourceList = this.getResources().getStringArray(R.array.CountryCodes);

        listview = (ListView) findViewById(R.id.listView);
        if (listview != null)
       dataAdapter = new CountriesListAdapter(this, resourceList);
       // listview.setAdapter(new ArrayAdapter < String >(getApplicationContext(),android.R.layout.simple_list_item_1,resourceList));
        listview.setAdapter(dataAdapter);
        //  ListView lv = listview.getListView();

         listview.setTextFilterEnabled(true);

        rlist = resourceList;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (row != null) {
              //      row.setBackgroundResource(R.color.hint_foreground_material_dark);
                }
                row = view;
           //     view.setBackgroundResource(R.color.abc_primary_text_material_light);

                String[] g = rlist[position].split(",");
                 String pngName = g[1].trim().toLowerCase();
                           Entry.countries_tv.setText(GetCountryZipCode(g[1]).trim()+" (+"+g[0]+")");
                Entry.code.setText("+"+g[0]);
                Entry.countries_iv.setImageResource(getResources().getIdentifier("drawable/" + pngName, null, getPackageName()));
                onBackPressed();
            }
        });


    }
    public static void updateList(String[] list){
       rlist = list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_countries, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);





        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("Nomad", "onQueryTextSubmit");
        return false;
    }

    public boolean onClose() {
        Log.i("Nomad", "onClose");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //dataAdapter.getFilter().filter(s);
      //  listview.setAdapter(new ArrayAdapter < String >(getApplicationContext(),android.R.layout.simple_list_item_1,resourceList));

     //  dataAdapter =  dataAdapter = new CountriesListAdapter(this, resourceList,0);
     //  listview.setAdapter(dataAdapter);

        Log.i("Nomad", "onQueryTextChange");
  if(!(s.length()>0)) {
      //listview.clearTextFilter();
    //  dataAdapter.getFilter().filter("");
  }
        if (TextUtils.isEmpty(s)) {
            //dataAdapter.getFilter().filter("");
            Log.i("Nomad", "onQueryTextChange Empty String");
            listview.clearTextFilter();
        } else {
            Log.i("Nomad", "onQueryTextChange " + s.toString());
            //dataAdapter.getFilter().filter(s);
 /*           String[] list;
            list = new String[resourceList.length];
            for(int i = 0 ; i< resourceList.length; i++) {
                String[] g = resourceList[i].split(",");
                 list[i] =GetCountryZipCode(g[1]).trim();
            }
            */
            //listview.setFilterText(s.toString());
            dataAdapter.getFilter().filter(s);
        }



        return true;
    }

    private String GetCountryZipCode(String ssid){
        Locale loc = new Locale("", ssid);

        return loc.getDisplayCountry().trim();
    }
}