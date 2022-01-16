package ui.utilizador;

import conexao.Conexao;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.net.Socket;

public class CitieCard_UI extends JPanel{
    private JTextField nomeCidade;
    private JPanel panel1;
    private JPanel panel_1;


    public CitieCard_UI(String numCity) {

        this.setLayout(new CardLayout());

        panel_1.setLayout(new BoxLayout(panel_1,BoxLayout.Y_AXIS));

        Border b = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createTitledBorder(b, "Cidade " + numCity));

        this.nomeCidade.setVisible(true);

        this.add(panel_1);
    }

    public String getCidadeName(){
        return nomeCidade.getText();
    }
}


