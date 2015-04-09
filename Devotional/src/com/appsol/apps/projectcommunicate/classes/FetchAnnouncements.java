package com.appsol.apps.projectcommunicate.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.appsol.apps.projectcommunicate.model.ChurchAnnouncements;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;

public class FetchAnnouncements extends AsyncTask<String, Void, String>{
    private final com.appsol.apps.projectcommunicate.classes.FetchAnnouncementListener listener;
    private String msg;
    private String month = "";
    
    public FetchAnnouncements(com.appsol.apps.projectcommunicate.classes.FetchAnnouncementListener listener) {
        this.listener = listener;
    }
    
    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;
        
        // get url from params
        ArrayList<NameValuePair> paramm= new ArrayList<NameValuePair>();
        String url = params[0];
        
//		
//        
        try {
            // create http connection
            HttpClient client = new DefaultHttpClient();
          //  HttpGet httpget = new HttpGet(url);
            HttpPost post = new HttpPost(url);
            
          //  paramm.add( new BasicNameValuePair("reason","Get Events"));
    		paramm.add( new BasicNameValuePair("cid",Connector.getChurchID()));
    		paramm.add( new BasicNameValuePair("bid",Connector.getBranch()));
    		
    		post.setEntity(new UrlEncodedFormEntity(paramm));
            
            // connect
            HttpResponse response = client.execute(post);
            
            // get response
            HttpEntity entity = response.getEntity();
            
            if(entity == null) {
                msg = "No response from server";
                return null;        
            } else {
            	msg = "Response obtained";
            }
         
            // get response content and convert it to json string
            InputStream is = entity.getContent();
            return streamToString(is);
        }
        catch(IOException e){
            msg = "No Network Connection";
        }
        
        return null;
    }
    
    @Override
    protected void onPostExecute(String sJson) {
        if(sJson == null) {
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }        
        
        try {
            // convert json string to json array
            JSONArray aJson = new JSONArray(sJson);
            // create apps list
            List<ChurchAnnouncements> announcements = new ArrayList<ChurchAnnouncements>();
            
            for(int i=0; i<aJson.length(); i++) {
            	JSONObject json = aJson.getJSONObject(i);
                ChurchAnnouncements announcement = new ChurchAnnouncements();
               // String tocheck = json.getString("");
             //   String month_num = tocheck.substring(5, 7);
                
             //   String month_num = month_numraw.toString();
               
                
                String month_num = "08"; //supposed to be gotten from the date.
                Log.d("Yaw, see", month_num);
                
               
           
                announcement.setCaption(json.getString("caption"));
                announcement.setContent(json.getString("content"));
//                event.setDetail(json.getString("details"));
//                event.setEndDate(json.getString("end_date"));
//                event.setType(json.getString("type"));
//                event.setLocation(json.getString("location"));
//                event.setStart_Time(json.getString("start_time"));
//               // app.setSaleOption(json.getString("saleoption"));
                
                // add the events to event list
                announcements.add(announcement);
             //   Log.d("JSSON", json.toString());
            
            }
     //           }
           
            
            //notify the activity that fetch data has been complete
            if(listener != null) listener.onFetchComplete(announcements);
        } catch (JSONException e) {
            msg = "Invalid response";
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }        
    }
    
    /**
     * This function will convert response stream into json string
     * @param is respons string
     * @return json string
     * @throws IOException
     */
    public String streamToString(final InputStream is) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder(); 
        String line = null;
        
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } 
        catch (IOException e) {
            throw e;
        } 
        finally {           
            try {
                is.close();
            } 
            catch (IOException e) {
                throw e;
            }
        }
        
        return sb.toString();
    }
{
}
}
