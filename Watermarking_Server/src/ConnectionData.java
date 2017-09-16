import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32706 on 2017/3/12.
 * 该类定义数据传送类
 */
public class ConnectionData implements java.io.Serializable{
    String function=null;//功能字段
    String phone=null;//用户手机号
    String password=null;//用户密码
    String token=null;//token数据
    String state="successful";
    String wrong_info=null;
    User userinfo=null;//用户个人信息
    List<Picture> picture_list=null;
    List<Asset> assets_lsit=null;



    /**设置各个参数**/
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
    public void setUserinfo(User in)
    {
        userinfo=new User(in.getPhoneNum(),in.getUsername(),in.getSex());

    }
    public void setPicture_list(List<Picture> in)
    {
        if(this.picture_list==null)
        picture_list=new ArrayList<>();
        this.picture_list.addAll(in);
    }
    public void setAssets_lsit(List<Asset> in)
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
    public User getUserinfo()
    {
        return this.userinfo;
    }
    public List<Picture> getPicture_list()
    {
        return this.picture_list;
    }
    public List<Asset> getAssets_lsit(){
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


}
