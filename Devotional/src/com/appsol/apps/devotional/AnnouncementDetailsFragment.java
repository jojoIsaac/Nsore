package com.appsol.apps.devotional;

import com.appsol.apps.devotional.AnnounceFragment.checkUpcomingEvent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class AnnouncementDetailsFragment extends Fragment {

	private View rootView;
	void restoreTitle()
	{
		MainActivity.mTitle="Announcements";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	public AnnouncementDetailsFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), "Again", Toast.LENGTH_LONG).show();
		
		 restoreTitle();
		super.onResume();
	}
	
TextView txtdetails,txtDateadded,txtTitle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		restoreTitle();
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_announcement_details,
				container, false);
		Bundle data= new Bundle();
		data= getArguments();
		String title="";
		String content="";
		String date="";
		title= data.getString("T");
		content= data.getString("C");
		date= data.getString("D");
		txtTitle=(TextView) rootView.findViewById(R.id.txtTitle);
		txtdetails=(TextView) rootView.findViewById(R.id.txtdetails);
		txtDateadded=(TextView) rootView.findViewById(R.id.txtDateadded);
		
		
		txtDateadded.setText(date);
		txtdetails.setText(content);
		txtTitle.setText(title);
		return rootView;
	}

}
