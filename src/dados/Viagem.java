package dados;

import java.util.Map;

public class Viagem {
    private Map<Integer, String> percursos;
    private int nPrecursos;
    private Utilizador utilizador;
    private String idViagem;

    public Viagem(Utilizador utilizador, String idViagem) {
        this.nPrecursos = 0;
        this.utilizador = utilizador;
        this.idViagem = idViagem;
    }

    public Map<Integer, String> getPercursos() {
        return percursos;
    }

    public void addPercurso(String idPercurso) {
        nPrecursos++;
        this.percursos.put(nPrecursos,idPercurso);
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }
}
