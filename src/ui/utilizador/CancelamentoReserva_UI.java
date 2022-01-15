package ui.utilizador;

import conexao.Conexao;
import ui.Login_UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class CancelamentoReserva_UI extends JFrame{
    private JPanel panel1;
    private JPanel cancPanel;
    private JLabel txtAviso;
    private JTextField codField;
    private JButton confirmarButton;
    private JButton voltarButton;

    private  String username;
    private Socket socket;
    private Conexao conexao;

    public CancelamentoReserva_UI(Socket sock, Conexao conect, String nome) {
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

        this.setTitle("Cancelar Reserva");
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
                String codViagemString = codField.getText();

            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Utilizador_UI(socket,conexao,username);
                dispose();
            }
        });
    }
}
