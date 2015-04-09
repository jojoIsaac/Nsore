package com.appsol.apps.projectcommunicate.model;

public class Testimony extends NsoreDevotionalUser {

private String content;
private String testimonyID;

private String fullcontent;
private String messageTime;
private String likes;
private String likeTestimony;
private String churchdp;
private String id;
private String Json;
public void setJson(String json) {
	Json = json;
}

public String getJson() {
	return Json;
}
public void setId(String id) {
	this.id = id;
}
public String getId() {
	return id;
}

public String getChurchdp() {
	return churchdp;
}



public void setChurchdp(String churchdp) {
	this.churchdp = churchdp;
}





public void setLikeTestimony(String likeTestimony) {
	this.likeTestimony = likeTestimony;
}


public String getLikeTestimony() {
	return likeTestimony;
}

public void setLikes(String likes) {
	this.likes = likes;
}

public String getLikes() {
	return likes;
}
public void setMessageTime(String messageTime) {
	this.messageTime = messageTime;
}

public String getMessageTime() {
	return messageTime;
}
public void setFullcontent(String fullcontent) {
	this.fullcontent = fullcontent;
}

public String getFullcontent() {
	return fullcontent;
}


public void setUserdp(String userdp) {
	setProfilePic(userdp);
}
public String getUserdp() {
	return getProfilePic();
}

public void setSenderid(String senderid) {
	setMemberID(senderid);
}

public String getSenderid() {
	return getMemberID();
}


public void setContent(String content) {
	this.content = content;
}
public String getContent() {
	return content;
}

public void setTestimonyID(String testimonyID) {
	this.testimonyID = testimonyID;
}
public String getTestimonyID() {
	return testimonyID;
}
	public Testimony() {
		// TODO Auto-generated constructor stub
	}

}
