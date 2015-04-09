package com.appsol.apps.projectcommunicate.classes;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.bool;
import android.app.Activity;

public class Devotion {

	public Devotion() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * daily_guide.`daily_guide_id` as 'DID', `daily_guide_lesson_id` as
	 * 'LID',`topic` as 'T',SUBSTRING(`message`,1,60) as 'M', `message` as
	 * 'Msg',`day` as 'D',`month` as 'MN',`reading_plan` as 'RP' ,`verse` as
	 * 'V',`prayer` as 'P'
	 */
	private String title;
	private String id, lesseonid, daily_guide_id;
	private String content, devotion;
	private String verse;
	private String prayer;
	private String readingPlan;
	private String devotionDate;
	private String summary;
	private String devotionday;
	private String normalDevotionDate;
	private String devotionJson;
	private String rawDate;
	private String CurrentDevotionID;
	private boolean isCurrent;
	private String devotionLikes;
	private String userLike;
    private String churchName;
    private String comments;
    
    public String getChurchName() {
		return churchName;
	}
    public void setChurchName(String churchName) {
		this.churchName = churchName;
	}
	public String getDevotionLikes() {
		return devotionLikes;
	}

	public String getUserLike() {
		return userLike;
	}

	public void setUserLike(String userLike) {
		this.userLike = userLike;
	}

	public void setDevotionLikes(String devotionLikes) {
		this.devotionLikes = devotionLikes;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrentDevotionID(String currentDevotion) {
		CurrentDevotionID = currentDevotion;
	}

	public String getCurrentDevotionID() {
		return CurrentDevotionID;
	}

	public void setRawDate(String rawDate) {
		this.rawDate = rawDate;
	}

	public String getRawDate() {
		return rawDate;
	}

	public void setDevotionJson(String devotionJson) {
		this.devotionJson = devotionJson;
	}

	public String getDevotionJson() {
		return devotionJson;
	}

	public void setNormalDevotionDate(String normalDevotionDate) {
		this.normalDevotionDate = normalDevotionDate;
	}

	public String getNormalDevotionDate() {
		return normalDevotionDate;
	}

	public void setDevotionday(String devotionday) {
		this.devotionday = devotionday;
	}

	public String getDevotionday() {
		return devotionday;
	}

	public String getDevotionDate() {
		return devotionDate;
	}

	public void setDevotionDate(String devotionDate) {
		this.devotionDate = devotionDate;
	}

	// Setters
	public void setDevotiondate(String devotiondate) {
		this.devotionDate = devotiondate;
	}

	public String getSummary() {
		return summary;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDaily_guide_id(String daily_guide_id) {
		this.daily_guide_id = daily_guide_id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public void setPrayer(String prayer) {
		this.prayer = prayer;
	}

	public void setReadingPlan(String readingPlan) {
		this.readingPlan = readingPlan;
	}

	public void setDevotion(String devotion) {
		this.devotion = devotion;
	}

	// Getters
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDevotiondate() {
		return devotionDate;
	}

	public String getDaily_guide_id() {
		return daily_guide_id;
	}

	public String getContent() {
		return content;
	}

	public String getPrayer() {
		return prayer;
	}

	public String getDid() {
		return lesseonid;
	}

	public String getLesseonid() {
		return lesseonid;
	}

	public void setLesseonid(String lesseonid) {
		this.lesseonid = lesseonid;
	}

	public String getDevotion() {
		return devotion;
	}

	public String getReadingPlan() {
		return readingPlan;
	}

	public String getTitle() {
		return title;
	}

	public String getVerse() {
		return verse;
	}
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getId() {
		return id;
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

	public static String getCurrentDevotion(Activity hostAcivity) {
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("CID", Connector.myPrefs
				.getString("CHURCH_ID", "")));
		parameters.add(new BasicNameValuePair("reason", "Get Lesson"));
		String response = "";
		response = Connector.sendData(parameters, Connector.context);

		return response;

	}
	
	
	

}
