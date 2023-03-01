import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ServerAppTest {
    @Mock
    ServerApp sut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        ServerApp.port = 1010;
        ServerApp.host = "127.0.0.1";
        ServerApp.exitCommand = "quit";
        ServerApp.logFilePathName = "logFilePathName.log";
    }

    @BeforeAll
    public static void startAllTests() {
        System.out.println("---START ServerAppTest ---");
    }

    @AfterAll
    public static void endAllTests() {
        System.out.println("---ServerAppTest COMPLETED---");
    }

    @AfterEach
    void afterTest(TestInfo testInfo) {
        System.out.println("Completed test: " + '"' + testInfo.getDisplayName() + '"');
    }

    @Test
    @DisplayName("ServerApp is Instance of ServerApp Object")
    public void serverAppIsInstanceOfServerAppObjectTest() {
        assertInstanceOf(ServerApp.class, sut);
    }

    @Test
    @DisplayName("ServerApp BlockingQueue add message")
    public void serverAppBlockingQueueAddMessageTest() throws InterruptedException {

        ServerApp.messages.put(new Message("Anna", "AnnaFirstMessageContent", MessageType.USER_TEXT_MESSAGE));
        ServerApp.messages.put(new Message("Anna", "AnnaSecondMessageContent", MessageType.USER_TEXT_MESSAGE));
        ServerApp.messages.put(new Message("Petr", "PetrFirstMessageContent", MessageType.USER_TEXT_MESSAGE));

        int expected = 3;
        int actual = ServerApp.messages.size();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ServerApp BlockingQueue take message")
    public void serverAppBlockingQueueTakeMessageTest() throws InterruptedException {

        ServerApp.messages.put(new Message("Anna", "AnnaFirstMessageContent", MessageType.USER_TEXT_MESSAGE));
        ServerApp.messages.put(new Message("Anna", "AnnaSecondMessageContent", MessageType.USER_TEXT_MESSAGE));
        ServerApp.messages.put(new Message("Petr", "PetrFirstMessageContent", MessageType.USER_TEXT_MESSAGE));


        //Берём сообщение из очереди
        while (!ServerApp.messages.isEmpty()) {
            ServerApp.messages.take();
        }

        int expected = 0;
        int actual = ServerApp.messages.size();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ServerApp available username to add in connection map")
    public void usernameIsAvailableConnectionMapTest() {

        Communicator connection = Mockito.mock(Communicator.class);
        Map<String, Communicator> userConnections = new ConcurrentHashMap<>();
        userConnections.put("Anna Montana", connection);

        int expected = 1;
        int actual = userConnections.size();

        assertEquals(expected, actual);
    }

}
