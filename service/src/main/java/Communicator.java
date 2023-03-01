import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicator implements Closeable {

    final Socket socket;
    final String communicatorName;
    final ObjectInputStream inputStream;
    final ObjectOutputStream outputStream;
    Logger log = Logger.getInstance();

    public Communicator(String host, int port) { //клиентский сокет
        this.communicatorName = "ClientSocket";
        try {
            this.socket = new Socket(host, port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Communicator(ServerSocket server) { //серверный сокет
        this.communicatorName = "ServerSocket";
        try {
            this.socket = server.accept();
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Message receiveMsg() throws IOException, ClassNotFoundException {
        return (Message) inputStream.readObject();
    }

    public void sendMsg(Message msg) throws IOException {
        outputStream.writeObject(msg);
        log.logMsg(msg);
    }

    public void printMsg(String message) {
        System.out.println(message);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        log.log(communicatorName, " inputStream closed");
        outputStream.close();
        log.log(communicatorName, " outputStream closed");
        socket.close();
        log.log(communicatorName, " socket closed");
    }
}
