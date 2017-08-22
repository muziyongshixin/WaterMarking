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
    public String address;
    public int port;
    private ConnectionData clientSendData;
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private Handler mHandler;

    //这里使用单例模式来创建这个子线程的socket通信
    private static Connection connection;

    private Connection(String address,int port){
        this.address = address;
        this.port = port;
        client = new Socket();
    }
    public static synchronized Connection getConnection(){
        if(connection == null){
            connection = new Connection(FLAG.ADDRESS,FLAG.PORT);
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
            client.connect(new InetSocketAddress(this.address,this.port),5000);
            //send the data by the output
            OutputStream os = client.getOutputStream();
            clientOutputStream= new ObjectOutputStream(os);
            clientOutputStream.writeObject(clientSendData);
            os.flush();
            //get  the input object
            clientInputStream = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            Object ob = clientInputStream.readObject();
            ConnectionData out = (ConnectionData)ob;
            Message msg = new Message();
            msg.obj = out;
            mHandler.sendMessage(msg);
            os.close();
            clientInputStream.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
