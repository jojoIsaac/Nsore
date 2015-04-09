package com.appsol.apps.devotional;

import java.io.File;
import java.lang.reflect.Field;

import com.appsol.apps.projectcommunicate.adapter.ChurchDetailsPagerAdapter;
import com.appsol.apps.projectcommunicate.adapter.DevotionPagerAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link ChurchDetailsFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class ChurchDetailsFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private TextView txtChurchname;
	private ImageView profilepix;
	private String filePath;
	private String imagename;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ChurchDetailsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChurchDetailsFragment newInstance(String param1, String param2) {
		ChurchDetailsFragment fragment = new ChurchDetailsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChurchDetailsFragment() {
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Devotion");
		View view = inflater.inflate(R.layout.viewpager_churchdetails, container,false);
		
		ViewPager mPager = (ViewPager) view.findViewById(R.id.viewPager);
		
		mPager.setAdapter(new ChurchDetailsPagerAdapter(getChildFragmentManager()));
		setUpUI(view);
		return view;
	}
	
	private void setUpUI(View view) {
		// TODO Auto-generated method stub
		txtChurchname= (TextView)view.findViewById(R.id.txtChurchname);
		txtChurchname.setText(Connector.getChurchName());
		  profilepix=(ImageView) view.findViewById(R.id.churchlogo);
			
	        
	        
	        filePath= Environment.getExternalStorageDirectory().getPath();
	        //Set some vital information
	        imagename = Connector.myPrefs.getString("CHURCH_LOGO", "NO_DATA");
	        String churchname= Connector.getChurchName();
	        if(churchname.equalsIgnoreCase("NO_CHURCH_FOUND"))
	        {
	        	txtChurchname.setText("Nsore Devotional");
	        }
	        else
	        	txtChurchname.setText(churchname);
				File	file= new File(filePath+"/"+Connector.AppFolder+"/"+imagename);
			if(file.exists() && file.isFile())
			{
				//Toast.makeText(this, imagename, Toast.LENGTH_LONG).show();
					profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 80, 80, false), 20) );
				
			}
			else
			{
//				if(MainActivity.churchLogo!=null)
//				{
//					profilepix.setImageBitmap( 
//							ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( churchLogo, 50, 50, false), 20) );
//					
//				}
//				else
					
				profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.appico), 80, 80, false), 20) );
				
			}
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
