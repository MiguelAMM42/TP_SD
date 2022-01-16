package ui.utilizador;

import conexao.Conexao;
import conexao.Frame;

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

import static java.lang.Integer.parseInt;

public class ListaPercursos_UI extends JFrame{
    private JPanel panel1;
    private JLabel lista;
    private JButton confirmarButton;
    private JLabel ansLabel;
    private JTextField origem;
    private JTextField destino;

    private  String username;
    private Socket socket;
    private Conexao conexao;


    public ListaPercursos_UI(Socket sock, Conexao conect, String nome) {
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



        this.setTitle("Lista Percursos com Escala");
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
                lstPercursos();
            }
        });

    }

    private void lstPercursos(){

        List<byte[]> dataToSend = new ArrayList<>();
        String origemString = origem.getText();
        String destinoString = destino.getText();

        dataToSend.add(origemString.getBytes(StandardCharsets.UTF_8));
        dataToSend.add(destinoString.getBytes(StandardCharsets.UTF_8));

        try {
            lista.setText("");
            ansLabel.setVisible(false);

            conexao.send(service.Type.PercursosPossíveis,username,dataToSend);

            Frame ansReceived  = conexao.receive();
            String sucesso = new String(ansReceived.getDataLst().get(0));

            if(sucesso.equals("0")){
                ansLabel.setText("!!!LISTA VAZIA!!!");
                ansLabel.setVisible(true);
                ansLabel.setForeground(Color.red);
                pack();

            }else{
                int setLength = parseInt(sucesso);

                StringBuilder html = new StringBuilder();

                html.append("<html>\n");
                html.append("<body>\n");

                Frame ansReceived_Set;

                for(int i = 0; i < setLength; i++){

                    ansReceived_Set  = conexao.receive();

                    for(int j = 0; j < ansReceived_Set.getDataLst().size(); j++){

                        if (j == ansReceived_Set.getDataLst().size()-1 && i == setLength-1){
                            html.append(new String(ansReceived_Set.getDataLst().get(j)));
                        }else{
                            if (j == ansReceived_Set.getDataLst().size()-1){
                                html.append(new String(ansReceived_Set.getDataLst().get(j))).append("<br/>");
                            }else{
                                html.append(new String(ansReceived_Set.getDataLst().get(j))).append("--->");

                            }

                        }

                    }



                }



                html.append("</body>\n");
                html.append("</html>");

                String htmlString = html.toString();
                lista.setText(htmlString);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

