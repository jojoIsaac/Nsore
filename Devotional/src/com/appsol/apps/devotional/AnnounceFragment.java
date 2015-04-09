package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.ChurchAnnouncements;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link AnnounceFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AnnounceFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment AnnounceFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AnnounceFragment newInstance(String param1, String param2) {
		AnnounceFragment fragment = new AnnounceFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public AnnounceFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		restoreTitle();
	}

	
	
	
	private View rootView;
	void restoreTitle()
	{
		MainActivity.mTitle="Announcements";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), "Again", Toast.LENGTH_LONG).show();
		if(Connector.isInternetAvailable(getActivity())
	){
			 (new checkUpcomingEvent()).execute();
		}
		else
		{
			showErrorDialog("");
		}
		 restoreTitle();
		super.onResume();
	}
	
	private void showErrorDialog(String error) {
		

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		if(!TextUtils.isEmpty(error))
		{
			b.setMessage("Response : " + error);
		}
		else
		{
			b.setMessage("Response : "+"Unable to perform requested action" );
		}
		b.setPositiveButton("Try Again",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 (new checkUpcomingEvent()).execute();
			}
	
		}).
		setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
	
		}).
		show();
	}
	
	
	public static ChurchEvents upcomingEvent;
static LinearLayout layouthead;
static ProgressBar progressbarLoad;

Button btnReadMore;
private static ImageView btnshow;
private View frags;

LinearLayout layout_no_event;
ListView lstDevotions;
ItemAdapter devotions;
ArrayList<ChurchAnnouncements> eventsList;
//private static DisplayImageOptions options;
static TextView txteventTitle,txteventstartdate,txteventLocation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		restoreTitle();
		rootView= inflater.inflate(R.layout.fragment_announce, container,
				false);
		
//		options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.announcementico)
//		.showImageForEmptyUri(R.drawable.announcementico)
//		.showImageOnFail(R.drawable.announcementico).cacheInMemory(true)
//		.cacheOnDisk(true).considerExifParams(true)
//		.displayer(new RoundedBitmapDisplayer(6)).build();
		 Connector.context=getActivity();

		 
		 
		 progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		

		
		 layout_no_event=(LinearLayout) rootView.findViewById(R.id.layout_no_event);
		 TextView txtchurchname=(TextView) rootView.findViewById(R.id.txt1);
		 ImageView imgeventBanner=(ImageView )rootView.findViewById(R.id.imgeventBanner);
		 //imgeventBanner.setImageResource(R.drawable.announcementico);
		
		 
		 if( layout_no_event!=null)
		 {
			 layout_no_event.setVisibility(View.GONE);
			 txtchurchname.setText(Connector.getChurchName()+" Announcements");
			 
		 }
		
			//layout_Month.setVisibility(View.GONE);
		
		 //Year: 
		 eventsList= new ArrayList<ChurchAnnouncements>();
			devotions = new ItemAdapter(getActivity(),
					android.R.layout.simple_list_item_1, eventsList);
		 lstDevotions = (ListView) rootView.findViewById(R.id.lstMessages);
			lstDevotions.setAdapter(devotions);
			registerForContextMenu(lstDevotions);
			lstDevotions.setOnItemClickListener( new OnItemClickListener() {

				

				

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub
					ChurchAnnouncements announcements= devotions.getItem(position);
					if(announcements!=null)
					{
					Fragment fragment= new AnnouncementDetailsFragment();
						Bundle data= new Bundle();
						data.putString("T", announcements.getCaption());
						data.putString("D",announcements.getDateAdded());
						data.putString("C", announcements.getContent());
						
						
						fragment.setArguments(data);
						
						FragmentManager manager= getActivity().getSupportFragmentManager();
						manager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

						
					}
				}
			});
		
		
		
		 return rootView;
	}

	
	


	class checkUpcomingEvent extends AsyncTask<Void, Integer, String>
	{
ProgressDialog progressDialog;
ProgressBar progressbar= new ProgressBar(getActivity());
private int myProgressCount;
@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	if(progressbarLoad!=null)
	 progressbarLoad.setProgress(0);
     myProgressCount = 0;
	super.onPreExecute();
}


@Override
protected void onProgressUpdate(Integer... values) {
	// TODO Auto-generated method stub
	
	if(progressbarLoad!=null)
	   progressbarLoad.setProgress(values[0]);
	super.onProgressUpdate(values);
}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String response=null;
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("reason",
					"get Church Announcements"));
			parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
			parameter.add(new BasicNameValuePair("CID", Connector
					.getChurchID()));
			response = Connector.sendData(parameter, getActivity());
			   while (response == null) {
                   myProgressCount++;
                   /**
                    * Runs on the UI thread after publishProgress(Progress...) is
                    * invoked. The specified values are the values passed to
                    * publishProgress(Progress...).
                    *
                    * Parameters values The values indicating progress.
                    */

                   publishProgress(myProgressCount);
                   SystemClock.sleep(100);
             }
			return response;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressbarLoad!=null)
			{
			progressbarLoad.setProgress(0);
			progressbarLoad.setVisibility(View.GONE);
			}
			//
			
			processJson(result);
			super.onPostExecute(result);
		}
		
		
		private void processJson(String result) {
			// TODO Auto-generated method stub
			Log.e("ANN",result);
			if(result.equalsIgnoreCase("Sorry No Announcements were Found"))
			{
				
				 layout_no_event.setVisibility(View.VISIBLE);
				 Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			}
			else
			{
				
					devotions.clear();
				
	
				
				
				
				JSONObject objects;

				try {
					objects = new JSONObject(result);
					if (objects != null) {
						JSONArray jarray = objects.getJSONArray("Church-Announcements");

						if (jarray != null) {
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject object = jarray.optJSONObject(i);

								if (object != null) {
									String ID=object.optString("church_branch_announcement_id", "0");
									String caption=object.optString("caption", "");
									String content=object.optString("content", "");
									String summary=object.optString("M", "");
									String dateAdded=object.optString("PD");
									
									
									//String location=object.optString("location", "");
									ChurchAnnouncements announcements= new ChurchAnnouncements();
									announcements.setCaption(caption);
									announcements.setID(ID);
									announcements.setSummary(summary);
									announcements.setDateAdded(dateAdded);
									announcements.setContent(content);
										
										
										devotions.add(announcements);
								

								} else {
									Log.e("ERR", "invalid Object");
								}
							}
							// Toast.makeText(getActivity(),
							// devotions.getCount() + " 8494949449",
							// Toast.LENGTH_LONG).show();
							devotions.notifyDataSetChanged();
						} else {
							Log.e("ERR", "invalid Object");
						}

					}

				} catch (JSONException e) {

				}
			}
		}


		
	}
	
	
	
	
	
	
	private static class ViewHolder {
		TextView title;
		TextView content;
		ImageView image;
		TextView txtTime;
		TextView txtlikes;
		
		
		LinearLayout lvDate;
		TextView txtDate;
		TextView tvc;
		
	}

	public static class ItemAdapter extends ArrayAdapter<ChurchAnnouncements> {

		List<ChurchAnnouncements> testimoniess;
    Context context;
		public ItemAdapter(Context context, int resource,
				List<ChurchAnnouncements> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			this.context= context;
			// TODO Auto-generated constructor stub
		}

		

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

			ChurchAnnouncements testimony = getItem(position);

			if (convertView == null) {
			view = ((Activity) context).getLayoutInflater().inflate(
						R.layout.announcementitem, parent, false);
				
				
				holder = new ViewHolder();
//				holder.lvDate =(LinearLayout) view.findViewById(R.id.lvDate);
//				holder.txtDate= (TextView) view.findViewById(R.id.txtdate);
//				holder.tvc=(TextView) view.findViewById(R.id.txtday);
//				
//				holder.lvDate.setVisibility(View.GONE);
				holder.title = (TextView) view.findViewById(R.id.title);
				//holder.image = (ImageView) view.findViewById(R.id.thumbImage);
				holder.content = (TextView) view
						.findViewById(R.id.txtContent);
				//holder.txtTime=(TextView) view.findViewById(R.id.txtDate);
				//holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (testimony != null) {
				holder.title.setText(testimony.getCaption());
				holder.content.setText(testimony.getSummary());
				
//				if(!likes.equalsIgnoreCase("0"))
//				{
//					holder.txtlikes.setText(likes+ " like(s)");
//				}
//				else
//				{
//					holder.txtlikes.setText("");
//					
//				}
				//holder.txtTime.setText(testimony.getDateAdded());
				//holder.image.setVisibility(View.GONE);

			}

			return view;
		}
	}

	
	
	
	

}


