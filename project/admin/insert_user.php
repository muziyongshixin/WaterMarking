<?php
	require "../admin/mydbtool.class.php";
	$AdminDBManage = new MyDBTool();
	$conn = $AdminDBManage->init();
	if(!empty($_POST['user_phone'])&&!empty($_POST[''])&&!empty($_POST['user_password'])&&!empty($_POST['user_sex'])){
			$AdminDBManage->insert_user($conn,$_POST['user_phone'],$_POST['user_name'],$_POST['user_password'],$_POST['user_sex']);
	}
	if($_POST['user_phone']==""){
		$arr['statue']="1";
		echo json_encode($arr);
	}
	if($_POST['user_name']==""){
		$arr['statue']="2";
		echo json_encode($arr);
	}
	if($_POST['user_password']==""){
		$arr['statue']="3";
		echo json_encode($arr);
	}
	if($_POST['user_sex']==""){
		$arr['statue']="4";
		echo json_encode($arr);
	}
	 $conn->close();
?>
