<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  $AdminDBManage->delete_user($conn,$_POST['user_phone']);
  $conn->close();
?>
