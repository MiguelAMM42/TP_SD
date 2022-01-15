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

        //Algumas infos para o sistema hardcoded; depois tentar adicionar manualmente algumas


        dados.registar("admin","admin123",true);
        dados.registar("utilizador","utilizador123",false);

        dados.addPercurso("Porto","Madrid",120);
        dados.addPercurso("Lisboa","Paris",120);
        dados.addPercurso("Nova Iorque","Miami",210);
        //Viagem viag1 = new Viagem(utilizador1,"viag123");


        //servidor a aceitar pedidos
        while (true){
            Socket sock = ssock.accept();
            Service service = new Service(new Conexao(sock),dados);
            new Thread(service).start();
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




