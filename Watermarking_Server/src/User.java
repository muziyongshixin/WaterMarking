
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public boolean upload_pic(ConnectionData info, ConnectionData rt) {
        List<Picture> pictures = info.getPicture_list();//拿到app传入数据里的picturelist
        if (pictures.size() > 0) {
            for (int i = 0; i < pictures.size(); i++) {
                Picture temPic = pictures.get(i);


                String info_in_pic = Get_info_in_Picture.Get_info(temPic.getPic_data(),this.getPhoneNum());
                //String info_in_pic="这是一条写死的测试信息==============xxxxxxxxxxxxxxxx";
                System.out.println(info_in_pic);
                if(info_in_pic==null)
                {
                    System.out.println("图片信息解析失败================User.upload_pic");
                    rt.setState("failed");
                    rt.setWrong_info("图片信息解析失败================User.upload_pic");
                    return false;
                }

                if (!Database.Insert_new_picture(this.phoneNum, temPic.getPic_data(),info_in_pic)) {
                    System.out.println("上传图片出错，在database中出错================User.upload_pic");
                    rt.setState("failed");
                    rt.setWrong_info("上传图片出错，在database中出错================User.upload_pic");
                    return false;
                }
            }
        } else {
            rt.setState("failed");
            rt.setWrong_info("上传图片为空===========================User.upload_pic");
            return false;
        }

        return true;
    }


    public boolean download_pic(ConnectionData in,ConnectionData rt)//下载用户上传过的历史图片
    {
       List<Picture> get_from_database=new ArrayList<>();

       get_from_database=Database.Download_all_picture(this.getPhoneNum());

       if(get_from_database.size()!=0&&get_from_database!=null)
       {
           rt.setPicture_list(get_from_database);
           System.out.println("成功从本地取到图片，已打包放入返回对象");
           return true;
       }
       else{
           rt.setState("failed");
           rt.setWrong_info("系统中没有任何历史图片===============User.download_pic");
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
