<?php
//显示用户
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  $AdminDBManage->showalluser($conn);
  $conn->close();
?>
