package com.appsol.apps.devotional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.Notes;

public class CreateNoteActivity extends ActionBarActivity {
public static String title;
public static boolean isNew=true,edits=false;;
static String title_="";
static  String content="";
static  String id="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		setTitle("Personal Note");
/*
 * ints.putExtra("CONTENT", bmark.getcontent());
			ints.putExtra("TITLE",bmark.getTitle());
			ints.putExtra("ID", bmark.getId());
 */
		try
		{
			//CreateNoteActivity.isNew
			title_=getIntent().getStringExtra("TITLE");
			//title_= getIntent().getExtras().getString("TITLE", "");
			content= getIntent().getStringExtra("CONTENT");
			id= getIntent().getStringExtra("ID");
			edits= getIntent().getBooleanExtra("EDIT", false);
			Log.e("Title",title);
		}
		catch(Exception e)
		{
			
		}
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	
	void endActivity(){
		finishActivity(RESULT_OK);
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
EditText body,txttitle;
Button btnsavenote;
private String curDate;





@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inf) {

	// Inflate the menu; this adds items to the action bar if it is present.
	inf.inflate(R.menu.create_note, menu);

}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.mn_save_note) {
		saveNote();
		return true;
	}
	else if(id==R.id.mn_Delete_note)
	{
		deleteNote();
		return true;
	}
	return super.onOptionsItemSelected(item);
}


private void deleteNote() {
	// TODO Auto-generated method stub
	
AlertDialog.Builder build= new AlertDialog.Builder(getActivity());
final Notes bmark= new Notes();
bmark.setId(id);

if(bmark!=null && !isNew)
{
	build.setTitle("Delete Note")
	.setMessage("Are you sure?")
	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		
		private List<Notes> bookmarks;

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			Connector.dbhelper = new DBHelper(getActivity());
			//Connector.openDB(getActivity());
			Connector.dbhelper .deleteNote(bmark.getId());
			//bookmarks= Notes.getNotes(Connector.dbhelper);
			Connector.dbhelper.close();
			Intent data= new Intent();
			data.setData(Uri.parse("RELOAD"));
			getActivity().setResult(RESULT_OK, data);
			
			getActivity().finish();

		}
	})
	.setNegativeButton("No", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	})
	.show();
	
	
	
//	bdapter= new BookmarkListAdapter(getActivity(), android.R.layout.simple_list_item_1, bookmarks);
//	bdapter.notifyDataSetChanged();
}
else
{
	build.setTitle("Delete Note")
	.setMessage("Are you sure? All Changes will be discarded.")
	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		
		private List<Notes> bookmarks;

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			Intent data= new Intent();
			data.setData(Uri.parse("RELOAD"));
			getActivity().setResult(RESULT_OK, data);
			
			getActivity().finish();

		}
	})
	.setNegativeButton("No", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	})
	.show();
}

}

@Override
public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	setHasOptionsMenu(true);
	super.onCreate(savedInstanceState);
}
		private void saveNote() {
	// TODO Auto-generated method stub
			if(txttitle.getText().toString().replace(" ","").length()>0 && body.getText().toString().replace(" ","").length()>0)
			{
				Notes note= new Notes();
				note.setcontent(body.getText().toString());
				note.setTitle(txttitle.getText().toString());
//				java.util.Date date= Calendar.getInstance().getTime();
//				String dateString=date.getYear()+"-" +(date.getMonth()+1) +"-"+date.getDay();
				
				
				  long msTime = System.currentTimeMillis();  
			        Date curDateTime = new Date(msTime);
			 	
			        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");  
			        curDate = formatter.format(curDateTime);    
				
				
				
				note.setDate(curDate);
				Connector.dbhelper = new DBHelper(getActivity());
				if(isNew)
				{
					long c=Connector.dbhelper.addNote(note);
					if(c>0)
					{
						
						Toast.makeText(getActivity(), "Note Saved",Toast.LENGTH_LONG).show();
						Connector.dbhelper.close();
						Intent data= new Intent();
						data.setData(Uri.parse("RELOAD"));
						getActivity().setResult(RESULT_OK, data);
						
						getActivity().finish();
					}
					
				}
				else
				{
					note.setId(id);
					long c=Connector.dbhelper.updateNote(note);
					if(c>0)
					{
						Toast.makeText(getActivity(), "Note Saved",Toast.LENGTH_LONG).show();
						Connector.dbhelper.close();
						Intent data= new Intent();
						data.setData(Uri.parse("RELOAD"));
						getActivity().setResult(RESULT_OK, data);
						
						getActivity().finish();
					}
					
				}
				
			}
	
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.personalnotelayout,
					container, false);
//			 mTitleText = (EditText) findViewById(R.id.title);
//		        mBodyText = (EditText) findViewById(R.id.body);
			body= (EditText) rootView.findViewById(R.id.body);
		txttitle=(EditText) rootView.findViewById(R.id.title);
		txttitle.setText(title_);
		
		if(edits)
		{
			txttitle.setText(title_);
			body.setText(content);
			isNew=false;
		}
		
		
//		//btnsavenote= (Button) rootView.findViewById(R.id.btnsavenote);
//		btnsavenote.setOnClickListener( new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//			
//				
//			}
//		});
			return rootView;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent data= new Intent();
		data.setData(Uri.parse("RELOAD"));
		setResult(RESULT_OK, data);
		
		finish();
		super.onBackPressed();
	}

	

}
