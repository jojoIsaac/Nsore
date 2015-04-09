package com.appsol.apps.projectcommunicate.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;

public class DevotionListAdapter extends ArrayAdapter<Devotion> {
Context Bcontext;
List<Devotion> mBookmarks;

	public DevotionListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		Bcontext=context;
	}

	public DevotionListAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		Bcontext=context;
		
		// TODO Auto-generated constructor stub
	}

	public DevotionListAdapter(Context context, int resource, Devotion[] objects) {
		super(context, resource, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public DevotionListAdapter(Context context, int resource, List<Devotion> objects) {
		super(context, resource, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Devotion>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}

	public DevotionListAdapter(Context context, int resource,
			int textViewResourceId, Devotion[] objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		ContentResolver cr = context.getContentResolver();
//		Intent calIntent = new Intent(Intent.ACTION_DELETE);
//		calIntent.setData(CalendarContract.Events.CONTENT_URI);
//		calIntent.putExtra(Events.ti, value)
//		startActivity(calIntent);
		//cr.delete(Reminders., where, selectionArgs)
		// TODO Auto-generated constructor stub
	}

	public DevotionListAdapter(Context context, int resource,
			int textViewResourceId, List<Devotion> objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Devotion>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}
	
	
	
	private static class ViewHolder {
		LinearLayout lvDate;
		TextView tvtitle;
		TextView txtDate;
		TextView tvc;
		TextView txtMemoryverse;
	}

	
	public List<Devotion> getBookmarksList()
	{
		return mBookmarks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = convertView;
		final ViewHolder holder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) Bcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.devotion_listitem, parent,false);
				
				
				holder = new ViewHolder();
				holder.lvDate =(LinearLayout) view.findViewById(R.id.lvDate);
				holder.tvtitle = (TextView) view.findViewById(R.id.txtTitle);;
				holder.txtDate= (TextView) view.findViewById(R.id.txtdate);
				holder.tvc=(TextView) view.findViewById(R.id.txtday);
				holder.txtMemoryverse =(TextView) view.findViewById(R.id.txtMemoryverse);
				//holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
		String todayDate= Connector.getServerTime();
	
		Devotion bmark= getItem(position);
		//LinearLayout lvDate =(LinearLayout) row.findViewById(R.id.lvDate);
		
	Log.e("VAL", bmark.getSummary());
		if(view!=null)
		{
			if(bmark!=null)
			{
				//TextView tvtitle = (TextView) row.findViewById(R.id.txtTitle);
				holder.tvtitle.setText(bmark.getTitle());
				//TextView txtDate= (TextView) row.findViewById(R.id.txtdate);
				holder.txtDate.setText(bmark.getNormalDevotionDate());
				//TextView tvc=(TextView) row.findViewById(R.id.txtday);
				holder.tvc.setText(bmark.getDevotionday());
				//TextView txtMemoryverse =(TextView) row.findViewById(R.id.txtMemoryverse);
				String verse = bmark.getVerse().replace(" ", "");
				
				if(bmark.isCurrent())
				{
					Log.e("CUUR", bmark.getLesseonid());
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.currentDevotion_marker));
				}
				else
				{
					Log.e("CUURS", bmark.getLesseonid());
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.normalDevColor));

				}
				if(verse.equalsIgnoreCase(""))
				{
					holder.txtMemoryverse.setVisibility(View.GONE);
				}
				Integer length=  bmark.getVerse().length();
				if(length > 160)
				{
					holder.txtMemoryverse.setText(""+ bmark.getVerse().substring(0, 159).trim()+" ...");
				}
				else
				{
					holder.txtMemoryverse.setText(""+ bmark.getVerse().trim()+"");
				}
				
				
			}
		
			return view;
		}
		
		return view;
	}

}
