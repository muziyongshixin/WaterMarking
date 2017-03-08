<?php
	require "../admin/mydbtool.class.php";
	$AdminDBManage = new MyDBTool();
	$conn = $AdminDBManage->init();
	if(!empty($_POST['admin_id'])&&!empty($_POST['admin_name'])&&!empty($_POST['admin_password'])&&!empty($_POST['admin_identity'])){
			$AdminDBManage->insert_admin($conn,$_POST['admin_id'],$_POST['admin_name'],$_POST['admin_password'],$_POST['admin_identity']);
	}
	if($_POST['user_phone']==""){
		$arr['statue']="1";
		echo json_encode($arr);
	}
	if($_POST['user_password']==""){
		$arr['statue']="2";
		echo json_encode($arr);
	}
	if($_POST['user_name']==""){
		$arr['statue']="3";
		echo json_encode($arr);
	}
	if($_POST['user_sex']==""){
		$arr['statue']="4";
		echo json_encode($arr);
	}
	 $conn->close();
?>
