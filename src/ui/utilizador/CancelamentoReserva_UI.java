package ui.utilizador;

import conexao.Conexao;
import conexao.Frame;
import service.Type;
import ui.Login_UI;
import ui.admin.Administrador_UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CancelamentoReserva_UI extends JFrame{
    private JPanel panel1;
    private JPanel cancPanel;
    private JLabel txtAviso;
    private JTextField codField;
    private JButton confirmarButton;
    private JButton voltarButton;
    private JLabel ansLabel;

    private  String username;
    private Socket socket;
    private Conexao conexao;

    public CancelamentoReserva_UI(Socket sock, Conexao conect, String nome) {
        this.username = nome;
        this.socket = sock;
        this.conexao = conect;

        setActions();

        ansLabel.setVisible(false);

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
                cancelarReserva();
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

    private void cancelarReserva(){
        String codViagemString = codField.getText();
        List<byte[]> dataToSend = new ArrayList<>();
        dataToSend.add(codViagemString.getBytes(StandardCharsets.UTF_8));


        try {
            conexao.send(service.Type.CancelamentoVoo,username,dataToSend);

            Frame ansReceived  = conexao.receive();
            String sucesso = new String(ansReceived.getDataLst().get(0));

            if(sucesso.equals("1")){
                JOptionPane.showMessageDialog(this,
                        "Viagem cancelada com sucesso!",
                        "CANCELADO",
                        JOptionPane.PLAIN_MESSAGE);
                new Utilizador_UI(socket,conexao,username);
                dispose();


            }else{
                ansLabel.setText("Falha no cancelamento da reserva: código inválido!");
                ansLabel.setVisible(true);
                ansLabel.setForeground(Color.red);
                pack();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
