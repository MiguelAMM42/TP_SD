package dados;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

public class Lugar implements Serializable {
    private Utilizador utilizador;
    private String codigoViagem;
    protected ReentrantLock rlLugar = new ReentrantLock();

    public Lugar(Utilizador utilizador, String codigoViagem) {
        this.utilizador = utilizador;
        this.codigoViagem = codigoViagem;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public String getCodigoViagem() {
        return codigoViagem;
    }

    public void setCodigoViagem(String codigoViagem) {
        this.codigoViagem = codigoViagem;
    }
}
