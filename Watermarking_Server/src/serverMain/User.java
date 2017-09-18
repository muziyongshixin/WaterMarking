package  serverMain;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.Serializable;

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
        System.out.println("获得图片的数据为"+picStr);
        //获得base64 编码的图片信息
        if(picStr != null) {
            try {
                byte[] picData = Base64.decode(picStr);
                //TODO 测试部分，将byte数据存储在本地，看图片内容是否变化，检测base64的用法
                System.out.println("获得图片的byte数据");
                //TODO 获得图片数据的算法还需要手机端加进来加进来
                //String infoInPic = Get_info_in_Picture.Get_info(picData, this.phoneNum);
                String infoInPic = "test";
                if (infoInPic == null) {
                    System.out.println("图片信息解析失败================User.upload_pic");
                    rt.put("state", "failed");
                    rt.put("wrongInfo", "图片信息解析失败================User.upload_pic");
                    return false;
                }
                if (!Database.Insert_new_picture(this.phoneNum, picData, infoInPic)) {
                    System.out.println("上传图片出错，在database中出错================User.upload_pic");
                    rt.put("state", "failed");
                    rt.put("wrongInfo", "上传图片出错，在database中出错================User.upload_pic");
                    return false;
                }
                rt.put("state","successful");
                return true;
            } catch (Base64DecodingException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            rt.put("state","failed");
            rt.put("wrongInfo","上传图片为空===========================User.upload_pic");
            return false;
        }
       /* JSONObject picListJson = JSONObject.fromObject(picListStr);
        if (picListJson.size() > 0) {
            for(int i = 0 ; i < picListJson.size() ; i++){
                String pictureData = picListJson.getString(String.valueOf(i));
                Picture tmpPic = new Picture();
                try {
                    tmpPic.setPic_data(Base64.decode(pictureData));
                } catch (Base64DecodingException e) {
                    e.printStackTrace();
                }
                String info_in_pic = Get_info_in_Picture.Get_info(tmpPic.getPic_data(),this.phoneNum);
                //String info_in_pic="这是一条写死的测试信息==============xxxxxxxxxxxxxxxx";
                System.out.println(info_in_pic);
                if(info_in_pic==null)
                {
                    System.out.println("图片信息解析失败================User.upload_pic");
                    rt.put("state","failed");
                    rt.put("wrongInfo","图片信息解析失败================User.upload_pic");
                    return false;
                }

                if (!Database.Insert_new_picture(this.phoneNum, tmpPic.getPic_data(),info_in_pic)) {
                    System.out.println("上传图片出错，在database中出错================User.upload_pic");
                    rt.put("state","failed");
                    rt.put("wrongInfo","上传图片出错，在database中出错================User.upload_pic");
                    return false;
                }
            }
        } else {
            rt.put("state","failed");
            rt.put("wrongInfo","上传图片为空===========================User.upload_pic");
            return false;
        }

        return true;*/
    }


    public boolean download_pic(JSONObject in,JSONObject rt)//下载用户上传过的历史图片
    {
       JSONObject get_from_database = Database.Download_all_picture(this.getPhoneNum());

       if(get_from_database.size()!=0&&get_from_database!=null)
       {
           rt.put("picList",get_from_database.toString());
           System.out.println("成功从本地取到图片，已打包放入返回对象");
           return true;
       }
       else{
           rt.put("state","failed");
           rt.put("wrongInfo","系统中没有任何历史图片===============User.download_pic");
           return false;
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
