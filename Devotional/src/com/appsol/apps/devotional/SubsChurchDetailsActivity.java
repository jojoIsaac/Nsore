package com.appsol.apps.devotional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.appsol.apps.devotional.ProgressActivity.setUserChurch;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.OtherBookmarks;
import com.appsol.apps.projectcommunicate.model.SubacribedEvents;
import com.squareup.picasso.Picasso;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubsChurchDetailsActivity extends ActionBarActivity {

	Button btnContinue;
	TextView txtaddress;
	TextView txtwebsite;
	TextView txtfax,txtphone2,txtphone1,txtbn,txtlocation,txtdesc;
	ImageView imgprofile;
	Button btnSelectBranch;
	private String imagename;
	private String filePath;
	static String churchdatas;
	private static Church churchdata;
	static Integer churchid;
	
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
			
			if(result.equalsIgnoreCase("Following"))
			{
				followChurch();
			}
			else if(result.equalsIgnoreCase("UnFollowed"))
			{
				unFollowChurch();
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
			parameter.add( new BasicNameValuePair("CID", churchdata.getchurchid()+""));
			parameter.add(new BasicNameValuePair("BID","-1"));
			String status = Connector.sendData(parameter, Connector.context);
			return status;
		}
		
	}
	
	void followChurch()
	{
		OtherBookmarks subscribedChurch=  new OtherBookmarks();
		subscribedChurch.setType(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH);
		
		if(churchdata!=null && churchdata.getchurchid()!=null)
		{
			subscribedChurch.setResource(churchdata.getChurchJson());
			subscribedChurch.setTypeid(churchdata.getchurchid()+"");
			subscribedChurch.setTitle(churchdata.getchurchname());
			Connector.dbhelper = new DBHelper(this);
			long result= Connector.dbhelper.addOtherBookMark(subscribedChurch);
			Connector.dbhelper.close();
			
			
			checkFollowing();
		}
		
	}
	void unFollowChurch()
	{
		OtherBookmarks subscribedChurch=  new OtherBookmarks();
		subscribedChurch.setType(OtherBookmarks.TYPE_SUBSCRIBED_CHURCH);
		subscribedChurch.setResource(churchdata.getChurchJson());
		subscribedChurch.setTypeid(churchdata.getchurchid().toString());
		subscribedChurch.setTitle(churchdata.getchurchname());
		Connector.dbhelper = new DBHelper(this);
		 Connector.dbhelper.deleteOtherBookmark(subscribedChurch.getTypeid());
		Connector.dbhelper.close();
		
		
		checkFollowing();
	}
	boolean isFollowing=false;
	void checkFollowing()
	{
		//checkAlreadyBookedMarked
		Connector.dbhelper = new DBHelper(this);
		isFollowing = OtherBookmarks.checkAlreadyBookedMarked(churchdata.getchurchid().toString(), OtherBookmarks.TYPE_SUBSCRIBED_CHURCH);
	   if(isFollowing)
	   {
			btnContinue.setText("« Following");
	   }
	   else
	   {
		   btnContinue.setText("+ Follow");
	   }
	   
	   Connector.dbhelper.close();
	}
	String branchCount="0";
	LinearLayout layoutBranches;
	Button btnBranches;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_subschurchdetail);
	Connector.myPrefs= Connector.context.getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
	String filePath= Environment.getExternalStorageDirectory().getPath();
	Connector.context=this;
	 churchdata= new Church();
	 btnBranches=(Button) findViewById(R.id.btnBranches);
	 layoutBranches= (LinearLayout) findViewById(R.id.layoutBranches);
	txtaddress = (TextView) findViewById(R.id.txtprayer);
 txtwebsite = (TextView)findViewById(R.id.txtfreadings);
 txtfax = (TextView) findViewById(R.id.txtreflection);
 txtphone2 = (TextView) findViewById(R.id.txtphone2);
 txtphone1 = (TextView) findViewById(R.id.txtreading);
 txtdesc=(TextView)  findViewById(R.id.txtdesc);
// txtbn = (TextView) rootView
//			.findViewById(R.id.txtbn);
 txtlocation = (TextView) findViewById(R.id.txtTitle);
 txtaddress.setText("");
	//txtbn.setText("");
	txtwebsite.setText("");
	txtfax.setText("");
	txtphone1.setText("");
	txtphone2.setText("");
	txtlocation.setText("");
	txtdesc.setText("");
	imgprofile= (ImageView) findViewById(R.id.imgprofile);
	btnContinue= (Button) findViewById(R.id.btnContinue);
	btnBranches.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent ints= new Intent(SubsChurchDetailsActivity.this,SubscribeChurchBranchFragment.class);
			ints.putExtra("CID", churchdata.getchurchid().toString());
			startActivity(ints);
		}
	});
	btnContinue.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//DownloadFile
//			setUserChurch st= new setUserChurch();
//			st.execute();
			 manageFollowing subscribe= new  manageFollowing();
			if(btnContinue.getText().toString().equalsIgnoreCase("+ Follow"))
			{
				//followChurch();
				String [] data= {"Follow Church"};
				subscribe.execute(data);
			}
			else if(btnContinue.getText().toString().equalsIgnoreCase("« Following"))
			{
				String [] data= {"UnFollow Church"};
				subscribe.execute(data);
			}
		}
	});
	 churchdatas= getIntent().getExtras().getString("DATA");
	 branchCount = getIntent().getExtras().getString("BC");
	 
	 if(branchCount.equalsIgnoreCase("0")|| branchCount.equalsIgnoreCase("1") )
		 layoutBranches.setVisibility(View.GONE);
	 else
		 layoutBranches.setVisibility(View.VISIBLE);
	 
	 //layoutBranches.setVisibility(View.GONE);
	 
	//Toast.makeText(this, data, Toast.LENGTH_LONG).show();
	if(churchdatas!=null)
	{
		try {
			processChurchJson(churchdatas);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}






ProgressDialog mProgressDialog;




@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	return super.onCreateOptionsMenu(menu);
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
		//Toast.makeText(getApplicationContext(), object.getString("Branchcount"), Toast.LENGTH_LONG).show();
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
		churchdata.setchurchlogo(object.optString("CL"));
		churchdata.setChurchJson(object.toString());
		churchid=churchdata.getchurchid();
		txtdesc.setText(churchdata.getchurchdescription());
		
		txtaddress.setText(churchdata.getaddress());
		//txtbn.setText(churchdata.getchurchname());
		txtwebsite.setText(churchdata.getwebsite());
		txtfax.setText(churchdata.getfax());
		txtphone1.setText(churchdata.getphone1());
		txtphone2.setText(churchdata.getphone2());
		txtlocation.setText(churchdata.getheadofficelocation());
		String logo= Connector.ChurchLogoURL+ churchdata.getchurchlogo();
		checkFollowing();
		String branchCount=object.getString("Branchcount");
		 if(branchCount.equalsIgnoreCase("0")|| branchCount.equalsIgnoreCase("1") )
			 layoutBranches.setVisibility(View.GONE);
		 else
			 layoutBranches.setVisibility(View.VISIBLE);
		 
		 //layoutBranches.setVisibility(View.GONE);
		 
		if(!logo.equalsIgnoreCase(Connector.ChurchLogoURL))
		{
		Picasso.with(Connector.context)
		  .load(logo)
		  .resize(250, 250)
		  .centerCrop()
		  .transform(new CropSquareTransformation())
		  .error(R.drawable.appico)
		  .into(imgprofile);
		
		
		
		
		
		
		
		}
		
	}
}
}
