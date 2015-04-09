package com.appsol.apps.projectcommunicate.adapter;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appsol.apps.devotional.ChurchBranchFragment;
import com.appsol.apps.devotional.ChurchLeadersFragment;
import com.appsol.apps.devotional.ChurchMembersList;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Church;

public class ChurchDetailsPagerAdapter extends FragmentPagerAdapter {
	final int PAGE_COUNT = 4;
	private String titles[] = new String[]{"ABOUT", "LEADERS","BRANCHES","MEMBERS","ACTIVITIES"};
	
	
	public ChurchDetailsPagerAdapter(FragmentManager fm) {
		super(fm);
	
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 1:
		 ChurchLeadersFragment todayDevotionalFragment = new ChurchLeadersFragment();
			return todayDevotionalFragment;

		case 0:
			AboutChurchDetails historyDevotionalFragment = new AboutChurchDetails();
			return historyDevotionalFragment;
			
		case 2:
			 ChurchBranchFragment branches = new  ChurchBranchFragment();
				return branches;
		case 3:
			 ChurchMembersList cmembers = new  ChurchMembersList();
				return cmembers;
		
		
		}
		
		
		
		return null;
	}
public CharSequence getPageTitle(int position){
	return titles[position];
}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

	
	
	public static class AboutChurchDetails extends Fragment
	{
		private View rootView;
		Button btnChurch;
		TextView txtaddress;
		TextView txtwebsite;
		TextView txtfax,txtphone2,txtphone1,txtbn,txtlocation,txtdesc;
		Button btnSelectBranch;
		private String imagename;
		private String filePath;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getArguments() != null) {
				
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			rootView= inflater.inflate(R.layout.fragment_church_details, container,
					false);
			intiCompoments(rootView);
		return	rootView;
		}

		
			private void intiCompoments(View rootView) {
				// TODO Auto-generated method stub
				txtaddress = (TextView) rootView
						.findViewById(R.id.txtprayer);
			 txtwebsite = (TextView) rootView
						.findViewById(R.id.txtfreadings);
			 txtfax = (TextView) rootView
						.findViewById(R.id.txtreflection);
			 txtphone2 = (TextView) rootView
						.findViewById(R.id.txtphone2);
			 txtphone1 = (TextView) rootView
						.findViewById(R.id.txtreading);
			 txtdesc=(TextView) rootView.findViewById(R.id.txtdesc);
//			 txtbn = (TextView) rootView
//						.findViewById(R.id.txtbn);
			 txtlocation = (TextView) rootView
						.findViewById(R.id.txtTitle);
			 txtaddress.setText("");
				//txtbn.setText("");
				txtwebsite.setText("");
				txtfax.setText("");
				txtphone1.setText("");
				txtphone2.setText("");
				txtlocation.setText("");
				txtdesc.setText("");
			
			 // Retrieve Church Details and display to the user
		    String churchinfo = Connector.getChurchJson();
		   // Toast.makeText(getActivity(), churchinfo, Toast.LENGTH_LONG).show();
		    if(churchinfo.equalsIgnoreCase("NOT_SET"))
		    {
		    	//start the Asynctask to fetch the details.
		    	AsyncTask<Void, Integer, String> fetchChurchDetails =  new AsyncTask<Void, Integer, String>() {
					
					
		    		@Override
					protected String doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						ArrayList<NameValuePair> parameters= new ArrayList<NameValuePair>();
						if(Connector.myPrefs.getString("CHURCH_ID","").length()>0 && !Connector.getChurchID().equalsIgnoreCase("NO_CHURCH_FOUND"))
						{
							parameters.add( new BasicNameValuePair("CID",Connector.getChurchID() ));
							parameters.add( new BasicNameValuePair("reason","get Church Details"));
							String response="";
							response= Connector.sendData(parameters, Connector.context);
									

										// TODO: register the new account here.
							
							  
										return response;
						}
							return "";
					}
		    		
		    		
					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						Connector.setChurchJson(result);
						try {
							processChurchJson(result);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onPostExecute(result);
					}
				};
				fetchChurchDetails.execute();
				
		    }
		    else
		    {
		    	try {
					processChurchJson(churchinfo);
					
					
		AsyncTask<Void, Integer, String> fetchChurchBranches =  new AsyncTask<Void, Integer, String>() {
						
						@Override
						protected String doInBackground(Void... arg0) {
							// TODO Auto-generated method stub
							ArrayList<NameValuePair> parameters= new ArrayList<NameValuePair>();
							if(!Connector.getChurchID().equalsIgnoreCase("NO_CHURCH_FOUND"))
							{
								parameters.add( new BasicNameValuePair("CID",Connector.getChurchID()));
								parameters.add( new BasicNameValuePair("reason","get Church Branch List"));
								String response="";
								response= Connector.sendData(parameters, Connector.context);
										

											// TODO: register the new account here.
											return response;
							}
								return "NOT_SET";
						}
						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							Connector.setBranches(result);
							 if(Connector.getBranch()=="NOT_SET")
							 {
								 //btnChurch.setVisibility(View.GONE);
							 }
							 else
							 {
								 //btnChurch.setVisibility(View.VISIBLE);
							 }
							super.onPostExecute(result);
						}
					};
					
					fetchChurchBranches.execute();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
				
			}	
		
			
			
			private void processChurchJson(String churchinfo) throws JSONException {
				// TODO Auto-generated method stub
			JSONObject object = new  JSONObject(churchinfo);
				
				if(object!=null)
				{
					/*
					 * church_id as 'CID',church_name as 'CN',`church_logo` as 'CL',
				`church_description` as 'CD',`website` as 'CW',`fax` as 'fax',`phone1` as 'Tel1',`phone2` as 'Tel2',
				`phone3` as as 'Tel3'
				,`address` as 'CA',`christianity_type` as 'CT'
					 */
					filePath= Environment.getExternalStorageDirectory().getPath();
					Church churchdata= new Church();
					churchdata.setchurchname(object.optString("CN"));
					churchdata.setchristianitytype(object.optString("CT"));
					churchdata.setchurchdescription(object.optString("CD"));
					churchdata.setwebsite(object.optString("CW"));
					churchdata.setfax(object.optString("fax"));
					churchdata.setphone1(object.optString("Tel1"));
					churchdata.setphone2(object.optString("Tel2"));
					churchdata.setaddress(object.optString("CA"));
					churchdata.setheadofficelocation(object.optString("HeadLocation"));
					churchdata.setphone3(object.optString("Tel3"));
					churchdata.setchurchid(object.optInt("CID"));
					txtdesc.setText(churchdata.getchurchdescription());
					
					txtaddress.setText(churchdata.getaddress());
					//txtbn.setText(churchdata.getchurchname());
					txtwebsite.setText(churchdata.getwebsite());
					txtfax.setText(churchdata.getfax());
					txtphone1.setText(churchdata.getphone1());
					txtphone2.setText(churchdata.getphone2());
					txtlocation.setText(churchdata.getheadofficelocation());
					
					
				}
			}
			
			
			
			
	}
	
	
	
	
	
	
	
}
