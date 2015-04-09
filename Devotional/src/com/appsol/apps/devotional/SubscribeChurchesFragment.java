package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.appsol.apps.devotional.SubscriptionActivity.churchDetails;
import com.appsol.apps.devotional.SubscriptionActivity.createAccount;
import com.appsol.apps.projectcommunicate.adapter.DevotionPagerAdapter;
import com.appsol.apps.projectcommunicate.adapter.FilteredArrayAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.OtherBookmarks;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * A simple {@link Fragment} subclass. Use the
 * {@link SubscribeChurchesFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class SubscribeChurchesFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View rootView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SubscribeChurchesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SubscribeChurchesFragment newInstance(String param1,
			String param2) {
		SubscribeChurchesFragment fragment = new SubscribeChurchesFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public SubscribeChurchesFragment() {
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
		rootView= inflater.inflate(R.layout.fragment_sample,
				container, false);
		onInitView(rootView);
		return rootView;
	}
	
	
	
	
	List<String> churches, churchID, churchlogo;
	ListView list_view;
	// Search EditText
	EditText inputSearch;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	private DisplayImageOptions options;
	public static Church selectedChurch;
   List<OtherBookmarks> subscription ;
	
	protected void onInitView(View rooView) {
		// TODO Auto-generated method stub
		//super.onCreate(arg0);
//		setTitle("Follow Church");
//		setContentView(R.layout.fragment_sample);
		Connector.context = getActivity();
		churchList = new ArrayList<Church>();
		inputSearch = (EditText) rooView.findViewById(R.id.inputSearch);
		list_view = (ListView) rooView.findViewById(R.id.list_view);
		churches = new ArrayList<String>();
		churchID = new ArrayList<String>();
		churchlogo = new ArrayList<String>();
		
		subscription= new ArrayList<OtherBookmarks>();
		Connector.dbhelper = new DBHelper(getActivity());
		
		subscription= OtherBookmarks.getBookMarks(Connector.dbhelper,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
		Connector.dbhelper.close();
		
		
		
		adapter = new FilteredArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,
				churchList);
		list_view.setAdapter(adapter);
		list_view.setTextFilterEnabled(true);
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.mobile)
//				.showImageForEmptyUri(R.drawable.mobile)
//				.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(6)).build();
		if(subscription.size()>0)
		{
			processJson();
//			DevotionPagerAdapter.PAGE_COUNT=3;
//			AllDevotions.adapter.notifyDataSetChanged();
		}
		else
		{
//			DevotionPagerAdapter.PAGE_COUNT=2;
//			AllDevotions.adapter.notifyDataSetChanged();
		}
	
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				selectedChurch = adapter.getItem(position);

				if (selectedChurch != null) {
					//Toast.makeText(getActivity(), selectedChurch.getchurchid(), Toast.LENGTH_LONG).show();
					Intent ints = new Intent(Connector.context,
							SubscribedDevotionsActivity.class);
					ints.putExtra("DATA", selectedChurch.getchurchid().toString());
					ints.putExtra("BC", selectedChurch.getBranchCount());
					startActivity(ints);
				}

			}
		});

		/**
		 * Enabling Search Filter
		 * */
		inputSearch.addTextChangedListener(filterTextWatcher);
		
	}

	
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		 
@Override
public void afterTextChanged(Editable s) {
// TODO Auto-generated method stub
 
}
 
@Override
public void beforeTextChanged(CharSequence s, int start, int count,
int after) {
// TODO Auto-generated method stub
 
}
 
@Override
public void onTextChanged(CharSequence s, int start, int before,
int count) {
if (adapter != null) {
adapter.getFilter().filter(s);
} else {
Log.d("filter", "no filter availible");
}
}
};
	
	
	
	class churchDetails extends Church {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String id, name, logo;

		private String churchJson;

		public void setChurchJson(String churchJson) {
			this.churchJson = churchJson;
		}

		public String getChurchJson() {
			return churchJson;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getLogo() {
			return logo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private static class ViewHolder {
		TextView title;

		ImageView image, thumbup;

	}

	class ItemAdapter extends ArrayAdapter<churchDetails> implements Filterable {
		@Override
		public Filter getFilter() {
			if (planetFilter == null)
				planetFilter = new ChurchsFilter();
			return planetFilter;
		}

		private Filter planetFilter;
		List<churchDetails> testimoniess;
		private List<churchDetails> origPlanetList;

		public ItemAdapter(Context context, int resource,
				List<churchDetails> objects) {
			super(context, resource, objects);
//			origPlanetList= 
			testimoniess = objects;
			origPlanetList = objects;
			// TODO Auto-generated constructor stub
		}

		public void resetData() {
			testimoniess = origPlanetList;
		}

		//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		@Override
		public int getCount() {
			return testimoniess.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;

			churchDetails testimony = getItem(position);

			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.list_item, parent,
						false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.product_name);
			
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (testimony != null) {
				holder.title.setText(testimony.getchurchname());

			}

			return view;
		}

		private class ChurchsFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				// We implement here the filter logic
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = testimoniess;
					results.count = testimoniess.size();
				} else {
					// We perform filtering operation
					List<churchDetails> nPlanetList = new ArrayList<churchDetails>();

					for (churchDetails p : testimoniess) {

						if (p.getchurchname()
								.toUpperCase()
								.startsWith(constraint.toString().toUpperCase()))
							nPlanetList.add(p);
					}

					results.values = nPlanetList;
					results.count = nPlanetList.size();

				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// Now we have to inform the adapter about the new list filtered
				if (results.count == 0)
					notifyDataSetInvalidated();
				else {
					testimoniess = (List<churchDetails>) results.values;
					notifyDataSetChanged();
				}
			}

		}

	}

//	public static class AnimateFirstDisplayListener extends
//			SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections
//				.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view,
//				Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}

	public static boolean logoutIfNew = false;
	FilteredArrayAdapter adapter;
	String church_logo = "";
	String id = "";
	String spinsVal = "";
	churchDetails church;
	List<Church> churchList;

	
	
	private void processJson() {
		// TODO Auto-generated method stub
		if(subscription.size()>0)
		{
			for (OtherBookmarks churchData : subscription) {
				
		
		//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		try {
			churches = new ArrayList<String>();
			churchID = new ArrayList<String>();
			churchlogo = new ArrayList<String>();
			churchList = new ArrayList<Church>();
			JSONObject object = new JSONObject(churchData.getResource());

//			if (objects != null) {
//				JSONArray jarray = objects.getJSONArray("Church");

				if ( object != null) {
					
					
					if(churchData.getType().equalsIgnoreCase(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH))
					{
						churchDetails church = new churchDetails();
						church.setId(object.optString("CID"));
						church.setName(object.optString("CN"));
						church.setLogo(object.optString("CL"));
//Toast.makeText(getActivity(), churchData.getType() + subscription.size(), Toast.LENGTH_LONG).show();
						Church churchdata = new Church();
						churchdata.setChurchJson(object.toString());
						churchdata.setchurchname(object.optString("CN"));
						churchdata.setchristianitytype(object
								.optString("CT"));
						churchdata.setchurchdescription(object
								.optString("CD"));
						churchdata.setwebsite(object.optString("CW"));
						churchdata.setfax(object.optString("fax"));
						churchdata.setphone1(object.optString("Tel1"));
						churchdata.setphone2(object.optString("Tel2"));
						churchdata.setaddress(object.optString("CA"));
						churchdata.setheadofficelocation(object
								.optString("HeadLocation"));
						churchdata.setphone3(object.optString("Tel3"));
						churchdata.setchurchid(object.optInt("CID"));
						churchdata.setLogo(object.optString("CL"));
						churchdata.setId(object.optString("CID"));
						churchdata.setBranchCount(object.getString("Branchcount"));
String branchCount=churchdata.getBranchCount().toString();
//Log.e("ERt", idd +" : "+ Connector.getChurchID());
if(branchCount.equalsIgnoreCase("0") && branchCount.equalsIgnoreCase("1") && branchCount.equalsIgnoreCase("-1") )
churchdata.setBranchID("-1");
else
 churchdata.setBranchID(object.optString("BID"));	



adapter.add(churchdata);
					
		
					}
					else
					{
						 Integer churchbranchid=object.optInt("church_branch_id");
					     Integer churchid=object.optInt("church_id");
						 String branchname= object.optString("branch_name");
						 String location= object.optString("location");
						 String address= object.optString("address");
						 String phone1= object.optString("phone1");
						 String phone2= object.optString("phone2");
						 String phone3= object.optString("phone3");
						 String website= object.optString("website");
						 String fax= object.optString("fax");
						 Church church_Data= new Church();
						// Toast.makeText(getActivity(), churchid.toString(), Toast.LENGTH_LONG).show();
						 church_Data.setBranchID(churchbranchid+"");
						 church_Data.setchurchid(churchid);
						 church_Data.setChurchJson(object.toString());
						 church_Data.setchurchname(branchname);
						 church_Data.setId(churchid+"");
						 church_Data.setBranchCount("0");
						 adapter.add(church_Data);
					}
				}
				
			//}
		

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			}
			 adapter.notifyDataSetChanged();
		}
	}
	
	
	
//	class createAccount extends AsyncTask<Void, Integer, String> {
//		ProgressDialog pdg;
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			pdg = new ProgressDialog(Connector.context);
//
//			pdg.setMessage("Please Wait. Fetching Church list");
//			pdg.setCancelable(false);
//			pdg.show();
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			pdg.cancel();
//			processJson(result);
//			super.onPostExecute(result);
//		}
//
//	
//
//		@Override
//		protected String doInBackground(Void... arg0) {
//			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
//			/*
//			 * $name=$_POST['N']; $phone=$_POST['P']; $email=$_POST['E'];
//			 * $password=$_POST['PSWD']; $username=$_POST['UN'];
//			 */
//			parameter.add(new BasicNameValuePair("reason", "get Church List"));
//			String status = Connector.sendData(parameter, Connector.context);
//			return status;
//		}
//
//	}
//
//	
//		
	
	
	
	

}
