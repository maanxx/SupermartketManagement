package server;

import server.rmi.HoaDonServiceImpl;
import server.rmi.NhanVienServiceImpl;
import server.rmi.SanPhamServiceImpl;
import server.rmi.KhachHangServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        try {
            // Mở cmd -> gõ ipconfig -> địa chỉ ipv4
            String serverIp = "172.16.1.29";
            int port = 1099;

            System.setProperty("java.rmi.server.hostname", serverIp);
            System.setProperty("java.rmi.server.logCalls", "true");

            System.out.println(" Server hostname đã set: " + serverIp);

            LocateRegistry.createRegistry(port);
            System.out.println(" RMI Registry đã khởi tạo tại port " + port);

            //  Khởi tạo và đăng ký các service
            Naming.rebind("NhanVienService", new NhanVienServiceImpl());
            Naming.rebind("SanPhamService", new SanPhamServiceImpl());
            Naming.rebind("KhachHangService", new KhachHangServiceImpl());
            Naming.rebind("HoaDonService", new HoaDonServiceImpl());

            System.out.println(" RMI Server đang chạy tại: rmi://" + serverIp + ":" + port);
            System.out.println(" Các service đã đăng ký thành công.");

        } catch (Exception e) {
            System.err.println(" Lỗi khi khởi động RMI Server:");
            e.printStackTrace();
        }
    }
}