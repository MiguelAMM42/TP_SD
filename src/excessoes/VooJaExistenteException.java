package excessoes;

public class VooJaExistenteException extends Exception{
    public VooJaExistenteException() {
    }
    public VooJaExistenteException(String message) {
        super(message);
    }
}
