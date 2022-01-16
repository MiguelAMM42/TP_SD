package dados;

import java.io.Serializable;

public class Utilizador implements Serializable {
    private String nome;
    private String password;
    private boolean isAdmin;

    public Utilizador(String nome, String password, boolean isAdmin) {
        this.nome = nome;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
