//显示
function register_inseruser(){
  var xmlhttp;
  if(window.XMLHttpRequest){
    //code for IE7+ firefox ,chrome,poera,safari
    xmlhttp = new XMLHttpRequest();
  }else{
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange=function(){
    if(xmlhttp.readyState==4 && xmlhttp.status==200) {
      document.getElementById('changeArea').innerHTML=xmlhttp.responseText;
    }
  }
  xmlhttp.open("GET","../php/inner_show_insert.php",true);
  xmlhttp.send();
}


//添加管理员
function show_insert_admin(){
  var xmlhttp;
  if(window.XMLHttpRequest){
    //code for IE7+ firefox ,chrome,poera,safari
    xmlhttp = new XMLHttpRequest();
  }else{
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange=function(){
    if(xmlhttp.readyState==4 && xmlhttp.status==200) {
      document.getElementById('admin_show').innerHTML=xmlhttp.responseText;
    }
  }
  xmlhttp.open("GET","../admin/show_insert_admin.php",true);
  xmlhttp.send();
}
//删除管理员
function showdmin(){
  var xmlhttp;
  if(window.XMLHttpRequest){
    //code for IE7+ firefox ,chrome,poera,safari
    xmlhttp = new XMLHttpRequest();
  }else{
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange=function(){
    if(xmlhttp.readyState==4 && xmlhttp.status==200) {
      document.getElementById('admin_show').innerHTML=xmlhttp.responseText;
    }
  }
  xmlhttp.open("GET","../admin/show_admin.php?showadmin=1",true);
  xmlhttp.send();
}
function deleteadmin(){

}
//显示用户
function showuser(){
  var xmlhttp;
  if(window.XMLHttpRequest){
    //code for IE7+ firefox ,chrome,poera,safari
    xmlhttp = new XMLHttpRequest();
  }else{
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange=function(){
    if(xmlhttp.readyState==4 && xmlhttp.status==200) {
      document.getElementById('admin_show').innerHTML=xmlhttp.responseText;
    }
  }
  xmlhttp.open("GET","../admin/show_user.php",true);
  xmlhttp.send();
}
//添加用户
function insertuser(){

}
//删除用户
function deleteuser(){

}
