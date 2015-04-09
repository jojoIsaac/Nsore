package com.appsol.apps.projectcommunicate.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.FeedImageView;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.PrayerRequest;
import com.appsol.apps.projectcommunicate.model.SubacribedEvents;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.apps.projectcommunicate.model.church_branches;
import com.appsol.apps.projectcommunicate.model.church_branches.BranchActivity;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

public class BranchActivityFeedListAdapter extends
		ArrayAdapter<church_branches.BranchActivity> {
	private Activity activity;
	private LayoutInflater inflater;
	private List<church_branches.BranchActivity> feedItems;
	Context context;
	ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager
			.getInstance().getImageLoader();

	public BranchActivityFeedListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	


	public List<BranchActivity> getBookmarksList() {
		return feedItems;
	}

	public BranchActivityFeedListAdapter(Activity activity,int resource,
			
			List<church_branches.BranchActivity> feedItems) {
		super(activity, resource, feedItems);
		this.activity = activity;
		context = activity.getBaseContext();
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public BranchActivity getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.eventfeed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.profilePic);
		TextView txttitle = (TextView) convertView.findViewById(R.id.txttitle);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);
		final TextView like_statement = (TextView) convertView
				.findViewById(R.id.like_statement);
		LinearLayout layout_share_like = (LinearLayout) convertView
				.findViewById(R.id.layout_share_like);

		final church_branches.BranchActivity item = feedItems.get(position);

		name.setText(item.getActivity().toUpperCase());

		timestamp.setText("Day:  " + item.getDay() + "    Time:  " + item.getTime());

		if (!TextUtils.isEmpty(item.getlocation())) {
			txttitle.setText("Venue: " + item.getVenue());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			txttitle.setVisibility(View.GONE);
		}
		like_statement.setVisibility(View.GONE);

		// layout_share_like.setVisibility(View.GONE);

		if (!TextUtils.isEmpty(item.getDescription())) {
			// int length= item.getDescription().length();

			// if(length<=300)
			statusMsg.setText(item.getDescription());
			// else
			// statusMsg.setText(item.getDescription().subSequence(0,
			// 299)+" ...");
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url

		// url is null, remove from the view
		url.setVisibility(View.GONE);

		profilePic.setImageUrl(Connector.ChurchLogoURL + item.getchurchlogo(),
				imageLoader);

		feedImageView.setVisibility(View.GONE);

		final Button shareEvent = (Button) convertView
				.findViewById(R.id.shareEvent);
		shareEvent.setOnClickListener(new OnClickListener() {

			private String messageToShare;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				messageToShare = "I would like to invite you to an upcoming event by the "
						+ item.getchurchname()
						+ "\n"
						+ "Titled: "
						+ item.getActivity()
						+ "\nDay: "
						+ item.getDay()
						+ " \nTime: "
						+ ""
						+ item.getTime()
						+ "\nLocation: "
						+ item.getVenue() + " \nShared from Nsore Devotional";
				Intent ints = new Intent(Intent.ACTION_SEND);
				ints.putExtra(Intent.EXTRA_TEXT, messageToShare);
				ints.setType("text/plain");

				Connector.context.startActivity(ints);
			}
		});
		// Handle the attendance button
		final Button willAttend = (Button) convertView
				.findViewById(R.id.willAttend);
		willAttend.setVisibility(View.GONE);

		return convertView;
	}

}