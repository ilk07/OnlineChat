import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {
    Logger sut; //system under test

    @BeforeAll
    public static void startAllTests() {
        System.out.println("---START LoggerTest TESTS---");
    }

    @AfterAll
    public static void endAllTests() {
        System.out.println("---LoggerTest TESTS COMPLETED---");
    }

    @AfterEach
    void afterTest(TestInfo testInfo) {
        System.out.println("Completed test: " + '"' + testInfo.getDisplayName() + '"');
    }

    //Logger log = Logger.getInstance();
    @BeforeEach
    public void initOneTest() {
        sut = Logger.getInstance();
    }

    @Test
    @DisplayName("Logger is Instance of Logger Object")
    public void loggerTest() {

        assertInstanceOf(Logger.class, sut);
    }

    @Test
    @DisplayName("Logger set correct logfile name")
    public void setLogFileNameTest() {
        sut.setLogFileName("test.log");

        String actual = sut.logFileName;

        String expected = "test.log";

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Logger save correct string to file")
    public void logMessageToFileTest() {

        sut.setLogFileName("testLogMessage.log");

        String expected = "test message";
        sut.logMessageToFile(expected);

        String actual = "";

        try (BufferedReader br = new BufferedReader(new FileReader("testLogMessage.log"))) {
            String s;
            while ((s = br.readLine()) != null) {
                actual += (s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        var testLogFile = new File("testLogMessage.log");
        testLogFile.delete();

        assertEquals(expected, actual);


    }

    @Test
    @DisplayName("Logger create correct string to save from message Object")
    public void logMsgTest() {
        sut.setLogFileName("testLogMessageFormat.log");

        Message msg = new Message("Sender", "MessageText", MessageType.USER_TEXT_MESSAGE);
        sut.logMsg(msg);

        String expected = "[#Message{messageText='MessageText', sender='Sender', messageType=USER_TEXT_MESSAGE}]";

        String actual = "";
        try (BufferedReader br = new BufferedReader(new FileReader("testLogMessageFormat.log"))) {
            String s;
            while ((s = br.readLine()) != null) {
                actual += (s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        var testLogFile = new File("testLogMessageFormat.log");
        testLogFile.delete();

        assertTrue(actual.contains(expected));
    }


    @Test
    @DisplayName("Logger create correct log line from input strings")
    public void loggerLogTest() {

        sut.setLogFileName("testLogMessageFromStrings.log");
        sut.log("SENDER", "CONTENT");

        String expected = "[#SENDER : CONTENT]";

        String actual = "";
        try (BufferedReader br = new BufferedReader(new FileReader("testLogMessageFromStrings.log"))) {
            String s;
            while ((s = br.readLine()) != null) {
                actual += (s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        var testLogFile = new File("testLogMessageFromStrings.log");
        testLogFile.delete();

        assertTrue(actual.contains(expected));
    }
}
