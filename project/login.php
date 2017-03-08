<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
</head>
<body>
	<center>
	<h1><font color="rgb(10,100,60)" size="10px" >数字水印信息系统后台管理</h1>
	<form action="loginProcess.php" method="POST">
		<table>
			<tr><td><font  color="rgb(10,100,80)" >管理员id</font></td><td><input type="text" name="id"></td></tr>
			<tr><td><font  color="rgb(10,100,80)" >密&nbsp;码</td><td><input type="password" name="password"></td></tr>
			<td><input type="submit" value="管理员登录"></td>
		    <td><input name="reset" type="Reset" value="重新填写"></td>
		</table>
	</form>
	</center>
</body>
</html>
<?php
if(isset($_GET['erro'])){
	if($_GET['erro'] ==1){
		echo "<script language=javascript>alert('登录失败');</script>";
	}
}
?>
