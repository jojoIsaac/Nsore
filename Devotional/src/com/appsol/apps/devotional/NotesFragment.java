package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.adapter.NotesListAdapter;
import com.appsol.apps.projectcommunicate.adapter.PersonalNoteAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.Notes;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NotesFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link NotesFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class NotesFragment extends Fragment implements OnFragmentInteractionListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	static ListView lstbookmarks;
	public static Notes bmark;
	public static NotesListAdapter bdapter;
	public static List<Notes> bookmarks;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
public  static final int RESULTS_CODE=90;
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment NotesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NotesFragment newInstance(String param1, String param2) {
		NotesFragment fragment = new NotesFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	void restoreTitle()
	{
		MainActivity.mTitle="Notes";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	public NotesFragment() {
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
		setHasOptionsMenu(true);
		
		
	}
	
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onResume();
	}
	
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
//		if(bookmarks!=null)
//		{
//			if(bookmarks.size()>0)
//			{
				inflater.inflate(R.menu.notelistmenu, menu);
//			}
//		}
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		if(bookmarks!=null)
//		{
//			if(bookmarks.size()>0)
//			{
//				getActivity().getMenuInflater().inflate(R.menu.notelistmenu, menu);
//			}
//		}
		super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mn_add_note:
			CreateNoteActivity.isNew=true;
			Intent ints= new Intent(getActivity(), CreateNoteActivity.class);
CreateNoteActivity.title_="";
			getActivity().startActivityForResult(ints, RESULTS_CODE);

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
@Override
public void onCreateContextMenu(ContextMenu menu, View v,
		ContextMenuInfo menuInfo) {
	// TODO Auto-generated method stub
	menu.setHeaderTitle("Options");
	MenuInflater inflate = new MenuInflater(getActivity());
	inflate.inflate(R.menu.context_notes, menu);
	super.onCreateContextMenu(menu, v, menuInfo);
}

@Override
public boolean onContextItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
	 bmark= bdapter.getItem(info.position);
	switch (item.getItemId()) {
	case R.id.mn_open:
		if(bmark!=null)
		{
			CreateNoteActivity.isNew=false;
			Intent ints= new Intent(getActivity(), CreateNoteActivity.class);
			ints.putExtra("CONTENT", bmark.getcontent());
			ints.putExtra("TITLE",bmark.getTitle());
			ints.putExtra("ID", bmark.getId());
			ints.putExtra("EDIT", true);
			getActivity().startActivity(ints);
		}
		break;
case R.id.mn_context_Share:
		if(bmark!=null)
		{
			Intent ints= new Intent(Intent.ACTION_SEND);
			ints.putExtra(Intent.EXTRA_TEXT, "Nsore Devotional Custom Notes  \nTitle:  "+ bmark.getTitle()+""+"\n"
					+ ""+bmark.getcontent());
			ints.setType("text/plain");
			
			startActivity(ints);
		}
		break;
case R.id.mn_context_delete:
	AlertDialog.Builder build= new AlertDialog.Builder(getActivity());
	
	
	if(bmark!=null)
	{
		build.setTitle("Delete Note")
		.setMessage("Are you sure?")
		.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Connector.dbhelper = new DBHelper(getActivity());
				//Connector.openDB(getActivity());
				Connector.dbhelper .deleteNote(bmark.getId());
				bookmarks= Notes.getNotes(Connector.dbhelper);
				Connector.dbhelper.close();
				if(bookmarks.size()>0)
				{
					bdapter.clear();
					
					for (Notes b : bookmarks) {
						bdapter.add(b);
					}
					toogleView(true);
					bdapter.notifyDataSetChanged();
					
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
		
		
		
//		bdapter= new BookmarkListAdapter(getActivity(), android.R.layout.simple_list_item_1, bookmarks);
//		bdapter.notifyDataSetChanged();
	}
	getActivity().supportInvalidateOptionsMenu();
	break;
	default:
		break;
	}
	
	
	return super.onContextItemSelected(item);
}

static Button btnCreateNew,btnnew;
static TextView txt1;
static TextView txt2;
static ImageView img1;
public static void toogleView(boolean b)
{
	if(b)
	{
		txt1.setVisibility(View.GONE);
		txt2.setVisibility(View.GONE);
		img1.setVisibility(View.GONE);
		btnCreateNew.setVisibility(View.GONE);
		//btnnew.setVisibility(View.VISIBLE);
		lstbookmarks.setVisibility(View.VISIBLE);
	}
	else
	{
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		img1.setVisibility(View.VISIBLE);
		btnCreateNew.setVisibility(View.VISIBLE);
		//btnnew.setVisibility(View.GONE);
		lstbookmarks.setVisibility(View.GONE);
	}
}
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);

	 if (requestCode == RESULTS_CODE && resultCode == Activity.RESULT_OK ) 
	{
		 //Toast.makeText(getActivity(), "Hey", Toast.LENGTH_LONG).show();
		 Connector.dbhelper = new DBHelper(getActivity());
			//Connector.openDB(getActivity());
			bookmarks= Notes.getNotes(Connector.dbhelper);
			Connector.dbhelper.close();
			if(bookmarks.size()>0)
			{
				bdapter.clear();
				
				for (Notes b : bookmarks) {
					bdapter.add(b);
				}
				bdapter.notifyDataSetChanged();
				toogleView(true);
				
			}
			else
			{
				toogleView(false);
			}
			getActivity().supportInvalidateOptionsMenu();
	}
	 
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null ;
		restoreTitle();
		getActivity().setTitle("Notes");
		
		bookmarks= new ArrayList<Notes>();
		Connector.dbhelper = new DBHelper(getActivity());
		CreateNoteActivity.title_="";
		//Connector.openDB(getActivity());
		bookmarks= Notes.getNotes(Connector.dbhelper);
		Connector.dbhelper.close();
		
	
			rootView = inflater.inflate(R.layout.fragment_notes,
					container, false);
			
			txt1 =(TextView) rootView.findViewById(R.id.txt1);
			txt2 =(TextView) rootView.findViewById(R.id.txtdevotionstatus);
			img1=(ImageView) rootView.findViewById(R.id.imgeventBanner);
			btnCreateNew = (Button) rootView.findViewById(R.id.btnx);
			//btnnew=(Button) rootView.findViewById(R.id.btnnew);
			lstbookmarks= (ListView) rootView.findViewById(R.id.lstbookmarks);
			
			bdapter= new NotesListAdapter(getActivity(), android.R.layout.simple_list_item_1, bookmarks);
			lstbookmarks.setAdapter(bdapter);
			
			registerForContextMenu(lstbookmarks);
			
			lstbookmarks.setOnItemClickListener( new OnItemClickListener() {

				

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub
					 bmark= bdapter.getItem(position);
					if(bmark!=null)
					{
						CreateNoteActivity.isNew=false;
						Intent ints= new Intent(getActivity(), CreateNoteActivity.class);
						ints.putExtra("CONTENT", bmark.getcontent());
						ints.putExtra("TITLE",bmark.getTitle());
						ints.putExtra("ID", bmark.getId());
						ints.putExtra("EDIT", true);
						getActivity().startActivityForResult(ints, RESULTS_CODE);

					}
					
				}
			});
			//LinearLayout rlay= (LinearLayout) rootView.findViewById(R.id.empty_notes);
			btnCreateNew.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					CreateNoteActivity.isNew=true;
					Intent ints= new Intent(getActivity(), CreateNoteActivity.class);

					getActivity().startActivityForResult(ints, RESULTS_CODE);
				}
			});
			
//	btnnew.setOnClickListener( new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					CreateNoteActivity.isNew=true;
//					Intent ints= new Intent(getActivity(), CreateNoteActivity.class);
//
//					getActivity().startActivityForResult(ints, RESULTS_CODE);
//				}
//			});
			
			if(bookmarks.size()>0)
			{
				
				//rlay.setVisibility(View.GONE);
				toogleView(true);
				
				
			}
			else
			{
				toogleView(false);
				
			
			}
			
	
		
		return rootView;
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

	@Override
	public void onFragmentInteraction(Uri uri,Integer Code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
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
