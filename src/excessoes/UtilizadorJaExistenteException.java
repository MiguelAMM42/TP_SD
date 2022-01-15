package excessoes;

public class UtilizadorJaExistenteException extends Exception{
    public UtilizadorJaExistenteException() {
    }
    public UtilizadorJaExistenteException(String message) {
        super(message);
    }
        
}
