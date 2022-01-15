package ui.admin;

import conexao.Conexao;
import ui.Login_UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class Administrador_UI extends JFrame{
    private JPanel panel1;
    private JPanel panelAdministrador;
    private JButton inserirVoo;
    private JButton encerrarDia;
    private JButton logOut;
    private JButton encerrar;

    private String username;
    private Socket socket;
    private Conexao conexao;

    public Administrador_UI(Socket sock, Conexao conect, String nome) {
        this.username = nome;
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

        this.setTitle("Menu Administrador");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void setActions() {
        inserirVoo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new InserirVoo_UI(socket,conexao,username);
                dispose();
            }
        });
        encerrarDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EncerrarDia_UI(socket,conexao,username);
                dispose();

            }
        });
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login_UI(socket,conexao);
                dispose();
            }
        });
        encerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}

