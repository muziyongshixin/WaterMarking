package tools;

import android.util.Base64;

/**
 * Created by Administrator on 2017-08-07.
 * 加密算法的实现
 */
public class Encrypt {
    byte[] photoContent;
    String key;
    public Encrypt(byte[] photo, String key){
        this.photoContent = photo;
        this.key = key;
    }
    public void makeEncrypt(){
        byte[] out = Base64.decode(photoContent,1);
    }
}
