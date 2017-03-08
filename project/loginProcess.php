<html>
</head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
</html>
<?php
	require './admin/mydbtool.class.php';
	//post传递数据
	$id = $_POST['id'];
	$password = $_POST['password'];

	//数据库操作初始化
	$DBManage = new MyDBTool();
	$conn = $DBManage->init();
	if($conn==false){
		echo "连接失败";
	}else{
		echo "连接成功";
		session_start();

		//防止sql注入攻击,变化验证逻辑

		//方式 通过输入的id号来得到密码再来比对
		$sql = "SELECT `admin_name`, `admin_password`, `admin_identity` FROM `admin` WHERE admin_id='".$id."'";
		$res = $conn->query($sql);
		if($row = $res->fetch_row()){
			var_dump($row);
			if($row[1]==md5($password)){
				//保存登录人的信息到session中
				$_SESSION['admin_identity'] = $row[2];
				$_SESSION['admin_id'] = $id;
				$_SESSION['admin_name'] = $row[0];
	 			if($row[2] == "root"){
					header("location:admin\adminmain.php");
					exit();
				}if($row[2] == "rigester"){
					header("location:php/rigester.php");
					exit();
				}if($row[2] == "accounter"){
					header("location:Accounter.php");
					exit();
				}
			}
		}
			//不合法，跳转到管理界面
		header("location:login.php?erro=1");
		exit();
		//关闭资源
		$conn->free_result($res);
		$conn->close();
	}
?>
