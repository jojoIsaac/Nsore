package com.appsol.apps.projectcommunicate.model;

public class ChurchGroup extends Church {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupID;
	private String groupName;
	private String dateCreated;
	private String slogan;
	private String description;
	private String grouplogo;
	private String groupType;
	private String memberSince;
	private String Membershipstatus;
	private String createdBy;
	private String membersCount;
	private String requestedby;
	private String json;
	//For server utilities
	private String operationReason;
	public void setOperationReason(String operationReason) {
		this.operationReason = operationReason;
	}
	
	public String getOperationReason() {
		return operationReason;
	}
	//
	
	
	
	
	

	// Getters
	
public String getMembersCount() {
	return membersCount;
}
	public String getGroupID() {
		return groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	
	public String getDateCreated() {
		return dateCreated;
	}
	
	public String getSlogan() {
		return slogan;
	}
	public String getDescription() {
		return description;
	}
	
	public String getGrouplogo() {
		return grouplogo;
	}
	public String getGroupType() {
		return groupType;
	}
	public String getMembershipstatus() {
		return Membershipstatus;
	}
	public String getMemberSince() {
		return memberSince;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	//Setters
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGrouplogo(String grouplogo) {
		this.grouplogo = grouplogo;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public void setMemberSince(String memberSince) {
		this.memberSince = memberSince;
	}

	public void setMembershipstatus(String membershipstatus) {
		Membershipstatus = membershipstatus;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public void setMembersCount(String membersCount) {
		this.membersCount = membersCount;
	}

	public ChurchGroup() {
		// TODO Auto-generated constructor stub
	}

	public void setJson(String string) {
		// TODO Auto-generated method stub
		json= string;
	}
	public String getJson() {
		return json;
	}
	

}
