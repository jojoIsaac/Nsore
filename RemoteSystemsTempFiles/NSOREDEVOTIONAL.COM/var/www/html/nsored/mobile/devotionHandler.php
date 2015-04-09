<?php 
include 'config.php';
include 'GMCManager.php';
include 'util.php';
//include 'GMCManagerClient.php';
//$query="UPDATE `church_member_prayer_requests`  set status='responded' where `church_member_prayer_request_id`='11'";
//$results= mysql_query($query) or die(mysql_error());
//echo json_encode(getGroupUsersDevices(2,4));
//echo count(getGroupUsersDevices(2,4));
//$devotionDetails= fetchChurchGroupFeed(1,4);
//echo ;
if(isset($_GET['reason']))
{
	$reason= $_GET['reason'];
}
if(isset($_POST['reason']))
{
	$reason= $_POST['reason'];
}

if($reason=="Load Connections")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$content['Friends']=loadFriends($memberID,$myID,$startIndex);
	echo json_encode($content);
}
else if($reason=="Load Church Members")
{
	if (isset($_POST['reason']))
	{

		$churchID=$_POST['CID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$churchID=$_GET['CID'];
		$myID= $_GET['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$content['Friends']=loadChurchMembers($churchID,$myID,$startIndex) ;
	echo json_encode($content);
	// echo json_encode(loadFriends($memberID,$myID,$startIndex));





}
else if($reason=="Save PrayerRequest")
{

	if (isset($_POST['reason']))
	{

		$cid = $_POST['CID'];
		$memID=$_POST['MID'];
		$request=$_POST['request'];

	}
	else{
		$cid = $_GET['CID'];
		$memID=$_GET['MID'];
		$request=$_GET['request'];

	}
	$query="INSERT INTO `church_member_prayer_requests`
	( `church_id`,  `member_id`, `prayer_request`)
	VALUES ('$cid', '$memID', '$request')";

	$results= mysql_query($query);
	if($results )
	{
		echo 'Saved';
	}
	else{
		echo "ERR";
	}
	exit();

}

else if($reason=="Get My PrayerRequest")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];

			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$memberID=$_GET['MID'];

		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$data= fetchPrayerRequests($memberID,$startIndex);
	// echo json_encode( $content);

	if(count($data))
	{
		$content['PrayerRequest']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Request';
		exit();
	}


}

else if($reason=="Get My PrayerRequest Response")
{
	if (isset($_POST['reason']))
	{

		$request=$_POST['RID'];
		$CID=$_POST['CID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$request=$_GET['RID'];
		$CID=$_GET['CID'];

		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$data= getPrayerRequestResponse($request,$CID,$startIndex);
	// echo json_encode( $content);

	if(count($data))
	{
		$content['PrayerRequest']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Request';
		exit();
	}


}

else if($reason=="Get Branch Weekly Activity")
{
	if (isset($_POST['reason']))
	{
		$request=$_POST['BID'];
		$CID=$_POST['CID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$request=$_GET['BID'];
		$CID=$_GET['CID'];

		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$data= getBranchActivities($request,$CID,$startIndex);
	// echo json_encode( $content);

	if(count($data))
	{
		$content['WeeklyActivities']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Request';
		exit();
	}


}






else if($reason=="Delete PrayerRequest")
{
	if (isset($_POST['reason']))
	{


		$request=$_POST['request'];

	}
	else{
			
		$request=$_GET['request'];

	}
	echo deletePrayerRequest($request);
}

else if($reason=="Load Church Groups")
{
	//getChurchGroups($myID,$cid,$startIndex=1)
	if (isset($_POST['reason']))
	{

		$cid=$_POST['CID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$cid=$_GET['CID'];
		$myID= $_GET['UID'];
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}
	$data=getChurchGroups($myID,$cid,$startIndex);

	if(count($data))
	{
		$content['groups']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Group Found';
		exit();
	}

}

else if($reason=="Join Group")
{
	if (isset($_POST['reason']))
	{

		$cid=$_POST['CID'];
		$myID= $_POST['UID'];
		$groupID=$_POST['GID'];
		$groupStatus=$_POST['GS'];
	}

	else{

		$cid=$_GET['CID'];
		$myID= $_GET['UID'];
		$groupID=$_GET['GID'];
		$groupStatus=$_GET['GS'];
		//$year= $_POST['Y'];

	}
if(!empty($myID) && !empty($groupID))
	echo  json_encode( joinGroup($myID,$groupID,$cid,$groupStatus));
else 
	echo "Error. Please try again";

}

else if($reason=="Leave Group" || $reason =="Cancel Group Request")
{
	if (isset($_POST['reason']))
	{


		$myID= $_POST['UID'];
		$groupID=$_POST['GID'];

	}

	else{


		$myID= $_GET['UID'];
		$groupID=$_GET['GID'];


	}
	echo  json_encode(  leaveGroup($groupID,$myID));
}

else if($reason=="Load my Groups")
{
	//loadMyGroups($userID)
	//$cid=$_POST['CID'];
	if (isset($_POST['reason']))
	{
		//$year= $_POST['Y'];
		$myID= $_POST['UID'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		// $cid=$_GET['CID'];
		$myID= $_GET['UID'];
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$data= loadMyGroups($myID,$startIndex);

	if(count($data))
	{
		$content['groups']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Group Found';
		exit();
	}
}

else if($reason=="Load Group Members")
{
	if (isset($_POST['reason']))
	{

		$groupID=$_POST['GID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$groupID=$_GET['GID'];
		$myID= $_GET['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$content['Friends']=getGroupMembers($groupID,$myID,$startIndex);
	echo json_encode($content);
	// echo json_encode(loadFriends($memberID,$myID,$startIndex));





}

else if($reason=="Load Group Feeds")
{
	//echo 89;
	if (isset($_POST['reason']))
	{

		$groupID=$_POST['GID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$groupID=$_GET['GID'];
		$myID= $_GET['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$data=fetchChurchGroupFeeds($groupID,$myID,$startIndex);
	//logAction(json_encode($content),"logs.txt"); 
	 
	if(count($data))
	{
		$content['GroupFeed']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Group Feed Found';
		exit();
	}
	//echo json_encode($content);
	// echo json_encode(loadFriends($memberID,$myID,$startIndex));





}

else if($reason=="Get Group Feed Comments")
{
	//daily_guide_lessons_comment_id - {bigint(20)} member_id -
	//{bigint(20)} daily_guide_lesson_id - {int(11)}
	//	comment - {text} date_added - {datetime}
	if (isset($_POST['reason']))
	{
		$devotionID = $_POST['LID'];
		$memberID=$_POST['MID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{
		$devotionID = $_GET['LID'];
		$memberID=$_GET['MID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}
	
	$data= fetchChurchGroupFeedComments($devotionID,$memberID,$startIndex);
	//loadMyGroups($myID,$startIndex);
	
	if(count($data))
	{
		$content['FeedComment']=$data;
		echo json_encode($content);
		exit();
	}
	else{
		echo 'No Comment Found.';
		exit();
	}
	//fetchChurchGroupFeedComments($feedID,$sss)
}






else if($reason=="UnLike Group Feed")
{
	$tid = $_POST['FID'];
	$uid=$_POST['USER_ID'];
	$uname=$_POST['UN'];
	$did=$_POST['DID'];
	$query="DELETE from `group_feed_likes` where `group_post_id`='$tid' and `member_id`='$uid'";
	mysql_query($query) or die(mysql_error());
	exit();
}
 
else if($reason=="Like Group Feed")
{
	 
	$tid = $_POST['FID'];
	$uid=$_POST['USER_ID'];
	$uname=$_POST['UN'];
	$did=$_POST['DID'];
	$ismine=$_POST['IS-MINE'];
	$groupID=getFeedGroupID($tid);
	$query="DELETE from `group_feed_likes` where `group_post_id`='$tid' and `member_id`='$uid'";
	mysql_query($query) or die(mysql_error());
	$query="INSERT INTO `group_feed_likes`(`group_post_id`,`member_id`) values('$tid','$uid')";
	$results= mysql_query($query) or die(mysql_error());
//$results= mysql_query($query) or die(mysql_error());

	if($results)
	{
		$devotionDetails= array();
	    $devotionDetails= fetchChurchGroupFeed($tid,$uid);
	    //getDevotionDetails($cid);
		//$churchID= getchurch($cid);
		//$memberID
		$deviceIDs = getGroupUsersDevices($groupID ,$uid);
	 $message=array();


	 $val= array();
	 $i=0;
		$val[$i]=  $deviceIDs;
		$message= array();
		$message['data-head']=$devotionID;
		$message['alert']= $uname. " liked a post on your group wall";
		$message['other-param']= $uname;
		$message['devotion_details']= $devotionDetails;
		$message= json_encode($message);
		$gcm = new GCMmanager ();
		$type="Group Item like";
		$gcm->sendPush( $deviceIDs,$message,$type);
		//echo alertForDevotionComment($cid,$devotionDetails,$churchID,$memberID,$username);
		echo trim('Saved');
	 exit();


	}
	else{

		echo "Not saved";
		exit();
	}
	 
}

else if($reason=="Save Feed Comment")
{
	$username="A user ";
	if (isset($_POST['reason']))
	{
	 $feedID=$_POST['topicid'];
	 $data=$_POST['data'];
	 if(isset($_POST['UN']))
	 {
	 	$username= $_POST['UN'];
	 }
	 $date=date('Y-m-d');
	 $memberID=$_POST['senderid'];
	}
	else
	{
		$feedID=$_GET['topicid'];
		$data=$_GET['data'];
	 if(isset($_GET['UN']))
	 {
	 	$username= $_GET['UN'];
	 }

	 $date=date('Y-m-d');
	 $memberID=$_GET['senderid'];
	}

	//daily_guide_lessons_comment_id - {bigint(20)} member_id -
	//{bigint(20)} daily_guide_lesson_id - {int(11)}
	//	comment - {text} date_added - {datetime}
	$date=date('Y-m-d h:i:s');
	

	$results= saveFeedComment($feedID,$memberID,$data);

	if($results=="Save")
	{
		$uid=$memberID;
	  $groupID=getFeedGroupID($feedID);
		$devotionDetails= array();
		$devotionDetails= fetchChurchGroupFeed($feedID,$uid);
	    //getDevotionDetails($cid);
		$churchID= getchurch($cid);
		//$memberID
		$deviceIDs = getGroupUsersDevices($groupID ,22);
	 $message=array();


	    $val= array();
	    $i=0;
		$val[$i]=  $deviceIDs;
		$message= array();
		$message['data-head']=$devotionID;
		$message['alert']= $username. " commented on a post on your group ";
		$message['other-param']= $username;
		echo $message['devotion_details']= $devotionDetails;
		$message= json_encode($message);
		$gcm = new GCMmanager ();
		$type="Group Feed Comment";
		$gcm->sendPush( $deviceIDs,$message,$type);
		//echo alertForDevotionComment($cid,$devotionDetails,$churchID,$memberID,$username);
		echo trim('Saved');
	 exit();


	}
	else{

		echo "Not saved";
		exit();
	}
}

else if($reason=="Delete Feed Comment")
{
	$cid=$_POST['topicid'];
	//$query="Delete from `daily_guide_lessons_comments` where `daily_guide_lessons_comment_id`='$cid' ";
	echo $results= deleteComment($cid);
	
	exit();

}



else if($reason=="Save Nsore Group Feed")

{

	$username="A user ";
	$title="";
	if (isset($_POST['reason']))
	{
		$cid=$_POST['GID'];
		$data=$_POST['userpost'];
		if(isset($_POST['UN']))
		{
			$uname= $_POST['UN'];
		}
		$date=date('Y-m-d');
		$memberID=$_POST['senderid'];
	}
	else
	{
		$cid=$_GET['GID'];
		$data=$_GET['userpost'];
		if(isset($_GET['UN']))
		{
			$uname= $_GET['UN'];
		}

		$date=date('Y-m-d');
		$memberID=$_GET['senderid'];
	}

if($memberID>0 && $cid>0)
{
	

	
	$response=postToGroup($memberID, $cid, "", $data,"");
	
	if($response=="Saved")
	{
		$devotionDetails= array();
		$devotionDetails= getChurchGroupDetails($cid,$memberID);
		//getDevotionDetails($cid);
		$churchID= getchurch($cid);
		//$memberID
		$deviceIDs = getGroupUsersDevices($cid ,$memberID);
		$message=array();
	
	
		$val= array();
		$i=0;
		$val[$i]=  $deviceIDs;
		$message= array();
		$message['data-head']=$devotionID;
		$message['alert']= $uname. " posted on your group wall";
		$message['other-param']= $uname;
		$message['devotion_details']= $devotionDetails;
		$message= json_encode($message);
		$gcm = new GCMmanager ();
		$type="Group Message Post";
		$gcm->sendPush( $deviceIDs,$message,$type);
		//echo alertForDevotionComment($cid,$devotionDetails,$churchID,$memberID,$username);
		echo trim('Data Saved');
		exit();
	
	
	}
	else{
	
		echo "Not saved";
		exit();
	}
	
	
	
}

}





else if($reason=="Load My Connections")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$content['Friends']=loadFriends($memberID,$memberID,$startIndex);
	echo json_encode($content);
	exit();




}

else if($reason=="Load Connection Request")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{
		$myID= $_GET['UID'];
		$memberID=$_GET['MID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}
	$data = loadFriendRequests($myID,0,$startIndex);
	if($data!="No request")
	{
	 $content['Friends']=$data;
	 echo json_encode($content);
	 exit();

	}
	else{
		echo $data;
		exit();

	}







}

else if($reason=="Cancel Request")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
			
	}

	echo cancelRequest($myID, $memberID);
}

else if($reason=="Delete Connection")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
			
	}

	echo cancelRequest($myID, $memberID);
}
else if($reason=="Accept Request")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
			
	}
	$result= acceptRequest($myID, $memberID);

	$deviceIDs = getUserDeviceIDs($memberID , $myID);
	if(count($deviceIDs)>=1 && $result!="Error")
	{
		$message=array();

		$devotionDetails=getUserDetails($myID );
		$val= array();
		$i=0;
		$val[$i]=  $deviceIDs;
		$message= array();
		$message['data-head']=$devotionID;
		$username=$devotionDetails['name'];
		$message['alert']= " Now friends with ".$username;
		$message['other-param']= $username;
		$message['User_details']= $devotionDetails;
		//echo
		$message= json_encode($message);
		$gcm = new GCMmanager ();
		$type="Accept Request";
		$gcm->sendPush( $deviceIDs,$message,$type);
	}

	echo $result;

}


else if($reason=="Search User")
{
	//findUser
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$cid= $_POST['CID'];
			
		$data= $_POST['data'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{
		$cid= $_GET['CID'];
		$memberID=$_GET['MID'];
			
		$data= $_GET['data'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}

	$result= findUser($data,$memberID,$cid,$startIndex);

	if($result=='No Friend Found')
	{
	}
	else{
		$content['Friends']=$result;
		echo json_encode($content);
	}

}

else if($reason=="Send Request")
{
	if (isset($_POST['reason']))
	{

		$memberID=$_POST['MID'];
		$myID= $_POST['UID'];
	}

	else{

		$memberID=$_GET['MID'];
		$myID= $_GET['UID'];
			
	}
	cancelRequest($myID, $memberID);
	$result= sendRequest($myID, $memberID);
	if($result=='Request Sent')
	{
		///getUserDeviceIDs($userID,$ignore="-1")
			
		$devotionDetails= array();
		$devotionDetails=getUserDetails($myID,$memberID );
		//$churchID= getchurch($cid);
		$deviceIDs = getUserDeviceIDs($memberID , $myID);
		if(count($deviceIDs)>=1)
		{
			$message=array();


			$val= array();
			$i=0;
			$val[$i]=  $deviceIDs;
			$message= array();
			$message['data-head']=$devotionID;
			$username=$devotionDetails['name'];
			$message['alert']=" Connection Request from ". $username;
			$message['other-param']= $username;
		 $message['User_details']= $devotionDetails;
		 //echo
		 $message= json_encode($message);
		 $gcm = new GCMmanager ();
		 $type="Friend Request";
		 $gcm->sendPush( $deviceIDs,$message,$type);
		}

			
			
	}

	echo $result;
	// echo cancelRequest($myID, $memberID);
}




else if($reason=="Get Devotion lesson Comments")
{
	//daily_guide_lessons_comment_id - {bigint(20)} member_id -
	//{bigint(20)} daily_guide_lesson_id - {int(11)}
	//	comment - {text} date_added - {datetime}
	if (isset($_POST['reason']))
	{
		$devotionID = $_POST['LID'];
		$memberID=$_POST['MID'];
			
		//$year= $_POST['Y'];
		if(isset($_POST['index']))
			$startIndex=$_POST['index'];
		else
			$startIndex=1;
	}

	else{
		$devotionID = $_GET['LID'];
		$memberID=$_GET['MID'];
			
		//$year= $_POST['Y'];
		if(isset($_GET['index']))
			$startIndex=$_GET['index'];
		else
			$startIndex=1;
	}




	$query="SELECT CONCAT(`first_name`,' ',`last_name`) as 'N',about,`userdp` as 'DP',church_members.`church_id` as 'church_id',`comment` as 'C',`email` as 'email',`phone_number` as 'phone'
	,SUBSTRING(`comment`,1,100) as 'M',`date_added` as 'date',`daily_guide_lesson_id` as 'TID',date_joined,
	`member_id` as 'SID',`daily_guide_lessons_comment_id` as 'comment_id' from daily_guide_lessons_comments,church_members where
	`daily_guide_lessons_comments`.member_id=church_members.church_member_id   and
	`daily_guide_lesson_id`='$devotionID'


	order by `daily_guide_lesson_id` desc limit ".($startIndex-1).", 10";



	$results= mysql_query($query) or die(mysql_error());
	if($results && mysql_num_rows($results))
	{
		$content=array();

		while($row= mysql_fetch_assoc($results))
		{
			$data=array();
			$data['email']= $row['email'];
			$data['phone']= $row['phone'];
			$data['about-user']=$row['about'];
			$data['name']= ucwords(trim($row['N']));
			$data['profilePic']= $row['DP'];
			$data['comment']= $row['C'];
			$data['lessonID']= $row['TID'];
			$date=  ago($row['date'], -1);
			$data['timeStamp']=$date;
			$data['comment_id']= $row['comment_id'];
			$data['memberID']= $row['SID'];
			$data['date_joined']=ago($row['date_joined'], -1);
		 $cid= $row['church_id'];
		 $data['CID']= $cid;
		 $data['cover']="NOT_SET";
		 $data['friends']= loadFriendsCount($data['memberID']);
		 $data['churchName']=getchurchName($cid);
		 $data['churchprofilePic']=
		 getchurchlogo($cid);
		 if($memberID!= $data['memberID'])
		 {
			 $data['isConnected']  = isConnected($memberID,$data['memberID']);
			 $data['connection']= loadFriendsCount($data['memberID']);
		 }
		 else{
		 	 
		 	$data['isConnected']  =5; //My records
			 $data['connection']=-1;
		 }

		 //name memberID comment_id timeStamp comment profilePic

		 $content['DevotionComment'][]=$data;

		}

	 echo json_encode($content);
	  

	}
	else

		echo 'No Comment Found.';

	// Full texts 	church_member_id


}


else if($reason=="Save devotion Comment")
{
	$username="A user ";
	if (isset($_POST['reason']))
	{
	 $cid=$_POST['topicid'];
	 $data=$_POST['data'];
	 if(isset($_POST['UN']))
	 {
	 	$username= $_POST['UN'];
	 }
	 $date=date('Y-m-d');
	 $memberID=$_POST['senderid'];
	}
	else
	{
		$cid=$_GET['topicid'];
		$data=$_GET['data'];
	 if(isset($_GET['UN']))
	 {
	 	$username= $_GET['UN'];
	 }

	 $date=date('Y-m-d');
	 $memberID=$_GET['senderid'];
	}

	//daily_guide_lessons_comment_id - {bigint(20)} member_id -
	//{bigint(20)} daily_guide_lesson_id - {int(11)}
	//	comment - {text} date_added - {datetime}
	$date=date('Y-m-d h:i:s');
	$query="INSERT into `daily_guide_lessons_comments`(
	`comment` ,
	`date_added` ,
	`member_id` ,
	`daily_guide_lesson_id` ) VALUES (
	'$data', NOW( ) , '$memberID', '$cid'
	);";

	$results= mysql_query($query) or die(mysql_error());

	if($results)
	{
		$devotionDetails= array();
	    $devotionDetails=getDevotionDetails($cid);
		$churchID= getchurch($cid);
		$deviceIDs = getReceiversDeviceIDs($churchID , $memberID);
	 $message=array();


	 $val= array();
	 $i=0;
		$val[$i]=  $deviceIDs;
		$message= array();
		$message['data-head']=$devotionID;
		$message['alert']= $username. " commented on a devotion ";
		$message['other-param']= $username;
		echo $message['devotion_details']= $devotionDetails;
		$message= json_encode($message);
		$gcm = new GCMmanager ();
		$type="Devotion Comment";
		$gcm->sendPush( $deviceIDs,$message,$type);
		//echo alertForDevotionComment($cid,$devotionDetails,$churchID,$memberID,$username);
		echo trim('Saved');
	 exit();


	}
	else{

		echo "Not saved";
		exit();
	}
}

else if($reason=="Delete Devotion Comment")
{
	$cid=$_POST['topicid'];
	$query="Delete from `daily_guide_lessons_comments` where `daily_guide_lessons_comment_id`='$cid' ";
	$results= mysql_query($query);
	if($results )
	{
		echo 'Deleted';
	}
	else{
		echo "ERR";
	}
	exit();

}












?>