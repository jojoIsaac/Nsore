package com.appsol.apps.projectcommunicate.model;

import java.io.Serializable;
import java.util.Date;

public class Church implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// This field is the primary key for the table churches
	private Integer churchid;
	private String churchname;
	private String churchdescription;
	private String churchlogo;
	private String superadminusername;
	private String superadminpassword;
	private Character accounttype;
	private Character status;
	private Date createdat;
	private Integer createdby;
	private String website;
	private String fax;
	private String phone1;
	private String phone2;
	private String phone3;
	private String address;
	private String christianitytype;
	private String headofficelocation;
	private String branchCount;
	private String branchID="-1";
	
	
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	
	public String getBranchID() {
		return branchID;
	}
	
	
	private String churchJson;
	public void setBranchCount(String branchCount) {
		this.branchCount = branchCount;
	}
	
	public String getBranchCount() {
		return branchCount;
	}
	public void setChurchJson(String churchJson) {
		this.churchJson = churchJson;
	}
	public String getChurchJson() {
		return churchJson;
	}
	
	public void setchurchid(Integer churchid) {
		this.churchid = churchid;
	}

	public Integer getchurchid() {
		return churchid;
	}

	public void setchurchname(String churchname) {
		this.churchname = churchname;
	}

	public String getchurchname() {
		return churchname;
	}

	public void setchurchdescription(String churchdescription) {
		this.churchdescription = churchdescription;
	}

	public String getchurchdescription() {
		return churchdescription;
	}

	public void setchurchlogo(String churchlogo) {
		this.churchlogo = churchlogo;
	}

	public String getchurchlogo() {
		return churchlogo;
	}

	public void setsuperadminusername(String superadminusername) {
		this.superadminusername = superadminusername;
	}

	public String getsuperadminusername() {
		return superadminusername;
	}

	public void setsuperadminpassword(String superadminpassword) {
		this.superadminpassword = superadminpassword;
	}

	public String getsuperadminpassword() {
		return superadminpassword;
	}

	public void setaccounttype(Character accounttype) {
		this.accounttype = accounttype;
	}

	public Character getaccounttype() {
		return accounttype;
	}

	public void setstatus(Character status) {
		this.status = status;
	}

	public Character getstatus() {
		return status;
	}

	public void setcreatedat(Date createdat) {
		this.createdat = createdat;
	}

	public Date getcreatedat() {
		return createdat;
	}

	public void setcreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getcreatedby() {
		return createdby;
	}

	public void setwebsite(String website) {
		this.website = website;
	}

	public String getwebsite() {
		return website;
	}

	public void setfax(String fax) {
		this.fax = fax;
	}

	public String getfax() {
		return fax;
	}

	public void setphone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getphone1() {
		return phone1;
	}

	public void setphone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getphone2() {
		return phone2;
	}

	public void setphone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getphone3() {
		return phone3;
	}

	public void setaddress(String address) {
		this.address = address;
	}

	public String getaddress() {
		return address;
	}

	public void setchristianitytype(String string) {
		this.christianitytype = string;
	}

	public String getchristianitytype() {
		return christianitytype;
	}

	public void setheadofficelocation(String headofficelocation) {
		this.headofficelocation = headofficelocation;
	}

	public String getheadofficelocation() {
		return headofficelocation;
	}

	public Church() {

	}

	public Church(Integer churchid) {

		this.churchid = churchid;

	}

	@Override
	public int hashCode() {

		int hash = 7;

		hash = 71 * hash
				+ (this.churchid != null ? this.churchid.hashCode() : 0);

		return hash;
	}

	@Override
	public boolean equals(Object object) {

		// TODO: Warning - this method won't work in the case the id fields are
		// not set

		if (!(object instanceof Church)) {

			return false;

		}

		Church other = (Church) object;

		if ((this.churchid == null && other.churchid != null)
				|| (this.churchid != null && !this.churchid
						.equals(other.churchid))) {

			return false;

		}

		return true;

	}
	public void setLogo(String optString) {
		// TODO Auto-generated method stub
		
	}
	public void setId(String optString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getchurchname();
	}
	

}