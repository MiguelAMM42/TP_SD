package ui;

import conexao.Conexao;
import dados.Dados;
import service.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerFechar_UI extends JFrame{
    private JButton fecharServidorButton;
    private JPanel panel1;

    private Dados data;
    private ServerSocket serverSocket;
    private boolean flag = true;


    public ServerFechar_UI(Dados d, ServerSocket ss) throws IOException {
        this.data = d;
        this.serverSocket = ss;


        this.setTitle("Servidor");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        while (flag){
            Socket sock = serverSocket.accept();
            Service service = new Service(new Conexao(sock),data);
            new Thread(service).start();

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //Ainda não sei como mas será para salvar no estado atual
                    //e será para salvar os dados
                    //usar enum do encerrar
                    //aqui tmb fecha
                    flag = false;
                    guardaDados();
                    dispose();
                }
            });

            fecharServidorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //fechar servidor
                    guardaDados();
                }
            });

        }


    }

    public void guardaDados(){
        try {
            File fileOne = new File("db/dados");
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            oos.close();
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}

