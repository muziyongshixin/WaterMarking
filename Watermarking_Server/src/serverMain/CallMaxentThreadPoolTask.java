package serverMain; /**
 * Created by 32706 on 2017/3/2.
 */

/*************************************
 *
 * 本类实现对一个socket连接的处理
 *
 * ***************************************/

import java.io.*;
import java.net.Socket;

import net.sf.json.JSONObject;

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
            System.out.println("get data ");
            BufferedReader is = new BufferedReader(new InputStreamReader(clientInput));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(clientOutput));

            JSONObject clientIn = JSONObject.fromObject(is.readLine());

            System.out.println(clientIn.toString());


            /***********调用功能，得到返回值**************************************/

            JSONObject writeback = Function.Parameter_extraction(clientIn);

            if(writeback==null)
            {
                System.out.println("将要写回的数据为空");
                writeback=new JSONObject();
                writeback.put("state","failed");
                writeback.put("wrongInfo","unknown mistakes,no data back");
            }
            else{
                System.out.println("服务器处理完成，准备反馈数据");
            }

            os.write(writeback.toString() + "\n");//写回

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