package com.appsol.apps.projectcommunicate.classes;
import java.util.List;

import com.appsol.apps.projectcommunicate.model.ChurchAnnouncements;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;


	public interface FetchAnnouncementListener {
	    public void onFetchComplete(List<ChurchAnnouncements> announcements);
	    public void onFetchFailure(String msg);
	    
	    
	}
