package com.appsol.apps.projectcommunicate.model;


public class DevotionComment extends NsoreDevotionalUser {

	private String lessonID;
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
	
	
	
	public String getLessonID() {
		return lessonID;
	}
	
	
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void setLessonID(String id) {
		this.lessonID = id;
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

	
	
	
	public DevotionComment() {
		// TODO Auto-generated constructor stub
	}

}
