package dados;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Percurso implements Serializable {
    private String id;
    private String origem;
    private String destino;
    private int capacidade;
    private Map<LocalDate, Voo> dias;
    ReentrantLock rlPercurso = new ReentrantLock();


    public String getId() {
        return id;
    }

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

        for ( int i = 0 ; i < 100 ; i++) {
            Voo voo = new Voo(nLugares);
            dias.put(LocalDate.now().plusDays(i),voo);
        }
    }

    public boolean fazerReserva(String id, Utilizador utilizador, LocalDate dia) {
        return dias.get(dia).fazerReserva(id, utilizador);
    }

    public void fazerCancelamento(String codigoViagem) {

        for (Voo voo : dias.values())
            voo.fazerCancelamento(codigoViagem);

    }

    public boolean encerrarDia(LocalDate dia) {
        try{
            rlPercurso.lock();
            if (!dias.containsKey(dia)){
                return false;
            }
            Voo v = dias.get(dia);
            v.setEncerrado(true);
            return true;
        }
        finally{
            rlPercurso.unlock();
        }
    }

}
