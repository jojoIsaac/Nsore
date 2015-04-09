package com.appsol.apps.projectcommunicate.model;

public class ChurchEvents {
	private String title;
	private String banner;
	private String detail;
	private String start_time;
	private String start_date;
	private String end_date;
	private String eventID;
	private String type;
	private String location;
	private String end_time;
	private String month;
	private String ispast;
	private String eventsSummary;
	private String isGoing;
	private String date_string;
	private String day_string;
	private String json;
	private String churchname;
	private Integer churchID;
	private String churchLogo,url,usersAttending;
	
	public String getUsersAttending() {
		return usersAttending;
	}
	public void setUsersAttending(String usersAttending) {
		this.usersAttending = usersAttending;
	}
	
	 public String getUrl() {
	        return url;
	    }
	 
	    public void setUrl(String url) {
	        this.url = url;
	    }
	
	public void setChurchID(Integer churchID) {
		this.churchID = churchID;
	}
	
	public void setChurchLogo(String churchLogo) {
		this.churchLogo = churchLogo;
	}
	
	public void setChurchname(String churchname) {
		this.churchname = churchname;
	}
	
	public Integer getChurchID() {
		return churchID;
	}
	public String getChurchLogo() {
		return churchLogo;
	}
	
	public String getChurchname() {
		return churchname;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	public String getJson() {
		return json;
	}
	
	public void setDate_string(String date_string) {
		this.date_string = date_string;
	}
	public String getDate_string() {
		return date_string;
	}
	public void setDay_string(String day_string) {
		this.day_string = day_string;
	}
	public String getDay_string() {
		return day_string;
	}
	
	public void setIsGoing(String isGoing) {
		this.isGoing = isGoing;
	}
	
	public String getIsGoing() {
		return isGoing;
	}
	
	
public void setEventsSummary(String eventsSummary) {
	this.eventsSummary = eventsSummary;
}

public String getEventsSummary() {
	return eventsSummary;
}
	
/*
 * `church_branch_event_id`,`event_title`,`details`,`start_date`,`end_date`,`start_time`
	          ,`end_time`,`banner`,`location`
 */
	// month
public void setEventID(String eventID) {
	this.eventID = eventID;
}
public String getEventID() {
	return eventID;
}
public String getEnd_date() {
	return end_date;
}
public String getEnd_time() {
	return end_time;
}
public String getStart_date() {
	return start_date;
}

public String getStart_time() {
	return start_time;
}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// startdate
	public String getStartDate() {
		return start_date;
	}

	public void setStartDate(String start_date) {
		this.start_date = start_date;
	}

	// time
	public String getStart_Time() {
		return start_time;
	}

	public void setStart_Time(String time) {
		this.start_time = time;
	}

	// endDate
	public String getEndDate() {
		return end_date;
	}

	public void setEndDate(String end_date) {
		this.end_date = end_date;
	}

	// location
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// details
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	// Banner
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	// type
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Endtime
	public String getEndTime() {
		return end_time;
	}

	public void setEndTime(String end_time) {
		this.end_time = end_time;
	}

	public void setMonth(String month) {
		this.month = month;

	}

	public String getMonth() {
		return month;
	}
	
	public void setIspast(String ispast) {
		this.ispast = ispast;
	}
	
	public String getIspast() {
		return ispast;
	}
	
	
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

}