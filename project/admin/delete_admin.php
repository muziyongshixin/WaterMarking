<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  $AdminDBManage->delete_admin($conn,$_POST['admin_id']);
  $conn->close();
?>
