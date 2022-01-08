import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private final Socket csock;
    private final Connection connection;
    private ReentrantLock rlClient = new ReentrantLock();
    private BlockingMap replies = new BlockingMap();

    public Client(InetAddress server, int port) throws IOException {
        this.csock = new Socket(server, port);
        this.connection = new Connection(csock);
    }

    private void run() {
        while(true) {
            var reply = (Service.Reply)connection.recieve();
            replies.put(reply);
        }
    }

    public Service.Reply service(Service.Request request) throws Exception {
        try {
            rlClient.lock();
            connection.send(request);
        }
        finally {
            rlClient.unlock();
        }

        return replies.get(request.getID());
    }

    public static void main(String[] args) {

        Client(,12345);
        //interpretador

    }
}
