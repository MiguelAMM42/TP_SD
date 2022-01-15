package ui;

import conexao.Conexao;
import conexao.Frame;
import dados.Utilizador;
import service.Type;
import ui.admin.Administrador_UI;
import ui.utilizador.Utilizador_UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Login_UI extends JFrame{
    private JPanel panel1;
    private JTextField username;
    private JPasswordField password;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JRadioButton mostrarPasswordRadioButton;
    private JButton confirmarButton;
    private JButton voltarButton;
    private JLabel fail;

    private Socket socket;
    private Conexao conexao;

    public Login_UI(Socket sock, Conexao conect) {

        this.getRootPane().setDefaultButton(confirmarButton);

        this.socket = sock;
        this.conexao = conect;                //not sure se devo dar dispose()

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

        fail.setVisible(false);
        this.setTitle("Login");
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
                login();
                dispose();

            }
        });
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Start_UI(socket,conexao);
                dispose();

            }
        });
        mostrarPasswordRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipMostrarPassword();
            }
        });

    }


    private void login() {
        String nome = username.getText();
        String passwordString = new String(password.getPassword());
        try {
            //enviar ao Servidor, esperar resposta
            //O servidor responde, tendo em conta a exceção de utilizador não existir

            List<byte[]> dataToSend= new ArrayList<>();

            dataToSend.add(nome.getBytes(StandardCharsets.UTF_8));
            dataToSend.add(passwordString.getBytes(StandardCharsets.UTF_8));

            conexao.send(service.Type.Autenticar,nome,dataToSend);


            Frame serveAns = conexao.receive();

            byte[] sucessBytes = serveAns.getDataLst().get(0);
            byte[] typeUserBytes = serveAns.getDataLst().get(1);
            String sucess = new String(sucessBytes);
            String  typeUser = new String(typeUserBytes);
            //System.out.println(ans);

            if (sucess.equals("1")) {
                //teve sucesso

                if(typeUser.equals("admin")){
                    new Administrador_UI(socket,conexao,nome);
                }else{
                    new Utilizador_UI(socket,conexao,nome);
                }
                dispose();

            }else{
                fail.setText("ID ou password inválidos");
                fail.setVisible(true);
                fail.setForeground(Color.red);
                this.pack();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addKeyStrokes() {
        panel1.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void flipMostrarPassword() {
        password.setEchoChar(mostrarPasswordRadioButton.isSelected() ? (char) 0 : '*');
    }

}



