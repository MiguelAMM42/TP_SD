package dados;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

public class Lugar implements Serializable {
    private String utilizador;
    private String codigoViagem;
    protected ReentrantLock rlLugar = new ReentrantLock();

    public Lugar(String utilizador, String codigoViagem) {
        this.utilizador = utilizador;
        this.codigoViagem = codigoViagem;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getCodigoViagem() {
        return codigoViagem;
    }

    public void setCodigoViagem(String codigoViagem) {
        this.codigoViagem = codigoViagem;
    }
}
