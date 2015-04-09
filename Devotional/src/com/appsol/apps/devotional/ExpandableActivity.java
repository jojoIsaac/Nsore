package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;

import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableActivity extends ActionBarActivity {

	//private DisplayImageOptions options;
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<FAQGroup> listDataHeader;
    HashMap<FAQGroup, List<FAQItem>> listDataChild;
    public int GetPixelFromDips(float pixels) {
	    // Get the screen's density scale 
	    final float scale = getResources().getDisplayMetrics().density;
	    // Convert the dps to pixels, based on density scale
	    return (int) (pixels * scale + 0.5f);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable);
		setTitle("FAQs");
		
		DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    int width = metrics.widthPixels; 
		
		
		 prepareListData();
		 expListView = (ExpandableListView) findViewById(R.id.lvExp);
		 expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));  

		   
		expListView.setOnChildClickListener( new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				
				FAQItem items= listAdapter.getChild(groupPosition, childPosition);
				if(items!=null)
				{
					
					Dialog dialog = new Dialog(ExpandableActivity.this);
					LayoutInflater inflate= (LayoutInflater) ExpandableActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
					View view= inflate.inflate(R.layout.fragment_faq_details, null);
					//dialog.setContentView(view);
					TextView txtContent,txtQuestion;
					txtQuestion=(TextView) view.findViewById(R.id.txtQuestion);
					txtContent=(TextView) view.findViewById(R.id.txtContent);
					txtQuestion.setText(items.getTitle());
					txtContent.setText(items.getFaq());
					
					dialog.setTitle("FAQs");
					//dialog.setCancelable(true);
					
					//dialog.show();
					
					
					AlertDialog.Builder b= new Builder(ExpandableActivity.this);
					b.setView(view);
					b.setPositiveButton("Done",  new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						
						}
					});
					b.show();
					
					
				}
				return false;
			}
		});
	       
	       
	}

	private void prepareListData() {
		// TODO Auto-generated method stub
		 listDataHeader = new ArrayList<FAQGroup>();
	     listDataChild = new HashMap<FAQGroup, List<FAQItem>>();
	  	(new loadFAQGroups()).execute();
	  	}
	  	
	  	//ASYNC to load the FAQs
	  	class loadFAQGroups extends AsyncTask<Void, Integer, String>
	  	{

	  		
	  		ProgressDialog pd;

	  		@Override
	  		protected void onPreExecute() {
	  			// TODO Auto-generated method stub
	  			pd = new ProgressDialog(Connector.context);
	  			pd.setMessage("Please Wait ....");
	  			pd.setCancelable(false);
	  			pd.show();
	  			super.onPreExecute();
	  		}
	  		
	  		
	  		@Override
	  		protected void onPostExecute(String result) {
	  			// TODO Auto-generated method stub
	  			
	  			Log.e("ERR", result);
	  			if(!result.equalsIgnoreCase("NOT_SET"))
	  			{
	  				try {
	  					JSONArray jarray = new JSONArray(result);

	  					if (jarray != null) {
	  						//JSONArray jarray = object.getJSONArray("FAQ");

	  						if (jarray != null) {
	  							
	  							for (int i = 0; i < jarray.length(); i++) {
	  								JSONObject objects = jarray.optJSONObject(i);
	  								
	  								String faq_group_id,faq_group,icon,status,sub_content;
	  								 faq_group_id = objects.optString("faq_group_id");
	  								 faq_group= objects.optString("faq_group");
	  								 icon= objects.optString("icon");
	  								 JSONArray subContentAyya= objects.getJSONArray("sub_content");
	  								 //sub_content= objects.optString("sub-Content");
	  								 
	  								 FAQGroup group= new FAQGroup();
	  								 group.setFaq_group(faq_group);
	  								 group.setFaq_group_id(faq_group_id);
	  								 group.setIcon(icon);
	  								 group.setSub_content(subContentAyya.toString());
	  								
	  								try
	  								{
	  									
	  									if(subContentAyya!=null)
	  									{
	  										group.setChildren( new ArrayList<ExpandableActivity.FAQItem>());
 			  								 
	  										//ArrayList<FAQItem> itemList= new ArrayList<ExpandableActivity.FAQItem>();
  			  								Log.e(group.getFaq_group_id(), subContentAyya.length()+"");
	  										for (int isub = 0; isub < subContentAyya.length(); isub++) {
	  											JSONObject subContent=subContentAyya.optJSONObject(isub);
	  			  								if(subContent!=null)
	  			  								{
	  			  									String faq_id,faq_group_ids,title,faq;
	  			  									FAQItem item = new FAQItem();
	  			  									item.setFaq(subContent.optString("faq"));
	  			  									item.setTitle(subContent.optString("title"));
	  			  									item.setFaq_id(subContent.optString("faq_id"));
	  			  								  
	  			  								      // Log.e(tag, msg)
	  			  								    group.getChildren().add(item);
	  			  								} 
	  			  							
	  										}
	  										
	  										
	  										
	  										//listDataChild.put(listDataHeader.get(i), itemList);
	  									}
	  								
	  								}
	  								catch(Exception e)
	  								{
	  									
	  								}
	  								
	  								 //adapter.add(group);
	  								//testimonies.add(testimony);
	  								listDataHeader.add(group);
	  							}
	  						}
	  					}
	  					
	  					
	  					if(listDataHeader.size()>0)
	  					{
	  						
	  						 listAdapter = new ExpandableListAdapter(ExpandableActivity.this, listDataHeader, listDataChild);
	  						 
	  				        // setting list adapter
	  				        expListView.setAdapter(listAdapter);
	  				      pd.dismiss();
	  					}
	  					else
	  					{
	  						pd.dismiss();
	  						ExpandableActivity.this.finish();
	  						
	  					}
	  					//adapter.notifyDataSetChanged();
	  				} catch (JSONException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}
	  			}
	  			//Toast.makeText(ExpandableActivity.this, listDataHeader.get(0).getChildren().get(0).getFaq(), Toast.LENGTH_LONG).show();
	  			super.onPostExecute(result);
	  		}
	  		
	  		@Override
	  		protected String doInBackground(Void... params) {
	  			// TODO Auto-generated method stub
	  			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	  			//param.add(new BasicNameValuePair("CID", Connector.getChurchID()));
	  			param.add(new BasicNameValuePair("reason", "Get FAQ Groups"));
	  			//param.add(new BasicNameValuePair("USER_ID", Connector.getUserId()));
	  			String response = Connector.sendData(param, Connector.context);

	  			return response;
	  		}
	  		
	  	}
	  	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.expandable, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	//
	

	public static class FAQGroup
	{
		private String faq_group_id,faq_group,icon,status,sub_content;
		ArrayList<FAQItem>children;
		public void setFaq_group(String faq_group) {
			this.faq_group = faq_group;
		}
		
		public void setFaq_group_id(String faq_group_id) {
			this.faq_group_id = faq_group_id;
		}
		
		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getFaq_group() {
			return faq_group;
		}
		public String getFaq_group_id() {
			return faq_group_id;
		}
		public String getIcon() {
			return icon;
		}
		public String getStatus() {
			return status;
		}
		
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public void setSub_content(String sub_content) {
			this.sub_content = sub_content;
		}
		public String getSub_content() {
			return sub_content;
		}
		public ArrayList<FAQItem> getChildren()
	    {
	        return children;
	    }
	     
	    public void setChildren(ArrayList<FAQItem> children)
	    {
	        this.children = children;
	    }
		
		
	}
	
	
	
	public static class FAQItem 
	{
		//faq_id ,faq_group_id,title,faq
		
	private String faq_id,faq_group_id,title,faq;
	
	public String getFaq() {
		return faq;
	}
	public String getFaq_group_id() {
		return faq_group_id;
	}
	public String getFaq_id() {
		return faq_id;
	}
	public String getTitle() {
		return title;
	}
	public void setFaq(String faq) {
		this.faq = faq;
	}
	public void setFaq_id(String faq_id) {
		this.faq_id = faq_id;
	}
	public void setFaq_group_id(String faq_group_id) {
		this.faq_group_id = faq_group_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

	
	}
	

	 ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	 public static class ViewHolder {
			TextView title;
			
			CircularNetworkImageView image;
			
		}
	class ExpandableListAdapter extends BaseExpandableListAdapter {
		 
	    private Context _context;
	    private List<FAQGroup> _listDataHeader; // header titles
	    // child data in format of header title, child title
	    private HashMap<FAQGroup, List<FAQItem>> _listDataChild;
	 
	    public ExpandableListAdapter(Context context, List<FAQGroup> listDataHeader,
	    		HashMap<FAQGroup, List<FAQItem>> listChildData) {
	        this._context = context;
	        this._listDataHeader = listDataHeader;
	        this._listDataChild = listChildData;
	    }
	 
//	    public ExpandableListAdapter(Context context,
//				List<FAQGroup> listDataHeader,
//				HashMap<FAQGroup, List<FAQItem>> listDataChild) {
//	    	this._context = context;
//	        this._listDataHeader = listDataHeader;
//	        this._listDataChild = listDataChild;
//			// TODO Auto-generated constructor stub
//		}

		@Override
	    public FAQItem getChild(int groupPosition, int childPosititon) {
	    	return  this._listDataHeader.get(groupPosition).getChildren().get(childPosititon);
//	        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	 
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,
	            boolean isLastChild, View convertView, ViewGroup parent) {
	    	//final FAQItem childText
	        final FAQItem childText =  getChild(groupPosition, childPosition);
	 
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.expandablelist_item, null);
	        }
	 
	        TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.lblListItem);
	 
	        txtListChild.setText(childText.getTitle());
	        return convertView;
	    }
	 
	    @Override
	    public int getChildrenCount(int groupPosition) {
	    	
	    	return this._listDataHeader.get(groupPosition).getChildren().size();
//	        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//	                .size();
	    }
	 
	    @Override
	    public FAQGroup getGroup(int groupPosition) {
	        return this._listDataHeader.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this._listDataHeader.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	 
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,
	            View convertView, ViewGroup parent) {
//	        String headerTitle = (String) getGroup(groupPosition);
//	        if (convertView == null) {
//	            LayoutInflater infalInflater = (LayoutInflater) this._context
//	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	            convertView = infalInflater.inflate(R.layout.faqlistitem, null);
//	        }
//	 
//	        TextView lblListHeader = (TextView) convertView
//	                .findViewById(R.id.lblListHeader);
//	        lblListHeader.setTypeface(null, Typeface.BOLD);
//	        lblListHeader.setText(headerTitle);
//	 
//	        return convertView;
	    	
	    	View view = convertView;
			final ViewHolder holder;
			 if (imageLoader == null)
		            imageLoader =com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		        	
			FAQGroup faqGroup = getGroup(groupPosition);

			if (convertView == null) {
				view =((Activity) _context).getLayoutInflater().inflate(
						R.layout.faqlistitem, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.txttitle);
				holder.image = (CircularNetworkImageView) view.findViewById(R.id.thumbImage);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			
			if(faqGroup!=null)
			{
				holder.title.setText(faqGroup.getFaq_group());
				
				
			}
			String url = Connector.faq_icons+ faqGroup.getIcon();
			
			if(!url.equalsIgnoreCase(Connector.imageURL))
			{
//			{
//				imageLoader.displayImage(url, holder.image, options,
//						animateFirstListener);
				holder.image.setImageUrl(url, imageLoader);
			}
			else
			{
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
			}
			
			
			
			
			
			
					return view;
	    }
	 
	    @Override
	    public boolean hasStableIds() {
	        return false;
	    }
	 
	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }
	}
	
}
