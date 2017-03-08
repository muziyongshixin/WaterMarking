<?php
  echo"<form action='user_insert.php' method='POST'><br/>";
  echo"<table>
    <br/>
    <tr><td>
      <font  color='rgb(10,100,80)' >用户名</font></td><td><input type='text' name='user_name'>
    </td></tr>
    <br/>
    <tr><td>
      <font  color='rgb(10,100,80)' >电话号码</td><td><input type='password' name='user_phone'>
    </td></tr>
    <br/>
    <tr><td>
      <font  color='rgb(10,100,80)' >身份证号</td><td><input type='password' name='user_ID'>
    </td></tr>
    <br/>
    <tr><td>
      <font  color='rgb(10,100,80)' >密&nbsp;码</td><td><input type='password' name='user_password'>
    </td></tr>
    <br/>
    <td>
      <input type='submit' value='注册'>
    </td>
    <br/>
    <td>
      <input name='reset' type='Reset' value='重新填写'>
    </td>
  <br/></table>";
?>
