package service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Autenticar extends Service implements IService{
    public static Service interpretRequest(DataInputStream dis);
    public static DataOutputStream makeResponse(Service s);

}
