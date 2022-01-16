package ui.utilizador;

import conexao.Conexao;
import conexao.Frame;
import service.Type;
import ui.admin.Administrador_UI;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
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
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class ReservaViagem_UI extends JFrame{
    private JPanel panel1;
    private JTextField diaInicio;
    private JTextField mesInicio;
    private JTextField anoInicio;
    private JTextField diaFim;
    private JTextField mesFim;
    private JTextField anoFim;
    private JTextField numEscalas;
    private JButton confirmarOpcoesButton;
    private JPanel cities;
    private JButton confirmarReservaButton;
    private JButton voltarButton;
    private JLabel ansLabel;
    private JLabel numEscalasLabel;

    private  String username;
    private Socket socket;
    private Conexao conexao;
    private List<CitieCard_UI> cities_Input;


    public ReservaViagem_UI(Socket sock, Conexao conect, String nome) {
        this.username = nome;
        this.socket = sock;
        this.conexao = conect;

        setActions();

        ansLabel.setVisible(false);
        numEscalasLabel.setVisible(false);

        cities.setLayout(new BoxLayout(cities,BoxLayout.Y_AXIS));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Ainda não sei como mas será para salvar no estado atual
                //e será para salvar os dados
                //usar enum do encerrar
                //ln.save();
            }
        });



        this.setTitle("Reserva de Viagem");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void setActions() {
        confirmarOpcoesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fazer ifs
                //envia logo cenas para o servidor
                List<byte[]> sendNumEscalas = new ArrayList<>();

                sendNumEscalas.add("0".getBytes(StandardCharsets.UTF_8));
                sendNumEscalas.add("100".getBytes(StandardCharsets.UTF_8));

                try {
                    conexao.send(service.Type.ReservaVoo,username,sendNumEscalas);

                    Frame ans = conexao.receive();
                    String ansString0 = new String(ans.getDataLst().get(0));
                    String ansString1 = new String(ans.getDataLst().get(1));
                    if (!ansString0.equals("0") || !ansString1.equals("100")){
                        numEscalas.setText("Formato de número de escalas inválido");
                        numEscalasLabel.setVisible(true);
                        numEscalasLabel.setForeground(Color.red);
                        pack();

                    }else{
                        cities_Input = createCitiesLabel(numEscalas.getText());

                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        confirmarReservaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ansLabel.setVisible(false);
                numEscalasLabel.setVisible(false);

                try {
                    if(confirmarReserva()){
                        new Utilizador_UI(socket,conexao,username);
                        dispose();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


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


    private List<CitieCard_UI> createCitiesLabel(String numCitiesString){

        List<CitieCard_UI> lst = new ArrayList<>();

        cities.removeAll();

        int numCities = parseInt(numCitiesString) + 2;

        for(int i=0; i < numCities; i++){
            CitieCard_UI city =  new CitieCard_UI(String.valueOf(i+1));
            cities.add(city);
            cities.add(Box.createRigidArea(new Dimension(0,10)));
            lst.add(city);

        }


        return lst;
    }

    private boolean confirmarReserva() throws IOException {

        boolean reservado=false;

        List<byte[]> sendInfos = new ArrayList<>();

        sendInfos.add("1".getBytes(StandardCharsets.UTF_8));
        sendInfos.add(diaInicio.getText().getBytes(StandardCharsets.UTF_8));
        sendInfos.add(mesInicio.getText().getBytes(StandardCharsets.UTF_8));
        sendInfos.add(anoInicio.getText().getBytes(StandardCharsets.UTF_8));
        sendInfos.add(diaFim.getText().getBytes(StandardCharsets.UTF_8));
        sendInfos.add(mesFim.getText().getBytes(StandardCharsets.UTF_8));
        sendInfos.add(anoFim.getText().getBytes(StandardCharsets.UTF_8));

        for (CitieCard_UI c : cities_Input) {

            sendInfos.add(c.getCidadeName().getBytes(StandardCharsets.UTF_8));

        }

        conexao.send(service.Type.ReservaVoo,username,sendInfos);

        Frame f = conexao.receive();

        String ans_reserva1 = new String(f.getDataLst().get(0));

        if (ans_reserva1.equals("0")){
            String ans_reserva2 = new String(f.getDataLst().get(1));
            if (ans_reserva2.equals("0")) {
                ansLabel.setText("Reserva não confirmada: Formato de data inválida!");
                ansLabel.setVisible(true);
                ansLabel.setForeground(Color.red);
                pack();

            }else{
                ansLabel.setText("Reserva não confirmada: Não há reservas diponíveis para o intervalo selecionado!");
                ansLabel.setVisible(true);
                ansLabel.setForeground(Color.red);
                pack();

            }

        }else{
            JOptionPane.showMessageDialog(this,
                    "Reserva feita com sucesso! CÓDIGO RESERVA: "+ ans_reserva1,
                    "RESERVA",
                    JOptionPane.PLAIN_MESSAGE);
            reservado = true;
            System.out.println(ans_reserva1+"\n");
        }





        return reservado;

    }
}
