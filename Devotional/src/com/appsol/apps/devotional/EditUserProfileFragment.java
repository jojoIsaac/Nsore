package com.appsol.apps.devotional;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditUserProfileFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
		}
		setHasOptionsMenu(true);
	}
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View rootView =inflater.inflate(R.layout.edituser_user_profile, container,
			false);
	getActivity().setTitle("Profile");
	return rootView;
}
}
