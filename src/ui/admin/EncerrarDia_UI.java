package ui.admin;

import conexao.Conexao;
import ui.Login_UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class EncerrarDia_UI extends JFrame{
    private JPanel panel1;
    private JTextField dia;
    private JTextField mes;
    private JTextField ano;
    private JButton confirmarButton;
    private JButton voltarButton;

    private String username;
    private Socket socket;
    private Conexao conexao;

    public EncerrarDia_UI(Socket sock, Conexao conect, String nome) {
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

        this.setTitle("Encerrar Dia");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void setActions() {
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String diaString = dia.getText();
                String mesString = mes.getText();
                String anoString = ano.getText();

            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Administrador_UI(socket,conexao,username);
                dispose();
            }
        });
    }
}
