package tools;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-12.
 */
public class Picture implements Serializable{
    private int picture_no;
    private String pic_name;
    private String pic_time;
    private String pic_info;
    private long  pic_size;
    private byte[] pic_data;




    public void setPicture_info(String info){
        this.pic_info = info;
    }
    //时间设置成 年-月-日 -小时-分钟形式
    public void setPicture_time(String time){
        this.pic_time = time;
    }
    public String getPicture_info(){
        return this.pic_info;
    }
    public String getPic_time(){
        return this.pic_time;
    }
    public void setPicture_no(int picture_no) {
        this.picture_no = picture_no;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public void setPic_size(long pic_size) {
        this.pic_size = pic_size;
    }

    public void setPic_data(byte[] pic_data) {
        this.pic_data = pic_data;
        this.pic_data=new byte[pic_data.length];
        /*for(int i=0;i<pic_data.length;i++)
        {
            this.pic_data[i]=pic_data[i];
        }*/
    }



    public int getPicture_no()
    {
        return picture_no;
    }
    public byte[] getPic_data()
    {
        return pic_data;
    }
    public String getPic_name(){return  pic_name;}
    public long getPic_size()
    {
        return pic_size;
    }
}
