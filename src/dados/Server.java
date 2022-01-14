package dados;

import service.Service;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        Service service;
        ServerSocket ssock = new ServerSocket(12345);

        while(true) {
            Socket sock = ssock.accept();
            Session session = new Session(service, sock);
            new Thread(() -> {
                session.serve(); //tratar dos pedidos
            }).start;
        }
    }
}
