package dados;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Dados implements Serializable {

    private Map<String, Percurso> listaPercursos;
    private Map<String, Utilizador> utilizadores;
    private Map<String, Viagem> viagens;
    //private ReentrantLock rlDados = new ReentrantLock();
    private ReentrantReadWriteLock lock;
    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;

    public Dados() {
        this.listaPercursos = new HashMap<>();
        this.utilizadores = new HashMap<>();
        this.viagens = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

    }


    //0 -> Não existe
    //1 -> Utilizador
    //2 -> Admin
    public int autenticar(String nome, String pass) {
        try{
            readLock.lock();
            if (!Objects.equals(utilizadores.get(nome).getPassword(), pass))
                return 0;
            if (utilizadores.get(nome).isAdmin())
                return 2;
            return 1;
        }finally {
            readLock.unlock();
        }
    }

    public boolean registar(String nome, String pass, Boolean isAdmin) {

        try{
            writeLock.lock();
            if (utilizadores.containsKey(nome))
                return false; //mandar msg a dizer q esse nome ja existe?

            Utilizador newU = new Utilizador(nome, pass, isAdmin);
            utilizadores.put(nome,newU);
            return true;

        }finally {
            writeLock.unlock();
        }
    }

    public Set<PairOrigemDestino> getPercursos() {
        try{
            readLock.lock();
            Set<PairOrigemDestino> p = new HashSet<>();
            for (Percurso percurso : listaPercursos.values()) {
                PairOrigemDestino pair = new PairOrigemDestino(percurso.getOrigem(),percurso.getDestino());
                p.add(pair);
            }
            return p;
        }finally {
            readLock.unlock();
        }
    }

    public boolean addPercurso(String origem, String destino, int nLugares) {
        String id = generateID();
        Percurso percurso = new Percurso(id,origem,destino,nLugares);
        try {
            writeLock.lock();
            Percurso value = listaPercursos.putIfAbsent(id, percurso);
            return value != null;
        }
        finally {
            writeLock.unlock();
        }
    }


    public String fazerReservaTodosPercursos(String[] locais, Utilizador utilizador, LocalDate diaI, LocalDate diaF) {
            String id = generateID();


            for (int i = 0 ; locais[i+1] != null ; i++)
                if (!existePercurso(locais[0],locais[1]))
                    return null;

            Viagem viagem = new Viagem(utilizador,id);
            viagens.put(id, viagem);

            boolean reservado = false;
            for (; !diaI.isAfter(diaF) && !reservado ; diaI = diaI.plusDays(1) ) {
                boolean f = true;
                for (int i = 0; locais[i + 1] != null && f; i++)
                    f = fazerReservaEntreDoisLocais(id, locais[i], locais[i + 1], utilizador, diaI, viagem);
            }


            if(!reservado){
                fazerCancelamento(utilizador, id);
                return null;
            }

            viagens.replace(id, viagem);
            return id;
    }

    public boolean existePercurso(String origem, String destino) {

        for (Percurso percurso: listaPercursos.values())
            if(Objects.equals(percurso.getOrigem(), origem) && Objects.equals(percurso.getDestino(), destino))
                return true;

        return false;
    }


    public boolean fazerReservaEntreDoisLocais(String id, String origem, String destino, Utilizador utilizador,
                                               LocalDate dia, Viagem viagem) {
        try{

            readLock.lock();

            for (Percurso percurso: listaPercursos.values()) {
                if(Objects.equals(percurso.getOrigem(), origem) && Objects.equals(percurso.getDestino(), destino)) {
                    viagem.addPercurso(id);
                    return percurso.fazerReserva(id, utilizador, dia);
                }
            }

            return false;

        }finally {
            readLock.unlock();
        }
    }

    public boolean fazerCancelamento(Utilizador utilizador, String codigoViagem) {
        try{

            writeLock.lock();

            if (utilizador != viagens.get(codigoViagem).getUtilizador())
                return false;

            Map<Integer, String> percursos = viagens.get(codigoViagem).getPercursos();

            viagens.remove(codigoViagem);

            for (String idPercurso : percursos.values()) {
                listaPercursos.get(idPercurso).fazerCancelamento(codigoViagem);
            }

            return true;

        }finally {
            writeLock.unlock();

        }
    }

    public void encerrarDia(LocalDate dia) {
        try{
            writeLock.lock();
            for (Percurso percurso : listaPercursos.values()) {
                percurso.encerrarDia(dia);
            }

        }finally {

            writeLock.unlock();

        }
    }

    public Set<String[]> percursosPossíveis(String origem, String destino) {
        return percursosPossíveisAux(origem, destino, 3);
    }

    public Set<String[]> percursosPossíveisAux(String origem, String destino, int n){
        Set<String[]> percursos = new HashSet<>();

        Set<String> destinos = percursosOrigem(origem);
        if (destinos.isEmpty())
            return percursos;

        if (n > 1) {
            for (String d : destinos)
                if(Objects.equals(d, destino)) {
                    percursos.add(d);
                }
                percursosPossíveisAux(d, destino, n - 1);
        }

        if (n == 1) {
            for (String d : destinos)
                if(Objects.equals(d, destino)) {
                    String[] apendes = new String[1];
                    return d;
                }
        }


        String[] p1voo = new String[2];
        p1voo[0] = origem;
    }

    public Set<String> percursosOrigem(String origem) {
        Set<PairOrigemDestino> allPairs = getPercursos();
        Set<String> origemPairs = new HashSet<>();

        for (PairOrigemDestino pair : allPairs) {
            if (Objects.equals(pair.getOrigem(), origem))
                origemPairs.add(pair.getDestino());
        }

        return origemPairs;
    }

    public String generateID() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        if (listaPercursos.containsKey(generatedString))
                generatedString = generateID();

        if (utilizadores.containsKey(generatedString))
            generatedString = generateID();

        if (viagens.containsKey(generatedString))
            generatedString = generateID();

        return generatedString;
    }
}
