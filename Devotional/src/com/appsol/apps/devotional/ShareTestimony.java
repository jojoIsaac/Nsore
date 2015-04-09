package com.appsol.apps.devotional;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Connector;

public class ShareTestimony extends ActionBarActivity {
	EditText txtContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_share_testimony);
		txtContent= (EditText) findViewById(R.id.body);
		
	}
 public void CancelTestimony(View v) {
	    
		
			finish();
		}
	public void shareTestimony(View v) {
    
		
		if(txtContent.getText().toString().length()>0 && !txtContent.getText().toString().equalsIgnoreCase(""))
		{
			(new shareTestimony()).execute();
		}
	}
	
	
	class shareTestimony extends AsyncTask<Void, Integer, String>
	{
		
		ProgressDialog pdg;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pdg= new ProgressDialog(ShareTestimony.this);
			pdg.setMessage("Please Wait ...");
			pdg.setCancelable(false);
			pdg.show();
			super.onPreExecute();
		}
		
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	pdg.dismiss();
	Toast.makeText(ShareTestimony.this, result, Toast.LENGTH_LONG).show();
	if(result.equalsIgnoreCase("Testimony saved"))
	{
		finish();
	}
	
	super.onPostExecute(result);
}
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> parameters= new ArrayList<NameValuePair>();
			if(Connector.myPrefs.getString("CHURCH_ID","").length()>0 && !Connector.getChurchID().equalsIgnoreCase("NO_CHURCH_FOUND"))
			{
				parameters.add( new BasicNameValuePair("CID",Connector.myPrefs.getString("CHURCH_ID","") ));
				parameters.add( new BasicNameValuePair("USER_ID",Connector.getUserId() ));
				parameters.add( new BasicNameValuePair("reason","Save Testimony"));
				parameters.add( new BasicNameValuePair("content",txtContent.getText().toString()));
				String response="";
				response= Connector.sendData(parameters, Connector.context);
						

							// TODO: register the new account here.
							return response;
			}
				return "NOT_SET";
		}
		
	}
}
