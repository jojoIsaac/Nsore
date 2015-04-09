<?php 
function sendMail2($email,$message,$subject)
{
	$headers = "MIME-Version: 1.0" . "\r\n";
	$headers .= "Content-type: text/html; charset=iso-8859-1" . "\r\n";
	$headers .= "From: info@appsolinfosystems.com" . "\r\n" ."Reply-To: info@appsolinfosystems.com" . "\r\n" ."X-Mailer: PHP/" . phpversion();
	mail($email,$subject, $message, $headers);
}
function mysql_prep($value) {
	$magic_quote_active = get_magic_quotes_gpc();
	$new_enough_php = function_exists("mysql_real_escape_string");
	if ($new_enough_php) {
		if ($magic_quote_active) {
			$value = stripslashes($value);
		}
		$value = mysql_real_escape_string($value);
	} else {
		if (!$magic_quote_active) {
			$value = addslashes($value);
		}
	}
	return $value;
}
function getSubscribedChurches($myID,$myChurch)
{
	$query="SELECT church_id as 'CID',church_name as 'CN',`church_logo` as 'CL',
	`church_description` as 'CD',`website` as 'CW',`fax` as 'fax',`phone1` as 'Tel1',`phone2` as 'Tel2',`phone3` as  'Tel3'
	,`address` as 'CA',`christianity_type` as 'CT',`head_office_location` as 'HeadLocation',
	(SELECT count(*) from `church_branches` where
	`church_branches`.church_id=`churches`.church_id and `church_branches`.`status`='active'     ) as 'Branchcount'
	from `churches` where ( `status`='active' and (`church_id` in (SELECT DISTINCT `church_id` from subscriptions where `member_id`='$myID') ))
	or church_id='$myChurch' ";

	$res= mysql_query($query) or die(mysql_error());
	if($res && mysql_num_rows($res))
	{
	 $content=array();


	 while($row= mysql_fetch_assoc($res))
		{




			$content['Church'][]=$row;

		}

	 echo json_encode($content);


	}
	 
}
//The functions within this section deals with the church's groups

function getChurchGroups($myID,$cid,$startIndex=1)
{
	//`church_branch_group`
	$query="SELECT `church_branch_group_id`, `group_name`, `church_id`, `group_slogan`,
	`group_category`,
	`description`,
	`group_logo`,`branch_id`,
	`created_at`, `group_type`
	from `church_branch_group`  WHERE (`church_branch_group`.`church_id` in (SELECT DISTINCT `church_id` from subscriptions where `member_id`='$myID')
	and  `group_type`='opened_to_other_churches')
	or `church_branch_group`.`church_id` = '$cid'  order by `church_branch_group_id` desc   limit ".($startIndex-1).", 10 ";
		
		
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result)
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_branch_group_id'];
			$dataItem['name']=$row['group_name'];
			$dataItem['date']=ago($row['created_at'],-1);
			$dataItem['slogan']= $row['group_slogan'];
			$dataItem['description']= $row['description'];
			$dataItem['logo']= $row['group_logo'];
			$dataItem['type']= $row['group_type'];
			$dataItem['memberCount']=getGroupMemberCount($dataItem['id']);
			//getchurchName($id) getchurchlogo($id)
			$church=$row['church_id'];
			$dataItem['churchID']= $church;
			$dataItem['church']=getchurchName($church);
			$dataItem['church-logo']= getchurchlogo($church);
			$branch1=loadBranchName($row['branch_id']);
			$dataItem['branch']=($branch1==''?"NA":$branch1);
			$joinData= groupjoinInfo($dataItem['id'],$myID);
			if(count($joinData)>0)
			{
				$since=ago($joinData['date_requested'],-1);
				$dataItem['since']=($since=='Long Time Ago'?"NA":$since);
					
				$dataItem['status']=($joinData['status']==''?"NA":$joinData['status']);

			}
			else{

				$dataItem['since']="NA";
				$dataItem['status']="NA";
			}
			$data[]= $dataItem;
		}
		return $data;
	}

}

function getChurchGroupDetails($groupID,$myID)
{
	//`church_branch_group`
	$query="SELECT `church_branch_group_id`, `group_name`, `church_id`, `group_slogan`,
	`group_category`,
	`description`,
	`group_logo`,`branch_id`,
	`created_at`, `group_type`
	from `church_branch_group`  WHERE `church_branch_group_id`='$groupID' ";


	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result)
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_branch_group_id'];
			$dataItem['name']=$row['group_name'];
			$dataItem['date']=ago($row['created_at'],-1);
			$dataItem['slogan']= $row['group_slogan'];
			$dataItem['description']= $row['description'];
			$dataItem['logo']= $row['group_logo'];
			$dataItem['type']= $row['group_type'];
			$dataItem['memberCount']=getGroupMemberCount($dataItem['id']);
			//getchurchName($id) getchurchlogo($id)
			$church=$row['church_id'];
			$dataItem['churchID']= $church;
			$dataItem['church']=getchurchName($church);
			$dataItem['church-logo']= getchurchlogo($church);
			$branch1=loadBranchName($row['branch_id']);
			$dataItem['branch']=($branch1==''?"NA":$branch1);
			$joinData= groupjoinInfo($dataItem['id'],$myID);
			if(count($joinData)>0)
			{
				$since=ago($joinData['date_requested'],-1);
				$dataItem['since']=($since=='Long Time Ago'?"NA":$since);
					
				$dataItem['status']=($joinData['status']==''?"NA":$joinData['status']);

			}
			else{

				$dataItem['since']="NA";
				$dataItem['status']="NA";
			}
			$data= $dataItem;
		}
		return $data;
	}

}


function getGroupMembers($groupID,$myID,$startIndex=1)
{
	$allmyfriends = array();
	$i = 0;
	$query = "
	SELECT `member_id` from `church_branch_group_request` where `member_id`!='' and `group_id`='$groupID' and `status`='accepted'
	order by `church_branch_group_request_id` desc   limit ".($startIndex-1).", 10";
	$result = mysql_query($query) or die(mysql_error());
	$num = mysql_affected_rows();
	if ($num > 0) {

		while ($row = mysql_fetch_array($result)) {
			$sender= $row['member_id'];
			$recipient_id= $row['member_id'];
				
			array_push($allmyfriends, getUserDetails($sender,$myID));

				
			$i++;
		}
	}




	if(count($allmyfriends)>0)

		return $allmyfriends;
	else
		return 'No Friend Found';
}
function getGroupMemberCount($groupID)
{
	$query1="SELECT count(`group_id`) from `church_branch_group_request` where `member_id`!='' and `group_id`='$groupID' and `status`='accepted' ";
	$result = mysql_query($query1) or die(mysql_error());
	$count=0;
	if($result)
	{
		$count=mysql_result($result,0);

	}


	return $count;
}
function loadMyGroups($userID,$startIndex)
{
	$query="SELECT `church_branch_group_id`, `group_name`, `church_id`, `group_slogan`,
	`group_category`,`branch_id`,
	`description`,
	`group_logo`,
	`created_at`, `group_type`
	from `church_branch_group`  WHERE (`church_branch_group_id` in (SELECT DISTINCT `group_id` from `church_branch_group_request` where  `member_id`='$userID' )
	)
	order by `church_branch_group_id` desc   limit ".($startIndex-1).", 10 ";
		
		
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result)
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_branch_group_id'];
			$dataItem['name']=$row['group_name'];
			$dataItem['date']=ago($row['created_at'],-1);
			$dataItem['slogan']= $row['group_slogan'];
			$dataItem['description']= $row['description'];
			$dataItem['logo']= $row['group_logo'];
			$dataItem['type']= $row['group_type'];
			$dataItem['memberCount']=getGroupMemberCount($dataItem['id']);
			//getchurchName($id) getchurchlogo($id)
			$church=$row['church_id'];
			$dataItem['churchID']= $church;
			$dataItem['church']=getchurchName($church);
			$dataItem['church-logo']= getchurchlogo($church);
			$branch1=loadBranchName($row['branch_id']);
			$dataItem['branch']=($branch1==''?"NA":$branch1);
			$joinData= groupjoinInfo($dataItem['id'],$myID);
			if(count($joinData)>0)
			{
				$since=ago($joinData['date_requested'],-1);
				$dataItem['since']=($since=='Long Time Ago'?"NA":$since);
					
				$dataItem['status']=($joinData['status']==''?"NA":$joinData['status']);

			}
			else{

				$dataItem['since']="NA";
				$dataItem['status']="NA";
			}
			$data[]= $dataItem;
		}
		return $data;
	}
}


function leaveGroup($groupID,$userID)
{
	$query1="DELETE from `church_branch_group_request` where `group_id`='$groupID' and `member_id`='$userID' ";
	$result = mysql_query($query1) or die(mysql_error());
	if($result)
	{
	 $return ="Left";
	}
	else{
	 $return ="Error";
	}

	$count= getGroupMemberCount($groupID);
	$response= array("response"=> $return,"count"=>$count);
	return $response;

}

function groupjoinInfo($groupID,$userID)
{
	$query1="SELECT `status`,`date_requested` from `church_branch_group_request` where `group_id`='$groupID' and `member_id`='$userID' ";
	$result = mysql_query($query1) or die(mysql_error());
	if($result)
	{
		return mysql_fetch_assoc($result);
	}
	return
	array();
	 
}




function joinGroup($userID,$groupID,$cid,$groupStatus="closed")
{
	/*
	 CREATE TABLE IF NOT EXISTS `church_branch_group_request` (
	 		`church_branch_group_request_id` bigint(20) NOT NULL AUTO_INCREMENT,
	 		`member_id` bigint(20) NOT NULL,
	 		`group_id` bigint(20) NOT NULL,
	 		`church_id` int(11) NOT NULL,
	 		`branch_id` int(11) NOT NULL,
	 		`status` enum('pending','accepted','rejected') NOT NULL DEFAULT 'pending',
	 		`date_requested` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 		PRIMARY KEY (`church_branch_group_request_id`)
	 ) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
	*/
	$query1="SELECT count(`group_id`) from `church_branch_group_request` where `group_id`='$groupID' and `member_id`='$userID' ";
	$result = mysql_query($query1) or die(mysql_error());
	$count=0;
	if($result)
	{
		$count=mysql_result($result,0);

	}

	if($count<=0)
	{

		$query="INSERT INTO `church_branch_group_request`(`group_id`,`member_id`,`church_id`,`status`) values('$groupID','$userID','$cid',";
		if($groupStatus!="closed")
		{
			$query.="'accepted')";

		}
		else{
			$query.="'pending')";

		}

	 $result = mysql_query($query) or die(mysql_error());

	 if($result)
	 {
		 if($groupStatus=="closed")
		 {
		 	$return ="Waiting";

		 }
		 else
		 	$return ="saved";

	 }
	 else{
		 $return= "error";
		 	
		 	
	 }
	}
	else{

		$return= 'Already a member';

	}

	$count= getGroupMemberCount($groupID);
	$response= array("response"=> $return,"count"=>$count);
	return $response;



}

function fetchChurchGroupFeeds($groupID,$userID,$startIndex)
{
	/*
	`feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`sender` bigint(20) NOT NULL,
	`group_id` bigint(20) NOT NULL,
	`title` text NOT NULL,
	`description` text NOT NULL,
	`status` enum('hide','showing') NOT NULL DEFAULT 'hide',
	`date_sent` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`isAdmin` int(2) NOT NULL DEFAULT '0'
	 attachment
	*/
	
 	//$query="Truncate `group_feed_likes` ";
//	$result = mysql_query($query) or die(mysql_error());
	$query="SELECT DISTINCT * from `church_group_feed` where `group_id` ='$groupID' and 
	`status`='showing' and sender > 0 order by `feed_id` desc   limit ".($startIndex-1).", 10 ";
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	// echo mysql_num_rows($result);
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['sender'] = $row['sender'];
		    $dataItem['user']=getUserDetails($dataItem['sender'],$userID);
			$dataItem['feed_id'] = $row['feed_id'];
			$dataItem['group_id'] = $row['group_id'];
			$dataItem['title'] = $row['title'];
			$dataItem['description']= $row['description'];
			$dataItem['status']= $row['status'];
			$dataItem['attachment']= $row['attachment'];
			$dataItem['isAdmin']= $row['isAdmin'];
			$dataItem['date_sent']=ago($row['date_sent'], -1);
			$dataItem['userliike']=checkIfUserLikesGroupFeed($dataItem['feed_id'],$userID);
			$dataItem['likes']= groupFeedLike($dataItem['feed_id']);
			$dataItem['comments']= groupFeedCommentCount($dataItem['feed_id']);
		    array_push($data,$dataItem);
		}
		return $data;
	}
}
function getFeedGroupID($feed)
{
	$query="SELECT group_id from `church_group_feed` where `feed_id` ='$feed'";
	$result = mysql_query($query) or die(mysql_error());
	return mysql_result($result, 0);
}
function fetchChurchGroupFeed($feedID,$sss)
{
	
	/*
	 `feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`sender` bigint(20) NOT NULL,
	`group_id` bigint(20) NOT NULL,
	`title` text NOT NULL,
	`description` text NOT NULL,
	`status` enum('hide','showing') NOT NULL DEFAULT 'hide',
	`date_sent` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`isAdmin` int(2) NOT NULL DEFAULT '0'
	attachment
	*/
	$query="SELECT * from `church_group_feed` where `feed_id` ='$feedID'";
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	// echo mysql_num_rows($result);
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['sender'] = $row['sender'];
			//echo $userID;
			$dataItem['user']=getUserDetails($dataItem['sender'],$sss);
			$dataItem['feed_id'] = $row['feed_id'];
			$dataItem['group_id'] = $row['group_id'];
			$dataItem['title'] = $row['title'];
			$dataItem['description']= $row['description'];
			$dataItem['status']= $row['status'];
			$dataItem['attachment']= $row['attachment'];
			$dataItem['isAdmin']= $row['isAdmin'];
			$dataItem['date_sent']=ago($row['date_sent'], -1);
			$dataItem['userliike']=checkIfUserLikesGroupFeed($dataItem['feed_id'],$sss);
			$dataItem['likes']= groupFeedLike($dataItem['feed_id']);
			$dataItem['comments']= groupFeedCommentCount($dataItem['feed_id']);
			array_push($data,$dataItem);
		}
		return $data;
	}
}

function fetchChurchGroupFeedComments($feedID,$sss,$startIndex=1)
{
//echo $sss;
	//comment_id ,group_post_id ,member_id ,comment ,date_sent
	$query="SELECT comment_id ,group_post_id ,member_id ,comment ,date_sent from 
	`group_feed_comments` where group_post_id ='$feedID' 
	and member_id  > 0 order by comment_id asc   limit ".($startIndex-1).", 10
	";
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	// echo mysql_num_rows($result);
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['member_id'] = $row['member_id'];
			//echo $userID;
			$dataItem['user']=getUserDetails($dataItem['member_id'],$sss);
			$dataItem['comment_id'] = $row['comment_id'];
			$dataItem['date_sent'] = 
			ago($row['date_sent'], -1);
			;
			$dataItem['comments'] = $row['comment'];
			$dataItem['group_post_id']= $row['group_post_id'];
			/*$dataItem['description']= $row['description'];
			$dataItem['status']= $row['status'];
			$dataItem['attachment']= $row['attachment'];
			$dataItem['isAdmin']= $row['isAdmin'];
			$dataItem['date_sent']=ago($row['date_sent'], -1);
			$dataItem['userliike']=checkIfUserLikesGroupFeed($dataItem['feed_id'],$sss);
			$dataItem['likes']= groupFeedLike($dataItem['feed_id']);
			$dataItem['comments']= groupFeedCommentCount($dataItem['feed_id']);*/
			array_push($data,$dataItem);
		}
		return $data;
	}
}



function groupFeedLike($devotionID)
{
	//like_id ,group_post_id ,member_id ,date_sent
	$query="SELECT count(`member_id`) FROM  `group_feed_likes` where  `group_post_id`='$devotionID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}

function groupFeedCommentCount($devotionID)
{
	//comment_id ,group_post_id ,member_id ,comment ,date_sent
	$query="SELECT count(`member_id`) FROM  `group_feed_comments` where  `group_post_id`='$devotionID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}

function saveFeedComment($feedID,$memberid,$comment)
{
	 $query="INSERT INTO `group_feed_comments`(group_post_id ,member_id ,comment )
			values('$feedID','$memberid','$comment');
			";
	$results= mysql_query($query) or die(mysql_error());
	if($results)
	{
		return "Save";
	}
	else {
		return "Error";
	}
}
function deleteComment($commentid)
{
	$query="DELETE FROM `group_feed_comments`where comment_id ='$commentid'
	";
	$results= mysql_query($query) or die(mysql_error());
	if($results)
	{
	return "Deleted";
	}
	else {
	return "Error";
	}
}

function checkIfUserLikesGroupFeed($devotionID,$mid)
{
	//$memberID
	//echo $mid;
	$query="SELECT count(*) FROM `group_feed_likes` where
	`group_post_id`='$devotionID' and `member_id`='$mid'";
 
	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}







function determineView($fileName)
{



}
function postToGroup($userid,$groupid,$title,$description,$attachment="")
{
	/*
	 `feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
	`sender` bigint(20) NOT NULL,
	`group_id` bigint(20) NOT NULL,
	`title` text NOT NULL,
	`description` text NOT NULL,
	`status` enum('hide','showing') NOT NULL DEFAULT 'hide',
	`date_sent` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`isAdmin` int(2) NOT NULL DEFAULT '0'
	attachment
	*/
  $query="INSERT INTO `church_group_feed`(`sender`,`title`,`group_id`,`description`,`attachment`,`status`)  
  		values('$userid','$title','$groupid','$description','$attachment','showing')
  		";
  $result = mysql_query($query) or die(mysql_error());
  if($result)
  {
  	return "Saved";
  }
  else {
  	return "Error";
  }

}
///
function fetchPrayerRequests($myID,$startIndex=1)
{
	$query="SELECT `church_member_prayer_request_id`, `church_id`, `branch_id`,
	`member_id`, `prayer_request`, `sent_at`, `status` from  `church_member_prayer_requests` where `member_id`='$myID'
	order by `church_member_prayer_request_id` desc   limit ".($startIndex-1).", 10";
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_member_prayer_request_id'];
			$dataItem['request']=$row['prayer_request'];
			$dataItem['date']=ago($row['sent_at'], -1);
			$dataItem['status']= $row['status'];
			$dataItem['church_id']=$row['church_id'];
			$dataItem['churchName']=getchurchName($row['church_id']);
			$data[]= $dataItem;
		}
		return $data;
	}
	else{
			
		return "No PrayerRequest";
	}
}

function getPrayerRequestResponse($ID,$CID,$startIndex)
{
	/*
	 CREATE TABLE IF NOT EXISTS `church_member_prayer_responses` (
	 		`church_member_prayer_response_id` bigint(20) NOT NULL AUTO_INCREMENT,
	 		`request_id` bigint(20) DEFAULT NULL,
	 		`response` text,
	 		`responded_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 		`responded_by` varchar(100) DEFAULT NULL,
	 		PRIMARY KEY (`church_member_prayer_response_id`)
	 ) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;
	*/

	$query="SELECT `church_member_prayer_response_id`,
	`request_id`,`response`, `responded_at`,`responded_by`
	from `church_member_prayer_responses` where `request_id`='$ID'
	order by  `church_member_prayer_response_id` desc   limit ".($startIndex-1).", 10";
		
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_member_prayer_response_id'];
			$dataItem['request_id']=$row['request_id'];
			$dataItem['date']=ago($row['responded_at'], -1);
			$dataItem['responded_by']= $row['responded_by'];
			$dataItem['response']=$row['response'];
			$dataItem['churchName']=getchurchName($CID);
			$data[]= $dataItem;
		}
		return $data;
	}
	else{
			
		return "No PrayerRequest";
	}

}
function deletePrayerRequest($ID)
{
	$query="DELETE from `church_member_prayer_requests` where `church_member_prayer_request_id` ='$ID';";
	$result = mysql_query($query) or die(mysql_error());
		
	if($result)
	{
		return 'Deleted';
	}
	else{
		return 'Error';
			
	}
}

function loadBranchName($ID)
{

}
function getBranchActivities($churchID,$branch="0",$startIndex="1")
{
	//"church_branch_activity_id" "church_id" "branch_id"
	//"day" "activity" "time" "venue" "description"
	//"created_at" "created_by" "status"
	$query="SELECT day,activity,time,venue,description,church_id,
	branch_id,church_branch_activity_id,church_id from
	church_branch_activities where branch_id='$branch' ";
		
	if(!empty($branch) && $branch>0)
	{
		$query.=" and branch_id='$branch'";
	}


		
	$result = mysql_query($query) or die(mysql_error());
	$dataItem=array();
	$data=array();
	if($result && mysql_num_rows($result))
	{
		while($row = mysql_fetch_array($result))
		{
			$dataItem['id']= $row['church_branch_activity_id'];
			$dataItem['day']=ucwords(trim($row['day']));
				
			$dataItem['description']=$row['description'];
			$dataItem['activity']= ucwords(trim($row['activity']));
			$dataItem['venue']=ucwords(trim($row['venue']));
			$dataItem['time']=$row['time'];
			$dataItem['churchName']=getchurchName($row['church_id']);
			$dataItem['Logo']= getchurchlogo($row['church_id']);
			$data[]= $dataItem;
		}
		return $data;
	}
	else{
			
		return "No PrayerRequest";
	}

}
function cancelConnection($myID,$otherUserID)
{
	$query="DELETE from `connections`  where  (`recipient_id`='$otherUserID'  and `sender_id`='$myID') or
	(`recipient_id`='$myID' and `sender_id`='$otherUserID') ";
	$result = mysql_query($query) or die(mysql_error());
	if($result)
	{
		echo $res= isConnected($myID,$otherUserID);
		if($res==-2)
		{
			return 'Request Cancelled';
		}
		else{
				
			return 'Error';
		}
			
	}

}

function cancelRequest($myID,$otherUserID)
{
	$query="DELETE from `connections`  where  (`recipient_id`='$otherUserID'  and `sender_id`='$myID') or
	(`recipient_id`='$myID'  and `sender_id`='$otherUserID') ";
	$result = mysql_query($query) or die(mysql_error());
	if($result)
	{
		//echo $res= isConnected($otherUserID,$myID);
		return 'Request Cancelled';
			
			
	}else{
		return 'Error';
	}
		


}

function sendRequest($myID,$otherUserID)
{
	/*
	 request_id - {bigint(20)} sender_id - {bigint(20)} recipient_id - {bigint(20)} sender_status - {enum('yes','no')}
	recipient_status - {enum('yes','no')} date_sent - {timestamp} date_received - {datetime} ssifeed - {enum('yes','no')}
	srifeed - {enum('yes','no')} sender_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}
	receipient_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}

	*/

	$query="INSERT into `connections`
	(`sender_id`,`recipient_id`,`sender_status`,`recipient_status`)
	values ('$myID','$otherUserID','no','no')";
	$result = mysql_query($query) or die(mysql_error());
	if($result)
	{
		$connected= isConnected($otherUserID,$myID);
		if($connected==2)
		{
			return 'Request Sent';
		}
		else{
				
			return 'Error';
		}
			
	}
	else{
			
		return 'Error';
	}

}

function acceptRequest($myID,$otherUserID)
{
	/*
	 request_id - {bigint(20)} sender_id - {bigint(20)} recipient_id - {bigint(20)} sender_status - {enum('yes','no')}
	recipient_status - {enum('yes','no')} date_sent - {timestamp} date_received - {datetime} ssifeed - {enum('yes','no')}
	srifeed - {enum('yes','no')} sender_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}
	receipient_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}

	*/

	$query="UPDATE `connections`
	set `recipient_status`='yes',`sender_status`='yes'
	where (`recipient_id`='$otherUserID' and `recipient_status`='no' and `sender_id`='$myID') or
	(`recipient_id`='$myID' and `recipient_status`='no' and `sender_id`='$otherUserID')";
	$result = mysql_query($query) or die(mysql_error());
	if($result)
	{
	 $connected= isConnected($otherUserID,$myID);
		if($connected==1)
		{
			return 'Friend Accepted';
		}
		else{
				
			return 'Error';
		}
			
	}
	else{
			
		return 'Error';
	}

}

function loadFriendsCount($user_id) {
	$total = 0;
	$query = "select count(`sender_id`) as total_sender from `connections` where `recipient_id`='$user_id' and `recipient_status`='yes'";
	$result = mysql_query($query) or die(mysql_error());
	$row = mysql_fetch_array($result);


	$total += $row['total_sender'];


	$query = "select count(`recipient_id`) as total_recipient from `connections` where `sender_id`='$user_id' and `recipient_status`='yes'";
	$result = mysql_query($query) or die(mysql_error());
	$row = mysql_fetch_array($result);

	$total += $row['total_recipient'];


	return $total;
}
function loadFriends2($user_id) {
	$allmyfriends = array();
	$i = 0;
	$query = "select `sender_id` from `connections` where `recipient_id`='$user_id' and `recipient_status`='yes' ";
	$result = mysql_query($query) or die(mysql_error());
	$num = mysql_affected_rows();
	if ($num > 0) {

		while ($row = mysql_fetch_array($result)) {
			array_push($allmyfriends, $row['sender_id']);
			$i++;
		}
	}




	$query = "select `recipient_id` from `connections` where `sender_id`='$user_id' and `recipient_status`='yes' ";
	$result = mysql_query($query) or die(mysql_error());
	$num = mysql_affected_rows();
	if ($num > 0) {

		while ($row = mysql_fetch_array($result)) {
			array_push($allmyfriends, $row['recipient_id']);
			$i++;
		}
	}

	return $allmyfriends;
}

function countMutualFriends($user_id,$myID)
{





	$hisfriends = loadFriends2($user_id);
	$myfriends = loadFriends2($myID);
	$countmuttual = sizeof(array_intersect($myfriends, $hisfriends));

	return $countmuttual;

}



function isConnected($userMain,$memberID)
{

	/*
	 request_id - {bigint(20)} sender_id - {bigint(20)} recipient_id - {bigint(20)} sender_status - {enum('yes','no')}
	recipient_status - {enum('yes','no')} date_sent - {timestamp} date_received - {datetime} ssifeed - {enum('yes','no')}
	srifeed - {enum('yes','no')} sender_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}
	receipient_relation - {enum('friend','family','brother','sister','close','acquintance','blocked')}

	*/

	/*
	 This function searches if two users are connected
	-10: The two Ids Provided are the same so we avoid check
	1: The users are Connected
	2: This if the other user is yet to accept request
	-1: If no request has been sent

	*/
	$query="SELECT count(*) from `connections` where (`recipient_id`='$userMain' and `recipient_status`='yes' and `sender_id`='$memberID')
	or (`sender_id`='$userMain' and `recipient_status`='yes'  and `recipient_id`='$memberID')  ";

	$results= mysql_query($query);
	$count=-1;
	if($results)
	{
		$count=mysql_result($results,0);
	}

	if($count<=0)
	{
		$query="select count(`sender_id`) from `connections` where
		`sender_id`='$memberID' and `recipient_status`='no' and `recipient_id`='$userMain'  ";
		$results= mysql_query($query);
		$count=0;
		if($results)
		{
			$count=mysql_result($results,0);
		}

		if($count<=0)
		{
			///Check also if the request has been the other way round instead

			$query="select count(`sender_id`) from `connections` where
			`recipient_id`='$memberID' and `recipient_status`='no' and `sender_id`='$userMain'  ";
			$results= mysql_query($query) or die(mysql_error());
			$count=0;
			if($results)
			{
				$count=mysql_result($results,0);
			}
			if($count>0)
			{
				$count=-2; // This if the main user is yet to accept request
			}
			else
				$count=-1;  // If no request has been sent
		}
		else
			$count=2; // This if the other user is yet to accept request


		return $count;
	}
	else
		return $count;

}



function loadFriends($user_id,$myID=0,$startIndex=0) {
	$allmyfriends = array();
	$i = 0;
	$query = "select `sender_id`,`recipient_id` from `connections` where (`recipient_id`='$user_id' and `recipient_status`='yes'
	) or (`sender_id`='$user_id' and `recipient_status`='yes' )
	order by request_id desc   limit ".($startIndex-1).", 10";
	$result = mysql_query($query) or die(mysql_error());
	$num = mysql_affected_rows();
	if ($num > 0) {

		while ($row = mysql_fetch_array($result)) {
			$sender= $row['sender_id'];
			$recipient_id= $row['recipient_id'];
			if($sender!=$user_id)
				array_push($allmyfriends, getUserDetails($sender,$myID));

			else if($recipient_id!=$user_id)
			{
				array_push($allmyfriends, getUserDetails($recipient_id,$myID));
			}
			$i++;
		}
	}


	if(count($allmyfriends)>0)

		return $allmyfriends;
	else
		return 'No Friend Found';
	//return $allmyfriends;
}



function loadChurchMembers($churchID,$myID=0,$startIndex=0) {
	$allmyfriends = array();
	$query="SELECT distinct CONCAT(`first_name`,' ',`last_name`) as 'N'
	,church_member_id,`church_id`,`userdp` as 'DP',`email` as 'email',
	`phone_number` as 'phone',date_joined,about


	from
	church_members where church_id='$churchID'  order by church_member_id  limit ".($startIndex-1).", 20";
	$results= mysql_query($query) or die(mysql_error());
	$mutualFriend=0;
	if($results && mysql_num_rows($results)>=1)
	{
		$data=array();
		while($row= mysql_fetch_assoc($results))
		{
			$dataItem['id']=$row['church_member_id'];
			$dataItem['memberID']=$dataItem['id'];
			$dataItem['cover']=loadUserCoverPix($dataItem['id']);
			$dataItem['name']=ucwords(trim($row['N']));
			$dataItem['profilePic']=$row['DP'];
			$dataItem['email']=$row['email'];
			$dataItem['phone']=$row['phone'];
			$dataItem['about-user']=$row['about'];
			$dataItem['date_joined']=ago($row['date_joined'], -1);
			$cid= $row['church_id'];
			$dataItem['churchName']=getchurchName($cid);
			$dataItem['friends']= loadFriendsCount($dataItem['id']);
			$dataItem['CID']= $cid;
			if($dataItem['id']!=$myID)
			{
				$dataItem['isConnected']= isConnected($dataItem['id'],$myID );
				if($dataItem['isConnected']==1)
				{
					$mutualFriend= $mutualFriend+1;
				}
				$dataItem['mutualFriends']= countMutualFriends($dataItem['id'],$myID);

			}
			else
			{
				$dataItem['isConnected']= -10; // This ID is mine so dont check Connection
				$dataItem['mutualFriends']= 0;

			}
				
			$dataItem['churchprofilePic']= getchurchlogo($cid);
			//$dataItem['Friends']= loadFriendsCount($userID);;
			// $data['USERS']= $dataItem;
			array_push($allmyfriends, $dataItem);
		}
		// echo json_encode($data);
		if(count($allmyfriends)>0)

			return $allmyfriends;
		else
			return 'No Friend Found';
		// return $data;

	}

}



function countRequests($myID)
{
	$query = "select count(`sender_id`) from `connections` where `recipient_id`='$myID' and `recipient_status`='no' order by request_id ";
	$result = mysql_query($query) or die(mysql_error());
	$count=0;
	if($result)
	{
		$count=mysql_result($result,0);
	}
	return $count;
	 
}
function loadFriendRequests($user_id,$myID=0,$startIndex) {
	// echo $user_id;
	$allmyfriends = array();
	$i = 0;
	$query = "select `sender_id` from `connections` where `recipient_id`='$user_id' and `recipient_status`='no' order by request_id  limit ".($startIndex-1).", 10 ";
	$result = mysql_query($query) or die(mysql_error());
	$num = mysql_affected_rows();
	if ($num > 0) {

		while ($row = mysql_fetch_array($result)) {
			array_push($allmyfriends, getUserDetails($row['sender_id'],$user_id));
			$i++;
		}
	}

	if(count($allmyfriends)>0)

		return $allmyfriends;
	else
		return 'No request';
}

function findUser($name,$myID,$myChurch,$startIndex)
{
	$allmyfriends = array();
	$query="SELECT distinct CONCAT(`first_name`,' ',`last_name`) as 'N'
	,church_member_id,`church_id`,`userdp` as 'DP',`email` as 'email',
	`phone_number` as 'phone',date_joined,about


	from
	church_members where church_id='$myChurch' and  (`last_name` like '%$name%' or `first_name` like '%$name%' )
	and church_member_id!='$myID'  order by church_member_id  limit ".($startIndex-1).", 20";
	$results= mysql_query($query) or die(mysql_error());
	$mutualFriend=0;
	if($results && mysql_num_rows($results)>=1)
	{
		$data=array();
		while($row= mysql_fetch_assoc($results))
		{
			$dataItem['id']=$row['church_member_id'];
			$dataItem['memberID']=$dataItem['id'];
			$dataItem['cover']=loadUserCoverPix($dataItem['id']);
			$dataItem['name']=ucwords(trim($row['N']));
			$dataItem['profilePic']=$row['DP'];
			$dataItem['email']=$row['email'];
			$dataItem['about-user']=$row['about'];
			$dataItem['phone']=$row['phone'];
			$dataItem['date_joined']=ago($row['date_joined'], -1);
			$cid= $row['church_id'];
			$dataItem['churchName']=getchurchName($cid);
			$dataItem['friends']= loadFriendsCount($dataItem['id']);
			$dataItem['CID']= $cid;
			if(  $dataItem['id']!=$myID)
			{
				$dataItem['isConnected']= isConnected($dataItem['id'],$myID );
				if($dataItem['isConnected']==1)
				{
					$mutualFriend= $mutualFriend+1;
				}
				$dataItem['mutualFriends']= countMutualFriends($dataItem['id'],$myID);

			}
			else
			{
				$dataItem['isConnected']= -10; // This ID is mine so dont check Connection
				$dataItem['mutualFriends']= 0;

			}
				
			$dataItem['churchprofilePic']= getchurchlogo($cid);
			//$dataItem['Friends']= loadFriendsCount($userID);;
			// $data['USERS']= $dataItem;
			array_push($allmyfriends, $dataItem);
		}
		// echo json_encode($data);
		if(count($allmyfriends)>0)

			return $allmyfriends;
		else
			return 'No Friend Found';
		// return $data;

	}


}

function getUserDetails($userID,$myID=0)
{
	//echo $userID.$myID;
	$query="SELECT CONCAT(`first_name`,' ',`last_name`) as 'N'
	,church_member_id,`church_id`,`userdp` as 'DP',`email` as 'email',
	`phone_number` as 'phone',date_joined,about


	from
	church_members where church_member_id='$userID'";
	$results= mysql_query($query) or die(mysql_error());
	$mutualFriend=0;
	if($results && mysql_num_rows($results)>=1)
	{
		$data=array();
		while($row= mysql_fetch_assoc($results))
		{
			$dataItem['id']=$row['church_member_id'];
			$dataItem['memberID']=$dataItem['id'];
			$dataItem['cover']=loadUserCoverPix($dataItem['id']);
			$dataItem['name']=ucwords(trim($row['N']));
			$dataItem['profilePic']=$row['DP'];
			$dataItem['email']=$row['email'];
			$dataItem['phone']=$row['phone'];
			$dataItem['about-user']=$row['about'];
			$dataItem['date_joined']=ago($row['date_joined'], -1);
			$cid= $row['church_id'];
			$dataItem['churchName']=getchurchName($cid);
			$dataItem['friends']= loadFriendsCount($userID);
			$dataItem['CID']= $cid;
			$data['connection']= loadFriendsCount($userID);
			if(  $userID!=$myID)
			{
				$dataItem['isConnected']= isConnected($dataItem['id'],$myID );
				if($dataItem['isConnected']==1)
				{
					$mutualFriend= $mutualFriend+1;
				}
				$dataItem['mutualFriends']= countMutualFriends($dataItem['id'],$myID);

			}
			else
			{
				$dataItem['isConnected']= -10; // This ID is mine so dont check Connection
				$dataItem['mutualFriends']= 0;

			}
				
			$dataItem['churchprofilePic']= getchurchlogo($cid);
			//$dataItem['Friends']= loadFriendsCount($userID);;
			$data= $dataItem;
		}
		// echo json_encode($data);
		return $data;

	}


}
function checkIfUserIsAttendingEvent($userID,$eventID)
{

	$query="SELECT count(`memberID`) FROM `member_eventAttending` where `memberID`='$userID'
	and `eventID`='$eventID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;


}
function getDevotionLikes($devotionID)
{
	//Fields: daily_devotional_lesson_like_id - {int(11)} daily_devotional_lesson_id - {bigint(20)} member_id - {bigint(20)} liked_at - {timestamp}
	$query="SELECT count(`member_id`) FROM daily_devotional_lesson_likes where  `daily_devotional_lesson_id`='$devotionID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}


function getDevotionComment($devotionID)
{
	//Fields: daily_devotional_lesson_like_id - {int(11)} daily_devotional_lesson_id - {bigint(20)} member_id - {bigint(20)} liked_at - {timestamp}
	$query="SELECT count(`member_id`) FROM  `daily_guide_lessons_comments` where  `daily_guide_lesson_id`='$devotionID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}
function checkIfUserLikesDevotion($devotionID,$mid)
{
	//$memberID
	$query="SELECT count(*) FROM daily_devotional_lesson_likes where  `daily_devotional_lesson_id`='$devotionID' and `member_id`='$mid'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}

function getThoseAttending($eventID)
{
	//$query="TRUNCATE `member_eventAttending` ";
	$query="SELECT count(*) FROM `member_eventAttending` where  `eventID`='$eventID'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}

function getchurchName($id)
{
	$query="SELECT church_name FROM `churches` where `status`='active' and  church_id='$id'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;

}
function getchurchlogo($id)
{

	//`church_logo`
	$query="SELECT `church_logo` FROM `churches` where `status`='active' and  church_id='$id'";

	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;
}


function ago($datefrom, $dateto = -1)
{
	if ($datefrom == 0) {
		return "Long Time Ago";

	}
	if ($dateto == -1) {
		$dateto = time();
	}
	$datefrom = strtotime($datefrom);
	$difference = $dateto - $datefrom;

	switch (true) {
		case (strtotime('-1 min', $dateto) < $datefrom):
			$datediff = $datedifference;
			$res = ($datediff == 1) ? $datediff . ' sec ago' : $datediff . ' sec ago';
			break;
		case (strtotime('-1 hour', $dateto) < $datefrom):
			$datediff = floor($difference / 60);
			$res = ($datediff == 1) ? $datediff . ' min ago' : $datediff . ' min ago';
			break;
		case (strtotime('-1 day', $dateto) < $datefrom):
			$datediff = floor($difference / 60 / 60);
			$res = ($datediff == 1) ? $datediff . ' hr ago' : $datediff . ' hrs ago';
			break;
		case (strtotime('-1 week', $dateto) < $datefrom):
			$day_difference = 1;
			while (strtotime('-' . $day_difference . 'day', $dateto) >= $datefrom) {
				$day_difference++;


			}
			$datediff = $day_difference;
			$res = ($datediff == 1) ? $datediff . ' yesterday' : $datediff . ' days ago';
			break;
		case (strtotime('-1 month', $dateto) < $datefrom):
			$week_difference = 1;
			while (strtotime('-' . $week_difference . 'week', $dateto) >= $datefrom) {
				$week_difference++;


			}
			$datediff = $week_difference;
			$res = ($datediff == 1) ? $datediff . ' last week' : $datediff . ' weeks ago';
			break;
		case (strtotime('-1 year', $dateto) < $datefrom):
			$month_difference = 1;
			while (strtotime('-' . $month_difference . 'month', $dateto) >= $datefrom) {
				$month_difference++;


			}
			$datediff = $month_difference;
			$res = ($datediff == 1) ? $datediff . ' month ago' : $datediff . ' months ago';
			break;
		case (strtotime('-1 year', $dateto) < $datefrom):
			$year_difference = 1;
			while (strtotime('-' . $year_difference . 'month', $dateto) >= $datefrom) {
				$year_difference++;


			}
			$datediff = $year_difference;
			$res = ($datediff == 1) ? $datediff . ' year ago' : $datediff . ' years ago';
			break;
	}
	return $res;
}

function loadUserCoverPix($userID)
{
	$query="SELECT photo from  church_member_cover_photos where status='active' and member_id='$userID'  limit 1";
	$results= mysql_query($query);
	$count=0;
	if($results && mysql_num_rows($results))
	{
		$count=mysql_result($results,0);
		return $count;
	}
	else{
		return "NOT_SET";
	}

}



function getchurch($ID)
{
	$query="SELECT `church_id` from `daily_guide`,`daily_guide_lessons`
	where `daily_guide_lesson_id`='$ID' and
	daily_guide.`daily_guide_id`=`daily_guide_lessons`.`daily_guide_id`";
	$results= mysql_query($query);
	$count=0;
	if($results)
	{
		$count=mysql_result($results,0);
	}
	return $count;

}


function getDevotionInformation($ID)
{
	$query="SELECT
	daily_guide.`daily_guide_id` as 'DID',
	`daily_guide_lesson_id` as 'LID',`topic` as 'T',SUBSTRING(`message`,1,60) as 'M',
	`message` as 'Msg',`day` as 'D',`month` as 'MN',`reading_plan` as 'RP'
	,`verse` as 'V',`date` as 'rawDate',`prayer` as 'P', DATE_FORMAT(`date`,'%a') as 'DW', DATE_FORMAT(`date`,'%b %d') as 'DD'
	from `daily_guide`,`daily_guide_lessons`
	where `daily_guide_lesson_id`='$ID'
	and daily_guide.`daily_guide_id`=`daily_guide_lessons`.`daily_guide_id`
	order by `date` DESC limit 1";
		
	$results= mysql_query($query) or die(mysql_error());
	if($results && mysql_num_rows($results)>=1)
	{
		$data=array();
		while($row= mysql_fetch_assoc($results))
		{
			$dataItem['rawDate']=$row['rawDate'];
			$dataItem['title']=trim($row['T']);
			$dataItem['day_name']=$row['DW'];
			$dataItem['loadeddate']=$row['DD'];
			$dataItem['LID']=$row['LID'];
			$data['Devotion'][]= $dataItem;
		}

		return json_encode($data);

	}


}


function getReceiversDeviceIDs($churchID,$ignore="-1")
{
	$q="SELECT `device_id` from  `church_members` where `church_id`='$churchID' and `device_id`!='' and `church_member_id`!= '$ignore' "."
			";
	//`church_member_id`='4'
	$res= mysql_query($q) or die(mysql_error());
	$deviceIDs = array();
	if($res && mysql_num_rows($res)>0)
	{
		//$val= array();
		$i=0;
		while($row= mysql_fetch_array($res,3))
		{
			$deviceIDs[$i]= $row['device_id'];
			$i=$i+1;
		}
			
	}
		
	return  $deviceIDs;

}


function getGroupUsersDevices($groupID,$ignore="-1")
{
	//`church_branch_group_request`  `member_id` `group_id`
	/*
	 * $q="SELECT `device_id` from  `church_members` where `church_member_id`='$userID' and `device_id`!='' and `church_member_id`!= '$ignore' "."
			";
	//`church_member_id`='4'
	$res= mysql_query($q) or die(mysql_error());
	$deviceIDs = array();
	if($res && mysql_num_rows($res)>0)
	{
		//$val= array();
		$i=0;
		while($row= mysql_fetch_array($res,3))
		{
			$deviceIDs[$i]= $row['device_id'];
			$i=$i+1;
		}
			
	}
		
	return  $deviceIDs;
	 */
	$q="SELECT `device_id` from  
	`church_members`,`church_branch_group_request` where 
	`church_branch_group_request`.`member_id`=`church_member_id`
	  and `device_id`!='' and `member_id`!='' and `church_member_id`!= '$ignore' and `group_id`='$groupID'"."
			";
	//`church_member_id`='4'
	$res= mysql_query($q) or die(mysql_error());
	$deviceIDs = array();
	if($res && mysql_num_rows($res)>0)
	{
		//$val= array();
		$i=0;
		while($row= mysql_fetch_array($res,3))
		{
			$deviceIDs[$i]= $row['device_id'];
			$i=$i+1;
		}
			
	}

	return  $deviceIDs;

}

function getUserDeviceIDs($userID,$ignore="-1")
{
	$q="SELECT `device_id` from  `church_members` where `church_member_id`='$userID' and `device_id`!='' and `church_member_id`!= '$ignore' "."
			";
	//`church_member_id`='4'
	$res= mysql_query($q) or die(mysql_error());
	$deviceIDs = array();
	if($res && mysql_num_rows($res)>0)
	{
		//$val= array();
		$i=0;
		while($row= mysql_fetch_array($res,3))
		{
			$deviceIDs[$i]= $row['device_id'];
			$i=$i+1;
		}
			
	}
		
	return  $deviceIDs;

}

function getDevotionDetails($ID)
{
	$query="SELECT
	daily_guide.`daily_guide_id` as 'DID',
	`daily_guide_lesson_id` as 'LID',`topic` as 'T',SUBSTRING(`message`,1,60) as 'M',
	`message` as 'Msg',`day` as 'D',`month` as 'MN',`reading_plan` as 'RP'
	,`verse` as 'V',`date` as 'rawDate',`prayer` as 'P', DATE_FORMAT(`date`,'%a') as 'DW', DATE_FORMAT(`date`,'%b %d') as 'DD'
	from `daily_guide`,`daily_guide_lessons`
	where `daily_guide_lesson_id`='$ID'
	and daily_guide.`daily_guide_id`=`daily_guide_lessons`.`daily_guide_id`
	order by `date` DESC limit 1";
		
	$results= mysql_query($query) or die(mysql_error());
	if($results && mysql_num_rows($results)>=1)
	{
		$data=array();
		while($row= mysql_fetch_assoc($results))
		{
			$dataItem['rawDate']=$row['rawDate'];
			$dataItem['title']=ucwords(trim($row['T']));
			$dataItem['day_name']=$row['DW'];
			$dataItem['loadeddate']=$row['DD'];
			$dataItem['LID']=$row['LID'];
			$data= $dataItem;
		}
		// echo json_encode($data);
		return json_encode($data);

	}


}

function  logAction($Message,$fileName)
{
	$file= fopen("errors.txt", "a") or die("Unable to open file!");
	if($file)
	{
		fwrite($file, $Message);
		fclose($file);
	}
}
?>