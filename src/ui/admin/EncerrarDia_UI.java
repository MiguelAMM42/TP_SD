package ui.admin;

import conexao.Conexao;
import conexao.Frame;
import service.Type;
import ui.Login_UI;

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

public class EncerrarDia_UI extends JFrame{
    private JPanel panel1;
    private JTextField dia;
    private JTextField mes;
    private JTextField ano;
    private JButton confirmarButton;
    private JButton voltarButton;
    private JLabel ansLabel;

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
                try {
                    conexao.send(service.Type.Encerrar,"fechar",new ArrayList<>());

                    Frame received = conexao.receive();

                    conexao.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        ansLabel.setVisible(false);
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
               encerrar();

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

    private void encerrar(){

        String diaString = dia.getText();
        String mesString = mes.getText();
        String anoString = ano.getText();

        List<byte[]> dataToSend = new ArrayList<>();

        dataToSend.add(diaString.getBytes(StandardCharsets.UTF_8));
        dataToSend.add(mesString.getBytes(StandardCharsets.UTF_8));
        dataToSend.add(anoString.getBytes(StandardCharsets.UTF_8));

        try {

            conexao.send(service.Type.EncerrarDia,username,dataToSend);
            Frame ansReceived  = conexao.receive();
            String sucesso = new String(ansReceived.getDataLst().get(0));

            if(sucesso.equals("1")){
                JOptionPane.showMessageDialog(this,
                        "Dia encerrado com sucesso!",
                        "ENCERRADO",
                        JOptionPane.PLAIN_MESSAGE);
                new Administrador_UI(socket,conexao,username);
                dispose();


            }else{
                String erro = new String(ansReceived.getDataLst().get(1));
                if(erro.equals("0")){
                    ansLabel.setText("Falha no encerramento de dia: dia não existente!");
                    ansLabel.setVisible(true);
                    ansLabel.setForeground(Color.red);
                    pack();
                }else{
                    ansLabel.setText("Falha no encerramento de dia: formato de dia inválido!");
                    ansLabel.setVisible(true);
                    ansLabel.setForeground(Color.red);
                    pack();
                }


            }



        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
