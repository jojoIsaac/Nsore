/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.appsol.apps.devotional;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.appsol.apps.projectcommunicate.classes.Connector.Config;
import com.appsol.volley.classes.LruBitmapCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
public class UILApplication extends Application {
	
	public static final String TAG = UILApplication.class
            .getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
 
    private static UILApplication mInstance;
 
	

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		 mInstance = this;
		//GCMStuffs();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		//ImageLoader.getInstance().init(config);
	}
	
	
	
	 public static synchronized UILApplication getInstance() {
	        return mInstance;
	    }
	 
	    public RequestQueue getRequestQueue() {
	        if (mRequestQueue == null) {
	            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	        }
	 
	        return mRequestQueue;
	    }
	 
	    public ImageLoader getImageLoader() {
	        getRequestQueue();
	        if (mImageLoader == null) {
	            mImageLoader = new ImageLoader(this.mRequestQueue,
	                    new LruBitmapCache());
	        }
	        return this.mImageLoader;
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
		
	
	
	
}