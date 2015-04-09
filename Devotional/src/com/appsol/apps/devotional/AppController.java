/**
 * 
 */
package com.appsol.apps.devotional;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.appsol.volley.classes.ImageCacheManager;
import com.appsol.volley.classes.ImageCacheManager.CacheType;
import com.appsol.volley.classes.LruBitmapCache;
import com.appsol.volley.classes.SslHttpClient;

/**
 * @author User
 *
 */
public class AppController extends Application {

	private static Context applicationContext;

	/**
	 * 
	 */
	public AppController() {
		// TODO Auto-generated constructor stub
		//applicationContext = this.getApplicationContext();
		//DiskBasedCache dc = new 
		
		
		}
		
//	    public static Context getContext() {
//	    	return applicationContext;
//	    }
	
	public AppController(SslHttpClient sslHttpClient) {
		// TODO Auto-generated constructor stub
	}

	public static final String TAG = AppController.class
            .getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
    private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 100; //PNG is lossless so quality is ignored but must be provided

    private static AppController mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
    	return com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
    private void createImageCache(){
    	ImageCacheManager.getInstance().init(this,
    	this.getPackageCodePath()
    	, DISK_IMAGECACHE_SIZE
    	, DISK_IMAGECACHE_COMPRESS_FORMAT
    	, DISK_IMAGECACHE_QUALITY
    	, CacheType.MEMORY);
    	}
	
}
