<?php
	class MyDBTool{
		public $host="127.0.0.1";
		public $user="jiaochong";
		public $password="123456";
		public $DB="sys_watermark";
		//数据库连接函数
		function Init(){
			$mysqli = new mysqli($this->host,$this->user,$this->password,$this->DB);
			if($mysqli->connect_error){
				die("连接失败".mysqli_errno());
				return false;
			}else{
				$mysqli->query("set names utf8");
				return $mysqli;
			}
		}


//管理员管理
		//管理员插入函数
		function insert_admin($conn,$admin_id,$admin_name,$admin_password,$admin_identity){
	      $sql =  "INSERT INTO `admin`(`admin_id`, `admin_name`, `admin_password`, `admin_identity`) VALUES ('$admin_id','$admin_name','".md5('$admin_password')."','$admin_identity')";
	      if($conn->query($sql)){
					$arr['statue']="0";
					echo json_encode($arr);
				}else{
					$arr['statue']='1';
					echo json_encode($arr);
	      }
	 	}
		//显示所有管理员信息
		function show_admin($conn){
			$sql="SELECT admin_id,admin_name,admin_identity FROM admin where 1";
			$res=$conn->query($sql);
			if($res){
				while($row =$res->fetch_assoc()){
					echo json_encode($row);
				}
			}
		}
		//删除管理员信息
		function delete_admin($conn,$admin_id){
				$sql="DELETE FROM `admin` WHERE admin_id='$admin_id'";
				if($conn->qurey($sql)){
					//删除成功
					$arry['statue']="0";
					echo json_encode($arry);
				}else{
					//删除失败
					$arry['statue']="1";
					echo json_encode($arry);
 				}
		}
//用户信息管理
		//查看所有用户信息
		function show_user($conn){
			$sql="select * from user";
			$res=$conn->query($sql);
			if($res){
				while($row =$res->fetch_assoc()){
						echo json_encode($row);
				}
			}
		}
		//添加用户
		function insert_user($conn,$user_phone,$user_name,$user_password,$user_sex){
			$sql =  "INSERT INTO `user`(`user_phone`, `user_name`, `user_password`, `user_sex`) VALUES ('$user_phone','$user_name','".md5('$user_password')."','$user_sex')";
			if($conn->query($sql)){
				$arr['statue']="0";
				echo json_encode($arr);
			}else{
				$arr['statue']='1';
				echo json_encode($arr);
			}
		}
		//删除用户
		function delete_user($conn,$user_phone){
			$sql="DELETE FROM `user` WHERE user_phone='$user_phone'";
			if($conn->qurey($sql)){
				//删除成功
				$arry['statue']="0";
				echo json_encode($arry);
			}else{
				//删除失败
				$arry['statue']="1";
				echo json_encode($arry);
			}
		}


//用户图库管理
		//显示所有用户所有图片
		function show_user_photo($conn,$user_phone){
				$sql="SELECT FROM `photo` WHERE user_phone='$user_phone'";$res=$conn->query($sql);
				if($res){
					while($row =$res->fetch_assoc()){
							echo json_encode($row);
					}
				}
		}


//用户资产管理
		//资产查看
		function show_user_asset($conn,$user_phone){
			$sql="SELECT FROM `asset` WHERE user_phone='$user_phone'";
			$res=$conn->query($sql);
			if($res){
				while($row =$res->fetch_assoc()){
						echo json_encode($row);
				}
			}
		}

		//资产增加
		function insert_user_asset($conn,$user_phone,$asset_name,$asset_money,$){
			..............................序号和时间结合生成
				$asset_id='time';
				$sql =  "INSERT INTO `asset`(`user_phone`, `asset_name`, `asset_money`) VALUES ('$user_phone',$asset_id',$asset_name','$asset_money')";
				if($conn->query($sql)){
					$arr['statue']="0";
					echo json_encode($arr);
				}else{
					$arr['statue']='1';
					echo json_encode($arr);
				}
		}
		//资产
		function delete_user_asset($conn,$assset_id){
			$sql="DELETE FROM `asset` WHERE asset_id='$asset_id'";
			if($conn->qurey($sql)){
				//删除成功
				$arry['statue']="0";
				echo json_encode($arry);
			}else{
				//删除失败
				$arry['statue']="1";
				echo json_encode($arry);
			}
		}
	}
?>
