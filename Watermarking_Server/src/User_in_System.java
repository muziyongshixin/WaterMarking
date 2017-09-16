import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by 32706 on 2017/3/2.
 */
public class User_in_System {
    private static HashMap<String,User> User_have_logged_in=new HashMap<>();
    private static HashMap<String,Long> time_record=new HashMap<>();

    public static User get_user_in_system(String token)
    {
        User rt=User_have_logged_in.get(token);
       if(rt!=null)
       {
           time_record.put(token,new Date().getTime());
           return rt;
       }
       else
       {
           return null;
       }

    }


    public static boolean Insert_user_to_system(String token,User u)
    {
        User_have_logged_in.put(token,u);
        time_record.put(token,new Date().getTime());
        return true;
    }


    public static void update_user_state ()//更新系统中的用户，时间过长没有消息的会自动从User_have_logged_in中删除
    {
       try{
           Iterator it=User_have_logged_in.keySet().iterator();



          while(it.hasNext())
           {
               long nowtime=new Date().getTime()/1000;
               String token=(String) it.next();
               long time_to_last_operation=nowtime-time_record.get(token)/1000;
               if(time_to_last_operation>600)  //超过10分钟没有心跳信息就默认掉线，重新登录
               {
                   time_record.remove(token);
                   User_have_logged_in.remove(token);
               }
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }



    public static void main(String arg[])
    {
        long nowtime=new Date().getTime();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new Date().getTime()-nowtime);


    }
}
