package tools;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2017-08-12.
 */
public class Connection extends Thread{
    public static Socket client;
    private ConnectionData clientSendData;
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private Handler mHandler;

    //这里使用单例模式来创建这个子线程的socket通信
    private static Connection connection;

    private Connection() throws IOException {
        client = new Socket(FLAG.ADDRESS,FLAG.PORT);
        clientOutputStream = new ObjectOutputStream(client.getOutputStream());
        clientInputStream = new ObjectInputStream(client.getInputStream());
    }
    public static synchronized Connection getConnection() throws IOException {
        if(connection == null){
            connection = new Connection();
        }
        return connection;
    }
    public void setHandler(){
        mHandler = FLAG.FLAG_HANDLER;
    }
    public void setData(ConnectionData in){
        this.clientSendData = in;
    }
    @Override
    public void run(){
        try {
            //send the data by the output
            clientOutputStream.writeObject(clientSendData);
            //get  the input object
            clientInputStream = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            Object ob = clientInputStream.readObject();
            ConnectionData out = (ConnectionData)ob;
            Message msg = new Message();
            msg.obj = out;
            mHandler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void connectStop() throws IOException {
        clientInputStream.close();
        clientOutputStream.close();
    }
}
