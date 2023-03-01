import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    Message sut;

    @BeforeAll
    public static void startAllTests(){
        System.out.println("---START Message TESTS---");
    }

    @AfterAll
    public static void endAllTests(){
        System.out.println("---Message TESTS COMPLETED---");
    }

    @AfterEach
    void afterTest(TestInfo testInfo) {
        System.out.println("Completed test: " + '"' +  testInfo.getDisplayName() +'"');
    }

    @Test
    @DisplayName("Message construct with sender + message text + type is Instance of Message Object")
    public void messageWithSenderTextTypeTest(){
        sut = new Message("Sender","MessageText", MessageType.SYSTEM_MESSAGE);
        assertInstanceOf(Message.class, sut);
    }

    @Test
    @DisplayName("Message construct with type + sender  is Instance of Message Object")
    public void messageWithSenderAndTypeTest(){
        sut = new Message(MessageType.SYSTEM_MESSAGE, "Sender");
        assertInstanceOf(Message.class, sut);
    }

    @Test
    @DisplayName("Get message from Message Object")
    public void getMessageTextTest(){
        sut = new Message("Sender","MessageText", MessageType.USER_NAME);

        //actual
        String actual = sut.getMessageText();

        //expected
        String expected = "MessageText";

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get sender from Message Object")
    public void getSenderTest(){
        sut = new Message("Sender","MessageText", MessageType.USER_NAME);

        //actual
        String actual = sut.getSender();

        //expected
        String expected = "Sender";

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get type of message from Message Object")
    public void getMessageTypeTest(){
        sut = new Message("Sender","MessageText", MessageType.USER_TEXT_MESSAGE);

        //actual
        MessageType actual = sut.getMessageType();

        //expected
        MessageType expected = MessageType.USER_TEXT_MESSAGE;

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test text view of the Message Object")
    public void messageToStringTest(){
        sut = new Message("Sender","MessageText", MessageType.USER_TEXT_MESSAGE);

        //actual
        String actual = sut.toString();

        //expected
        String expected = "Message{messageText='MessageText', sender='Sender', messageType=USER_TEXT_MESSAGE}";

        //then
        assertEquals(expected, actual);
    }

}
