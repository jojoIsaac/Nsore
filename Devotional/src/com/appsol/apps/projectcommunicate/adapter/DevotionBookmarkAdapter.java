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

public class DevotionBookmarkAdapter extends ArrayAdapter<Bookmark> {
Context Bcontext;
List<Bookmark> mBookmarks;
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
	public DevotionBookmarkAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		Bcontext=context;
	}

	public DevotionBookmarkAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		Bcontext=context;
		
		// TODO Auto-generated constructor stub
	}

	public DevotionBookmarkAdapter(Context context, int resource, Bookmark[] objects) {
		super(context, resource, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public DevotionBookmarkAdapter(Context context, int resource, List<Bookmark> objects) {
		super(context, resource, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Bookmark>();
		mBookmarks=objects;
		// TODO Auto-generated constructor stub
	}

	public DevotionBookmarkAdapter(Context context, int resource,
			int textViewResourceId, Bookmark[] objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		// TODO Auto-generated constructor stub
	}

	public DevotionBookmarkAdapter(Context context, int resource,
			int textViewResourceId, List<Bookmark> objects) {
		super(context, resource, textViewResourceId, objects);
		Bcontext=context;
		mBookmarks= new ArrayList<Bookmark>();
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

	
	public List<Bookmark> getBookmarksList()
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
	
		Bookmark bmark= getItem(position);
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
			    processdevotionJson(bmark.getDevotion(), holder,bmark);
				
				
			}
		
			return view;
		}
		
		return view;
	}
	
	
	//Process the JSon data obtain from the server Request
	// processes the Jsondata representing the devotion for the day.
	public void processdevotionJson(String result,ViewHolder holder,Bookmark bookmark) {
		// TODO Auto-generated method stub
		
			JSONObject object;

			try {
				object = new JSONObject(result);
				if (object != null) {
			
					 DID = object.optString("DID");
					 LID = object.optString("LID");
					
					 summary = object.optString("summary");
					 msg = object.optString("Msg");
					 day = object.optString("DN");
					 month = object.optString("MN");
					 readingplan = object
							.optString("readingplan");
					 verse = object.optString("verse");
					
					 day_name= object.optString("DW");
					 loadeddate= object.optString("DD");
					 rawDate=object.optString("rawDate");
					 
					 
					 
					 
					 DID = object.optString("DID");
					 LID = object.optString("LID");
					 Title = object.optString("title");
					 summary = object.optString("summary");
					 msg = object.optString("Msg");
					 day = object.optString("DN");
					 month = object.optString("MN");
					 readingplan = object
							.optString("readingplan");
					 verse = object.optString("verse");
				     prayer=object.optString("prayer");
					 day_name= object.optString("day_name");
					 loadeddate= object.optString("loadeddate");
					 rawDate=object.optString("rawDate");
					 bookmark.setVerse(verse);
//					txtfreadings.setText(readingplan + "");
//					txtreflection.setText(msg);
//					txtprayer.setText(prayer);
//					txtdate.setText(day );
//					txtreading.setText(verse);
//					txtTitle.setText(Title);

						
						
						//TextView tvtitle = (TextView) row.findViewById(R.id.txtTitle);
						holder.tvtitle.setText(Title);
						//TextView txtDate= (TextView) row.findViewById(R.id.txtdate);
						holder.txtDate.setText(loadeddate);
						//TextView tvc=(TextView) row.findViewById(R.id.txtday);
						holder.tvc.setText(day_name);
						//TextView txtMemoryverse =(TextView) row.findViewById(R.id.txtMemoryverse);
						//String verse = bmark.getVerse().replace(" ", "");
						
						
							Log.e("CUURS", day_name);
							
						if(verse.equalsIgnoreCase(""))
						{
							holder.txtMemoryverse.setVisibility(View.GONE);
						}
						Integer length=  verse.length();
						if(length > 160)
						{
							holder.txtMemoryverse.setText(""+ verse.substring(0, 159).trim()+" ...");
						}
						else
						{
							holder.txtMemoryverse.setText(""+ verse.trim()+"");
						}
							
						
					
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
	
	

}
