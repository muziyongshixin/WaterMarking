package tools;

import android.util.Base64;

/**
 * Created by Administrator on 2017-08-07.
 * 加密算法的实现
 */
public class Encrypt {
    public static String makeEncrypt(String key,String content){
        String result = "";
        return result;
    }
    public static String makeDecrypted(String key,String content){
        String result = "";
        return result;
    }
    public static byte[] EmbedInfo(byte[] photoData,String info){
        byte[] result  = Base64.encode(photoData,Base64.DEFAULT);
        return result;
    }
}
