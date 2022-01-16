package excessoes;

public class ListaVaziaException extends Exception{
    public ListaVaziaException() {
    }

    public ListaVaziaException(String message) {
        super(message);
    }
}
