package tools;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Administrator on 2017-08-12.
 */
public class Connection extends Thread{
    public static Socket client;
    private JSONObject clientSendData;
    private BufferedWriter clientWr;
    private BufferedReader clientBR;
    private Handler mHandler;
    public boolean sendFlag;

    //这里使用单例模式来创建这个子线程的socket通信
    private static Connection connection;

    private Connection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* try {
                    client = new Socket(FLAG.ADDRESS,FLAG.PORT);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();

    }
    public static synchronized Connection getConnection(){
        if(connection == null){
            connection = new Connection();
        }
        return connection;
    }
    public void setHandler(){
        mHandler = FLAG.FLAG_HANDLER;
    }
    public void setData(JSONObject in){
        this.clientSendData = in;
    }
    public void startSend(){
        sendFlag = true;
    }
    void stopSend(){
        sendFlag = false;
    }
    @Override
    public void run(){
        while(true) {
            if(sendFlag) {
                try {
                    //send the data by the output
                    client = new Socket(FLAG.ADDRESS, FLAG.PORT);
                    clientWr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    clientWr.write(clientSendData.toString()+"\n");
                    clientWr.flush();
                    //get  the input object
                    clientBR = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String serverData = clientBR.readLine();
                    JSONObject out = null;
                    try {
                        out = new JSONObject(serverData);
                        Message msg = new Message();
                        msg.obj = out;
                        mHandler.sendMessage(msg);
                        stopSend();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clientWr.close();
                    clientBR.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    stopSend();
                }
            }
        }

    }
   /* public void connectStop() throws IOException {
        clientWr.close();
        client.close();
    }*/
}
