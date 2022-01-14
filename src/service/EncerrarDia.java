package service;
import dados.Dados;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class EncerrarDia extends Service implements IService{
    public static Service interpretRequest(DataInputStream dis);
    public static DataOutputStream makeResponse(Service s);

}
