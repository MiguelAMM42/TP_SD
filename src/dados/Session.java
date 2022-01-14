package dados;


import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import service.Service;
import conexao.Conexao;

public class Session {
    private final Service service;
    private final Socket sock;
    private final ReentrantLock rlSession = new ReentrantLock();

    public Session (Service service, Socket sock) {
        this.service = service;
        this.sock = sock;
    }

    public void serve() throws Exception {
        Conexao connection = new Conexao(sock);

            while (true) {
                var req = connection.receive();
                if (req != null) {
                    var rep = service.execute(req);
                    try {
                        rlSession.lock();
                        connection.send(rep);
                    } finally {
                        rlSession.unlock();
                    }
                }
            }

        sock.close();
    }
}
