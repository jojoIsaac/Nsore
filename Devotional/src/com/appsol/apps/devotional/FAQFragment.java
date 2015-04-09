package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appsol.apps.devotional.TestimoniesCommunityFragment.AnimateFirstDisplayListener;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Use the {@link FAQFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class FAQFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View rootView;
    private ListView lstContent;
    List<FAQGroup> FAQlist;
    private DisplayImageOptions options;
    private static FAQAdapter adapter;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment FAQFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FAQFragment newInstance(String param1, String param2) {
		FAQFragment fragment = new FAQFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public FAQFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_faq, container, false);
		FAQlist= new ArrayList<FAQFragment.FAQGroup>();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.appico)
		.showImageForEmptyUri(R.drawable.appico)
		.showImageOnFail(R.drawable.appico).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(6)).build();
		setupUI(rootView);
		return rootView;
	}

	private void setupUI(View rootView) {
		// TODO Auto-generated method stub
		lstContent= (ListView) rootView.findViewById(R.id.lstFAQS);
		adapter= new FAQAdapter(getActivity(), android.R.layout.simple_list_item_1	, FAQlist);
		lstContent.setAdapter(adapter);
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
			pd.dismiss();
			Log.e("ERR", result);
			if(!result.equalsIgnoreCase("NOT_SET"))
			{
				try {
					JSONObject object = new JSONObject(result);

					if (object != null) {
						JSONArray jarray = object.getJSONArray("FAQ");

						if (jarray != null) {
							adapter.clear();
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject objects = jarray.optJSONObject(i);
								
								String faq_group_id,faq_group,icon,status,sub_content;
								 faq_group_id = objects.optString("faq_group_id");
								 faq_group= objects.optString("faq_group");
								 icon= objects.optString("icon");
								 sub_content= objects.optString("sub-Content");
								 
								 FAQGroup group= new FAQGroup();
								 group.setFaq_group(faq_group);
								 group.setFaq_group_id(faq_group_id);
								 group.setIcon(icon);
								 group.setSub_content(sub_content);
								 adapter.add(group);
								//testimonies.add(testimony);
							}
						}
					}
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
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
	
	
	
	public static class FAQGroup
	{
		private String faq_group_id,faq_group,icon,status,sub_content;
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
	
	}
	
	public static class ViewHolder {
		TextView title;
		
		ImageView image;
		
	}
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	class FAQAdapter extends ArrayAdapter<FAQGroup>
	{
   List<FAQGroup> FAQlist;
		public FAQAdapter(Context context, int resource, List<FAQGroup> objects) {
			super(context, resource, objects);
			FAQlist= new ArrayList<FAQFragment.FAQGroup>();
			// TODO Auto-generated constructor stub
		}
		
		
		
		@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
			
			View view = convertView;
			final ViewHolder holder;

			FAQGroup faqGroup = getItem(position);

			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.faqlistitem, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.txttitle);
				holder.image = (ImageView) view.findViewById(R.id.thumbImage);
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
				imageLoader.displayImage(url, holder.image, options,
						animateFirstListener);
			}
			
			
			
			
			
			
					return view;
				}
		
	}

}
