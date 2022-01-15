package service;

import conexao.Conexao;
import conexao.Frame;
import dados.Dados;
import dados.PairOrigemDestino;
import dados.Voo;
import excessoes.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;


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
                Frame received = this.conexao.receive();
                List<byte[]> data = new ArrayList<>(); //data a enviar no frame de resposta
                try {
                    switch (received.tagOp) {
                        case Autenticar -> autenticar(received.dataLst);
                        case Registar -> registar(received.dataLst)    ;
                        case AdicionarVoo ->  adicionarVoo(received.dataLst);
                        case CancelamentoVoo -> cancelarVoo(received.dataLst);
                        case EncerrarDia -> encerrarDia(received.dataLst);
                        case ReservaVoo -> reservarVoo(received.dataLst);
                        case MostrarListaVoo -> mostrarListaVoos();
                        case PercursosPossíveis -> percursosPossiveis(received.dataLst);
                        case LogOut -> logOut();
                        case Encerrar -> encerrarService();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autenticar(List<byte[]> data) throws IOException {

        String nome = new String(data.get(0));
        String passe = new String(data.get(1));
        int existe = dados.autenticar(nome, passe);
        String sucesso;
        List<byte[]> dataToSend = new ArrayList<>();
        try {
            if (existe != 0) {
                sucesso = "1";
                dataToSend.add(sucesso.getBytes(StandardCharsets.UTF_8));
                String userType;
                if (existe == 1) {
                    userType = "util";
                    dataToSend.add(userType.getBytes(StandardCharsets.UTF_8));
                } else {
                    userType = "admin";
                    dataToSend.add(userType.getBytes(StandardCharsets.UTF_8));
                }
                conexao.send(Type.Autenticar, nome, dataToSend);
                this.username = nome;
                //TODO alterar a cena nos dados, pq agr o bool e diferente
            } else {
                throw new LoginInvalidoException();
            }
        }
        catch (IOException | LoginInvalidoException e) {
            sucesso = "0";
            dataToSend.add(sucesso.getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.Autenticar, nome, dataToSend);

        }
    }

    public void registar(List<byte[]> data) throws IOException {
        String nome = new String(data.get(0));
        String passe = new String(data.get(1));
        int existeInt = dados.autenticar(nome, passe);
        List<byte[]> info = new ArrayList<>();
        try {
            if (existeInt != 0)
                throw new UtilizadorJaExistenteException();
                //utilizador já existe
                //só pode criar utilizador
            dados.registar(nome, passe, false);
            info.add("1".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.Registar, nome, info);
        }
        catch (IOException | UtilizadorJaExistenteException e) {
            info.add("0".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.Registar, nome, info);
        }

    }

    public void adicionarVoo(List<byte[]> data) throws IOException {
        List<byte[]> dataToSend = new ArrayList<>();
        try {
            String origem = new String(data.get(0));
            String destino = new String(data.get(1));
            String nLugaresString = new String(data.get(2));
            int nLugares = parseInt(nLugaresString);
            boolean sucesso = dados.addPercurso(origem, destino, nLugares);

            if (sucesso)
                dataToSend.add("1".getBytes(StandardCharsets.UTF_8));
            else
                throw new VooJaExistenteException();

            conexao.send(Type.AdicionarVoo, username, dataToSend);
        }
        catch (NumberFormatException e1) {
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.AdicionarVoo, username, dataToSend);
        }
        catch (VooJaExistenteException e2){
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            dataToSend.add("1".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.AdicionarVoo, username, dataToSend);
        }

    }


    public void cancelarVoo(List<byte[]> data) throws IOException {
        String codViagem = new String(data.get(0));
        boolean sucesso = dados.fazerCancelamento(username,codViagem);
        List<byte[]> dataToSend = new ArrayList<>();

        if (sucesso)
            dataToSend.add("1".getBytes(StandardCharsets.UTF_8));
        else
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));

        conexao.send(Type.CancelamentoVoo,username,dataToSend);
    }

    public void encerrarDia(List<byte[]> data) throws IOException, InterruptedException {
        //Thread.sleep(30000);
        List<byte[]> dataToSend = new ArrayList<>();
        try{
            String diaDia = new String(data.get(0));
            String diaMes = new String(data.get(1));
            String diaAno = new String(data.get(2));

            int dia = parseInt(diaAno);
            int mes = parseInt(diaMes);
            int ano = parseInt(diaDia);

            LocalDate day = LocalDate.of(dia,mes,ano);

            boolean sucesso = dados.encerrarDia(day);

            if (sucesso) {
                dataToSend.add("1".getBytes(StandardCharsets.UTF_8));
            }
            else{
                throw new DiaNaoExistenteException();
            }

            conexao.send(Type.Encerrar,username,dataToSend);

        }catch (DiaNaoExistenteException e) {
            //e.getMessage();
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.Encerrar, username, dataToSend);

        }catch (NumberFormatException e) {
            dataToSend.add("0".getBytes(StandardCharsets.UTF_8));
            dataToSend.add("1".getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.Encerrar, username, dataToSend);
        }
    }

    public void reservarVoo(List<byte[]> data) throws IOException {
        int diaDiaI = parseInt(Arrays.toString(data.get(0)));
        int diaMesI = parseInt(Arrays.toString(data.get(1)));
        int diaAnoI = parseInt(Arrays.toString(data.get(2)));
        LocalDate diaI = LocalDate.of(diaAnoI,diaMesI,diaDiaI);

        int diaDiaF = parseInt(Arrays.toString(data.get(3)));
        int diaMesF = parseInt(Arrays.toString(data.get(4)));
        int diaAnoF = parseInt(Arrays.toString(data.get(5)));
        LocalDate diaF = LocalDate.of(diaAnoF,diaMesF,diaDiaF);

        int imax = data.size() - 5;
        String[] locais = new String[imax];
        for (int i = 0 ; i < imax ; i++)
            locais[i] = Arrays.toString(data.get(i + 6));

        String codViagem = dados.fazerReservaTodosPercursos(locais, username, diaI, diaF);
        List<byte[]> dataToSend = new ArrayList<>();

        dataToSend.add(codViagem.getBytes(StandardCharsets.UTF_8));

        conexao.send(Type.ReservaVoo,username,dataToSend);

    }

    public void logOut(){
        this.loggedIn = false;
    }


    public void encerrarService() throws IOException {
        conexao.close();
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
            dataLst.add(voo.getBytes(StandardCharsets.UTF_8));
        }

        conexao.send(Type.MostrarListaVoo, username, dataLst);


    }

    public void percursosPossiveis(List<byte[]> data) throws IOException {
        String origem = new String(data.get(0));
        String destino = new String(data.get(1));
        Set<String[]> set = dados.percursosPossiveis(origem, destino);

        for(String[] viagem : set) {
            List<byte[]> dataToSend = new ArrayList<>();
            for(String local : viagem)
                dataToSend.add(local.getBytes(StandardCharsets.UTF_8));
            conexao.send(Type.AdicionarVoo,username,dataToSend);
        }

    }




}
