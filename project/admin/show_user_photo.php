<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  $AdminDBManage->show_user_photo($conn,$_POST['user_phone']);
  $conn->close();
?>
