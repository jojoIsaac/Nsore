package com.appsol.apps.projectcommunicate.adapter;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.model.Church;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;
/**
 * Arrayadapter (for Android) with text filtering for the use with a TextWatcher.
 * Note: the objects in the List need a valid toString() method.
 * @author Tobias Schürg
 * 
 */


public class FilteredArrayAdapter extends ArrayAdapter<Church> {

	private List<Church> objects;
	private Context context;
	private Filter filter;
	private LayoutInflater inflator;

	public FilteredArrayAdapter(Activity context, int resourceId,	List<Church> objects) {
		super(context, resourceId, objects);
		this.context = context;
		this.objects = objects;   inflator =  context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Church getItem(int position) {
		return objects.get(position);
	}

	

	   static class ViewHolder {
	        protected TextView text;
	        protected CheckBox checkbox;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = null;
	        
	        Church m = getItem(position);
	        ViewHolder viewHolder = null;
	        if (convertView == null) {
	            
	            view = inflator.inflate(R.layout.list_item, null);
	            viewHolder = new ViewHolder();
	            viewHolder.text = (TextView) view.findViewById(R.id.product_name);
	            //viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
	            view.setTag(viewHolder);            
	        } else {
	            view = convertView;
	            viewHolder = ((ViewHolder) view.getTag());
	        }
	        viewHolder.text.setText(m.getchurchname());
	        //viewHolder.checkbox.setChecked(m.isSelected());

	        return view;
	    }
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter<Church>(objects);
		return filter;
	}

	/**
	 * Class for filtering in Arraylist listview. Objects need a valid
	 * 'toString()' method.
	 * 
	 * @author Tobias Schürg inspired by Alxandr
	 *         (http://stackoverflow.com/a/2726348/570168)
	 * 
	 */
	private class AppFilter<T> extends Filter {

		private ArrayList<T> sourceObjects;

		public AppFilter(List<T> objects) {
			sourceObjects = new ArrayList<T>();
			synchronized (this) {
				sourceObjects.addAll(objects);
			}
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			String filterSeq = chars.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (filterSeq != null && filterSeq.length() > 0) {
				ArrayList<T> filter = new ArrayList<T>();

				for (T object : sourceObjects) {
					// the filtering itself:
					if (object.toString().toLowerCase().contains(filterSeq))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				// add all objects
				synchronized (this) {
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<T> filtered = (ArrayList<T>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((Church) filtered.get(i));
			notifyDataSetInvalidated();
		}
	}

}