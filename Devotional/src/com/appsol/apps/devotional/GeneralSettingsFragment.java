package com.appsol.apps.devotional;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link GeneralSettingsFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link GeneralSettingsFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class GeneralSettingsFragment extends Fragment  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	class myAdapter extends ArrayAdapter<String> 
	{

		public myAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = LayoutInflater.from(getActivity()).inflate(R.layout.settings_item, parent,false);
			
			if(row!=null)
			{
				TextView txt= (TextView) row.findViewById(R.id.title);
				txt.setText(getItem(position));
				
				if(position %2 ==1){
					row.setBackgroundColor(getContext().getResources().getColor(R.color.row1)); 
					
				} else {
					row.setBackgroundColor(getContext().getResources().getColor(R.color.row2)); 
				}
				
				return row;
			}
			return super.getView(position, convertView, parent);
		}
	}
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment GeneralSettingsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static GeneralSettingsFragment newInstance(String param1,
			String param2) {
		GeneralSettingsFragment fragment = new GeneralSettingsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public GeneralSettingsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	
	private String[] navMenuTitles;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		getActivity().setTitle("General Settings");
		navMenuTitles = getResources().getStringArray(R.array.General_Settings_page_menus);
		View rootview= inflater.inflate(R.layout.fragment_help_page, container, false);
		ListView lstItems= (ListView) rootview.findViewById(R.id.lstabt);
		myAdapter mm= new myAdapter(getActivity(), android.R.layout.simple_list_item_1, navMenuTitles);
		
		lstItems.setAdapter(mm);
		
		lstItems.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub'
				FragmentManager fmanager= getActivity().getSupportFragmentManager();
			switch (position) {
//			 <item >
//	            Contact Support
//	        </item>
//	        <item >
//	            Forgot Password
//	        </item>
			case 0:
			changeChurch();
				break;
				
			case 1:
				
				break;	
			case 2:
			
				break;
			
			default:
				break;
			}
				
			}
		});
		
		
		return rootview;
	}
	
	

	protected void changeChurch() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder build=  new Builder(getActivity());
		build.setTitle("Change Church")
		.setMessage("After changing the church you will be required to log in. \nDo you want to proceed?")
		.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ChooseChurch.logoutIfNew=true;
				Intent ints = new Intent(getActivity(), ChooseChurch.class);
				startActivity(ints);
			}
		})
		.setNegativeButton("No", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
		
		
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	

}
