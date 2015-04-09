package com.appsol.apps.projectcommunicate.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.Notes;

public class PersonalNoteAdapter extends ArrayAdapter<Notes> {
Context Bcontext;
List<Notes> mBookmarks;
private String devotiondate;
private static String DID;
private static String LID;
private static String Title;
private static String summary;
private static String msg;
private static String day;
private static String month;
private static String readingplan;
private static String verse;
private static String prayer;
private static String day_name;
private static String loadeddate;
private static String rawDate;
private static int iCounter=0;
	public PersonalNoteAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		Bcontext=context;
	}

	public PersonalNoteAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		Bcontext=context;
		
		// TODO Auto-generated constructor stub
	}

	public PersonalNoteAdapter(Context context, int resource, Notes[] objects) {
		super(context, resource, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public PersonalNoteAdapter(Context context, int resource, List<Notes> objects) {
		super(context, resource, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Notes>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}

	public PersonalNoteAdapter(Context context, int resource,
			int textViewResourceId, Notes[] objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public PersonalNoteAdapter(Context context, int resource,
			int textViewResourceId, List<Notes> objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Notes>();
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

	
	public List<Notes> getPersonalNotesList()
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
	
		Notes bmark= getItem(position);
		//BookmarkLinearLayout lvDate =(LinearLayout) row.findViewById(R.id.lvDate);
		
	//Log.e("VAL", bmark.getSummary());
		
		
		if(view!=null)
		{
			if(bmark!=null)
			{
				
				iCounter=iCounter+1;
				int check=iCounter % 4;
				if((check)==0)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_A));

				}
				else if((check)==1)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_B));

				}
				else if((check)==2)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_C));

				}
				else if((check)==3)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_D));

				}
			    processdevotionJson(bmark, holder);
				
				
			}
		
			return view;
		}
		
		return view;
	}
	
	
	//Process the JSon data obtain from the server Request
	// processes the Jsondata representing the devotion for the day.
	public void processdevotionJson(Notes result,ViewHolder holder) {
		// TODO Auto-generated method stub
		
			JSONObject object;

			try {
				
				if (result != null) {
			
				

						
						
						//TextView tvtitle = (TextView) row.findViewById(R.id.txtTitle);
						holder.tvtitle.setText(result.getTitle());
						//TextView txtDate= (TextView) row.findViewById(R.id.txtdate);
						holder.txtDate.setText(result.getDate_string());
						//TextView tvc=(TextView) row.findViewById(R.id.txtday);
						holder.tvc.setText(result.getDay());
						//TextView txtMemoryverse =(TextView) row.findViewById(R.id.txtMemoryverse);
						//String verse = bmark.getVerse().replace(" ", "");
						
						
							Log.e("CUURS", result.getcontent());
							
						if(result.getcontent().equalsIgnoreCase(""))
						{
							holder.txtMemoryverse.setVisibility(View.GONE);
						}
						Integer length=  result.getcontent().length();
						if(length > 160)
						{
							holder.txtMemoryverse.setText(""+ result.getcontent().substring(0, 159).trim()+" ...");
						}
						else
						{
							holder.txtMemoryverse.setText(""+  result.getcontent().trim()+"");
						}
							
						
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	

}
