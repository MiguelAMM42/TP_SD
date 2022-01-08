import service.Service;

import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws Exception {
        Service service = new Service();
        ServerSocket ssock = new ServerSocket(12345);

        while(true) {
            var sock = ssock.accept();
            var session = new Session(service, sock);
            new Thread(() -> {
                session.serve(); //tratar dos pedidos
            }).start;
        }
    }
}
