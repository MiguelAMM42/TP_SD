package conexao;

import service.Type;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conexao implements AutoCloseable{

    private final DataOutputStream out;
    private final Lock l_out= new ReentrantLock();
    private final DataInputStream in;
    private final Lock l_in= new ReentrantLock();
    private final Socket socket;

    public Conexao(Socket socket) throws IOException {
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.socket = socket;
    }

    public void send(Frame type) throws IOException {
        try {
            l_out.lock();
            out.writeUTF(type.tagOp.name());
            out.writeUTF(type.username);
            int tamanho = type.dataLst.size();
            out.writeInt(tamanho);
            for (int i = 0; i < tamanho; i++) {
                out.writeInt(type.dataLst.get(i).length);
                out.write(type.dataLst.get(i));
            }
            out.flush();
        } finally {
            l_out.unlock();
        }
    }



    public void send(Type tag, String username, List<byte[]> data) throws IOException {
        this.send(new Frame(tag, username, data));
    }

    public Frame receive() throws IOException {
        Type tag;
        String username;
        byte[] data;
        try {
            l_in.lock();
            tag = Type.valueOf(in.readUTF());
            username = in.readUTF();
            int tamanho = in.readInt();
            int i;
            for(i = 0; i < tamanho; i++) {

            }
            data = new byte[n];
            in.readFully(data);
        }
        finally {
            l_in.unlock();
        }
        return new Frame(tag,username,data);
    }

    public void close() throws IOException {
        try {
            l_out.lock();
            l_in.lock();
            out.close();
            in.close();
        } finally {
            l_out.unlock();
            l_in.unlock();
        }
    }

}





