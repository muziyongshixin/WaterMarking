<html>
	<head>
		<meta http-equiv="content-type" content="text/html;charset = utf-8">
		<script src ="../js/register_show_insert.js" type="text/javascript"></script>
	</head>
	<body onload=showinf()>
		<p>最高权限的管理员登录界面，可控制整个数据库中所有表的操作。</p>
		<input type="button" value="添加管理员" onclick=show_insert_admin()></input>
		<input type="button" value="显示所有管理员" onclick=admin_showdmin()></input>
		<input type="button" value="查看用户" 	onclick=admin_showuser()></input>
		<input type="button" value="添加用户" 	onclick=admin_insertuser()></input>
		<div id="admin_inf">
		</div>
		<div id="admin_show">
		</div>
	</body>
</html>
