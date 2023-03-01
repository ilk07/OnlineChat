import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String messageText;
    protected String sender;
    private final MessageType messageType;

    public Message(String sender, String messageText, MessageType messageType) {
        this.messageText = messageText;
        this.sender = sender;
        this.messageType = messageType;
    }

    public Message(MessageType messageType, String sender) {
        this.messageType = messageType;
        this.sender = sender;
    }


    public String getMessageText() {
        return messageText;
    }

    public String getSender() {
        return sender;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageText='" + messageText + '\'' +
                ", sender='" + sender + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}

