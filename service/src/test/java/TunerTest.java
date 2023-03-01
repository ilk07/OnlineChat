import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TunerTest {

    @Mock
    Tuner sut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        Tuner.port = 1010;
        Tuner.host = "127.0.0.1";
        Tuner.exitCommand = "quit";
        Tuner.logFilePathName = "logFilePathName.log";
    }

    @Test
    @DisplayName("Tuner is Instance of Tuner Object")
    public void tunerConstructorTest(){
        assertInstanceOf(Tuner.class, sut);
    }

    @Test
    @DisplayName("Tuner port setting")
    public void tunerPortTest(){

        int actual = Tuner.port;
        int expected = 1010;

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Tuner host setting")
    public void tunerHostTest(){

        String actual = Tuner.host;
        String expected = "127.0.0.1";

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Tuner exit command setting")
    public void tunerExitCommandTest(){

        String actual = Tuner.exitCommand;
        String expected = "quit";

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Tuner log file path setting")
    public void tunerlogFilePathNameTest(){

        String actual = Tuner.logFilePathName;
        String expected = "logFilePathName.log";

        //then
        assertEquals(expected, actual);
    }
}
