
package  serverMain;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class Get_info_in_Picture {
    private static int Get_infoLength(BufferedImage bufferedImage) {
        //获取信息长度
        int rt=-1;
        try{
            int[] length = new int[1];
            length[0] = 0;
            int temp;
            for (int i = 0; i < 8; i++) {
                int pixel = bufferedImage.getRGB(i, 0);
                temp = ((pixel & 0xff) & 0X01) << (7 - i);
                length[0] = length[0] | temp;
            }
            byte[] result = {(byte) length[0]};
           try{
               rt= Integer.parseInt(new String(result));
           }
           catch (Throwable ue)
           {
               ue.printStackTrace();
           }
        }catch (Exception  e)
        {
            e.printStackTrace();
        }
        return  rt;
    }

    private static byte[][] intTOByte(int[][] a) {
        //转换成字节数组
        byte[][] b = new byte[a.length][3];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < 3; j++)
                b[i][j] = (byte) a[i][j];
        }
        return b;
    }

    public static String Get_info(byte[] inb, String token) {
        String real_info=null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(inb);    //将b作为输入流；
            BufferedImage bufferedImage = ImageIO.read(in);     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();


            int length = Get_infoLength(bufferedImage);
            if(length==-1)
                return  null;
            int[] Str = new int[length];
            int Strstart = 0;//记录当前字节位置
            int bytestart = 7;//记录当前记录字节中位的位置
            int temp;
            int Ystart = bufferedImage.getMinY();
            int Yend = bufferedImage.getHeight();
            int Xstart = bufferedImage.getMinX();
            int Xend = bufferedImage.getWidth();
            int rgb;
            for (int j1 = Ystart + 1; j1 < Yend; j1++) {
                for (int j2 = Xstart; j2 < Xend; j2++) {
                    int pixel = bufferedImage.getRGB(j2, j1);
                    rgb = (pixel & 0xff);
                    if (Strstart < length) {
                        if (bytestart >= 0) {
                            temp = (rgb & 0X01) << bytestart;
                            Str[Strstart] = Str[Strstart] | temp;
                            bytestart--;
                        } else {
                            bytestart = 7;
                            Strstart++;
                        }
                    }
                }
            }
            int[][] a = new int[Str.length / 3][3];
            for (int i = 0; i < Str.length; i++) {
                a[i / 3][i % 3] = Str[i];
            }
            byte[][] b = intTOByte(a);
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                buffer.append(new String(b[i]));
            }


            String encrypted_info= buffer.toString();

            real_info=AESDecrypt.decryptAES(encrypted_info,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return real_info;
        }
    }

}


class AESDecrypt //解密
{
    private static byte[] asBin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    public static String decryptAES(String encData, String key) {
        byte[] tmp = asBin(encData);
        byte[] Dekey = asBin(key);
        SecretKeySpec sKey = new SecretKeySpec(Dekey, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            byte[] decrypted = cipher.doFinal(tmp);
            return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
