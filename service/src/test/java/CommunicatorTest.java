import org.junit.jupiter.api.*;
import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommunicatorTest {


    @BeforeAll
    public static void startAllTests() {
        System.out.println("---START CommunicatorTest ---");
    }

    @AfterAll
    public static void endAllTests() {
        System.out.println("---CommunicatorTest COMPLETED---");
    }

    @Test
    @DisplayName("ServerSocket constructor")
    public void serverSocketCommunicatorConstructorTest() throws IOException {

        Socket socket = mock(Socket.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("ex".getBytes());

        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);

        Communicator connection = mock(Communicator.class);
        when(connection.communicatorName).thenReturn("testCommunicator");
        when(connection.socket).thenReturn(socket);

        String actual = out.toString();
        String expected = "1";

        assertEquals(expected, actual);

    }



}
