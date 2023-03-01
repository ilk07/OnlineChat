import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ServerAppTest{
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
    public static void startAllTests(){
        System.out.println("---START ServerAppTest ---");
    }
    @AfterAll
    public static void endAllTests(){
        System.out.println("---ServerAppTest COMPLETED---");
    }

    @Test
    @DisplayName("ServerApp is Instance of ServerApp Object")
    public void serverAppIsInstanceOfServerAppObjectTest(){
        assertInstanceOf(ServerApp.class, sut);
    }

}
