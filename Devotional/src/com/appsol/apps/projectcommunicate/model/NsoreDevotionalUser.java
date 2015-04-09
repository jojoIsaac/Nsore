package com.appsol.apps.projectcommunicate.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ArrayAdapter;



public class NsoreDevotionalUser {

	private String memberID, isConnected,mutualFriends,friendsCount;
	private String churchID, name, phone_number, email, deviceID, profilePic,memberSince,churchname;
	private String userCover;
	private String operationReason="";
	private String serverResponse="";
	private boolean mInError;
	private boolean newFiend;
	private String totalFriends;
	private String aboutUser;
	
	public void setAboutUser(String aboutUser) {
		this.aboutUser = aboutUser;
	}
	
	public void setTotalFriends(String totalFriends) {
		this.totalFriends = totalFriends;
	}
	
	public String getTotalFriends() {
		return totalFriends;
	}
	
	
	public void setNewFiend(boolean newFiend) {
		this.newFiend = newFiend;
	}
	
	public boolean isNewFiend() {
		return newFiend;
	}
	
	public void setServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}
	public String getServerResponse() {
		return serverResponse;
	}
	
	public void setOperationReason(String operationReason) {
		this.operationReason = operationReason;
	}
	public String getOperationReason() {
		return operationReason;
	}
	public void setMutualFriends(String mutualFriends) {
		this.mutualFriends = mutualFriends;
	}
	public void setFriendsCount(String friendsCount) {
		this.friendsCount = friendsCount;
	}
	public String getFriendsCount() {
		return friendsCount;
	}
	public String getMutualFriends() {
		return mutualFriends;
	}
	public void setUserCover(String userCover) {
		this.userCover = userCover;
	}
	
	public String getUserCover() {
		return userCover;
	}
    
	public String getAboutUser() {
		return aboutUser;
	}
	
	public void setMemberSince(String memberSince) {
		this.memberSince = memberSince;
	}
	
	public String getMemberSince() {
		return memberSince;
	}
	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	
	public void setChurchname(String churchname) {
		this.churchname = churchname;
	}
	public String getChurchname() {
		return churchname;
	}

	// `phone_number`,`email`,`username`
	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public void setChurchID(String churchID) {
		this.churchID = churchID;
	}

	public String getChurchID() {
		return churchID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setIsConnected(String isConnected) {
		this.isConnected = isConnected;
	}

	public String getIsConnected() {
		return isConnected;
	}

	public NsoreDevotionalUser() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	///User Utilities Functions
	public String handleConnections()
	{
		String response="";
		//handleServerRequest(this);
		return this.getServerResponse();
	}
	
	
	
	//This function when given the user Detail JsonObject returns an object of this class
	public static  NsoreDevotionalUser processUsersListJson(String response,NsoreDevotionalUser item)
	{
	
		  try {
			  
			  JSONObject objects = new JSONObject(response.trim());
				if (objects != null) {
				
	      
	          //JSONArray feedArray = response.getJSONArray("feed");


	//justClicked=199;
	         
	              JSONObject feedObj = objects;

	         item = new  NsoreDevotionalUser();
	            /*
	             name memberID comment_id timeStamp comment profilePic
	             * 
	             */
	           
	            item.setName(feedObj.getString("name"));
	            item.setMemberID(feedObj.getString("memberID"));
	         
	              item.setEmail(feedObj.getString("email"));
	              item.setPhone_number(feedObj.getString("phone"));
	            //item.setCurrent(false);
				  item.setProfilePic(feedObj.getString("profilePic"));
				  item.setIsConnected(feedObj.getInt("isConnected")+"");
				  item.setFriendsCount(feedObj.getString("friends")+"");
				  item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
				  item.setMemberSince(feedObj.getString("date_joined"));
				  item.setUserCover(feedObj.getString("cover"));
				  item.setChurchname(feedObj.getString("churchName"));
	              item.setChurchID(feedObj.getString("CID"));
	              item.setAboutUser(feedObj.getString("about-user"));
					
	               
	           //startAT = 
	            
	           return  item;
	      
					
				}
	      } catch (JSONException e) {
	          e.printStackTrace();
	          return null;
	      }
		  return item;
	}
	
	
	
	
	
	

}
