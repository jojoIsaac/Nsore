package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsol.apps.projectcommunicate.adapter.DevotionBookmarkFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Connector.GridMonthAdapter;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link DevotionalBookmarks#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class DevotionalBookmarks extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View rootView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment DevotionHistoryFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static DevotionalBookmarks newInstance(String param1,
			String param2) {
		DevotionalBookmarks fragment = new DevotionalBookmarks();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	void restoreTitle()
	{
		MainActivity.mTitle="Bookmarks";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	public DevotionalBookmarks() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		restoreTitle();
		getActivity().setTitle("Bookmarks");
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub

		getActivity().getMenuInflater().inflate(R.menu.devotionbookmark_menu, menu);
		menu.setHeaderTitle("");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	static LinearLayout layouthead;
	static ProgressBar progressbarLoad;

	Button btnReadMore;
	private static ImageView btnshow;
	private View frags;
	static LinearLayout layout_Month;
	LinearLayout layout_no_event;
	static ListView lstDevotions;
	//ItemAdapter devotions;
	ArrayList<ChurchEvents> eventsList;
	private GridView gridView;
	private GridMonthAdapter MonthAdapter;
	
	static TextView txteventTitle,txteventstartdate,txteventLocation;
	DevotionBookmarkFeedListAdapter devotions;
	ArrayList<Bookmark> devotionList;
	static Bookmark selectedDevotion;
	private Bookmark bmark;
	//DevotionBookmarkAdapter bdapter;
	List<Bookmark> bookmarks;
	private static LinearLayout layout_no_Bookmark;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		restoreTitle();
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_devotion_history, container,
				false);
		
		
		
		setUpUI(rootView);
		
		
		return rootView;
	}


	public static void toggleCalender()
	{
		if( layout_Month.getVisibility()!=View.GONE)
		{
			btnshow.setImageResource(R.drawable.uparrow);
			layout_Month.setVisibility(View.GONE);
		}
		
		
	}
	
	private void setUpUI(View rootView) {
		// TODO Auto-generated method stub
		getActivity().setTitle("Bookmarks");
restoreTitle();
		bookmarks= new ArrayList<Bookmark>();
		Connector.dbhelper = new DBHelper(getActivity());
		//Connector.openDB(getActivity());
		bookmarks= Bookmark.getBookMarks(Connector.dbhelper);
		Connector.dbhelper.close();
		 progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		
		 layout_no_Bookmark= (LinearLayout) rootView.findViewById(R.id.layout_no_Bookmark);
		 layout_no_Bookmark.setVisibility(View.GONE);
		 gridView = (GridView) rootView.findViewById(R.id.grid_view);
		 layout_Month=(LinearLayout) rootView.findViewById(R.id.layout_Month);
		 layout_no_event=(LinearLayout) rootView.findViewById(R.id.layout_no_event);
		 TextView txtchurchname=(TextView) rootView.findViewById(R.id.txt1);
		 
		 
		 if(layout_Month!=null)
		 {
			 layout_Month.setVisibility(View.GONE);
		 }
		
		 
		 if( layout_no_event!=null)
		 {
			 layout_no_event.setVisibility(View.GONE);
			 txtchurchname.setText(Connector.getChurchName()+"Devotional Bookmarks");
			 
		 }
		
			//layout_Month.setVisibility(View.GONE);
			

		 TextView txtYear= (TextView) rootView.findViewById(R.id.txtYear);
		 
		 txtYear.setText("Browse Devotions within the year "+Connector.getYear());
		 txtYear.setVisibility(View.GONE);
		 
		 btnshow=(ImageView) rootView.findViewById(R.id.btnshow);
		 btnshow.setImageResource(R.drawable.uparrow);
		 btnshow.setVisibility(View.GONE);
		 //Set up the ListView
		 //devotionList= new ArrayList<Devotion>();
		 lstDevotions = (ListView) rootView.findViewById(R.id.lstMessages);
		 devotions= new DevotionBookmarkFeedListAdapter(getActivity(), bookmarks);
		 lstDevotions.setAdapter(devotions);
		 registerForContextMenu(lstDevotions);
		 if(bookmarks.size()>0)
			{
				
				//rlay.setVisibility(View.GONE);
				toogleView(true);
				
				
			}
			else
			{
				toogleView(false);
				
			
			}
		 
		 //Respond to clicks
		 //When the user clicks the Devotion
	lstDevotions.setOnItemClickListener( new OnItemClickListener() {

				

				private ChurchEvents selectedDevotion;
				//private PlaceholderFragment fragment;
				private Object fragmentmanger;

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					Bookmark devotion = null;
					
					// TODO Auto-generated method stub
					devotion= (Bookmark) devotions.getItem(position);
					if(devotion!=null)
					{
						Intent intent= new Intent(getActivity(), DevotionDetailActivity.class);
						intent.putExtra("DEVOTION-JSON", devotion.getDevotion());
						intent.putExtra("LID",devotion.getDid());
						intent.putExtra("DID",devotion.getDaily_guide_id());
						intent.putExtra("HIDE-BOOOKMARK", "1");
						intent.putExtra("From-New", "No");
						getActivity().startActivity(intent);
					}

				}
			});
		 //If the GridViewItem is Clicked
		  
	 progressbarLoad.setVisibility(View.GONE);
		 
		 //At the page load, Fetch the Immediate 20 devotions
		
		 
		 
	}
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
		final Integer position= info.position;
		selectedDevotion= (Bookmark) devotions.getItem(position);
	//fragmentmanger= getActivity().getSupportFragmentManager();
		//fragment=null;
		switch (item.getItemId()) {
		case R.id.mn_open:

			if(selectedDevotion!=null)
			{
				
				Intent intent= new Intent(getActivity(), DevotionDetailActivity.class);
				intent.putExtra("DEVOTION-JSON", selectedDevotion.getDevotion());
				intent.putExtra("LID",selectedDevotion.getDid());
				intent.putExtra("DID",selectedDevotion.getDaily_guide_id());
				intent.putExtra("HIDE-BOOOKMARK", "1");
				getActivity().startActivity(intent);
			}
			
			break;
		case R.id.mn_Share_devotion:
			String data=Connector.getChurchName()+"" +
					"\n "+selectedDevotion.getTitle()+"\n" +
						"Memory Verse: "+selectedDevotion.getVerse()+"\n" +
								"- Register and access more at: " +
								""+Connector.parentURL;
			Connector.shareText(getActivity(), data);
			break;
		
		case R.id.mn_deleteBookmark:
			AlertDialog.Builder build= new AlertDialog.Builder(getActivity());
			
			
			if(selectedDevotion!=null)
			{
				build.setTitle("Delete Bookmark")
				.setMessage("Are you sure?")
				.setPositiveButton("Yes", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Connector.dbhelper = new DBHelper(getActivity());
						//Connector.openDB(getActivity());
						Connector.dbhelper .deleteBookmark(selectedDevotion.getId());
						bookmarks= Bookmark.getBookMarks(Connector.dbhelper);
						Connector.dbhelper.close();
						if(bookmarks.size()>0)
						{
//							devotions.clear();
//							
//							for (Bookmark b : bookmarks) {
//								devotions.add(b);
//							}
							
                            devotions.remove(position);
							toogleView(true);
							devotions.notifyDataSetChanged();
						}
						else
						{
							toogleView(false);
						}
					}

					
				})
				.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				})
				.show();
		
			}
			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);
	}
	
	public static void toogleView(boolean b)
	{
		if(b)
		{
			 layout_no_Bookmark.setVisibility(View.GONE);
			
			 lstDevotions.setVisibility(View.VISIBLE);
		}
		else
		{
			 layout_no_Bookmark.setVisibility(View.VISIBLE);
			
			 lstDevotions.setVisibility(View.GONE);
		}
	}
}
