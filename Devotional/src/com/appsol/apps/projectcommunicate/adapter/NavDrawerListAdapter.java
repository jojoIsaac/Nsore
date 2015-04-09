package com.appsol.apps.projectcommunicate.adapter;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.model.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public NavDrawerItem getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		NavDrawerItem item= navDrawerItems.get(position);
		 LayoutInflater mInflater = (LayoutInflater)
                 context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(!item.isSection())
		{
		
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
      
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        try
        {
        imgIcon.setImageResource(item.getIcon()); 
        txtTitle.setText(item.getTitle());
        if(item.getCounterVisibility()){
        	txtCount.setText(item.getCount());
        }else{
        	// hide the counter view
        	txtCount.setVisibility(View.GONE);
        }
        }
        catch(Exception e)
        {
        	
        }
       
        
        // displaying count
        // check whether it set visible or not
       
		}
		else
		{
			
	           
	            convertView = mInflater.inflate(R.layout.list_item_section, null);
	            
	      
	            convertView.setOnClickListener(null);
	            convertView.setOnLongClickListener(null);
	            convertView.setLongClickable(false);
			
			final TextView sectionView = (TextView) convertView.findViewById(R.id.list_item_section_text);
			sectionView.setText(item.getTitle());
			
		}
        
        return convertView;
	}

}
