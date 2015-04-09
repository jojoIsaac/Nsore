package com.appsol.apps.projectcommunicate.model;

import java.io.Serializable;
import java.util.Date;

public class church_branches extends Church implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// This field is the primary key for the table church_branches
	private Integer churchbranchid;
	private Integer churchid;
	private String branchname;
	private String location;
	private String address;
	private String phone1;
	private String phone2;
	private String phone3;
	private String website;
	private String fax;
	private String adminusername;
	private String adminpassword;
	private Character status;
	private Character isheadoffice;
	private Date createdat;
	private Integer createdby;
	private Integer usergroup;
	private String branchJson;
	
	public void setBranchJson(String branchJson) {
		this.branchJson = branchJson;
	}
	public String getBranchJson() {
		return branchJson;
	}

	public void setchurchbranchid(Integer churchbranchid) {
		this.churchbranchid = churchbranchid;
	}

	public Integer getchurchbranchid() {
		return churchbranchid;
	}


	public void setbranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getbranchname() {
		return branchname;
	}

	public void setlocation(String location) {
		this.location = location;
	}

	public String getlocation() {
		return location;
	}
	
	public String getBranchname() {
		return branchname;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	

	public void setadminusername(String adminusername) {
		this.adminusername = adminusername;
	}

	public String getadminusername() {
		return adminusername;
	}

	public void setadminpassword(String adminpassword) {
		this.adminpassword = adminpassword;
	}

	public String getadminpassword() {
		return adminpassword;
	}


	public void setisheadoffice(Character isheadoffice) {
		this.isheadoffice = isheadoffice;
	}

	public Character getisheadoffice() {
		return isheadoffice;
	}

	

	public void setusergroup(Integer usergroup) {
		this.usergroup = usergroup;
	}

	public Integer getusergroup() {
		return usergroup;
	}

	public church_branches() {

	}

	public church_branches(Integer churchbranchid) {

		this.churchbranchid = churchbranchid;

	}

	@Override
	public int hashCode() {

		int hash = 7;

		hash = 71
				* hash
				+ (this.churchbranchid != null ? this.churchbranchid.hashCode()
						: 0);

		return hash;
	}

	@Override
	public boolean equals(Object object) {

		// TODO: Warning - this method won't work in the case the id fields are
		// not set

		if (!(object instanceof church_branches)) {

			return false;

		}

		church_branches other = (church_branches) object;

		if ((this.churchbranchid == null && other.churchbranchid != null)
				|| (this.churchbranchid != null && !this.churchbranchid
						.equals(other.churchbranchid))) {

			return false;

		}

		return true;

	}

	
	public static class BranchActivity extends church_branches
	{
		/*
		 * {"id":"1","day":"monday","description":"","activity":"Mens Fellowship",
		 * "venue":"Church Auditorium","time":"6pm"},{"id":"2","day":"tuesday"
		 * ,"description":"","activity":"Bible Studies","venue":"Church Auditorium","time":"6pm - 8:30pm"}
		 */
		private String ID;
		private String day;
		private String description;
		private String activity;
		private String venue;
		private String time;
	
		
		
		public void setVenue(String venue) {
			this.venue = venue;
		}
		public String getVenue() {
			return venue;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTime() {
			return time;
		}
		
		public void setID(String iD) {
			ID = iD;
		}
		
		public String getID() {
			return ID;
		}
		public void setDay(String day) {
			this.day = day;
		}
		public String getDay() {
			return day;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		public void setActivity(String activity) {
			this.activity = activity;
		}
		public String getActivity() {
			return activity;
		}
		
	}
	
}