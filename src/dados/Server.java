package dados;

import conexao.Conexao;
import service.Service;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        Dados dados = Service.carregaDados();
        //Service service;
        ServerSocket ssock = new ServerSocket(12345);

        //Algumas infos para o sistema hardcoded; depois tentar adicionar manualmente algumas


        dados.registar("admin","admin123",true);
        dados.registar("a","a",false);

        dados.addPercurso("Porto","Madrid",120);
        dados.addPercurso("Lisboa","Paris",120);
        dados.addPercurso("Porto","Miami",210);
        dados.addPercurso("Lisboa","Miami",220);
        dados.addPercurso("Nova Iorque","São Francisco",210);
        dados.addPercurso("Miami","Tokyo",190);
        dados.addPercurso("Nova Iorque","Miami",160);
        dados.addPercurso("Zurique","Oslo",210);
        dados.addPercurso("Roma","Sofia",210);
        dados.addPercurso("Paris","Roma",180);
        dados.addPercurso("Viseu","Londres",195);
        dados.addPercurso("Porto","Nova Iorque",210);
        dados.addPercurso("Leiria","Roterdão",110);

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




