package com.appsol.apps.devotional;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserDetails extends Fragment {

	public UserDetails() {
		// TODO Auto-generated constructor stub
	}
	private View rootView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.activity_facebook_feed, container,
				false);
		intiCompoments(rootView);
	return	rootView;
	}

	private void intiCompoments(View rootView2) {
		// TODO Auto-generated method stub
		
	}
}
