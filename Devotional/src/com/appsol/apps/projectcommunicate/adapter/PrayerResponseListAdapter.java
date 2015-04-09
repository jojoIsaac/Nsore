package com.appsol.apps.projectcommunicate.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.model.Notes;
import com.appsol.apps.projectcommunicate.model.PrayerRequest;

public class PrayerResponseListAdapter extends ArrayAdapter<PrayerRequest.PrayerResponses> {
Context Bcontext;
List<PrayerRequest.PrayerResponses> mBookmarks;

	public PrayerResponseListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		Bcontext=context;
	}

	public PrayerResponseListAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		Bcontext=context;
		
		// TODO Auto-generated constructor stub
	}

	public PrayerResponseListAdapter(Context context, int resource, PrayerRequest.PrayerResponses[] objects) {
		super(context, resource, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public PrayerResponseListAdapter(Context context, int resource, List<PrayerRequest.PrayerResponses> objects) {
		super(context, resource, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<PrayerRequest.PrayerResponses>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}

	public PrayerResponseListAdapter(Context context, int resource,
			int textViewResourceId,PrayerRequest.PrayerResponses[] objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public PrayerResponseListAdapter(Context context, int resource,
			int textViewResourceId, List<PrayerRequest.PrayerResponses> objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<PrayerRequest.PrayerResponses>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}
	
	public List<PrayerRequest.PrayerResponses> getBookmarksList()
	{
		return mBookmarks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) Bcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row= inflater.inflate(R.layout.fragment_prayer_request_item, parent,false);
		PrayerRequest.PrayerResponses bmark= getItem(position);
		if(row!=null)
		{
			if(bmark!=null)
			{
				TextView tvtitle = (TextView) row.findViewById(R.id.title);
				tvtitle.setText(bmark.getResponse());
				
		    TextView tvc=(TextView) row.findViewById(R.id.txtdate);
		    TextView txt_status=(TextView) row.findViewById(R.id.txt_status);
		    txt_status.setText(bmark.getResponded_at());
		    tvc.setVisibility(View.GONE);
		    //tvc.setText(bmark.getSent_at());
			int x= bmark.getResponse().length();
			String content ="";
//			if(x> 200)
//			{
//				 content = bmark.getPrayer_request().substring(0,198 )+" .....";
//			}
//			else
//			{
				content = bmark.getResponse();
			//}
			TextView txtcontent =(TextView) row.findViewById(R.id.txtcontent);	
			txtcontent.setText(bmark.getResponded_by());
				//txtcontent
				
			}
			return row;
		}
		
		return super.getView(position, convertView, parent);
	}

}
