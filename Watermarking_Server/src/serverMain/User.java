package  serverMain;

import com.sun.javafx.runtime.async.BackgroundExecutor;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;

/**
 * Created by 32706 on 2017/3/2.
 */
public class User implements Serializable{
    private String token;
    private String phoneNum;
    private String username;
    private String sex;

    public User(String phone, String name, String sex) {

        this.phoneNum = phone;
        this.username = name;
        this.sex = sex;
    }

    public boolean set_token(String t) {
        this.token = t;
        return true;
    }

    public User get_user_info() {
        return this;
    }

    public boolean upload_pic(JSONObject info, JSONObject rt) {
        String picStr = info.getString("pic");
        //获得base64 编码的图片信息
        if(picStr != null) {
            try {
                byte[] picData = Base64.decode(picStr);
                System.out.println("获得图片的byte数据");
                //获取嵌入的信息
                String infoInPic = Get_info_in_Picture.Binary2Info(Get_info_in_Picture.getBinaryInfo(picData));
                System.out.println(infoInPic);
                //TODO 解密算法的解决
                Test test = new Test(this.token,"utf-8");
                String  jsonInfoInPic = test.decode(infoInPic);
                if (jsonInfoInPic == null) {
                    System.out.println("图片信息解析失败================User.upload_pic");
                    rt.put("state", "failed");
                    rt.put("wrongInfo", "图片信息解析失败================User.upload_pic");
                    return false;
                }
                System.out.println("解析出来的图片信息是"+ jsonInfoInPic);

                //解析json数据
                JSONObject picJson = JSONObject.fromObject(jsonInfoInPic);
                String type = picJson.getString("type");
                String assertNo = picJson.getString("assertNo");
                String assertDesc = picJson.getString("assertDesc");
                String assertValue = picJson.getString("assertValue");
                String picNo = picJson.getString("picNo");
                if(type.equals("new")) {
                    //将图片信息存储在数据库中
                    if (!Database.Insert_new_picture(this.phoneNum, picData, assertNo, picNo, picJson.toString())) {
                        System.out.println("上传图片出错，在database中出错================User.upload_pic");
                        rt.put("state", "failed");
                        rt.put("wrongInfo", "上传图片出错，在database中出错================User.upload_pic");
                        return false;
                    }
                    if (!Database.Insert_new_asset(this.phoneNum, assertNo, assertDesc, assertValue)) {
                        System.out.println("上传资产出错，在database中出错================User.upload_pic");
                        rt.put("state", "failed");
                        rt.put("wrongInfo", "上传资产出错，在database中出错================User.upload_pic");
                    }
                }else if(type.equals("add")){
                    if (!Database.Insert_new_picture(this.phoneNum, picData, assertNo, picNo, picJson.toString())) {
                        System.out.println("上传图片出错，在database中出错================User.upload_pic");
                        rt.put("state", "failed");
                        rt.put("wrongInfo", "上传图片出错，在database中出错================User.upload_pic");
                        return false;
                    }
                }
                //将新添加的资产信息插入到数据库之中

                rt.put("state","successful");
                return true;
            } catch (Base64DecodingException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            rt.put("state","failed");
            rt.put("wrongInfo","上传图片为空===========================User.upload_pic");
            return false;
        }
    }


    public int getAssetMaxId(){
        return Database.getMaxAssetId(this.phoneNum);
    }

    public boolean download_pic(JSONObject in,JSONObject rt)//下载用户上传过的历史图片
    {
       JSONObject get_from_database = Database.Download_all_picture(this.getPhoneNum());

       if(get_from_database.size()!=0&&get_from_database!=null)
       {
           rt.put("picList",get_from_database.toString());
           rt.put("state","successful");
           System.out.println("成功从本地取到图片，已打包放入返回对象");
           return true;
       } else{
           rt.put("state","failed");
           rt.put("wrongInfo","系统中没有任何历史图片===============User.download_pic");
           return false;
       }

    }

    public boolean delete_asset(String assetNo,JSONObject rt){
        if(Database.Delete_picture(assetNo,this.getPhoneNum(),rt)) {
            return true;
        }else{
            return  false;
        }
    }
    public String getToken() {
        return token;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }



    public void setToken(String token) {
        this.token = token;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setSex(String sex) {
        this.sex = sex;
    }
}
