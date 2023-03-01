import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    protected String logFileName;
    private static Logger INSTANCE = null;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    //private static Logger logger;

    private Logger() {
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger();
                }
            }
        }
        return INSTANCE;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    void logMessageToFile(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFileName, true))) {
            bw.write(message);
            bw.write('\n');
            bw.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void logMsg(Message msg) {
        logMessageToFile(LocalDateTime.now().format(format) + " [#" + msg.toString() + "]");
    }

    public void log(String user, String msg) {
        logMessageToFile(LocalDateTime.now().format(format) + " [#" + user + " : " + msg + "]");
    }
}