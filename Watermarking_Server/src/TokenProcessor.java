import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 令牌处理器 
 *
 * @author Vicky
 * @emial eclipser@163.com 
 *
 */
public class TokenProcessor {

    private static TokenProcessor instance = new TokenProcessor();

    private static long previous;

    protected TokenProcessor() {
    }

    public static TokenProcessor getInstance() {
        return instance;
    }

    public static synchronized String generateToken(String msg, boolean timeChange) {
        try {

            long current = System.currentTimeMillis();
            if (current == previous)
                current++;
            previous = current;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            if (timeChange) {
                // byte now[] = (current+"").toString().getBytes();  
                byte now[] = (new Long(current)).toString().getBytes();
                md.update(now);
            }
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }

        return sb.toString();
    }

    public  static void main(String arg[])
    {
        System.out.println(generateToken("15271581490",false));
        System.out.println(generateToken("15271581491",false));
    }
}  