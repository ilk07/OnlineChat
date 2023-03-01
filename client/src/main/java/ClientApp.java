import java.io.*;
import java.util.Scanner;

public class ClientApp extends Tuner {
    protected String username = null;

    public ClientApp() {
        super();
    }

    public static void main(String[] args) {

        //получаем настройки
        ClientApp client = new ClientApp();

        //пробуем соединиться с сервером
        try (
                Communicator connection = new Communicator(host, port)
        ) {

            startReceiveMsgThread(connection, client);
            startUserMessageSender(connection, client);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void startReceiveMsgThread(Communicator connection, ClientApp client) {
        new Thread(() -> { //receiveMsg Thread
            while (true) {
                try {
                    Message responseMsg = connection.receiveMsg();
                    if (responseMsg != null) {
                        if (responseMsg.getMessageType().equals(MessageType.EXIT_CHAT)) {
                            break;
                        }

                        switch (responseMsg.getMessageType()) {
                            case REQUEST_USER_NAME:
                                connection.printMsg("Enter your nickname for this chat...");
                                break;
                            case NAME_USED:
                                connection.printMsg("This nickname is busy. Please, choose other...");
                                break;
                            case ACCEPT_USER_NAME:
                                client.setUsername(responseMsg.getMessageText());
                                connection.printMsg("Great! You are in! Write your messages...");
                                break;
                            case USER_TEXT_MESSAGE:
                                connection.printMsg(responseMsg.getSender() + ": " + responseMsg.getMessageText());
                                break;
                            case SYSTEM_MESSAGE:
                            default:
                                connection.printMsg(responseMsg.getMessageText());
                                break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    return;
                }
            }

            Thread.currentThread().interrupt();

        }).start();
    }

    private static void startUserMessageSender(Communicator connection, ClientApp client) {
        while (true) { //sendMsg
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message != null) {
                try {
                    if (message.equals(exitCommand)) {
                        if (client.username != null) {
                            connection.sendMsg(new Message(MessageType.EXIT_CHAT, client.username));
                        } else {
                            connection.sendMsg(new Message(MessageType.EXIT_CHAT, Thread.currentThread().getName()));
                        }
                        break;
                    }

                    if (client.username == null) {
                        connection.sendMsg(new Message(Thread.currentThread().getName(), message, MessageType.USER_NAME));
                    } else {
                        connection.sendMsg(new Message(client.username, message, MessageType.USER_TEXT_MESSAGE));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}