package dados;

import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import Connection;
import service.Service;

public class Session {
    private final Service service;
    private final Socket sock;
    private final ReentrantLock rlSession = new ReentrantLock();

    public Session (Service service, Socket sock) {
        this.service = service;
        this.sock = sock;
    }

    public void serve() throws Exception {
        Connection connection = new Connection(sock);
        var req = connection.receive();

        while(req != null) {
            new Thread(()->{
                var rep = service.execute(req);
                try{
                    rlSession.lock();
                    ser.send(rep);
                }
                finally {
                    rlSession.unlock();
                }
            }).start();
        }

        sock.close();
    }
}
