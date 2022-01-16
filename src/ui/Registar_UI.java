package ui;

import conexao.Conexao;
import conexao.Frame;
import service.Type;
import ui.admin.Administrador_UI;
import ui.utilizador.Utilizador_UI;

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

public class Registar_UI extends JFrame{
    private JPanel panel1;
    private JTextField nomeField;
    private JTextField passwordField;
    private JLabel nomeLabel;
    private JLabel passLabel;
    private JButton confirmarButton;
    private JLabel fail;

    private Socket socket;
    private Conexao conexao;

    public Registar_UI(Socket sock, Conexao conect) {
        this.socket = sock;
        this.conexao = conect;

        fail.setVisible(false);

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

        this.setTitle("Registar Utilizador");
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
                registar();
                //quando tem sucesso faz dispose

            }
        });
    }

    private void registar() {
        String nome = nomeField.getText();
        String passwordString = passwordField.getText();
        try {
            //enviar ao Servidor, esperar resposta
            //O servidor responde, tendo em conta a exceção de utilizador não existir

            List<byte[]> dataToSend= new ArrayList<>();

            dataToSend.add(nome.getBytes(StandardCharsets.UTF_8));
            dataToSend.add(passwordString.getBytes(StandardCharsets.UTF_8));

            conexao.send(service.Type.Registar,nome,dataToSend);


            Frame serveAns = conexao.receive();

            byte[] sucessBytes = serveAns.getDataLst().get(0);
            String sucess = new String(sucessBytes);

            //System.out.println(ans);

            if (sucess.equals("1")) {
                //teve sucesso
                JOptionPane.showMessageDialog(this,
                        "Utilizador registado com sucesso!",
                        "REGISTO",
                        JOptionPane.PLAIN_MESSAGE);

                new Start_UI(socket,conexao);
                dispose();

            }else{
                fail.setText("Utilizador já existente");
                fail.setForeground(Color.red);
                fail.setVisible(true);
                this.pack();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
