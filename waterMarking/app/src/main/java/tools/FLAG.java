package tools;

import android.os.Handler;

/**
 * Created by Administrator on 2017-08-13.
 */
public class FLAG {
    public static String ADDRESS = "192.168.191.1";
    public static  int PORT = 20000;
    public static String[] STATE = {"successful","failed"};
    public static final String[] FUNCTION = {
            "LOGIN",
            "UPLOAD",
            "DOWNLOAD",
            "USERINFO",
            "REFRESH",
            "MODIFY",
            "DELETE",
            "LOGOUT"
    };
    public static Handler FLAG_HANDLER;
    public static String TOKEN;
    public static String USERACCOUNT;
    public static String USERPASSWORD;
    public static String HEADIMAGEPATH;
    public static StringBuffer HISTORY;
}
