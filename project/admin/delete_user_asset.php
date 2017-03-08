<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  $AdminDBManage->delete_user_asset($conn,$_POST['asset_id']);
  $conn->close();
 ?>
