package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.rmi.NhanVienServiceImpl;
import shared.services.NhanVienService;

public class MainServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("NhanVienService", new NhanVienServiceImpl());

            System.out.println(" Server RMI đang chạy trên cổng 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
