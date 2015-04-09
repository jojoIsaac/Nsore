<?php 
header('Content-type: text/html; charset=utf-8'); 
include 'config.php';
include 'GMCManager.php';

  $query="SELECT  faq_group_id,faq_group,icon,status from faq_group;";
  $res= mysql_query($query) or die(mysql_error());

 
   if($res && mysql_num_rows($res))
	 {
	    $data = array();
	    while($row = mysql_fetch_assoc($res))
		{
		   $id= $row['faq_group_id'];
		   
		   $querys="SELECT * from faqs where faq_group_id='$id'";
		   $ts=  mysql_query($querys) or die(mysql_error());
		   $sub_content = array();
		   while($subrow = mysql_fetch_assoc($ts))
			{
				array_push($sub_content,$subrow);
			}
			$row['sub_content'] = $sub_content;

             array_push($data,$row);
		}
	

 echo json_encode($data);
		  
	 
	 }
	 else{
	 echo 'NOT_SET';
	 }
?>