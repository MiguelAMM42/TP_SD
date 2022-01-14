package dados;

import conexao.Conexao;
import service.Service;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        Dados dados = new Dados();
        //Service service;
        ServerSocket ssock = new ServerSocket(12345);

        Utilizador admin = new Utilizador("admin","admin123",true);

        while (true){
            Socket sock = ssock.accept();
            new Thread(new Service(new Conexao(sock),dados)).start();
        }

        /*
        while(true) {
            Socket sock = ssock.accept();
            Session session = new Session(service, sock);
                try {
                    session.serve(); //tratar dos pedidos
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }*/
    }
}




