package excessoes;

public class DiaNaoExistenteException extends Exception{
    public DiaNaoExistenteException() {
    }

    public DiaNaoExistenteException(String message) {
        super(message);
    }
}
