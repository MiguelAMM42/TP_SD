package dados;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Voo implements Serializable {
    private boolean encerrado;
    private Map<Integer, Lugar> lugaresAviao;
    ReentrantLock rlVoo = new ReentrantLock();

    public boolean isEncerrado() {
        return encerrado;
    }

    public void setEncerrado(boolean encerrado) {
        this.encerrado = encerrado;
    }

    public Voo(int nLugares, String codigoViagem) {
        this.encerrado = false;
        this.lugaresAviao = new HashMap<>();

        for (int i = 1 ; i <= nLugares ; i++) {
            lugaresAviao.put(i,null);
        }
    }

    public String fazerReserva(int lugar, String utilizador) {
        rlVoo.lock();
        //dependendo se eu posso encerrar enquanto se reserva um lugar vai esta versao ou a q está comentada
        try {
            if(encerrado)
                return null;

            if (!lugaresAviao.containsKey(lugar))
                return null;

            Lugar l = lugaresAviao.get(lugar);
            if (l.getUtilizador() != null) {
                rlVoo.unlock();
                return null;
            }
            else {
                l.setUtilizador(utilizador);
                return l.getCodigoViagem();
            }
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

    public boolean fazerCancelamento(String codigoViagem, String utilizador) {
        rlVoo.lock();
        try {
            if(encerrado)
                return false;

            if (!lugaresAviao.containsKey(lugar))
                return false;

            Lugar l = lugaresAviao.get(lugar);
            if (!Objects.equals(l.getUtilizador(), utilizador)) {
                rlVoo.unlock();
                return false;
            }
            else {
                l.setUtilizador(null);
                return true;
            }
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
