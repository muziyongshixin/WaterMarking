import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 *
 *
 * 本类测试对象传输
 *
 *
 *
 */
public class MyServer {

    private final static Logger logger = Logger.getLogger(MyServer.class.getName());

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(20000);

        while (true) {
            Socket socket = server.accept();
            invoke(socket);
        }
    }

    private static void invoke(final Socket socket) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                ObjectInputStream is = null;
                ObjectOutputStream os = null;
                try {
                    is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    os = new ObjectOutputStream(socket.getOutputStream());

                    Object obj = is.readObject();
                    ConnectionData con=(ConnectionData)obj;
                    System.out.println("user: " + con.getPhone());

                    con.setPhone("1");

                    os.writeObject(con);
                    os.flush();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch(ClassNotFoundException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        is.close();
                    } catch(Exception ex) {}
                    try {
                        os.close();
                    } catch(Exception ex) {}
                    try {
                        socket.close();
                    } catch(Exception ex) {}
                }
            }
        }).start();
    }
}