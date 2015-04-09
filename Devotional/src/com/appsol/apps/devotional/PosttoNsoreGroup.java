package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class PosttoNsoreGroup extends ActionBarActivity {

	private String churchGroupName, logo;
	private static String id;
	private String type;
	private String members;
	private String status;
	private String Clogo;
	private String churchname;
	private String intentString="";

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		/*
		 * intent.putExtra("id",id);
		intent.putExtra("GN", churchGroupName);
		intent.putExtra("intent", "Text-POST");
		 */
		super.onSaveInstanceState(outState);
		outState.putString("id", id);
		outState.putString("GN", churchGroupName);
		outState.putString("intent", intentString);
		
	}
	Fragment postFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postto_nsore_group);
		Connector.context= this;
		Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
		churchGroupName = getIntent().getStringExtra("GN");
		id = getIntent().getStringExtra("id");
		intentString = getIntent().getStringExtra("intent");
		Bundle data= new Bundle();
		//members = getIntent().getStringExtra("membersCount");
		if (savedInstanceState != null) {
			/*
			 * outState.putString("id", id);
		outState.putString("GN", churchGroupName);
		outState.putString("intent", intentString);
			 */
			churchGroupName = savedInstanceState.getString("GN");
			id = savedInstanceState.getString("id");
			intentString = savedInstanceState.getString("intent");
			data.putString("id",id);
			data.putString("GN",churchGroupName);
			postFragment = new PlaceholderFragment();
			postFragment.setArguments(data);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container,postFragment).addToBackStack(null).commit();
			//data.putString("i", value)
			
		} else {
			churchGroupName = getIntent().getStringExtra("GN");
			id = getIntent().getStringExtra("id");
			intentString = getIntent().getStringExtra("intent");
			data.putString("id",id);
			data.putString("GN",churchGroupName);
			postFragment = new PlaceholderFragment();
			postFragment.setArguments(data);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container,postFragment).addToBackStack(null).commit();
			
		}
		
		if(intentString.equalsIgnoreCase("POST-TEXT"))
		{
			
		}
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.postto_nsore_group, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	static ProgressDialog saveDialog;
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_postto_nsore_group, container, false);
			final EditText body= (EditText) rootView.findViewById(R.id.body);

			//body.setText(prayer);
			///

					
			Button btnconnect =(Button) rootView.findViewById(R.id.btnsavenote);
			Button btnCancel=(Button) rootView.findViewById(R.id.btnCancel);


			btnconnect.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// TODO Auto-generated method stub
					
					saveDialog = new ProgressDialog(getActivity());
					saveDialog.setMessage("Posting to group wall ...");
					saveDialog.setCancelable(false);
				
					
					String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
					StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
							createMyReqSuccessListeners(), createMyReqErrorListener()) {
						

						@Override
						protected Map<String, String> getParams() {
							 String spinsVal;
							
							Map<String, String> params = new HashMap<String, String>();
						
							 if(id!=null)
							params.put("GID", id);
							 params.put("senderid", Connector.getUserId());
							 params.put("UN", Connector.getUserDetails().get(1));
							params.put("userpost", body.getText().toString());
							
							params.put("reason","Save Nsore Group Feed");
							
							return params;
						}
					};
					InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

					if(!TextUtils.isEmpty( body.getText().toString().trim()) )
					{
						RequestQueue queue = Volley.newRequestQueue(Connector.context,
								new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
						// Adding request to volley request queue
						queue.add(jsonReq);	
						saveDialog.show();
					}
					
					
				}

				private Listener<String> createMyReqSuccessListeners() {
					// TODO Auto-generated method stub
					  return new Response.Listener<String>() {
				        	
				        	
				        	
				        	
				        	
				            @Override
				            public void onResponse(String response) {
				            	//Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
				            	saveDialog.dismiss();
				            	if(response.trim().contains("Data Saved"))
				            	{
				            		getActivity().finish();
				            	}
				            	
				            }
				        };
					
				}
			});
			return rootView;
		}
	}
	
	private static Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//progressbarLoad.setVisibility(View.GONE);
				saveDialog.dismiss();
				if(error!=null)
				{
				showErrorDialog(Connector.HandleVolleyerror(error, Connector.context));
				if(error.getMessage()!=null)
				Log.d("RES", error.getMessage());
				}
				else
				{
					
					showErrorDialog("An error occured. Please check your internet Connection");
				}
				
			}
		};
	}
	
	private static void showErrorDialog(String error) {
		//mInError = true;

		AlertDialog.Builder b = new AlertDialog.Builder(Connector.context);
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
				//handleServerRequest(churchgroup, btntoggle_status);
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

}
