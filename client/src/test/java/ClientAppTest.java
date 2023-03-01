import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ClientAppTest{
    @Mock
    Tuner tuner;

    ClientApp sut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        Tuner.port = 1010;
        Tuner.host = "127.0.0.1";
        Tuner.exitCommand = "quit";
        Tuner.logFilePathName = "logFilePathName.log";
        sut = new ClientApp();
    }

    @BeforeAll
    public static void startAllTests(){
        System.out.println("---START ClientAppTest ---");
    }
    @AfterAll
    public static void endAllTests(){
        System.out.println("---ClientAppTest COMPLETED---");
    }

    @Test
    @DisplayName("ClientAppTest is Instance of ClientApp Object")
    public void clientAppIsInstanceOfClientAppTest(){
        assertInstanceOf(ClientApp.class, sut);
    }

    @Test
    @DisplayName("Set ClientApp username")
    public void setClientAppUserNameTest(){
        sut.setUsername("newUserName");
        String expected = "newUserName";
        String actual = sut.getUsername();

        assertEquals(expected, actual);
    }




}
