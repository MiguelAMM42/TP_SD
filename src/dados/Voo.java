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
        //try {
        //    if (encerrado)
        //        return false;

        //    if (!lugaresAviao.containsKey(lugar))
        //        return false;

        //    Lugar l = lugaresAviao.get(lugar);
        //    if (l.getUtilizador() != null) {
        //        rlVoo.unlock();
        //        return false;
        //    }
        //    else {
        //        try {
        //            l.rlLugar.lock();
        //            rlVoo.unlock();
        //            l.setUtilizador(utilizador);
        //            return true;
        //        }
        //        finally {
        //            l.rlLugar.unlock();
        //        }
        //    }
        //}
        //finally {
        //    if (rlVoo.isLocked())
        //        rlVoo.unlock();
        //}

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
    //    try{
    //        if (encerrado)
    //            return false;

    //        if (!lugaresAviao.containsKey(lugar))
    //            return false;

    //        Lugar l = lugaresAviao.get(lugar);
    //        if (!Objects.equals(l.getUtilizador(), utilizador)) {
    //            rlVoo.unlock();
    //            return false;
    //        }
    //        else {
    //            try {
    //                l.rlLugar.lock();
    //                rlVoo.unlock();
    //                l.setUtilizador(null);
    //                return true;
    //            }
    //            finally {
    //                l.rlLugar.unlock();
    //            }
    //        }
   //     }
    //    finally {
    //        if (rlVoo.isLocked())
    //            rlVoo.unlock();
   //     }
    }

}
