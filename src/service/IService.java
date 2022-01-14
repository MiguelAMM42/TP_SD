package service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IService {
    public static DataOutputStream makeRequest();
    public static Service interpretRequest(DataInputStream dis);
    public static DataOutputStream makeResponse(Service s);
}
