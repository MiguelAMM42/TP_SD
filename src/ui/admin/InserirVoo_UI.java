package ui.admin;

import conexao.Conexao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class InserirVoo_UI extends JFrame{
    private JPanel panel1;
    private JTextField origemField;
    private JTextField destinoField;
    private JTextField capacidadeField;
    private JButton confirmarButton;
    private JButton voltarButton;

    private String username;
    private Socket socket;
    private Conexao conexao;

    public InserirVoo_UI(Socket sock, Conexao conect, String nome) {
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

        this.setTitle("Inserir Voo");
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
                String origemString = origemField.getText();
                String destinoString = destinoField.getText();
                String capacidadeString = capacidadeField.getText();;

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
