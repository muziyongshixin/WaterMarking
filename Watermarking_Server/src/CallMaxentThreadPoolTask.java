/**
 * Created by 32706 on 2017/3/2.
 */

/*************************************
 *
 * 本类实现对一个socket连接的处理
 *
 * ***************************************/

import java.io.*;
import java.net.Socket;

public class CallMaxentThreadPoolTask implements Runnable, Serializable {
    private static final long serialVersionUID = 0;

    private Socket socket;
    private InputStream clientInput;
    private OutputStream clientOutput;

    CallMaxentThreadPoolTask(Socket socket) {
        this.socket = socket;

        try {
            this.clientInput = this.socket.getInputStream();
            this.clientOutput = this.socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            //接收到客户端传来信息

            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(clientInput));
            ObjectOutputStream os = new ObjectOutputStream(clientOutput);

            Object obj = is.readObject();
            ConnectionData data_from_app = (ConnectionData) obj;


            /***********调用功能，得到返回值**************************************/

            ConnectionData writeback = Function.Parameter_extraction(data_from_app);

            if(writeback==null)
            {
                System.out.println("将要写回的数据为空");
                writeback=new ConnectionData();
                writeback.setState("failed");
                writeback.setWrong_info("unknown mistakes,no data back");
            }
            else{
                System.out.println("服务器处理完成，准备反馈数据");
            }

            os.writeObject(writeback);//写回

            os.flush();
            System.out.println("Task Finish!");
            socket.close();
            clientInput.close();
            clientOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}