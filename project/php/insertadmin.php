<html>
  <head>
    <meta http-equiv="content-type"  content="text/html;charset=utf-8">
  </head>
  <p>注册管理员</P>
  <body>
    <form action='../admin/adminmange.php' method='post' >
      <table>
          <tr>
            <td>管理员id</td>
            <td><input type='text' name='admin_id'></td>
          </tr>
          <tr>
            <td>管理员姓名</td>
            <td><input type='text' name='admin_name'></td>
          </tr>
          <tr>
            <td>管理员密码</td>
            <td><input type='password' name='admin_password'></td>
          </tr>
          <tr>
            <td>请选择管理员身份</td>
            <td><input type='radio' name='admin_identity' value='root'>root </td>
            <td><input type='radio' name='admin_identity' value='regiser'>register</td>
            <td><input type='radio' name='admin_identity' value='accounter'>accounter</td>
          </tr>
          <td><input type="submit" value="管理员注册"></td>
    		    <td><input name="reset" type="Reset" value="重新填写"></td>
      </table>
    </form>
  </body>
</html>
