package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import conexao.Conexao;
import conexao.Frame;
import service.*;
import ui.Start_UI;

import javax.swing.*;


public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);
        Conexao c = new Conexao(socket);

        new Start_UI(socket,c);

    }
}



