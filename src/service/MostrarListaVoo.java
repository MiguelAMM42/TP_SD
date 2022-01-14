package service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MostrarListaVoo extends Service implements IService{
    public static Service interpretRequest(DataInputStream dis);
    public static DataOutputStream makeResponse(Service s);
}
