package ui.utilizador;

import conexao.Conexao;
import conexao.Frame;
import ui.Login_UI;
import ui.Registar_UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Utilizador_UI extends JFrame{
    private JPanel panel1;
    private JPanel panelAdministrador;
    private JButton reservaViagem;
    private JButton cancelamentoReserva;
    private JButton lstVoos;
    private JButton lstPercursos;
    private JButton logoutButton;
    private JButton sairButton;

    private  String username;
    private Socket socket;
    private Conexao conexao;

    public Utilizador_UI(Socket sock, Conexao conect, String nome) {
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
                try {
                    conexao.send(service.Type.Encerrar,"fechar",new ArrayList<>());

                    Frame received = conexao.receive();

                    conexao.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        this.setTitle("Menu Utilizador");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void setActions() {
        reservaViagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReservaViagem_UI(socket,conexao,username);
                dispose();

            }
        });
        cancelamentoReserva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CancelamentoReserva_UI(socket,conexao,username);
                dispose();

            }
        });
        lstVoos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListaVoosDiretos_UI(socket,conexao,username);
                dispose();

            }
        });
        lstPercursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListaPercursos_UI(socket,conexao,username);
                dispose();



            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login_UI(socket,conexao);
                dispose();

            }
        });
    }
}
