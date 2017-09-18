package serverMain; /**
 * Created by 32706 on 2017/3/2.
 * <p>
 * <p>
 * 本类实现一个线程池，同时建立socket连接
 * 得到连接后，将调用callmaxentThreadPooltask里的方法
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallMaxentThreadPool {
    private static int produceTaskSleepTime = 20;
    private static int produceTaskMaskNumber = 10;
    private static final int port = 23333;
    public static boolean flag = false;

    public static void main(String[] args) {
        startServ();
        //构造线程池
        System.out.println("Server listening on port: " + port + "......");


        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Socket clientSocket = null;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(   //新建线程池
                2,      //corePoolSize
                50,      //maximumPoolSize
                3,      //keepAliveTime
                TimeUnit.SECONDS,   //unit
                new ArrayBlockingQueue<Runnable>(5),  //workQueue
                new ThreadPoolExecutor.DiscardOldestPolicy()//
        );

       // start_update();//启动系统中用户的已登录用户的定期更新

        long i = 0;
        while (flag) {
            //获得客户端请求
            try {

                clientSocket = serverSocket.accept();
                String task = "new connection@ " + i;
                System.out.println("\n\n\nthere is a " + task);
                threadPool.execute(new CallMaxentThreadPoolTask(clientSocket));
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startServ() {
        flag = true;

    }

    public static void start_update() {
        Update_user_in_system updateThread = new Update_user_in_system();
        updateThread.start();
    }

    public static void stopServ() {
        flag = false;
    }
}


class Update_user_in_system extends Thread {



    @Override
    public void run() {


       while(CallMaxentThreadPool.flag)
       {
           User_in_System.update_user_state();//更新系统中已登录用户
           DateFormat format = new SimpleDateFormat("HH:mm:ss");
           System.out.println("user_in_system updated @ " + format.format(new Date().getTime()));
           try {
               Thread.sleep(60000);//60S更新一次系统中的用户  如果超过60s没有和服务器进行数据交互需要重新登录
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

       }

    }

}