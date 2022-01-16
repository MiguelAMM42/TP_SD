package ui.utilizador;

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

public class ListaVoosDiretos_UI extends JFrame{
    private JPanel panel1;
    private JButton confirmarButton;
    private JLabel ansLabel;
    private JLabel lista;

    private  String username;
    private Socket socket;
    private Conexao conexao;

    public ListaVoosDiretos_UI(Socket sock, Conexao conect, String nome) {
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
            }
        });

        lstVoosDiretos();

        this.setTitle("Lista Voos Diretos");
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
                new Utilizador_UI(socket,conexao,username);
                dispose();
            }
        });
    }

    private void lstVoosDiretos(){

        List<byte[]> dataToSend = new ArrayList<>();



        try {
            conexao.send(service.Type.MostrarListaVoo,username,dataToSend);

            Frame ansReceived  = conexao.receive();
            String sucesso = new String(ansReceived.getDataLst().get(0));

            if(sucesso.equals("1")){
                List<byte[]> sublist = ansReceived.getDataLst().subList(1, ansReceived.getDataLst().size());
                this.lista.setText(toHTMLListaVoosDiretos(sublist));



            }else{
                ansLabel.setText("!!!LISTA VAZIA!!!");
                ansLabel.setVisible(true);
                ansLabel.setForeground(Color.red);
                pack();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String toHTMLListaVoosDiretos(List<byte[]> lstBytes){

        StringBuilder html = new StringBuilder();

        html.append("<html>\n");
        html.append("<body>\n");

        for(byte[] voo : lstBytes.subList(0, lstBytes.size()-1)) {
            html.append(new String(voo)).append("<br/>");
        }

        html.append(new String(lstBytes.get(lstBytes.size()-1)));

        html.append("</body>\n");
        html.append("</html>");


        String htmlString = html.toString();


        return htmlString;
    }


}
