package com.appsol.apps.devotional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;


import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChurchLeaderDetails extends ActionBarActivity {
public static Bitmap userProfile;
//private DisplayImageOptions options;
//protected ImageLoader imageLoader = ImageLoader.getInstance();
private TextView textname,txtPosition,txtdesc;
//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();

CircularNetworkImageView profileImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_church_leader_details);
		setUpUi();
	}
	private void setUpUi() {
		
		// TODO Auto-generated method stub
		txtdesc=(TextView) findViewById(R.id.txtdesc);
		textname=(TextView) findViewById(R.id.textname);
		txtPosition=(TextView) findViewById(R.id.txtPosition);
		profileImage=(CircularNetworkImageView) findViewById(R.id.profileImage);
		
		String name = getIntent().getExtras().getString("N");
		String position= getIntent().getExtras().getString("P");
		String profile=getIntent().getExtras().getString("PF");
		String imagepath= getIntent().getExtras().getString("DP");
		
		textname.setText(name);
		txtdesc.setText(profile);
		txtPosition.setText(position);
		
		String url = Connector.leadersImages + imagepath;
		if(!url.equalsIgnoreCase(Connector.leadersImages))
		{
			
			profileImage.setImageUrl(url, imageLoader);
			 
		}
		else
		{
			profileImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
			 
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.church_leader_details, menu);
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
}
