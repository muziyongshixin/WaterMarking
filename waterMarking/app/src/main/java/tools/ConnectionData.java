package tools;


import com.example.administrator.watermarking.AssetActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-12.
 */
public class ConnectionData implements Serializable{
    String function=null;//功能字段
    String phone=null;//用户手机号
    String password=null;//用户密码
    String token=null;//token数据
    String state=FLAG.STATE[0];
    String wrong_info=null;
    User userinfo=null;//用户个人信息
    List<Picture> picture_list=null;
    List<AssetActivity> assets_lsit=null;

    private int assertId;
    private JSONObject assertInfo;

    public void setFunction(String fun)
    {
        this.function=fun;
    }
    public void setPhone(String phone)
    {
        this.phone=phone;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public void setToken(String tk)
    {
        this.token=tk;
    }
   /* public void setUserinfo(User in)
    {
        userinfo=new User(in.getPhoneNum(),in.getUsername(),in.getSex());

    }*/

    public void setPicture_list(List<Picture> in)
    {
        if(this.picture_list==null)
            picture_list=new ArrayList<>();
        this.picture_list.addAll(in);
    }
    public void setAssets_lsit(List<AssetActivity> in)
    {
        assets_lsit=new ArrayList<>();
        assets_lsit.addAll(in);
    }
    public void setState(String in)
    {
        this.state=in;
    }
    public  void setWrong_info(String info)
    {
        if(this.wrong_info==null)
        {
            this.wrong_info=info;
        }
        else {
            this.wrong_info+=info;
        }

    }




    /**取出各个参数**/
    public String getFunction()
    {
        return this.function;
    }
    public String getPhone()
    {
        return this.phone;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getToken()
    {
        return this.token;
    }
   /* public User getUserinfo()
    {
        return this.userinfo;
    }*/
    public List<Picture> getPicture_list()
    {
        return this.picture_list;
    }
    public List<AssetActivity> getAssets_lsit(){
        return this.assets_lsit;
    }
    public String getState()
    {
        return this.state;
    }
    public String getWrong_info()
    {
        return this.wrong_info;
    }
    public void setAssertId(int id){
        this.assertId = id;
    }
    public int getId(){
        return this.assertId;
    }
    public void setAssertInfo(JSONObject in){
        this.assertInfo = in;
    }
    public JSONObject getAssertInfo(){
        return this.assertInfo;
    }

}
