import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Tuner {

    protected static String settingsFilePathName = "./service/src/main/resources/settings.txt";

    protected static int port;
    protected static String host;
    protected static String exitCommand;
    protected static String logFilePathName;

    public Tuner() {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(settingsFilePathName);
            property.load(fis);

            logFilePathName = property.getProperty("logFilePathName");

            port = Integer.parseInt(property.getProperty("port"));

            host = property.getProperty("host");

            exitCommand = property.getProperty("exitCommand");

        } catch (IOException e) {
            System.err.println("Error: setting file not found");
        }
    }
}