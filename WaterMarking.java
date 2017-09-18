
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import javax.imageio.ImageIO;
public class WaterMarking {
    /**
     * 读取一张图片的RGB值
     *
     * @throws Exception
     */
    public void getImagePixel(String image) throws Exception {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        System.out.println("width=" + width + ",height=" + height + ".");
        System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字  
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
                        + rgb[1] + "," + rgb[2] + ")");
            }
        }
    }

    /**
     * 返回屏幕色彩值
     *
     * @param x
     * @param y
     * @return
     * @throws AWTException
     */
    public int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。  
        Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。  
        rb = new Robot();
        Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包  
        Dimension di = tk.getScreenSize(); // 屏幕尺寸规格  
        System.out.println(di.width);
        System.out.println(di.height);
        Rectangle rec = new Rectangle(0, 0, di.width, di.height);
        BufferedImage bi = rb.createScreenCapture(rec);
        int pixelColor = bi.getRGB(x, y);

        return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。  
    }

    /**
     * 将数字转换成指定长度的01010字符串
     *
     * @param num
     * @param len
     * @return
     */
    private static String getFormatBinary(int num, int len) {
        String rt = Integer.toBinaryString(num);
        while (rt.length() < len) {
            rt = "0" + rt;
        }
        return rt;
    }

    /**
     * 根据信息转换成二进制信息，返回为0101010字符串
     *
     * @param info
     * @return
     */
    private static String Info2Binary(String info) {
        String rt = "";
        byte[] bys = info.getBytes();
        for (int i = 0; i < bys.length; i++) {
            byte b = bys[i];
            for (int j = 7; j >= 0; j--) {
                rt += (byte) ((b >> j) & 0x1);
            }
        }
        System.out.println("转换为二进制长度为=="+rt.length()+"=="+rt);
        System.out.println("需要的像素数为："+(rt.length()/3+((rt.length()-rt.length()/3*3)==0?0:1)));
        return rt;
    }


    /**
     * 将010010字符串转换为原始的字符串信息。
     *
     * @param bina
     * @return
     */
    private static String Binary2Info(String bina)//将二进制信息还原为字符串
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
        return info;
    }

    private static BufferedImage writeBinaryInfo(String info, BufferedImage img) {
        if (img.getWidth() <= 2)
            return img;
        char[] infc = info.toCharArray();
        boolean writeflag = true;
        int cur = 0;
        int count = 1;   //表示最后一个像素使用了几个位
        int x = 0, y = 0;
        for (x = 1; x < img.getHeight() && writeflag; x++) {
            for (y = 0; y < img.getWidth() && writeflag; y++) {
                // 获取到rgb的组合值
                if (info.length() - cur < 3) {
                    writeflag = false;
                    count = info.length() - cur;
                }
                if (count == 0)//如果cur指向string之外，直接退出
                {
                    break;
                }
                int[] bits = new int[3];
                int i = 0;
                while (cur < info.length() && i < 3) {
                    bits[i] = infc[cur] - '0';
                    cur++;
                    i++;
                }
                //每一像素的每一个通道存储一位信息，奇数表示1，偶数表示0
                int rgb = img.getRGB(y, x);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                if (r % 2 != 0 && bits[0] == 0) {
                    r = (r + 1) <= 255 ? (r + 1) : (r - 1);
                } else if (r % 2 == 0 && bits[0] == 1) {
                    r = (r + 1) <= 255 ? (r + 1) : (r - 1);
                }

                if (g % 2 != 0 && bits[1] == 0) {
                    g = (g + 1) <= 255 ? (g + 1) : (g - 1);

                } else if (g % 2 == 0 && bits[1] == 1) {
                    g = (g + 1) <= 255 ? (g + 1) : (g - 1);
                }

                if (b % 2 != 0 && bits[2] == 0) {
                    b = (b + 1) <= 255 ? (b + 1) : (b - 1);

                } else if (b % 2 == 0 && bits[2] == 1) {
                    b = (b + 1) <= 255 ? (b + 1) : (b - 1);
                }
                color = new Color(r, g, b);
                img.setRGB(y, x, color.getRGB());
            }
            if(!writeflag)
            {
                y--;
                break;
            }
        }

        //第一行的前8像素用来标记endx（范围0-16777216）,8~16的8个响度标记endy（范围0-16777216），第17的1个像素标记endflag（范围0-7）
        int endx = x, endy = y, endflag = count;
        if (count == 0) {
            endflag = 3;
            if (endy <= 0) {
                endy = img.getHeight() - 1;
                endx--;
            }
        }
        char[] endxBinary = getFormatBinary(endx, 24).toCharArray();
        char[] endyBinary = getFormatBinary(endy, 24).toCharArray();
        char[] endflagBinary = getFormatBinary(endflag, 3).toCharArray();

        System.out.println(endx+"=="+endy+"=="+endflag);
        img = writeHeader(0, 8, img, endxBinary);
        img = writeHeader(8, 8, img, endyBinary);
        img = writeHeader(16, 1, img, endflagBinary);
        return img;
    }


    /**
     * 将头部信息写入图片第一行
     *
     * @param begin
     * @param pixNum
     * @param img
     * @param info
     * @return
     */
    private static BufferedImage writeHeader(int begin, int pixNum, BufferedImage img, char[] info) {
        int cur = 0;
        for (int i = begin; i < begin + pixNum; i++) {
            int[] bits = new int[3];
            int j = 0;
            while (j < 3) {
                bits[j] = info[cur] - '0';
                cur++;
                j++;
            }
            //每一像素的每一个通道存储一位信息，奇数表示1，偶数表示0
            int rgb = img.getRGB(i, 0);
            Color color = new Color(rgb);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            if (r % 2 != 0 && bits[0] == 0) {
                r = (r + 1) <= 255 ? (r + 1) : (r - 1);
            } else if (r % 2 == 0 && bits[0] == 1) {
                r = (r + 1) <= 255 ? (r + 1) : (r - 1);
            }
            if (g % 2 != 0 && bits[1] == 0) {
                g = (g + 1) <= 255 ? (g + 1) : (g - 1);

            } else if (g % 2 == 0 && bits[1] == 1) {
                g = (g + 1) <= 255 ? (g + 1) : (g - 1);
            }
            if (b % 2 != 0 && bits[2] == 0) {
                b = (b + 1) <= 255 ? (b + 1) : (b - 1);

            } else if (b % 2 == 0 && bits[2] == 1) {
                b = (b + 1) <= 255 ? (b + 1) : (b - 1);
            }
            color = new Color(r, g, b);
            img.setRGB(i, 0, color.getRGB());
        }
        return img;
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
            int rgb = img.getRGB(i, 0);
            Color color = new Color(rgb);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
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


    private static String getBinaryInfo(BufferedImage img) {
        String binaryStr = "";
        int endx = getHeader(0, 8, img);
        int endy = getHeader(8, 8, img);
        int endflag = getHeader(16, 1, img);
        for (int i = 1; i < endx && i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int rgb = img.getRGB(j, i);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                binaryStr += r % 2 == 0 ? "0" : "1";
                binaryStr += g % 2 == 0 ? "0" : "1";
                binaryStr += b % 2 == 0 ? "0" : "1";
            }
        }
        for (int j = 0; j < endy && j < img.getWidth(); j++) {
            int rgb = img.getRGB(j, endx);
            Color color = new Color(rgb);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            binaryStr += r % 2 == 0 ? "0" : "1";
            binaryStr += g % 2 == 0 ? "0" : "1";
            binaryStr += b % 2 == 0 ? "0" : "1";
        }
        int rgb = img.getRGB(endy, endx);
        Color color = new Color(rgb);
        int[] temp = new int[3];
        temp[0] = color.getRed();
        temp[1] = color.getGreen();
        temp[2] = color.getBlue();
        for (int i = 0; i < endflag && i < 3; i++) {
            binaryStr += temp[i] % 2 == 0 ? "0" : "1";
        }

        return binaryStr;
    }



    /**
     * @param args
     */
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
    }

}  