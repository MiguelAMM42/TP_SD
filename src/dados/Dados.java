package dados;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Dados implements Serializable {

    private Map<String, Percurso> listaPercursos;
    private Map<String, String> utilizadores;
    private String passAdmin;
    private ReentrantLock rlDados = new ReentrantLock();
    private int dia;

    public Dados() {
        this.listaPercursos = new HashMap<>();
        this.utilizadores = new HashMap<>();
        this.dia = 0;
        this.passAdmin = "admin";
    }

    public boolean autenticar(String nome, String pass) {
        if (Objects.equals(nome, "admin"))
            return Objects.equals(pass, passAdmin);

        return Objects.equals(utilizadores.get(nome), pass);
    }

    public Set<PairOrigemDestino> getPercursos() {
        Set<PairOrigemDestino> p = new HashSet<>();
        for (Percurso percurso : listaPercursos.values()) {
            PairOrigemDestino pair = new PairOrigemDestino(percurso.getOrigem(),percurso.getDestino());
            p.add(pair);
        }
        return p;
    }

    public boolean addPercurso(String origem, String destino, int nLugares) {
        String id = generateID();
        Percurso percurso = new Percurso(origem,destino,nLugares);
        try {
            rlDados.lock();
            Percurso value = listaPercursos.putIfAbsent(id, percurso);
            boolean b = value != null;
            if (b)
                for (int i = 1 ; i <= dia ; i++)
                    percurso.encerrarDia(i);

            return b;
        }
        finally {
            rlDados.unlock();
        }
    }

    public String fazerReserva(String id, int lugar, String utilizador, Integer dia) {
        if (! listaPercursos.containsKey(id))
            return null;

        return listaPercursos.get(id).fazerReserva(lugar, utilizador, dia);
    }

    public boolean fazerCancelamento(String codigoViagem) {
        String id;
        boolean b = false;
        for (Percurso percurso : listaPercursos.values())
            if (percurso.fazerCancelamento(codigoViagem))
                return true;

        return false;
    }

    public void encerrarDia() {
        this.dia++;
        for (Percurso percurso : listaPercursos.values()) {
            percurso.encerrarDia(dia);
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

        return generatedString;
    }
}
