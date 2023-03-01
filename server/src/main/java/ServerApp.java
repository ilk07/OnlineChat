import java.io.*;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ServerApp extends Tuner {
    private static final String serverName = "Server";

    public static final Map<String, Communicator> userConnections = new ConcurrentHashMap<>();
    public static final BlockingQueue<Message> messages = new ArrayBlockingQueue<>(100);
    private static final Logger log = Logger.getInstance();

    public ServerApp() {
        super();
    }

    public static void main(String[] args) {

        //получаем настройки
        new ServerApp();

        //запускаем сервер
        try (ServerSocket server = new ServerSocket(port)) {

            log.log(serverName, "server started!");
            startMessageSenderThread();
            startCommunicatorListener(server);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startCommunicatorListener(ServerSocket server) {
        while (true) {

            //ждем подключения клиента
            Communicator connection = new Communicator(server);
            log.log(serverName, "accept new connection");

            //создаём отдельный поток для клиента
            new Thread(() -> {

                //пишем в лог старт потока
                log.log(serverName, "started a separate thread for the client " + Thread.currentThread().getName());

                messenger(connection);

                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.log(serverName, "start interruption in thread " + Thread.currentThread().getName());
                Thread.currentThread().interrupt();

            }).start();

        }
    }

    private static void startMessageSenderThread() {
        //создаем поток для отправки сообщений в чат
        new Thread(() -> {
            log.log(serverName, "the thread of sending messages to the chat has started " + Thread.currentThread().getName());
            while (true) {
                try {
                    //Берём сообщение из очереди
                    Message message = messages.take();

                    //в цикле идём по активным соединениям и отправляем в соответствии с типом сообщения

                    for (Map.Entry<String, Communicator> entry : userConnections.entrySet()) {
                        entry.getValue().sendMsgFromQueue(message);
                    }

                } catch (InterruptedException | IOException e) {
                    return;
                }
            }
        }).start();
    }

    private static void messenger(Communicator connection) {
        log.log(serverName, "Started a conversation with a client in thread " + Thread.currentThread().getName());

        try {
            //запрашиваем имя пользователя
            connection.sendMsg(new Message(MessageType.REQUEST_USER_NAME, serverName));
            while (true) {
                Message responseMsg = connection.receiveMsg();

                if (responseMsg.getMessageType().equals(MessageType.EXIT_CHAT)) {

                    if (responseMsg.getSender() != null) {
                        log.log(responseMsg.getSender(), "try to exit from chat");
                        userConnections.remove(responseMsg.getSender());
                        //messages.put(new Message(serverName, "User " + responseMsg.getSender() + "leave chat", MessageType.SYSTEM_MESSAGE));
                    } else {
                        log.log("user", "try to exit from chat");
                    }

                    //прерываем цикл
                    break;
                }

                switch (responseMsg.getMessageType()) {
                    case USER_NAME:
                        if (usernameIsAvailable(responseMsg.getMessageText())) {
                            //добавляем в userConnections!
                            userConnections.put(responseMsg.getMessageText(), connection);

                            //отвечаем клиенту, что всё ок
                            connection.sendMsg(new Message(
                                    serverName,
                                    responseMsg.getMessageText(),
                                    MessageType.ACCEPT_USER_NAME)
                            );

                            //добавляем сообщение, что новый юзер в чате
                            messages.put(new Message(serverName, "New user " + responseMsg.getMessageText() + " added to chat!", MessageType.SYSTEM_MESSAGE));
                        } else {
                            connection.sendMsg(new Message(MessageType.NAME_USED, serverName));
                        }
                        break;
                    case SYSTEM_MESSAGE:
                    case USER_TEXT_MESSAGE:
                    default:
                        messages.put(responseMsg);
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    private static boolean usernameIsAvailable(String username) {
        return !userConnections.containsKey(username);
    }
}
