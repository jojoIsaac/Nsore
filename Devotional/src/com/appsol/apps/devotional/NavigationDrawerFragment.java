package com.appsol.apps.devotional;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.adapter.NavDrawerListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;
import com.appsol.apps.projectcommunicate.model.NavDrawerItem;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
        /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private static Integer y=0;
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private static DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView,rootView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private TypedArray navMenuIcons;
    public static NavDrawerListAdapter adapter1;
   // private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private String[] navtopMenuTitles;
	private TextView txtChurcName;
	private String imagename;
	private String filePath;
	private ImageView profilepix;
	public static Bitmap churchLogo;
    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
          
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
     
       // selectItem(mCurrentSelectedPosition);
    }

  public static  void manipulateDrawer()
	{
		if(y==0)
		{
		 mDrawerLayout.openDrawer(Gravity.LEFT);
		 y=-1;
		}
		else 
		{
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			y=0;
		}
		
	}
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	navtopMenuTitles = getResources().getStringArray(R.array.nav_top_menu);
    	
    	navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_top_menu_icons);
    	
    	navDrawerItems = new ArrayList<NavDrawerItem>();
    
    	
    	
    	
    	for (int i = 0; i < navtopMenuTitles.length; i++) {
			try {
				
				NavDrawerItem object ;
				String values= navtopMenuTitles[i];
				if(values.equalsIgnoreCase("My Account"))
				{
					
					object = new NavDrawerItem(values,navMenuIcons.getResourceId(i, -1),true);
				}
				else
				{
					
					object = new NavDrawerItem(values,navMenuIcons.getResourceId(i, -1),false);
				}
				if(object!=null)
				{
					navDrawerItems.add(object);
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    	
    	
    	
    	
    	rootView= inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) rootView.findViewById(R.id.menues);
    	adapter1 = new NavDrawerListAdapter(getActivity(), navDrawerItems);
    
		// Recycle the typed array
		navMenuIcons.recycle();
      
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
       
        mDrawerListView.setAdapter(adapter1);
        
        
        
       
        
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
     
        txtChurcName=(TextView)rootView.findViewById(R.id.txtChurchname);
        profilepix=(ImageView) rootView.findViewById(R.id.churchlogo);
		
        
        
        filePath= Environment.getExternalStorageDirectory().getPath();
        //Set some vital information
        imagename = Connector.myPrefs.getString("CHURCH_LOGO", "NO_DATA");
        String churchname= Connector.getChurchName();
        if(churchname.equalsIgnoreCase("NO_CHURCH_FOUND"))
        {
        	 txtChurcName.setText("Nsore Devotional");
        }
        else
        txtChurcName.setText(churchname);
		Log.e("File Name", imagename);
		Log.e("Server Date", imagename);
		//Toast.makeText(this, imagename + filePath+"/"+Connector.AppFolder+"/church_logo.jpg", Toast.LENGTH_LONG).show();
		File	file= new File(filePath+"/"+Connector.AppFolder+"/"+imagename);
		if(file.exists() && file.isFile())
		{
			//Toast.makeText(this, imagename, Toast.LENGTH_LONG).show();
				profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 50, 50, false), 20) );
			
		}
		else
		{
			if(MainActivity.churchLogo!=null)
			{
				profilepix.setImageBitmap( 
						ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( churchLogo, 50, 50, false), 20) );
				
			}
			else
				
			profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeResource(getResources(), R.drawable.appico), 50, 50, false), 20) );
			
		}
        
        return rootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                y=0;
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
y=-1;
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        try
        {
        	 if (rootView != null) {
                 mDrawerListView.setItemChecked(position, true);
             }
             if (mDrawerLayout != null) {
                 mDrawerLayout.closeDrawer(mFragmentContainerView);
             }
             if (mCallbacks != null) {
                 mCallbacks.onNavigationDrawerItemSelected(position);
             }
        }
        catch
        (Exception e)
        {
        	
        }
       
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
          
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
      
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
       
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
        void onNavigationDrawerItemSelected2(int position);
    }
}
