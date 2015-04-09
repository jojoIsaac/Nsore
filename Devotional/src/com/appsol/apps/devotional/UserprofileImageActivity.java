package com.appsol.apps.devotional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Base64;
import com.appsol.apps.projectcommunicate.classes.ConnectionDetector;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.squareup.picasso.Picasso;
public class UserprofileImageActivity extends ActionBarActivity {
	private int RESULT_LOAD_IMAGE=90;
	 static ImageView imgprofile;
	 String oldimage="";
	 // Static final constants
	    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	    private static final int ROTATE_NINETY_DEGREES = 90;
	    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	    private static final int ON_TOUCH = 1;

	    // Instance variables
	    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	    Bitmap croppedImage;
		private String filePath;
		private ImageView cropImageView;
		Button btnSaveImage;
		private static String imagename="",imagepath="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_user_image);
		Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
		Connector.context=this;
		oldimage= Connector.getUserdp();
		setContentView(R.layout.activity_userprofile_image);
		  cropImageView = (ImageView) findViewById(R.id.CropImageView);
	        
	        ///Prepare the image to display
	        filePath= Environment.getExternalStorageDirectory().getPath();
	        
	        filePath= Environment.getExternalStorageDirectory().getPath();
			
	        //Settup the application folder
	        File file = new File(filePath+ "/"+Connector.AppFolder+"/userDP/");
	    	
			if(!file.exists())
			{
				
				file.mkdirs();
				//Log.d("D",files.getAbsolutePath());
			}
	        
			String imagename=Connector.getUserdp();
			
			File files = new File(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename);
			if(files.exists() && files.isFile())
			{
				cropImageView.setImageBitmap( 
						ImageHelper.getCircledBitmap(BitmapFactory.decodeFile(files.getAbsolutePath()), 20) );
				
			}
			else
			{
				if(ConnectionDetector.isConnectingToInternet(this))
				{
					  Picasso.with(this)
					  .load(Connector.imageURL+imagename)
					  .resize(250, 250)
					  .centerCrop()
					  .transform(new CropSquareTransformation())
					  .error(R.drawable.mobile)
					  .into( cropImageView);
				}
			}
			
			
			
			//croppedImage= imgprofile.ge
	}
	protected void saveImageOnServer(Bitmap croppedImage2) {
		// TODO Auto-generated method stub
		
	}
	private void saveImageOnserver(final Bitmap image,final String name,final String fullpath) {
	// TODO Auto-generated method stub
	AsyncTask<Void, Integer, String> saveuserimage= new AsyncTask<Void, Integer, String>() {
		
		private InputStream is;
		ProgressDialog pdialog;
@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	pdialog= new ProgressDialog(UserprofileImageActivity.this);
	pdialog.setMessage("Please Wait ...");
	pdialog.setCancelable(false);
	pdialog.show();
	super.onPreExecute();
}

@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	pdialog.dismiss();
	filePath= Environment.getExternalStorageDirectory().getPath();
	//Toast.makeText(context, text, duration)
	if(result.equalsIgnoreCase("Completed"))
	{
		cropImageView.setImageBitmap( 
				ImageHelper.getCircledBitmap(image, 20) );
		
		AsyncTask<Void, Void, Boolean> saveImageLocaly = new AsyncTask<Void, Void, Boolean>() {
			
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				File xx= new File(fullpath);
				Bitmap bmp=null;
				
				if(xx.exists() && xx.isFile())
				{
					  bmp = BitmapFactory.decodeFile(fullpath);
			            if (null != bmp)
			            {

			            	final int destWidth = 600;
//			            	// we'll start with the original picture already open to a file

			            	String imagepath = filePath+ "/"+Connector.AppFolder+"/userDP/"+name;
			            	try {
								FileOutputStream fout= new FileOutputStream(imagepath);
								 ByteArrayOutputStream bstream= new ByteArrayOutputStream();
					            	
					            	bmp.compress(Bitmap.CompressFormat.JPEG, 70, bstream);
					            	File f= new File(imagepath);
					            	f.createNewFile();
					            	FileOutputStream fouts=  new FileOutputStream(f);
					            	fouts.write(bstream.toByteArray());
					            	fouts.close();
					            
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			             
			            	
			                return true;
			                
			            }
				}
				return false;
			}
			
			protected void onPostExecute(Boolean result) {
				if(result)
				{
					Connector.setUserdp(name);
					String imagepath = filePath+ "/"+Connector.AppFolder+"/userDP/"+oldimage;
					
					File imFile= new File(imagepath);
					if(imFile.exists() && imFile.isFile())
					{
						imFile.delete();
					}
				}
			};
		};
		
		saveImageLocaly.execute();
		
	
	}
	else
	{
		Toast.makeText(getApplicationContext(), "Image update failed", Toast.LENGTH_LONG).show();
	}
	super.onPostExecute(result);
}
String data="";

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			 
	       image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
	 
	        byte[] ba = bao.toByteArray();
	 
	        String ba1 = Base64.encodeBytes(ba);
	 
	        ArrayList<NameValuePair> nameValuePairs = new
	 
	        ArrayList<NameValuePair> ();
	 
	        nameValuePairs.add(new BasicNameValuePair("image", ba1));
	        nameValuePairs.add(new BasicNameValuePair("oldimage",Connector.getUserdp() ));
	        nameValuePairs.add(new BasicNameValuePair("name",name ));
	        nameValuePairs.add(new BasicNameValuePair("userID",Connector.getUserId() ));
	        //userID
	        try {
	 
	            HttpClient httpclient = new DefaultHttpClient();
	 
	            HttpPost httppost = new
	 
	            HttpPost("https://nsoredevotional.com/mobile/imageprocessor.php");
	 
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 
	            HttpResponse response = httpclient.execute(httppost);
	 
	            HttpEntity entity = response.getEntity();
	 
	            is = (InputStream) entity.getContent();
	             data= Connector.processStream(is);
	            //is = (InputStream) entity.getContent();
	 return data;
	        } catch (Exception e) {
	 
	            Log.e("log_tag", "Error in http connection " + e.toString());
	 
	        }
	 
	   
	 
			return data;
		}
	};
	
	saveuserimage.execute();
}
	final int PIC_CROP = 1;
	
	private void performCrop(Uri picUri, String name, String path) {
	    try {

	        Intent cropIntent = new Intent("com.android.camera.action.CROP");
	        // indicate image type and Uri
	        cropIntent.setDataAndType(picUri, "image/*");
	        // set crop properties
	        cropIntent.putExtra("crop", "true");
	        // indicate aspect of desired crop
	        cropIntent.putExtra("aspectX", 1);
	        cropIntent.putExtra("aspectY", 1);
	        // indicate output X and Y
	        cropIntent.putExtra("outputX", 128);
	        cropIntent.putExtra("outputY", 128);
	        // retrieve data on return
	        cropIntent.putExtra("return-data", true);
	        // start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, PIC_CROP);
	        imagename=name;
	        imagepath= path;
	    }
	    // respond to users whose devices do not support the crop action
	    catch (ActivityNotFoundException anfe) {
	        // display an error message
	        String errorMessage = "Whoops - your device doesn't support the crop action!";
	        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
	        toast.show();
	    }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == PIC_CROP) {
	        if (data != null) {
	            // get the returned data
	            Bundle extras = data.getExtras();
	            // get the cropped bitmap
	            Bitmap selectedBitmap = extras.getParcelable("data");
	           // cropImageView.setImageURI(Crop.getOutput(data));
	           // cropImageView.
	            croppedImage=selectedBitmap;
	            cropImageView.setImageBitmap(selectedBitmap);
	            saveImageOnserver(selectedBitmap,imagename,imagepath);
	            
	        }
	    }
	    else 	if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			
			Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA,MediaStore.Video.Media.DATA };

	        Cursor cursor = getContentResolver().query(selectedImage,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        if(columnIndex<0)
	        {
	        	 columnIndex = cursor.getColumnIndex(filePathColumn[1]);
	        }
	        String picturePath = cursor.getString(columnIndex);
	       // Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
	        cursor.close();
	        
	       try
	       {
	    	   File file= new File(picturePath);
	           if(file.exists())
	           {
	        	   
	        	   String extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
				  	

					if(extension.equalsIgnoreCase("jpg")|| extension.equalsIgnoreCase("JPG")|| extension.equalsIgnoreCase("png")||
							extension.equalsIgnoreCase("gif")|| extension.equalsIgnoreCase("GIF")|| extension.equalsIgnoreCase("bmp") ||extension.equalsIgnoreCase("jpg"))
					{
						  Bitmap image= BitmapFactory.decodeFile(picturePath);
						  Uri uri= data.getData();
				          performCrop(uri,file.getName(),file.getAbsolutePath());
					}
					
					
	           }
	       }
	       catch(Exception e)
	       {
	    	   
	       }
			
			
		}
	    else
	    {
//	    	imagename="";
//	    	imagepath="";
	    }

	}
	
	
	// Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_image, menu);
		return true;
	}
	

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.changeImage) {
			Intent i = new Intent(
        			Intent.ACTION_PICK, 
        			
        			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	i.setType("image/*");
        			startActivityForResult(i, RESULT_LOAD_IMAGE);
			
		}
		return super.onOptionsItemSelected(item);
	}
}
