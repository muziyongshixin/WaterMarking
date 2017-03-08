<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  if(isset($_GET['showadmin'])){
      if($_GET['showadmin']==1){
        $AdminDBManage->showadmin($conn);
      }
  }
  //显示用户
  if(isset($_GET['showuser'])){
      if($_GET['showuser']==1){
        $AdminDBManage->showalluser($conn);
      }
  }
?>
