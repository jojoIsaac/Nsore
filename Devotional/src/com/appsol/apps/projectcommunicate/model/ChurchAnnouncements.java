package com.appsol.apps.projectcommunicate.model;

public class ChurchAnnouncements{
	private String caption;
    private String content;
    private String summary;
    private String ID;
    private String dateAdded;
    
    
    
    public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
    
    public String getDateAdded() {
		return dateAdded;
	}
    
    public void setSummary(String summary) {
		this.summary = summary;
	}
    
    public String getSummary() {
		return summary;
	}
    
    public void setID(String iD) {
		ID = iD;
	}
    
    public String getID() {
		return ID;
	}
    /*
     * `church_branch_announcement_id`,
	 `content`,SUBSTRING(`content`,1,60) as 'M',
	 `church_id`,
	 `caption`,DATE_FORMAT(`created_at`,'%M %d,%Y') as 'PD'
	 `created_at`
     */
  //  private String detail;

    
    //month
    
   
    
    
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    } 
    
    
   
    
    
}