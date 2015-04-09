package com.appsol.apps.projectcommunicate.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionDetector {
 
    private static Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    /**
     * Checking for all possible internet providers
     * **/
    public static boolean isConnectingToInternet(Context _context){
    	try
    	{
    		 ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
             if (connectivity != null)
             {
                 NetworkInfo[] info = connectivity.getAllNetworkInfo();
                 if (info != null)
                     for (int i = 0; i < info.length; i++)
                         if (info[i].getState() == NetworkInfo.State.CONNECTED)
                         {
                             return true;
                         }
    
             }
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
       
          return false;
    }
}
