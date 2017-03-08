<?php
  require "../admin/mydbtool.class.php";
  $AdminDBManage = new MyDBTool();
  $conn = $AdminDBManage->init();
  if(!empty($_POST['user_phone'])&&!empty($_POST['asset_name'])&&!empty($_POST['asset_money'])){
      $AdminDBManage->insert_user($conn,$_POST['user_phone'],$_POST['asset_name'],$_POST['asset_money']);
  }
  if($_POST['user_phone']==""){
    $arr['statue']="1";
    echo json_encode($arr);
  }
  if($_POST['asset_name']==""){
    $arr['statue']="2";
    echo json_encode($arr);
  }
  if($_POST['asset_money']==""){
    $arr['statue']="3";
    echo json_encode($arr);
  }
   $conn->close();
?>
