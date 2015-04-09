package com.appsol.apps.projectcommunicate.model;

public class ChurchGroupFeeds extends NsoreDevotionalUser {
 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String feedID;
	private String title;
	private String description;
	private String status;
	private String date_sent;
	private String isAdmin;
    private String attachment;
	private String likeFeed,Likes;
	private String groupID;
	private String comment;
	
	// Getters

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
   public String getLikeFeed() {
	return likeFeed;
}public String getLikes() {
	return Likes;
}
	public String getFeedID() {
		return feedID;
	}
	public String getTitle() {
		return title;
	}


	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
		
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public String getDate_sent() {
		return date_sent;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public String getAttachment() {
		return attachment;
	}
	//Setters
	 public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setFeedID(String feedID) {
		this.feedID = feedID;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDate_sent(String date_sent) {
		this.date_sent = date_sent;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public void setLikeFeed(String likeFeed) {
		this.likeFeed = likeFeed;
	}
	
	public void setLikes(String likes) {
		Likes = likes;
	}
	
	public ChurchGroupFeeds() {
		// TODO Auto-generated constructor stub
	}

}
