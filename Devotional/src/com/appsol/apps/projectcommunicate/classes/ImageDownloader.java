package com.appsol.apps.projectcommunicate.classes;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloader {
	
	public void download(String url, ImageView imageView) {
		String cookie="";
	     if (cancelPotentialDownload(url, imageView)) {
	         BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
	         DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
	         imageView.setImageDrawable(downloadedDrawable);
	         task.execute(url,cookie );
	     }
	}
	
	
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
	    if (imageView != null) {
	        Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof DownloadedDrawable) {
	            DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
	            return downloadedDrawable.getBitmapDownloaderTask();
	        }
	    }
	    return null;
	}
	
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
	    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

	    if (bitmapDownloaderTask != null) {
	        String bitmapUrl = bitmapDownloaderTask.url;
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	            bitmapDownloaderTask.cancel(true);
	        } else {
	            // The same URL is already being downloaded.
	            return false;
	        }
	    }
	    return true;
	}
  
	  static class DownloadedDrawable extends ColorDrawable {
		    private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		    public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
		        super(Color.BLACK);
		        bitmapDownloaderTaskReference =
		            new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		    }

		    public BitmapDownloaderTask getBitmapDownloaderTask() {
		        return bitmapDownloaderTaskReference.get();
		    }
		}
	  
class BitmapDownloaderTask extends AsyncTask<String, Integer, Bitmap>
{
	private String url;
    private final WeakReference<ImageView> imageViewReference;

	

    public BitmapDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }


    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
         // params comes from the execute() call: params[0] is the url.
         return downloadBitmap(params[0]);
    }

    private Bitmap downloadBitmap(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
            // Change bitmap only if this process is still associated with it
            //
//            if(MainActivity.fromMain)
//            {
//            	MainActivity.profilepix.setImageBitmap( ImageHelper.getCircledBitmap(Bitmap.createScaledBitmap( BitmapFactory.decodeFile(file.getAbsolutePath()), 120, 120, false), 20) );
//        		
//            }
            
            if (this == bitmapDownloaderTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
       
    }
}
	

}
