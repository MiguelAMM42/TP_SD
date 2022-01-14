package dados;

import java.io.Serializable;
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
    private int dia;

    public Dados() {
        this.listaPercursos = new HashMap<>();
        this.utilizadores = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        this.dia = 0;

    }

    public boolean autenticar(String nome, String pass) {
        try{
            readLock.lock();
            return Objects.equals(utilizadores.get(nome), pass);
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
            boolean b = value != null;
            if (b)
                for (int i = 1 ; i <= dia ; i++)
                    percurso.encerrarDia(i);

            return b;
        }
        finally {
            writeLock.unlock();
        }
    }

    public String fazerReservaTodosPercursos(String[] locais, Utilizador utilizador, Date diaI, Date diaF) {
            String id = generateID();
            boolean haPercurso = true;

            Viagem viagem = new Viagem(utilizador,id);

            for (int i = 0 ; locais[i+1] != null ; i++) {
                haPercurso = fazerReservaEntreDoisLocais(id, locais[i],locais[i+1],utilizador,diaI,diaF,viagem);
                if (!haPercurso) {
                    fazerCancelamento(id);
                    return null;
                }
            }
            return id;
    }

    public boolean fazerReservaEntreDoisLocais(String id, String local1, String local2, Utilizador utilizador, Date diaI, Date diaF, Viagem viagem) {
        try{

            readLock.lock();

            for (Percurso percurso: listaPercursos.values()) {
                if(Objects.equals(percurso.getOrigem(), local1) && Objects.equals(percurso.getDestino(), local2)) {
                    viagem.addPercurso(id);
                    return percurso.fazerReserva(id, utilizador, diaI, diaF);
                }
            }

            return false;

        }finally {
            readLock.unlock();
        }
    }

    public boolean fazerCancelamento(String codigoViagem) {
        try{

            writeLock.lock();

            String id;
            boolean b = false;
            for (Percurso percurso : listaPercursos.values())
                if (percurso.fazerCancelamento(codigoViagem))
                    return true;

            return false;

        }finally {
            writeLock.unlock();

        }
    }

    public void encerrarDia() {
        try{
            writeLock.lock();
            this.dia++;
            for (Percurso percurso : listaPercursos.values()) {
                percurso.encerrarDia(dia);
            }

        }finally {

            writeLock.unlock();

        }
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
