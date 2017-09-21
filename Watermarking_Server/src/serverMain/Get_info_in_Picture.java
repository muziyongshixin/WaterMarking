
package  serverMain;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Vector;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class Get_info_in_Picture {



    /**
     * 将010010字符串转换为原始的字符串信息。
     *
     * @param bina
     * @return
     */
    public static String Binary2Info(String bina)//将二进制信息还原为字符串
    {
        Vector<Byte> bys = new Vector<>();
        int cur = 0;
        while (cur < bina.length()) {
            char[] c = bina.substring(cur, cur + 8).toCharArray(); //拿到每8个字符组成一个新的byte
            cur += 8;
            byte tbyte = (byte) 0xFF;
            for (int i = 0; i < 8; i++) {
                int bit = c[i] - '0';
                if (bit == 0)
                    tbyte = (byte) ((tbyte << 1) | 0x0);
                else if (bit == 1)
                    tbyte = (byte) ((tbyte << 1) | 0x1);
            }
            bys.add(tbyte);
        }
        byte[] infobyte = new byte[bys.size()];
        for (int i = 0; i < infobyte.length; i++) {
            infobyte[i] = bys.get(i);
        }
        String info = new String(infobyte);
        //TODO　解密算法的实现
        //info = AESDecrypt.decryptAES(info,key);
        return info;
    }




    /**
     * 得到水印图片的头部信息
     *
     * @param begin
     * @param pixNum
     * @param img
     * @return
     */
    private static int getHeader(int begin, int pixNum, BufferedImage img) {
        String rt = "";
        for (int i = begin; i < begin + pixNum; i++) {
            int[] rgb = getRGB(img.getRGB(i, 0));
            int r = rgb[0];
            int g = rgb[1];
            int b = rgb[2];
            if (r % 2 == 0) {
                rt += "0";
            } else {
                rt += "1";
            }
            if (g % 2 == 0) {
                rt += "0";
            } else {
                rt += "1";
            }
            if (b % 2 == 0) {
                rt += "0";
            } else {
                rt += "1";
            }
        }
        int rtNum = Integer.valueOf(rt, 2);
        return rtNum;
    }


    public static String getBinaryInfo(byte[] dataIn) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(dataIn));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String binaryStr = "";
        int endx = getHeader(0, 8, img);
        int endy = getHeader(8, 8, img);
        int endflag = getHeader(16, 1, img);
        for (int i = 1; i < endx && i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int[] rgb = getRGB(img.getRGB(j, i));
                binaryStr += addBinaryStr(rgb);
            }
        }
        for (int j = 0; j < endy && j < img.getWidth(); j++) {
            int[] rgb = getRGB(img.getRGB(j, endx));
            binaryStr += addBinaryStr(rgb);
        }
        int[] temp = getRGB(img.getRGB(endy,endx));
        for (int i = 0; i < endflag && i < 3; i++) {
            binaryStr += temp[i] % 2 == 0 ? "0" : "1";
        }
        return binaryStr;
    }
    private static int[] getRGB(int pixel){
        Color color = new Color(pixel);
        int[] data = new int[3];
        data[0] = color.getRed();
        data[1] = color.getGreen();
        data[2] = color.getBlue();
        return data;
    }
    private static String addBinaryStr(int[] rgb){
        String binaryStr = "";
        binaryStr += rgb[0] % 2 == 0 ? "0" : "1";
        binaryStr += rgb[1] % 2 == 0 ? "0" : "1";
        binaryStr += rgb[2] % 2 == 0 ? "0" : "1";
        return binaryStr;
    }



  /*  *//**
     * @param args
     *//*
    public static void main(String[] args) throws Exception {
        BufferedImage bi = ImageIO.read(new File("image/1.jpg"));
        BufferedImage newimage = writeBinaryInfo(Info2Binary("[{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":5[{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}][{\"CityId\":18,\"CityName\":\"西安\",\"ProvinceId\":27,\"CityOrder\":1},{\"CityId\":53,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}]3,\"CityName\":\"广州\",\"ProvinceId\":27,\"CityOrder\":1}]"), bi);
        String str=Binary2Info(getBinaryInfo(newimage));
        System.out.println(str);

//        ImageIO.write(newimage, "JPEG", new File("image/3.jpg"));


        //   System.out.println(Binary2Info(Info2Binary("{sdfasdf/asdfwe*23023401uasdkjf}")));

//        int x = 0;
//        ReadColorTest rc = new ReadColorTest();
//        x = rc.getScreenPixel(100, 345);
//        System.out.println(x + " - ");
//        rc.getImagePixel("image/1.jpg");
    }*/

}


class AESDecrypt //解密
{

    private  static final String AES = "AES";//AES 加密
    private  static final String  SHA1PRNG="SHA1PRNG";
    private  static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    /*
   * 解密
   */
    // 对密钥进行处理
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom sr = null;
        sr = SecureRandom.getInstance(SHA1PRNG,"Crypto");
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static String decrypt(String key, String encrypted) {
        if (encrypted.isEmpty()) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 解密
     */
    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

}
