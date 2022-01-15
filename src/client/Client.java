package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import conexao.Conexao;
import conexao.Frame;
import service.*;
import ui.Start_UI;

import javax.swing.*;


public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);
        Conexao c = new Conexao(socket);

        new Start_UI(socket,c);

        /*

        while(true){

            /*
            System.out.println("CLIENTE FAZ AUTENTICAÇÃO");


            String utilizador = "utilizador";
            String password = "utilizador123";

            List<byte[]> dataAutenticacao = new ArrayList<>();

            dataAutenticacao.add(utilizador.getBytes(StandardCharsets.UTF_8));
            dataAutenticacao.add(password.getBytes(StandardCharsets.UTF_8));

            c.send(Type.Autenticar,utilizador,dataAutenticacao);

            Frame respostaAutenticacao = c.receive();

            for(byte[] parte: respostaAutenticacao.getDataLst()){
                String ans = new String(parte);
                System.out.println(ans);
            }

            System.out.println("\nChega aqui\n");

            c.send(Type.MostrarListaVoo,utilizador,new ArrayList<>());

            Frame respostaLista = c.receive();

            System.out.println("Lista dos percursos");

            for(byte[] parte: respostaLista.getDataLst()){
                String ans = new String(parte);
                System.out.println(ans);
            }

            c.close();

        }*/









        // send requests
        //c.send("Ola".getBytes());
        //c.send("Hola".getBytes());
        //c.send("Hello".getBytes());

        // get replies
        //byte[] b1 = c.receive();
        //byte[] b2 = c.receive();
        //byte[] b3 = c.receive();
        //System.out.println("Some Reply: " + new String(b1));
        //System.out.println("Some Reply: " + new String(b2));
        //System.out.println("Some Reply: " + new String(b3));

    }
}



/*
public class Client {
    private final Socket csock;
    private final Connection connection;
    private ReentrantLock rlClient = new ReentrantLock();
    private BlockingMap replies = new BlockingMap();

    public Client(String server, int port) throws IOException {
        this.csock = new Socket(server,port);
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

    public static void main(String[] args) throws IOException {

        Client cli = new Client("localhost",12345);
        //interpretador
        cli.run();


    }
}*/
