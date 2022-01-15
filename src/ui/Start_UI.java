package ui;

import conexao.Conexao;
import dados.Dados;
import service.Type;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Start_UI extends JFrame{
    private JPanel panel1;
    private JButton logInButton;
    private JButton registarButton;
    private JButton encerrarButton;
    private JPanel panelStart;

    private Socket socket;
    private Conexao conexao;



    public Start_UI(Socket sock, Conexao conect) {
        this.socket = sock;
        this.conexao = conect;

        setActions();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Ainda não sei como mas será para salvar no estado atual
                //e será para salvar os dados
                //usar enum do encerrar
                //ln.save();
            }
        });

        this.setTitle("FLY");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void setActions() {
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login_UI(socket,conexao);
                dispose();
            }
        });
        registarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registar_UI(socket,conexao);
                dispose();
            }
        });
        encerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conexao.send(service.Type.Encerrar," ",new ArrayList<>());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

}
