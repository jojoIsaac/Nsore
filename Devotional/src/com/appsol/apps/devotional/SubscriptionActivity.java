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

import com.appsol.apps.devotional.ChooseChurch.churchDetails;
import com.appsol.apps.devotional.ChooseChurch.createAccount;
import com.appsol.apps.projectcommunicate.adapter.FilteredArrayAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Church;


import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


public class SubscriptionActivity extends ActionBarActivity {
	List<String> churches, churchID, churchlogo;
	ListView list_view;
	// Search EditText
	EditText inputSearch;

	public static Church selectedChurch;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTitle("Follow Church");
		setContentView(R.layout.fragment_sample);
		Connector.context = this;
		churchList = new ArrayList<Church>();
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		list_view = (ListView) findViewById(R.id.list_view);
		churches = new ArrayList<String>();
		churchID = new ArrayList<String>();
		churchlogo = new ArrayList<String>();
		adapter = new FilteredArrayAdapter(this, android.R.layout.simple_spinner_item,
				churchList);
		list_view.setAdapter(adapter);
		list_view.setTextFilterEnabled(true);
		
		(new createAccount()).execute();
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				selectedChurch = adapter.getItem(position);

				if (selectedChurch != null) {
					Intent ints = new Intent(Connector.context,
							SubsChurchDetailsActivity.class);
					ints.putExtra("DATA", selectedChurch.getChurchJson());
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
				view = getLayoutInflater().inflate(R.layout.list_item, parent,
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
Toast.makeText(Connector.context, "Filtering", Toast.LENGTH_LONG).show();
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

	class createAccount extends AsyncTask<Void, Integer, String> {
		ProgressDialog pdg;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pdg = new ProgressDialog(Connector.context);

			pdg.setMessage("Please Wait. Fetching Church list");
			pdg.setCancelable(false);
			pdg.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pdg.cancel();
			processJson(result);
			super.onPostExecute(result);
		}

		private void processJson(String result) {
			// TODO Auto-generated method stub
			//Log.e("RETS", result);
			//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			try {
				churches = new ArrayList<String>();
				churchID = new ArrayList<String>();
				churchlogo = new ArrayList<String>();
				churchList = new ArrayList<Church>();
				JSONObject objects = new JSONObject(result);

				if (objects != null) {
					JSONArray jarray = objects.getJSONArray("Church");

					if (jarray != null) {
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject object = jarray.optJSONObject(i);
							churchDetails church = new churchDetails();
							church.setId(object.optString("CID"));
							church.setName(object.optString("CN"));
							church.setLogo(object.optString("CL"));

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
String idd=churchdata.getchurchid().toString();
//Log.e("ERt", idd +" : "+ Connector.getChurchID());
if(Connector.getChurchID()!=null && !idd.equalsIgnoreCase(Connector.getChurchID()))
		adapter.add(churchdata);
							// churchID.add(objects.optString("CID"));
							// churchlogo.add(objects.optString("CL"));
							// churchList.add(church);
						}
					}
				}
			 adapter.notifyDataSetChanged();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(Void... arg0) {
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			/*
			 * $name=$_POST['N']; $phone=$_POST['P']; $email=$_POST['E'];
			 * $password=$_POST['PSWD']; $username=$_POST['UN'];
			 */
			parameter.add(new BasicNameValuePair("reason", "get Church List"));
			String status = Connector.sendData(parameter, Connector.context);
			return status;
		}

	}

	
		
	
	
}
