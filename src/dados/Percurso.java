package dados;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Percurso implements Serializable {
    private String id;
    private String origem;
    private String destino;
    private int capacidade;
    private Map<Date, Voo> dias;
    ReentrantLock rlPercurso = new ReentrantLock();

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Percurso(String id, String origem, String destino, int nLugares) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.capacidade = nLugares;
        this.dias = new HashMap<>();

        for ( int i = 1 ; i < 31 ; i++) {
            String codigoViagem = generateID();
            Voo voo = new Voo(nLugares,codigoViagem);
            dias.put(LocalDateTime.now().getDayOfYear(),voo);
        }
    }

    public boolean temViagem(String codigoViagem) {


        return false;
    }

    public boolean fazerReserva(String id, Utilizador utilizador, Date diaI, Date diaF) {
        return dias.get(dia).fazerReserva(lugar, utilizador);
    }

    public boolean fazerCancelamento(String codigoViagem) {
        for (Voo voo : dias.values())
            if (Objects.equals(voo.getCodigoViagem(), codigoViagem)) {
                voo.fazerCancelamento(codigoViagem);
                return true;
            }

        return false;
    }

    public void encerrarDia(int dia) {

        try{
            rlPercurso.lock();
            Voo v = dias.get(dia);
            v.setEncerrado(true);
        }
        finally{
            rlPercurso.unlock();
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

        for (Voo voo : dias.values()) {
            if (Objects.equals(voo.getCodigoViagem(), generatedString))
                generatedString = generateID();
        }

        return generatedString;
    }
}
