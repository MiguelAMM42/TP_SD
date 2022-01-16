package dados;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Voo implements Serializable {
    private boolean encerrado;
    private final Map<Integer, Lugar> lugaresAviao;
    ReentrantLock rlVoo = new ReentrantLock();

    public boolean isEncerrado() {
        return encerrado;
    }

    public void setEncerrado(boolean encerrado) {
        this.encerrado = encerrado;
    }

    public Voo(int nLugares) {
        this.encerrado = false;
        this.lugaresAviao = new HashMap<>();

        for (int i = 1 ; i <= nLugares ; i++) {
            Lugar lugar = new Lugar(null, null);
            lugaresAviao.put(i,lugar);
        }
    }

    public boolean fazerReserva(String id, Utilizador utilizador) {
        rlVoo.lock();
        //dependendo se eu posso encerrar enquanto se reserva um lugar vai esta versao ou a q está comentada
        try {
            if(encerrado)
                return false;

            for (Lugar lugar : lugaresAviao.values())
                if (lugar.getUtilizador() == null) {
                    lugar.setUtilizador(utilizador);
                    lugar.setCodigoViagem(id);
                    return true;
                }

            return false;
        }
        finally {
            rlVoo.unlock();
        }

    }

    public boolean fazerCancelamento(String codigoViagem) {
        rlVoo.lock();
        try {
            if(encerrado)
                return false;

            for (Lugar lugar : lugaresAviao.values()) {
                if (Objects.equals(lugar.getCodigoViagem(), codigoViagem)) {
                    lugar.setUtilizador(null);
                    lugar.setCodigoViagem(null);
                    return true;
                }
            }

            return false;
        }
        finally {
            rlVoo.unlock();
        }
   
    }

}
