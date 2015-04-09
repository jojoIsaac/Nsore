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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.login.Entry;
import com.squareup.picasso.Picasso;

public class ProgressActivity extends ActionBarActivity {

	void endChurchChanage()
	{
		if(churchdatas!=null)
			Connector.setChurchJson(churchdatas);
			Editor edt= Connector.myPrefs.edit();
			edt.putBoolean("REG_USER",true);
			edt.putString("CHURCH_ID",churchdata.getchurchid().toString());
			edt.putString("CHURCH_NAME", churchdata.getchurchname());
			edt.commit();
			
			Connector.setChurchID(Connector.context		, churchdata.getchurchid().toString());
			String filepath = Environment.getExternalStorageDirectory()
					.getPath();
			File f= new File(filepath + "/"
					+ Connector.AppFolder+"/"+churchdata.getchurchlogo());
			if(f.exists()&& f.isFile())
			{
				Connector.setChurchImage(churchdata.getchurchlogo());
			}
			
			if(logoutIfNew)
			{
				Connector.setisLoggedIn(Connector.context, false);
				Intent ints= new Intent(Connector.context, Entry.class);
				
				startActivity(ints);
				((Activity) Connector.context).finish();
			}
			else
			{
				Connector.setisLoggedIn(Connector.context, true);
				Intent ints= new Intent(Connector.context, MainActivity.class);
				startActivity(ints);
				((Activity) Connector.context).finish();
			}
	}
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
		
		class setUserChurch extends AsyncTask<Void, Integer, String>
		{
			ProgressDialog pdg;
	@Override
	protected void onPreExecute() {
	// TODO Auto-generated method stub
	 pdg= new ProgressDialog(Connector.context);
	 
	 pdg.setMessage("Please Wait. Setting Preffered Church");
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
		Log.i("D",result);
		try {
			JSONObject object = new  JSONObject(result);
			
			if(object!=null)
			{
				/*
				 * 	$message['message']='Account Created';
		$message['id']=$id;
				 */
				String status= object.optString("status");
				if(status.equalsIgnoreCase("Completed"))
				{
					String logo= Connector.ChurchLogoURL+ churchdata.getchurchlogo();
					if(!logo.equalsIgnoreCase(Connector.ChurchLogoURL))
					{
						DownloadFile df= new DownloadFile();
						String url=logo;
						df.execute(logo);
						
					}
					else
					{
						endChurchChanage();
					}
				}
			}	
	}
		catch(Exception e)
		{
			
		}
	}
	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		if(Connector.isInternetAvailable(ProgressActivity.this))
		{
			ArrayList<NameValuePair> parameter= new ArrayList<NameValuePair>();
			
			parameter.add(new BasicNameValuePair("reason","Set Church"));
			/*
			 * $uID= $_POST['U'];
$church=$_POST['C'];
$phone=$_POST['P'];
			 */
			parameter.add(new BasicNameValuePair("U",Connector.getUserId()));
			parameter.add(new BasicNameValuePair("P",Connector.contact));
			
			parameter.add(new BasicNameValuePair("C",churchdata.getchurchid().toString()));
			
			String status= Connector.sendData(parameter, Connector.context);
			return status;
		}
		else
		return "No internet Connection";
	}
	
	


		}
		
		
		
		
		public static boolean logoutIfNew=false;	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		Connector.myPrefs= Connector.context.getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
		String filePath= Environment.getExternalStorageDirectory().getPath();
		Connector.context=this;
		 churchdata= new Church();
		txtaddress = (TextView) findViewById(R.id.txtprayer);
	 txtwebsite = (TextView)findViewById(R.id.txtfreadings);
	 txtfax = (TextView) findViewById(R.id.txtreflection);
	 txtphone2 = (TextView) findViewById(R.id.txtphone2);
	 txtphone1 = (TextView) findViewById(R.id.txtreading);
	 txtdesc=(TextView)  findViewById(R.id.txtdesc);
//	 txtbn = (TextView) rootView
//				.findViewById(R.id.txtbn);
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
		btnContinue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//DownloadFile
				setUserChurch st= new setUserChurch();
				st.execute();
			}
		});
		 churchdatas= getIntent().getExtras().getString("DATA");
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
	
	// DownloadFile AsyncTask
	private class DownloadFile extends AsyncTask<String, Integer, String> {

		private boolean logoutIfNew;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create progress dialog
			mProgressDialog = new ProgressDialog(Connector.context);
			// Set your progress dialog Title
			mProgressDialog.setTitle("Nsore Devotional");
			// Set your progress dialog Message
			mProgressDialog.setMessage("Downloading ChurchLogo, Please Wait!");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Show progress dialog
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... Url) {
			try {
				URL url = new URL(Url[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				// Detect the file lenghth
				int fileLength = connection.getContentLength();

				// Locate storage location
				String filepath = Environment.getExternalStorageDirectory()
						.getPath();

				// Download the file
				InputStream input = new BufferedInputStream(url.openStream());

				// Save the downloaded file
				OutputStream output = new FileOutputStream(filepath + "/"
						+ Connector.AppFolder+"/"+churchdata.getchurchlogo());

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// Publish the progress
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				// Close connection
				output.flush();
				output.close();
				input.close();
				return "DONE";
			} catch (Exception e) {
				// Error Log
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// Update the progress dialog
			mProgressDialog.setProgress(progress[0]);
			// Dismiss the progress dialog
			//mProgressDialog.dismiss();
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equalsIgnoreCase("DONE"))
			{
				mProgressDialog.cancel();
				if(churchdatas!=null)
				Connector.setChurchJson(churchdatas);
				Editor edt= Connector.myPrefs.edit();
				edt.putBoolean("REG_USER",true);
				edt.putString("CHURCH_ID",churchdata.getchurchid().toString());
				edt.putString("CHURCH_NAME", churchdata.getchurchname());
				edt.commit();
				
				Connector.setChurchID(Connector.context		, churchdata.getchurchid().toString());
				String filepath = Environment.getExternalStorageDirectory()
						.getPath();
				File f= new File(filepath + "/"
						+ Connector.AppFolder+"/"+churchdata.getchurchlogo());
				if(f.exists()&& f.isFile())
				{
					Connector.setChurchImage(churchdata.getchurchlogo());
				}
				
				if(logoutIfNew)
				{
					Connector.setisLoggedIn(Connector.context, false);
					Intent ints= new Intent(Connector.context, Entry.class);
					
					startActivity(ints);
					((Activity) Connector.context).finish();
				}
				else
				{
					Connector.setisLoggedIn(Connector.context, true);
					Intent ints= new Intent(Connector.context, MainActivity.class);
					startActivity(ints);
					((Activity) Connector.context).finish();
				}
			}
			super.onPostExecute(result);
		}
	}
	
	
	
	
	
	
	
	private class DownloadWallpaperTask extends AsyncTask<String, Integer, String> {

		private ProgressDialog downloadProgressDialog;
		private String filepath;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			downloadProgressDialog = new ProgressDialog(Connector.context);
			downloadProgressDialog.setTitle("Downloading Wallpaper");
			downloadProgressDialog.setMessage("Downloading in progress...");
			downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			downloadProgressDialog.setProgress(0);
			downloadProgressDialog.show();
			Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Locate storage location
		 filepath = Environment.getExternalStorageDirectory()
					.getPath();
			String wallpaperURLStr = params[0];
			String localPath = filepath + "/"
					+ Connector.AppFolder+"/"+churchdata.getchurchlogo();
			try {
				URL wallpaperURL = new URL(wallpaperURLStr);
				URLConnection connection = wallpaperURL.openConnection();
				
				//get file length
				int filesize = connection.getContentLength();
				if(filesize < 0) {
					downloadProgressDialog.setMax(1000000);
				} else {
					downloadProgressDialog.setMax(filesize);
				}
				
				InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
				//String appName = getResources().getString(R.string.app_name);
				OutputStream outputStream = openFileOutput(localPath, Context.MODE_PRIVATE);
				byte buffer[] = new byte[1024];
				int dataSize;
				int loadedSize = 0;
	            while ((dataSize = inputStream.read(buffer)) != -1) {
	            	loadedSize += dataSize;
	            	publishProgress(loadedSize);
	            	outputStream.write(buffer, 0, dataSize);
	            }
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return localPath;
		}
		
		
		protected void onProgressUpdate(Integer... progress) {
			downloadProgressDialog.setProgress(progress[0]);
		}
		
		protected void onPostExecute(String result) {
			downloadProgressDialog.hide();
			//
			File f= new File(filepath + "/"
					+ Connector.AppFolder+"/"+churchdata.getchurchlogo());
			if(f.exists()&& f.isFile())
			{
				Connector.setChurchImage(churchdata.getchurchlogo());
			}
			//open preview activity
//			Bundle postInfo = new Bundle();
//			postInfo.putString("localpath", result);

			
		}
	}

	
	
	
	
	
	Bitmap getChurchLogo(ImageView imageView)
	{
		imageView.setDrawingCacheEnabled(true);
		 imageView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
		                   MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		 imageView.layout(0, 0, 
		                  imageView.getMeasuredWidth(), imageView.getMeasuredHeight()); 
		 imageView.buildDrawingCache(true);
		 Bitmap bmap = Bitmap.createBitmap(imageView.getDrawingCache());
		 imageView.setDrawingCacheEnabled(false);
		 return bmap;
	}
	
	
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
