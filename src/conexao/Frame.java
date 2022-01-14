package conexao;

import service.Type;

import java.util.List;

public class Frame {

    public final Type tagOp;
    public final String username;
    public final List<byte[]> dataLst;

    public Frame(Type tagOp, String username, List<byte[]> dataLst) {
        this.tagOp = tagOp;
        this.username = username;
        this.dataLst = dataLst;
    }
}