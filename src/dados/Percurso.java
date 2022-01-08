package dados;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Percurso implements Serializable {
    private String origem;
    private String destino;
    private Map<Integer, Voo> dias;
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

    public Percurso(String origem, String destino, int nLugares) {
        this.origem = origem;
        this.destino = destino;
        this.dias = new HashMap<>();

        for ( int i = 1 ; i < 31 ; i++) {
            String codigoViagem = generateID();
            Voo voo = new Voo(nLugares,codigoViagem);
            dias.put(i,voo);
        }
    }

    public boolean temViagem(String codigoViagem) {


        return false;
    }

    public String fazerReserva(int lugar, String utilizador, Integer dia) {
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
