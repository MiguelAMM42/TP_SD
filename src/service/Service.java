package service;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Service {

    public boolean execute(DataInputStream dis) throws IOException {
        byte type = dis.readByte();

        Service response;

        switch(type) {
            case (byte)0:
                response = (AdicionarVoo) AdicionarVoo.interpretRequest(dis);
                break;
            case (byte)1:
                response = (Autenticar) Autenticar.interpretRequest(dis);
                break;
            case (byte)2:
                response = (CancelamentoVoo) CancelamentoVoo.interpretRequest(dis);
                break;
            case (byte)3:
                response = (EncerrarDia) CancelamentoVoo.interpretRequest(dis);
                break;
            case (byte)4:
                response = (MostrarListaVoo) CancelamentoVoo.interpretRequest(dis);
                break;
            case (byte)5:
                response = (ReservaVoo) CancelamentoVoo.interpretRequest(dis);
                break;
            default:
                break;
        }
    }

}
