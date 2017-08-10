package tools;

import android.util.Base64;

/**
 * Created by Administrator on 2017-08-07.
 */
public class encrypt {
    byte[] photoContent;
    String key;
    public encrypt(byte[] photo,String key){
        this.photoContent = photo;
        this.key = key;
    }
    public void makeEncrypt(){
        byte[] out = Base64.decode(photoContent,1);
    }
}
