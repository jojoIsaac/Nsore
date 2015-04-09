package com.appsol.apps.projectcommunicate.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsol.apps.devotional.DevotionFragment;
import com.appsol.apps.devotional.DevotionHistoryFragment;
import com.appsol.apps.devotional.DevotionalHistoryFeedActivity;
import com.appsol.apps.devotional.SubscribeChurchesFragment;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.OtherBookmarks;

public class DevotionPagerAdapter extends FragmentPagerAdapter {
	public static int PAGE_COUNT = 3;
	private String titles[] = new String[]{"TODAY", "HISTORY","FOLLOWING"};
	private List<OtherBookmarks> subscription;
	
	
	public DevotionPagerAdapter(FragmentManager fm) {
		super(fm);
	
	}
	Integer checkSubs()
	{
		subscription= new ArrayList<OtherBookmarks>();
		Connector.dbhelper = new DBHelper(Connector.context);
		subscription= OtherBookmarks.getBookMarks(Connector.dbhelper,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH,OtherBookmarks.TYPE_SUBSCRIBED_CHURCH_BRANCH);
		Connector.dbhelper.close();
		
		if(subscription.size()>0)
		{
			return 3;
		}
		else
		{
			return 2;
		}
		//adapter.notifyDataSetChanged();
		
	}
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
		  DevotionFragment todayDevotionalFragment = new DevotionFragment();
			return todayDevotionalFragment;

		case 1:
//			DevotionHistoryFragment historyDevotionalFragment = new DevotionHistoryFragment();
//			return historyDevotionalFragment;
			DevotionalHistoryFeedActivity historyDevotionalFragment = new DevotionalHistoryFeedActivity();
			return historyDevotionalFragment;
		case 2:
			
				SubscribeChurchesFragment subscribeChurchesFragment= new SubscribeChurchesFragment();
				 return subscribeChurchesFragment;
			
			 
		
		}
		
		
		
		return null;
	}
public CharSequence getPageTitle(int position){
	return titles[position];
}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return checkSubs();
	}

}
