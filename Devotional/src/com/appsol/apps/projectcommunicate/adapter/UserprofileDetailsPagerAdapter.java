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
import com.appsol.apps.devotional.FriendsList;
import com.appsol.apps.devotional.R;
import com.appsol.apps.devotional.UserDetails;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;

public class UserprofileDetailsPagerAdapter extends FragmentPagerAdapter {
	final int PAGE_COUNT = 2;
	private String titles[] = new String[]{"ABOUT ME", "FRIENDS","Requests"};
	private NsoreDevotionalUser nsoreUser;
	private Bundle dataPackage;
	public UserprofileDetailsPagerAdapter(FragmentManager fm,NsoreDevotionalUser user) {
		super(fm);
		nsoreUser = user;
		dataPackage= new Bundle();
		if(user!=null)
		{
			dataPackage.putString("UID", nsoreUser.getMemberID());
			dataPackage.putString("name", nsoreUser.getName());
			dataPackage.putString("churchname", nsoreUser.getChurchname());
			dataPackage.putString("churchid", nsoreUser.getChurchID());
			dataPackage.putString("userdevice", nsoreUser.getDeviceID());
			dataPackage.putString("membersince", nsoreUser.getMemberSince());
			dataPackage.putString("connected", nsoreUser.getIsConnected());
			dataPackage.putString("userdp", nsoreUser.getProfilePic());
			dataPackage.putString("cover", nsoreUser.getUserCover());
			dataPackage.putString("mutual", nsoreUser.getMutualFriends());
			
			
			
		}
		else
		{
			dataPackage=null;
		}
	
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 1:
			FriendsList friendslist = new FriendsList();
			friendslist.setArguments(dataPackage);
			return friendslist;

		case 0:
			UserDetails historyDevotionalFragment = new UserDetails();
			historyDevotionalFragment.setArguments(dataPackage);
			return historyDevotionalFragment;
		
		
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

	
	
	
	
	
	//Fragment to handle user details
	
	
	
	
	
	
	
	
	
}
