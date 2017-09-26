package tools;

import android.os.Environment;
import android.os.Handler;

import java.io.File;

/**
 * Created by Administrator on 2017-08-13.
 */
public class FLAG {
    public static String ADDRESS = "192.168.191.1";
    public static  int PORT = 23333;
    public static String[] STATE = {"successful","failed"};
    public static final String[] FUNCTION = {
            "LOGIN",
            "UPLOAD",
            "DOWNLOAD",
            "USERINFO",
            "DELETE",
            "ASSETNO",
            "LOGOUT"
    };
    public static Handler FLAG_HANDLER;
    public static String TOKEN;
    public static String USERACCOUNT;
    public static String USERPASSWORD;
    public static String HEADIMAGEPATH;
    public static StringBuffer HISTORY = new StringBuffer();
    public static double LATITUDE = 1.1;
    public static double LONGTITUDE = 1.2;
    public static File picPathDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"waterMarking");
}
