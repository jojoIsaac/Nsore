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

public class NotesListAdapter extends ArrayAdapter<Notes> {
Context Bcontext;
List<Notes> mBookmarks;

	public NotesListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		Bcontext=context;
	}

	public NotesListAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		Bcontext=context;
		
		// TODO Auto-generated constructor stub
	}

	public NotesListAdapter(Context context, int resource, Notes[] objects) {
		super(context, resource, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public NotesListAdapter(Context context, int resource, List<Notes> objects) {
		super(context, resource, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Notes>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}

	public NotesListAdapter(Context context, int resource,
			int textViewResourceId,Notes[] objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public NotesListAdapter(Context context, int resource,
			int textViewResourceId, List<Notes> objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Notes>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}
	
	public List<Notes> getBookmarksList()
	{
		return mBookmarks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) Bcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row= inflater.inflate(R.layout.fragment_note_item, parent,false);
		Notes bmark= getItem(position);
		if(row!=null)
		{
			if(bmark!=null)
			{
				TextView tvtitle = (TextView) row.findViewById(R.id.title);
				tvtitle.setText(bmark.getTitle());
				TextView tvc=(TextView) row.findViewById(R.id.txtdate);
				tvc.setText(bmark.getDate_string());
			int x= bmark.getcontent().length();
			String content ="";
			if(x> 40)
			{
				 content = bmark.getcontent().substring(0,39 )+" .....";
			}
			else
			{
				content = bmark.getcontent();
			}
			TextView txtcontent =(TextView) row.findViewById(R.id.txtcontent);	
			txtcontent.setText(content);
				//txtcontent
				
			}
			return row;
		}
		
		return super.getView(position, convertView, parent);
	}

}
