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

import com.appsol.apps.devotional.SubsChurchDetailsActivity.manageFollowing;
import com.appsol.apps.projectcommunicate.adapter.DevotionPagerAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;
import com.appsol.apps.projectcommunicate.model.ChurchBranch;
import com.appsol.apps.projectcommunicate.model.ChurchLeader;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.OtherBookmarks;
import com.appsol.apps.projectcommunicate.model.church_branches;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link SubscribeChurchBranchFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class SubscribeChurchBranchFragment extends ActionBarActivity {
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
	

	public SubscribeChurchBranchFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_church_branch);
		// Inflate the layout for this fragment
		//rootView= inflater.inflate(R.layout.fragment_church_branch, container,
				//false);
		setUpUI();
		//return rootView;
	}

	static ProgressBar progressBar;
private DisplayImageOptions options;
protected ImageLoader imageLoader = ImageLoader.getInstance();
private static ItemAdapter ChurchLeaderAdapter;
List<church_branches>ChurchLeaderlist;
static List<OtherBookmarks>  subscription;
static String churchID="-1";
public static void checkSubs()
{
	subscription= new ArrayList<OtherBookmarks>();
	Connector.dbhelper = new DBHelper(Connector.context);
	subscription= OtherBookmarks.getBookMarks(Connector.dbhelper,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
	Connector.dbhelper.close();
	
	
	
}
private ListView lstleaders;

	private void setUpUI() {
		// TODO Auto-generated method stub
		checkSubs();
		churchID= getIntent().getStringExtra("CID");
		progressBar=(ProgressBar) findViewById(R.id.progressbar);
		ChurchLeaderlist = new ArrayList<church_branches>();
		lstleaders = (ListView) findViewById(R.id.lstLeaders);
		ChurchLeaderAdapter = new ItemAdapter(this,
				android.R.layout.simple_list_item_1, ChurchLeaderlist);
		lstleaders.setAdapter(ChurchLeaderAdapter);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.mobile)
				.showImageForEmptyUri(R.drawable.mobile)
				.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(6)).build();
		lstleaders.setOnItemClickListener(new OnItemClickListener() {

			  //static church_branches UserBranch;
		
		

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				church_branches UserBranch= ChurchLeaderAdapter.getItem(arg2);
				//startBranchDetailActivity(UserBranch);
			}
		});
		registerForContextMenu(lstleaders);
		
		(new fetchChurchBranches()).execute();
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
		church_branches UserBranch= ChurchLeaderAdapter.getItem(info.position);
		if(UserBranch!=null)
		{
			switch (item.getItemId()) {
//			case R.id.mn_Details:
//				startBranchDetailActivity(UserBranch);
//				break;
//            case R.id.mn_set_as_branch:
//            	changeChurchBranch(UserBranch);
//				break;
//			default:
//				break;
			}
		}
		
		return super.onContextItemSelected(item);
	}
	
	
	
	
	private void setAsUserDefaultBranch(church_branches userBranch) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater minflater= new MenuInflater(this);
		if(v.getId()== lstleaders.getId())
		{
			minflater.inflate(R.menu.branchlistmenu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	///get Church Leaders
	
	////Set up some house stufss
	private static class ViewHolder {
		
		ImageView image;
		TextView txtlocation;
		TextView txtname;
		Button btnSubscribe;
		
	}

	class ItemAdapter extends ArrayAdapter<church_branches> {

		List<church_branches> testimoniess;

		public ItemAdapter(Context context, int resource,
				List<church_branches> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			// TODO Auto-generated constructor stub
		}

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
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
		boolean isFollowing=false;
		
		void checkFollowing(String ID,ViewHolder holder )
		{
			//checkAlreadyBookedMarked
			Connector.dbhelper = new DBHelper(SubscribeChurchBranchFragment.this);
			isFollowing = OtherBookmarks.checkAlreadyBookedMarked(ID,subscription);
			
			if(isFollowing)
		   {
			   holder.btnSubscribe.setText("« Following");
		   }
		   else
		   {
			   holder.btnSubscribe.setText("+ Follow");
		   }
		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;

			final church_branches testimony = getItem(position);

			if (convertView == null) {
				view = getLayoutInflater().inflate(
						R.layout.churchbranchlistitem_subscribe, parent, false);
				holder = new ViewHolder();
				holder.btnSubscribe=(Button) view.findViewById(R.id.btnSubscribe);
				holder.image = (ImageView) view.findViewById(R.id.thumbImage);
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
			        
			        
			    	File	file= new File(filePath+"/"+Connector.AppFolder+"/"+imagename);
					if(file.exists() && file.isFile())
					{
						//Toast.makeText(this, imagename, Toast.LENGTH_LONG).show();
						holder.image.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 80, 80, false), 20) );
//						imageLoader.displayImage(file.getAbsolutePath(), holder.image, options,
//								animateFirstListener);
					}
					else
					{

							
						holder.image.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.appico), 80, 80, false), 20) );
						
					}
			        
			        
				String url ="" ;
				holder.txtname.setText(testimony.getbranchname());
				
				holder.txtlocation.setText(testimony.getlocation());
				checkFollowing(testimony.getchurchbranchid().toString(),holder);
				holder.btnSubscribe.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						//Toast.makeText(SubscribeChurchBranchFragment.this, "Hello", Toast.LENGTH_LONG).show();
						class manageFollowing extends AsyncTask<String, Void, String>
						{

							private ProgressDialog pdg;
					        

							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								pdg = new ProgressDialog(Connector.context);

								pdg.setMessage("Please Wait ...");
								pdg.setCancelable(false);
								pdg.show();
								super.onPreExecute();
							}

							@Override
							protected void onPostExecute(String result) {
								// TODO Auto-generated method stub
								pdg.cancel();
								Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
								if(result.equalsIgnoreCase("Following"))
								{
									followChurch(testimony);
								}
								else if(result.equalsIgnoreCase("UnFollowed"))
								{
									unFollowChurch(testimony);
								}
								
								super.onPostExecute(result);
							}
							
							
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								
								ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
								/*
								 * $name=$_POST['N']; $phone=$_POST['P']; $email=$_POST['E'];
								 * $password=$_POST['PSWD']; $username=$_POST['UN'];
								 */
								parameter.add(new BasicNameValuePair("reason", params[0]));
								parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
								parameter.add( new BasicNameValuePair("CID", testimony.getchurchid()+""));
								parameter.add(new BasicNameValuePair("BID",testimony.getchurchbranchid().toString()));
								String status = Connector.sendData(parameter, Connector.context);
								return status;
							}
							
						}
						
						
						
						
						 manageFollowing subscribe= new  manageFollowing();
							if(holder.btnSubscribe.getText().toString().equalsIgnoreCase("+ Follow"))
							{
								//followChurch();
								String [] data= {"Follow Church"};
								subscribe.execute(data);
							}
							else if(holder.btnSubscribe.getText().toString().equalsIgnoreCase("« Following"))
							{
								String [] data= {"UnFollow Church"};
								subscribe.execute(data);
							}
					}
				});
				

			}

			return view;
		}
		
		void followChurch(church_branches churchdata)
		{
			OtherBookmarks subscribedChurch=  new OtherBookmarks();
			subscribedChurch.setType(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
			
			if(churchdata!=null && churchdata.getchurchid()!=null)
			{
				subscribedChurch.setType(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
				subscribedChurch.setResource(churchdata.getBranchJson());
				subscribedChurch.setTypeid(churchdata.getchurchbranchid().toString());
				subscribedChurch.setTitle(churchdata.getbranchname());
				Connector.dbhelper = new DBHelper(SubscribeChurchBranchFragment.this);
				long result= Connector.dbhelper.addOtherBookMark(subscribedChurch);
				Connector.dbhelper.close();
				
				
				checkSubs();
				notifyDataSetChanged();
			}
			
		}
		void unFollowChurch(church_branches churchdata)
		{
			OtherBookmarks subscribedChurch=  new OtherBookmarks();
			subscribedChurch.setType(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
			subscribedChurch.setResource(churchdata.getBranchJson());
			subscribedChurch.setTypeid(churchdata.getchurchbranchid().toString());
			subscribedChurch.setTitle(churchdata.getbranchname());
			Connector.dbhelper = new DBHelper(SubscribeChurchBranchFragment.this);
			 Connector.dbhelper.deleteOtherBookmark(subscribedChurch.getTypeid());
			Connector.dbhelper.close();
		
			
			
			checkSubs();
			notifyDataSetChanged();
		}
		
		
		
		
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
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
			param.add(new BasicNameValuePair("CID",churchID));
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
