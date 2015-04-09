package com.appsol.apps.projectcommunicate.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.appsol.apps.devotional.MainActivity;
import com.appsol.apps.devotional.NsoreUserDetailsActivity;



import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {
public static String churchLogo="";
public static boolean gotoMain=false;
    ImageView imageView = null;
ProgressDialog pdg;
String imagepath="";
String filePath= Environment.getExternalStorageDirectory().getPath();
@Override
protected void onPreExecute() {
	//TODO Auto-generated method stub	
	File file = new File(filePath+ "/"+Connector.AppFolder);

	if(!file.exists())
	{
		
		file.mkdir();
		Log.d("D",file.getAbsolutePath());
	}
	 pdg= new ProgressDialog(Connector.context);
	 
	 pdg.setMessage("Please Wait. Setting Environment");
	 pdg.setCancelable(false);
	 pdg.show();
	super.onPreExecute();
}
    @Override
    protected Bitmap doInBackground(String... vals) {
        //this.imageView = vals[0];
        return download_Image((String)vals[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
    
    	
       // imageView.setImageBitmap(result);
    	
    	if(result!=null)
    	{
    		Connector.myPrefs= Connector.context.getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
			String filePath= Environment.getExternalStorageDirectory().getPath();
			
			imagepath= filePath+ "/"+Connector.AppFolder+"/"+DownloadImagesTask.churchLogo;
			
			File	file= new File(filePath+"/"+Connector.AppFolder+"/"+churchLogo);
			if(file.exists() && file.isFile())
			{
				Connector.setChurchImage(churchLogo);
			}
			
			
			  if(MainActivity.fromMain)
	            {
	            	//MainActivity.profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 120, 120, false), 20) );
				  MainActivity.churchLogo=result;
	            }
//			Editor edt= Connector.myPrefs.edit();
//			edt.putString("CHURCH_LOGO", "church_logo.jpg");
//			edt.commit();
    	}
        pdg.cancel();
   	 
    }
    Bitmap bmp =null;
private Bitmap download_Image(String url,Integer s)
{
	ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	 
	 try
	 {
			imageLoader.get(url,  new ImageListener() {
				
				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
				public void onResponse(ImageContainer arg0, boolean arg1) {
					// TODO Auto-generated method stub
					ImageContainer dayt= arg0;
					
					//Toast.makeText(NsoreUserDetailsActivity.this, urlCover, Toast.LENGTH_LONG).show();
					
					if(dayt!=null)
					{
						Bitmap data= dayt.getBitmap();
						if(data!=null)
						{
							 Drawable img = new BitmapDrawable(Connector.context.getResources(),data);
							 //layout_cover_photo.setBackground(img);
							 //BitmapDrawable img= data;
							 
							 
							 try
							 {
							  ByteArrayOutputStream bstream= new ByteArrayOutputStream();
				            	data.compress(Bitmap.CompressFormat.JPEG, 70, bstream);
				            	File f= new File(imagepath);
				            	f.createNewFile();
				            	FileOutputStream fouts=  new FileOutputStream(f);
				            	fouts.write(bstream.toByteArray());
				            	fouts.close();
							 }catch(Exception e)
							 {
								 
							 }
				            	
				            	if(gotoMain)
				            	{
				            	
				            		Intent ints= new Intent(Connector.context, MainActivity.class);
				            		Connector.context.startActivity(ints);
				            		((Activity) Connector.context).finish();
				            	}
				            	gotoMain=false;
				                
							 
						}
						 bmp= data;
					}
				}
			}, 100, 100);
	 }catch(Exception e)
	 {
		 
	 }
	 return bmp;
}
    private Bitmap download_Image(String url) {

        Bitmap bmp =null;
        try{
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
            {
            	//	imagepath= filePath+ "/"+Connector.AppFolder+"/church_logo.jpg";
            	imagepath= filePath+ "/"+Connector.AppFolder+"/"+churchLogo;
            	FileOutputStream fout= new FileOutputStream(imagepath);
                ByteArrayOutputStream bstream= new ByteArrayOutputStream();
            	bmp.compress(Bitmap.CompressFormat.JPEG, 70, bstream);
            	File f= new File(imagepath);
            	f.createNewFile();
            	FileOutputStream fouts=  new FileOutputStream(f);
            	fouts.write(bstream.toByteArray());
            	fouts.close();
            	is.close();
            	if(gotoMain)
            	{
            	
            		Intent ints= new Intent(Connector.context, MainActivity.class);
            		Connector.context.startActivity(ints);
            		((Activity) Connector.context).finish();
            	}
            	gotoMain=false;
                return bmp;
                
            }

            }catch(Exception e){}
        return bmp;
    }
}