package com.appsol.apps.devotional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsol.apps.projectcommunicate.adapter.DevotionPagerAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.OtherBookmarks;

public class AllDevotions extends android.support.v4.app.Fragment {
		
	
	private List<OtherBookmarks> subscription;

	void restoreTitle()
	{
		MainActivity.mTitle="Devotional";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onResume();
	}
	public static DevotionPagerAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		restoreTitle();
		//checkSubs();
		View view = inflater.inflate(R.layout.viewpager_devotions, container,false);
		
		ViewPager mPager = (ViewPager) view.findViewById(R.id.viewPagers);
		adapter= new DevotionPagerAdapter(getChildFragmentManager());
		
		mPager.setAdapter(adapter);
		//checkSubs();
		return view;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
