package com.appsol.apps.devotional;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.church_branches;
import com.appsol.apps.projectcommunicate.model.church_branches.BranchActivity;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BranchDetailActivity extends ActionBarActivity {
	TextView txtaddress;
	TextView txtwebsite;
	TextView txtfax, txtphone2, txtphone1, txtbn, txtlocation, txtdesc,
			txt_events;
	private static int churchbranchid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_church_branch_details);
		intiCompoments();
	}

	Button btnBranches;
	LinearLayout layoutBranches;
	church_branches branch;

	private void intiCompoments() {
		// TODO Auto-generated method stub
		txtaddress = (TextView) findViewById(R.id.txtprayer);
		txtwebsite = (TextView) findViewById(R.id.txtfreadings);
		txtfax = (TextView) findViewById(R.id.txtreflection);
		txtphone2 = (TextView) findViewById(R.id.txtphone2);
		txtphone1 = (TextView) findViewById(R.id.txtreading);
		txtdesc = (TextView) findViewById(R.id.txtdesc);
		txt_events = (TextView) findViewById(R.id.txt_events);
		txtlocation = (TextView) findViewById(R.id.txtTitle);
		txtaddress.setText("");
		txtwebsite.setText("");
		txtfax.setText("");
		txtphone1.setText("");
		txtphone2.setText("");
		txtlocation.setText("");
		txtdesc.setText("");
		btnBranches = (Button) findViewById(R.id.btnBranches);
		layoutBranches = (LinearLayout) findViewById(R.id.layoutBranches);
		String json = getIntent().getExtras().getString("json");

		if (json != null) {
			processJson(json);
		}

		btnBranches.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (branch != null) {
					changeChurchBranch(branch);
				}
			}
		});
		txt_events.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//BranchWeeklyActivities weeklyActivities= new BranchWeeklyActivities();
				Intent intents= new Intent(BranchDetailActivity.this, BranchWeeklyActivities.class);
				intents.putExtra("CID",branch.getchurchid().toString());
				intents.putExtra("BID",branch
						.getchurchbranchid().toString());
				intents.putExtra("BN",branch.getbranchname());
				intents.putExtra("CN",branch.getchurchid().toString());
				startActivity(intents);
			}
		});
	}

	/*
	 * churchID = data.getString("CID");
			ChurchName = data.getString("CN");
			BranchID= data.getString("BID");
			BranchName=data.getString("BN");
	 */
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.branch_detail, menu);
		return true;
	}

	void processJson(String result) {
		try {
			JSONObject objects = new JSONObject(result);

			if (objects != null) {

				branch = new church_branches();
				churchbranchid = objects.optInt("church_branch_id");
				Integer churchid = objects.optInt("church_id");
				String branchname = objects.optString("branch_name");
				String location = objects.optString("location");
				String address = objects.optString("address");
				String phone1 = objects.optString("phone1");
				String phone2 = objects.optString("phone2");
				String phone3 = objects.optString("phone3");
				String website = objects.optString("website");
				String fax = objects.optString("fax");
				txtdesc.setText(branchname);

				txtaddress.setText(address);
				// txtbn.setText(churchdata.getchurchname());
				txtwebsite.setText(website);
				txtfax.setText(fax);
				txtphone1.setText(phone1);
				txtphone2.setText(phone2);
				txtlocation.setText(location);

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

				if (Connector.getBranch().equalsIgnoreCase(
						String.valueOf(branch.getchurchbranchid()))) {
					layoutBranches.setVisibility(View.GONE);
				}
				// String userBranch= Connector.get

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	protected void changeChurchBranch(final church_branches branch) {
		// TODO Auto-generated method stub
		// Toast.makeText(getActivity(), branch.toString(),
		// Toast.LENGTH_LONG).show();

		AlertDialog.Builder build = new Builder(BranchDetailActivity.this);
		build.setTitle("Change Branch")
				.setMessage("Do you want to proceed?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								AsyncTask<Void, Void, String> changebranch = new AsyncTask<Void, Void, String>() {

									@Override
									protected void onPostExecute(String result) {
										// TODO Auto-generated method stub
										if (result.equalsIgnoreCase("Saved")) {
											Connector.setBranch(branch
													.getchurchbranchid()
													.toString());
											Toast.makeText(
													BranchDetailActivity.this,
													"Church Branch Changed",
													Toast.LENGTH_LONG).show();
											BranchDetailActivity.this.finish();
										}

										super.onPostExecute(result);
									}

									@Override
									protected String doInBackground(
											Void... arg0) {
										ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();

										parameter.add(new BasicNameValuePair(
												"reason",
												"Change Church member Branch"));
										parameter.add(new BasicNameValuePair(
												"BID", branch
														.getchurchbranchid()
														.toString()));
										parameter.add(new BasicNameValuePair(
												"UID", Connector.getUserId()));
										String status = Connector.sendData(
												parameter,
												BranchDetailActivity.this);
										return status;
									}
								};
								changebranch.execute();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).show();

	}

}
