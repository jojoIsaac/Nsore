package com.appsol.apps.devotional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;
import com.appsol.apps.projectcommunicate.model.ChurchBranch;
import com.appsol.apps.projectcommunicate.model.ChurchLeader;
import com.appsol.apps.projectcommunicate.model.church_branches;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link ChurchBranchFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class ChurchBranchFragment extends Fragment {
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
	 * @return A new instance of fragment ChurchBranchFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChurchBranchFragment newInstance(String param1, String param2) {
		ChurchBranchFragment fragment = new ChurchBranchFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChurchBranchFragment() {
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
		rootView= inflater.inflate(R.layout.fragment_church_branch, container,
				false);
		setUpUI(rootView);
		return rootView;
	}

	static ProgressBar progressBar;
//private DisplayImageOptions options;
//protected ImageLoader imageLoader = ImageLoader.getInstance();
private static ItemAdapter ChurchLeaderAdapter;
List<church_branches>ChurchLeaderlist;
private ListView lstleaders;

	private void setUpUI(View rootView) {
		// TODO Auto-generated method stub
		progressBar=(ProgressBar) rootView.findViewById(R.id.progressbar);
		ChurchLeaderlist = new ArrayList<church_branches>();
		lstleaders = (ListView) rootView.findViewById(R.id.lstLeaders);
		ChurchLeaderAdapter = new ItemAdapter(getActivity(),
				android.R.layout.simple_list_item_1, ChurchLeaderlist);
		lstleaders.setAdapter(ChurchLeaderAdapter);
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.mobile)
//				.showImageForEmptyUri(R.drawable.mobile)
//				.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(6)).build();
		lstleaders.setOnItemClickListener(new OnItemClickListener() {

			  //static church_branches UserBranch;
		
		

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				church_branches UserBranch= ChurchLeaderAdapter.getItem(arg2);
				startBranchDetailActivity(UserBranch);
			}
		});
		registerForContextMenu(lstleaders);
		
		(new fetchChurchBranches()).execute();
	}
	
@Override
public void onResume() {
	// TODO Auto-generated method stub
	(new fetchChurchBranches()).execute();
	super.onResume();
}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
		church_branches UserBranch= ChurchLeaderAdapter.getItem(info.position);
		if(UserBranch!=null)
		{
			switch (item.getItemId()) {
			case R.id.mn_Details:
				startBranchDetailActivity(UserBranch);
				break;
            case R.id.mn_set_as_branch:
            	changeChurchBranch(UserBranch);
				break;
			default:
				break;
			}
		}
		
		return super.onContextItemSelected(item);
	}
	
	
	protected void changeChurchBranch(final church_branches branch) {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), branch.toString(), Toast.LENGTH_LONG).show();
		

		AlertDialog.Builder build=  new Builder(getActivity());
		build.setTitle("Change Branch")
		.setMessage("Do you want to proceed?")
		.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
		AsyncTask<Void, Void, String> changebranch = new AsyncTask<Void, Void, String>() {
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if(result.equalsIgnoreCase("Saved"))
				{
					Connector.setBranch(branch.getchurchbranchid().toString());
					Toast.makeText(getActivity(), "Church Branch Changed", Toast.LENGTH_LONG).show();
					
				}
				
				super.onPostExecute(result);
			}
			
			@Override
			protected String doInBackground(Void... arg0) {
				ArrayList<NameValuePair> parameter= new ArrayList<NameValuePair>();
				
				parameter.add(new BasicNameValuePair("reason","Change Church member Branch"));
				parameter.add(new BasicNameValuePair("BID",branch.getchurchbranchid().toString() ));
				parameter.add(new BasicNameValuePair("UID",Connector.getUserId()));
				String status= Connector.sendData(parameter, getActivity());
				return status;
			}
		};
		changebranch.execute();
			}
		})
		.setNegativeButton("No", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
		
	}
	
	
	private void setAsUserDefaultBranch(church_branches userBranch) {
		// TODO Auto-generated method stub
		
	}

	private void startBranchDetailActivity(church_branches userBranch) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), BranchDetailActivity.class);
		intent.putExtra("json", userBranch.getBranchJson());
		
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater minflater= new MenuInflater(getActivity());
		if(v.getId()== lstleaders.getId())
		{
			minflater.inflate(R.menu.branchlistmenu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	///get Church Leaders
	
	////Set up some house stufss
	private static class ViewHolder {
		
		CircularNetworkImageView image;
		TextView txtlocation;
		TextView txtname;
		
	}

	class ItemAdapter extends ArrayAdapter<church_branches> {

		List<church_branches> testimoniess;
		 ImageLoader imageLoader =com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
         
			
		public ItemAdapter(Context context, int resource,
				List<church_branches> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			// TODO Auto-generated constructor stub
		}

		//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private String filePath;
		private String imagename;

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

			church_branches testimony = getItem(position);

			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.churchbranchlistitem, parent, false);
				holder = new ViewHolder();
				
				holder.image = (CircularNetworkImageView) view.findViewById(R.id.thumbImage);
				//holder.thumbup=(ImageView) view.findViewById(R.id.testimonylikeICo);
				
		holder.txtname=(TextView) view.findViewById(R.id.txtname);
		holder.txtlocation=(TextView) view.findViewById(R.id.txtPosition);
//				holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (testimony != null) {
				
				//holder.content.setText(testimony.getContent());
				 filePath= Environment.getExternalStorageDirectory().getPath();
			        //Set some vital information
			        imagename = Connector.myPrefs.getString("CHURCH_LOGO", "NO_DATA");
			        holder.image.setVisibility(View.GONE);
//			       holder.image.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.appico), 80, 80, false), 20) );
					
			    	File	file= new File(filePath+"/"+Connector.AppFolder+"/"+imagename);
					if(file.exists() && file.isFile())
					{
						//Toast.makeText(this, imagename, Toast.LENGTH_LONG).show();
						//holder.image.setImageUrl(file.getAbsolutePath(), imageLoader);
						
						//holder.image.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 80, 80, false), 20) );
//						imageLoader.displayImage(file.getAbsolutePath(), holder.image, options,
//								animateFirstListener);
					}
					else
					{

							
						//holder.image.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.appico), 80, 80, false), 20) );
						
					}
			        
			        
				String url ="" ;
				holder.txtname.setText(testimony.getbranchname());
				
				holder.txtlocation.setText(testimony.getlocation());
				

			}

			return view;
		}
	}

	
	private static  class fetchChurchBranches extends AsyncTask<Void, Integer, String> {
		ProgressDialog pd;

		private int myProgressCount;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(progressBar!=null)
				progressBar.setProgress(0);
		     myProgressCount = 0;
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressBar!=null)
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Get Church Testimonies
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("CID",Connector.getChurchID()));
			param.add(new BasicNameValuePair("reason", "get Church Branch List"));
			param.add(new BasicNameValuePair("USER_ID", Connector.getUserId()));
			String response = null;
			response =Connector.sendData(param, Connector.context);
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
			if(progressBar!=null)
			{
				progressBar.setProgress(0);
				progressBar.setVisibility(View.GONE);
			}
			//pd.dismiss();
			//Toast.makeText(Connector.context, result, Toast.LENGTH_LONG).show();
			Log.e("ERR", result);
			
			if(!result.equalsIgnoreCase("NOT_SET"))
			{
				try {
					JSONObject object = new JSONObject(result);

					if (object != null) {
						JSONArray jarray = object.getJSONArray("Branches");

						if (jarray != null) {
							ChurchLeaderAdapter.clear();
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject objects = jarray.optJSONObject(i);
								
								church_branches branch= new church_branches();
								
								 Integer churchbranchid=objects.optInt("church_branch_id");
							     Integer churchid=objects.optInt("church_id");
								 String branchname= objects.optString("branch_name");
								 String location= objects.optString("location");
								 String address= objects.optString("address");
								 String phone1= objects.optString("phone1");
								 String phone2= objects.optString("phone2");
								 String phone3= objects.optString("phone3");
								 String website= objects.optString("website");
								 String fax= objects.optString("fax");
								 branch.setwebsite(website);
								 branch.setphone1(phone1);
								 branch.setphone2(phone2);
								 branch.setphone3(phone3);
								 branch.setfax(fax);
								 branch.setaddress(address);
								 branch.setbranchname(branchname);
								 branch.setchurchbranchid(churchbranchid);
								 branch.setlocation(location);
								 branch.setchurchid(churchid);
								branch.setBranchJson(objects.toString());
								 ChurchLeaderAdapter.add( branch);
								
								
							}
						}
					}
					ChurchLeaderAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			

			super.onPostExecute(result);
		}

	}


}
