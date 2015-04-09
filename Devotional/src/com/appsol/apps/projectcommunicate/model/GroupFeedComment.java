package com.appsol.apps.projectcommunicate.model;


public class GroupFeedComment extends NsoreDevotionalUser {
//comment_id ,group_post_id ,member_id ,comment ,date_sent
	private String group_post_id;
//	private String name;

	private String comment_id;
	private String timeStamp;
	private String comment;

	
	

	
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	
	public String getComment_id() {
		return comment_id;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	
	public String getGroup_post_id() {
		return group_post_id;
	}
	public void setGroup_post_id(String group_post_id) {
		this.group_post_id = group_post_id;
	}
	
	
	
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	
	
	
	public GroupFeedComment() {
		// TODO Auto-generated constructor stub
	}

}
