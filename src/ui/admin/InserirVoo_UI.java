package ui.admin;

import conexao.Conexao;
import conexao.Frame;
import service.Type;

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

public class InserirVoo_UI extends JFrame{
    private JPanel panel1;
    private JTextField origemField;
    private JTextField destinoField;
    private JTextField capacidadeField;
    private JButton confirmarButton;
    private JButton voltarButton;
    private JLabel ansLabel;

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
                adicionar();
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

    private void adicionar(){
        String origemString = origemField.getText();
        String destinoString = destinoField.getText();
        String capacidadeString = capacidadeField.getText();

        List<byte[]> dataToSend = new ArrayList<>();
        dataToSend.add(origemString.getBytes(StandardCharsets.UTF_8));
        dataToSend.add(destinoString.getBytes(StandardCharsets.UTF_8));
        dataToSend.add(capacidadeString.getBytes(StandardCharsets.UTF_8));


        try {
            conexao.send(service.Type.AdicionarVoo,username,dataToSend);

            //recebe resposta
            Frame ans = conexao.receive();
            String sucesso = new String(ans.getDataLst().get(0));
            if(sucesso.equals("1")){
                //sucesso
                //mudar para box de mensagem
                JOptionPane.showMessageDialog(this,
                        "Voo adicionado com sucesso!",
                        "ADICIONADO",
                        JOptionPane.PLAIN_MESSAGE);
                new Administrador_UI(socket,conexao,username);
                dispose();

            }else{
                String erro = new String(ans.getDataLst().get(1));
                if(erro.equals("0")) {
                    ansLabel.setText("Falha na adição do voo: capacidade inválida!");
                    ansLabel.setVisible(true);
                    ansLabel.setForeground(Color.red);
                    pack();
                }
                if(erro.equals("1")) {
                    ansLabel.setText("Falha na adição do voo: Voo já existente!");
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
