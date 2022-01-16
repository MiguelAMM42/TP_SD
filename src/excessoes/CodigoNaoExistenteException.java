package excessoes;

public class CodigoNaoExistenteException extends Exception {
    public CodigoNaoExistenteException() {
    }

    public CodigoNaoExistenteException(String message) {
        super(message);
    }
}
