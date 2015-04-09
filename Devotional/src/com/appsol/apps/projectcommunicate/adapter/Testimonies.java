package com.appsol.apps.projectcommunicate.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Testimonies extends ArrayAdapter<Testimony> {
LayoutInflater inflater;
Context context;
	public Testimonies(Context context, int resource) {
		super(context, resource);
		inflater= LayoutInflater.from(context);
		this.context= context;
		// TODO Auto-generated constructor stub
	}

	public Testimonies(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		inflater= LayoutInflater.from(context);
		this.context= context;
		// TODO Auto-generated constructor stub
	}

	public Testimonies(Context context, int resource, Testimony[] objects) {
		super(context, resource, objects);
		inflater= LayoutInflater.from(context);
		this.context= context;
		// TODO Auto-generated constructor stub
	}

	

	public Testimonies(Context context, int resource, int textViewResourceId,
			Testimony[] objects) {
		super(context, resource, textViewResourceId, objects);
		inflater= LayoutInflater.from(context);
		this.context= context;
		// TODO Auto-generated constructor stub
	}

	public Testimonies(Context context, int resource, int textViewResourceId,
			List<Testimony> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater= LayoutInflater.from(context);
		this.context= context;
	}
	
	
	public Testimonies(Context context, int resource, 
			List<Testimony> objects) {
		super(context, resource, objects);
		inflater= LayoutInflater.from(context);
		this.context= context;
		// TODO Auto-generated constructor stub
	}
	private static class ViewHolder {
		TextView name;
		TextView content;
		ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	//	inflater= (LayoutInflater)
		View row= inflater.inflate(R.layout.testimontyitem, parent, false);
		Testimony testimony= getItem(position);
		if(testimony!=null)
		{
			if(row!=null)
			{
				ImageView imgdp = (ImageView) row.findViewById(R.id.thumbImage);
				TextView content=(TextView) row.findViewById(R.id.txttitle);
				TextView txtcontent=(TextView) row.findViewById(R.id.txttestimonycontent);
				String url=Connector.imageURL+ testimony.getUserdp();
				System.out.println(url);
				imgdp.setImageResource(R.drawable.usericos);
				if(!url.equalsIgnoreCase(Connector.imageURL))
				{
				Picasso.with(Connector.context)
				  .load(url)
				  .resize(120, 120)
				  .centerCrop()
				  .transform(new CropSquareTransformation())
				  .into(imgdp, new Callback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError() {
						// TODO Auto-generated method stub
						
					}
				});
				
				}
				content.setText(testimony.getName());
				txtcontent.setText(testimony.getContent());
				
			}
			
			return row;
		}
		return super.getView(position, convertView, parent);
	}
	

}
