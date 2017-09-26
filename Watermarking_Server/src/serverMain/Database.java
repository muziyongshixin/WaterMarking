/**
 * Created by 32706 on 2017/3/2.
 */
package  serverMain;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.imageio.ImageIO;
import java.util.List;

/**
 * Created by 32706 on 2016/11/14.
 */

/***********本类实现与数据库相关的操作**********************/


public class Database {

    private static DatabaseConnectionPool connpool=null;

    // 驱动程序名
    private static String driver = "com.mysql.jdbc.Driver";
    // URL指向要访问的数据库名world
    private static String url = "jdbc:mysql://127.0.0.1:3306/watermarking";
    // MySQL配置时的用户名
    private static String user = "root";
    // MySQL配置时的密码
    private static String password = "123456";

    private  static final  String absolutePath="./";//存储文件的绝对路径根目录


    public static void main(String arg[]) {

    }


    public static void test()
    {
        try {
            Class.forName(driver);
            // 连续数据库
            Connection conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            // statement用来执行SQL语句
            Statement statement = conn.createStatement();

            String is_user_in_db = "select * from user";
            ResultSet r = statement.executeQuery(is_user_in_db);
            while(r.next())
            {
                System.out.println(r.getString(1));

            }

        }
        catch(Exception e)
        {

        }
    }



    private static void init_connection_pool()
    {
        try{
            connpool=new DatabaseConnectionPool(driver,url,user,password);
            connpool.createPool();
            System.out.println("数据库连接池已初始化完毕");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("初始化数据库连接池失败=================Database.init_connection_pool");
        }
    }





    public static boolean Insert_new_picture(String phone,byte[] picData,String assetNo,String picNo,String info)//插入一个新的图片
    {
        boolean flag=true;
        if(connpool==null)//如果数据库连接池还没建立，初始化连接池
            init_connection_pool();

        java.util.Date date = new java.util.Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(date);//得到完成订单的时间
       //这里存储的是手机端的照片序号这样可以避免向手机端传输照片，直接从本地获取照片，减少延迟
        String picture_NO = picNo;
        String data_url=save_picture_in_file(picData,picture_NO);
        String info_in_pic = info;


        if(data_url.equals(""))
        {
            System.out.println("图片存储失败=================Database.Insert_new_picture");
            return false;
        }

        String sql_insert = "INSERT into photo VALUES (?,?,?,?,?,?)";


        try {
            // 连续数据库
            Connection conn = connpool.getConnection();
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            // statement用来执行SQL语句
            PreparedStatement statement = conn.prepareStatement(sql_insert);
            // 结果集
            statement.setString(1,picture_NO);
            statement.setString(2,assetNo);
            statement.setString(3,phone);
            statement.setString(4,time);
            statement.setString(5,data_url);
            statement.setString(6,info_in_pic);

            int rs=statement.executeUpdate();
            conn.close();
            if (rs == 0) {
                System.out.println("插入图片失败========================Database.Insert_new_picture");
                flag= false;
            } else {
                System.out.println("图片已保存到服务器，插入图片信息到数据库成功");
                flag= true;
            }
            statement.close();
            connpool.returnConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }


    public static boolean Insert_new_asset(String  userPhone,String assetNo,String assetDesc,String assetMoeny){
        boolean flag=true;
        if(connpool==null)//如果数据库连接池还没建立，初始化连接池
            init_connection_pool();
        String sql_insert = "INSERT into asset VALUES (?,?,?,?)";


        try {
            // 连续数据库
            Connection conn = connpool.getConnection();
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            // statement用来执行SQL语句
            PreparedStatement statement = conn.prepareStatement(sql_insert);
            // 结果集
            statement.setString(1,userPhone);
            statement.setString(2,assetNo);
            statement.setString(3,assetDesc);
            statement.setString(4,assetMoeny);

            int rs=statement.executeUpdate();
            conn.close();
            if (rs == 0) {
                System.out.println("插入资产失败========================Database.Insert_new_picture");
                flag= false;
            } else {
                System.out.println("资产已保存到服务器，插入资产信息到数据库成功");
                flag= true;
            }
            statement.close();
            connpool.returnConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static User User_identification(String phone_num, String passwd)//验证用户登录，成功返回true，失败返回false
    {
        if(connpool==null)
            init_connection_pool();


        User rt=null;
        String sql = "select * from user where user_phone='" + phone_num+"'";
        try {

            Connection conn=connpool.getConnection();
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            // statement用来执行SQL语句
            Statement statement = conn.createStatement();
            // 结果集
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                if (passwd.equals(rs.getString("user_password")))
                {
                    rt=new User(rs.getString("user_phone"),rs.getString("user_name"),rs.getString("user_sex"));
                }
                else {
                    System.out.println("无此用户，认证失败=================================Database.user_identification");
                }
            }

            rs.close();
            statement.close();
            connpool.returnConnection(conn);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rt;
        }
    }




    public static int getMaxAssetId(String phone){
        int number = 0;
        if(connpool == null){
            init_connection_pool();
        }
        String sql = "SELECT  MAX(asset_no) FROM `asset` WHERE user_phone = '"+ phone +"'";
        try {
            Connection conn = connpool.getConnection();
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the database");
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(sql);
            while(res.next()){
                String asstN0 =  res.getString("Max(asset_no)");
                number = Integer.valueOf(asstN0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(number == 0)
            number = 1;
        else
            number += 1;
        return number;

    }

    public static JSONObject Download_all_picture(String phone) {
        if(connpool==null)//如果数据库连接池还没建立，初始化连接池
            init_connection_pool();

        JSONObject rt=new JSONObject();
        //下载资产对应的最新图片
        String sql = "SELECT photo_no ,asset_no FROM photo WHERE asset_no IN(SELECT asset_no FROM `asset` WHERE user_phone = '"+ phone +"') and time IN(" +
                "SELECT MAX(time) from photo where user_phone = '"+ phone+"' and asset_no IN(" +
                " SELECT asset_no FROM `asset` WHERE user_phone = "+ phone +") GROUP BY asset_no)ORDER BY asset_no ASC";

        try {

            // 连续数据库
            Connection conn = connpool.getConnection();
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            // statement用来执行SQL语句
            Statement statement = conn.createStatement();
            // 结果集
            ResultSet rs = statement.executeQuery(sql);
            int length = 0;
            while (rs.next()) {

                    JSONObject tmpJson = new JSONObject();
                String picNo = rs.getString("photo_no");
                String asset_no = rs.getString("asset_no");
                String tmpSql = "select asset_desc, asset_money from asset where asset_no='"+asset_no+"'";
                Statement tmpStatement = conn.createStatement();
                ResultSet tmpRs = tmpStatement.executeQuery(tmpSql);
                while(tmpRs.next()) {
                    String asset_desc = tmpRs.getString("asset_desc");
                    String asset_money = tmpRs.getString("asset_money");
                    tmpJson.put("pic_no",picNo);//图片编号
                    tmpJson.put("asset_desc",asset_desc);//资产描述
                    tmpJson.put("asset_money",asset_money);//资产价值
                    tmpJson.put("asset_no",asset_no);//资产编号
                    rt.put(String.valueOf(length),tmpJson.toString());
                    length++;
                }
                tmpRs.close();
                tmpStatement.close();

            }
            rs.close();
            statement.close();
            connpool.returnConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            return rt;
        }
    }
    public static boolean Delete_picture(String asset_no,String user_phone,JSONObject rt){
        String dAsql = "DELETE FROM `asset` WHERE asset_no = '" + asset_no + "' AND user_phone = '"+ user_phone+"'";
        String dPsql = "DELETE FROM `photo` WHERE asset_no = '"+ asset_no+"' AND user_phone = '"+user_phone+"'";
        try {
            Connection connection  = connpool.getConnection();
            Statement statement = connection.createStatement();
            statement.addBatch(dAsql);
            statement.addBatch(dPsql);
            int[] result = statement.executeBatch();
            if(result[0] > 0 && result[1] > 0){
                rt.put("state","successful");
                return  true;
            }else if(result[0] == 0 && result[1] > 0){
                rt.put("state","fail");
                rt.put("wrongInfo","删除资产失败");
                return  false;
            }else if(result[0] > 0 && result[1] == 0){
                rt.put("state","fail");
                rt.put("wrongInfo","删除资产图片失败");
                return  false;
            }else{
                rt.put("state","fail");
                rt.put("wrongInfo","所有删除失败");
                return  false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rt.put("state","fail");
            rt.put("wrongInfo","服务器异常");
            return  false;
        }
    }


    private static byte[] load_picture_from_file(String pic_url)
    {
        try {
            File f = new File(absolutePath+pic_url);
            FileInputStream is = new FileInputStream(f);
            byte[] b = new byte[(int) f.length()];
            is.read(b);
            is.close();
            return b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("读取本地文件出错============Database.load_picture_from_file");
        }
        return null;
    }


    private  static String save_picture_in_file(byte[] pic_data,String pic_no)//将图片存储在本地的文件里
    {
        String rt="";
        java.util.Date date = new java.util.Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(date);//得到完成订单的时间
        String dir_url = "image/" + time+"/";
        try {

            File file = new File(absolutePath + dir_url);//每天的图片放在一个文件夹里
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                System.out.println(dir_url + "文件夹不存在，新建一个");
                file.mkdir();
            }

            FileOutputStream fos = new FileOutputStream(new File(dir_url + pic_no + ".jpg"));
            fos.write(pic_data);           fos.flush();
            fos.close();
            rt=dir_url+pic_no+".jpg";
            System.out.println("图片存储在"+dir_url+pic_no+".jpg");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("保存文件到文件中出错=================Database.save_picture_in_file");
            rt="";
        }
        finally {

            return rt;
        }
    }

}
