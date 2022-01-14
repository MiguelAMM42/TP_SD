package service;

import conexao.Conexao;
import conexao.Frame;
import dados.Dados;
import dados.PairOrigemDestino;
import dados.Percurso;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



public class Service implements Runnable {
    private final Conexao conexao;
    private final Dados dados;
    private String username;
    private boolean loggedIn;
    private boolean online;
    private boolean isAdmin;

    public Service(Conexao conexao, Dados dados) {
        this.conexao = conexao;
        this.dados = dados;
        this.username = null;
        this.loggedIn = false;
        this.online = true;
    }

    public Conexao getConexao() {
        return conexao;
    }

    public Dados getDados() {
        return dados;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public void run() {
        try {
            while (this.online) {
                Frame command = this.conexao.receive();
                List<byte[]> data = new ArrayList<>(); //data a enviar no frame de resposta
                try {
                    switch (command.tagOp) {
                        case Autenticar -> autenticar(data);
                        case Registar -> registar(data)    ;
                        case AdicionarVoo ->  adicionarVoo(data); ;
                        case CancelamentoVoo -> // ;
                        case EncerrarDia -> // ;
                        case ReservaVoo -> // ;
                        case MostrarListaVoo -> //  ;
                        case LogOut -> logOut(); ;
                        case Encerrar -> encerrarService() // ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autenticar(List<byte[]> data){
        String nome = new String(data.get(0));
        String passe = new String(data.get(1));
        boolean existe = dados.autenticar(nome, passe);
        if (existe) {
            //TODO alterar a cena nos dados, pq agr o bool e diferente
        }
    }

    public void registar(List<byte[]> data){
        String nome = new String(data.get(0));
        String passe = new String(data.get(1));
        boolean existe = dados.autenticar(nome, passe);
        dados.registar(nome, passe, existe);
        List<byte[]> info = new ArrayList<>();
        info.add("1".getBytes(StandardCharsets.UTF_8));
        conexao.send(Type.Registar, nome, info);

    }

    public void adicionarVoo(List<byte[]> data){

    }

    public void logOut(){
        this.loggedIn = false;
    }


    public void encerrarService(){
        this.online = false;
    }


    public void mostrarListaVoos() throws IOException {

        //adicionar o Lock

        List<PairOrigemDestino> listaVoos = new ArrayList<PairOrigemDestino>(this.dados.getPercursos());

        List<String> listaVoosToSend = new ArrayList<>();

        for (PairOrigemDestino pair : listaVoos){
            StringBuilder sb = new StringBuilder();
            sb.append("Origem: ");
            sb.append(pair.getOrigem());
            sb.append("      ");
            sb.append("Destino: ");
            sb.append(pair.getDestino());

            listaVoosToSend.add(sb.toString());
        }

        List<byte[]> dataLst = new ArrayList<>();

        for (String voo: listaVoosToSend){

        }

        conexao.send(Type.MostrarListaVoo, username, dataLst);


    }






}
