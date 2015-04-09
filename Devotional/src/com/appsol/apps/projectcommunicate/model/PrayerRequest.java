package com.appsol.apps.projectcommunicate.model;

public class PrayerRequest {
	private String church_member_prayer_request_id;
	private String church_id;
	private String branch_id;
	private String member_id;
	private String prayer_request;
	private String sent_at;
	private String status;
	private String churchname;
	public void setChurchname(String churchname) {
		this.churchname = churchname;
	}
	public String getChurchname() {
		return churchname;
	}

	public void setChurch_member_prayer_request_id(
			String church_member_prayer_request_id) {
		this.church_member_prayer_request_id = church_member_prayer_request_id;
	}

	public String getChurch_member_prayer_request_id() {
		return church_member_prayer_request_id;
	}

	public void setChurch_id(String church_id) {
		this.church_id = church_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public String getChurch_id() {
		return church_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setPrayer_request(String prayer_request) {
		this.prayer_request = prayer_request;
	}

	public String getPrayer_request() {
		return prayer_request;
	}

	public void setSent_at(String sent_at) {
		this.sent_at = sent_at;
	}

	public String getSent_at() {
		return sent_at;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public PrayerRequest() {
		// TODO Auto-generated constructor stub
	}

	
	public static class PrayerResponses extends PrayerRequest
	{
		private String church_member_prayer_response_id;
		private String request_id,response,responded_at,responded_by;
		public String getRequest_id() {
			return request_id;
		}
		public void setRequest_id(String request_id) {
			this.request_id = request_id;
		}
		public void setChurch_member_prayer_response_id(
				String church_member_prayer_response_id) {
			this.church_member_prayer_response_id = church_member_prayer_response_id;
		}
		
		public String getChurch_member_prayer_response_id() {
			return church_member_prayer_response_id;
		}
		
		public void setResponded_at(String responded_at) {
			this.responded_at = responded_at;
		}
		public String getResponded_by() {
			return responded_by;
		}
		
		public void setResponse(String response) {
			this.response = response;
		}
		public void setResponded_by(String responded_by) {
			this.responded_by = responded_by;
		}
		
		public String getResponse() {
			return response;
		}
		public String getResponded_at() {
			return responded_at;
		}
	}
			
}
