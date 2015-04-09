package com.appsol.apps.projectcommunicate.model;

public class ChurchLeader {
//`leader_name`,`leader_title`,`position`,`leader_photo`,`profile`
	private String leader_name;
	private String leader_title;
	private String position;
	private String leader_photo;
	private String profile;
	private String Id;
	
	public void setId(String id) {
		Id = id;
	}
	public String getId() {
		return Id;
	}
	
	public void setLeader_name(String leader_name) {
		this.leader_name = leader_name;
	}
	
	public String getLeader_name() {
		return leader_name;
	}
	
	public void setLeader_photo(String leader_photo) {
		this.leader_photo = leader_photo;
	}
	
	public String getLeader_photo() {
		return leader_photo;
	}
	public String getLeader_title() {
		return leader_title;
	}
	public void setLeader_title(String leader_title) {
		this.leader_title = leader_title;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPosition() {
		return position;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getProfile() {
		return profile;
	}
	public ChurchLeader() {
		// TODO Auto-generated constructor stub
	}

}
