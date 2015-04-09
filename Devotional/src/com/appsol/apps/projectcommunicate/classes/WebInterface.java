package com.appsol.apps.projectcommunicate.classes;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebInterface {
	Context context;
	WebView webView=null;
	public WebInterface (Context c)
	{
		context=c;
	}
	
	public WebInterface (Context c,WebView wv)
	{
		context=c;
		webView = wv;
	}
	
	@JavascriptInterface
	public void showToast(String t)
	{
		Toast.makeText(context, t, Toast.LENGTH_LONG).show();
	}
	
	@JavascriptInterface
	public void setdevotionVariables(String lid,String message,String title)
	{
		Connector.setDailyGuideLesson(Connector.context,lid);
		Connector.LID=lid;
		Connector.title= title;
		Connector.message=message;
		
		//Toast.makeText(context, Connector.getServerTime()+" / "+t, Toast.LENGTH_LONG).show();
		
	}
	
	
	
	@JavascriptInterface
	public void serverDate(String t)
	{
		Connector.setServerDate(t);
		//Toast.makeText(context, Connector.getServerTime()+" / "+t, Toast.LENGTH_LONG).show();
		
	}
	
	
	@JavascriptInterface
	public void goBack()
	{
		if(webView!=null)
		{
			if(webView.canGoBack())
			{
				webView.goBack();
			}
		}
	}
}
