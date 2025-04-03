//package server;
//
//import server.rmi.HoaDonServiceImpl;
//import server.rmi.NhanVienServiceImpl;
//import server.rmi.SanPhamServiceImpl;
//import server.rmi.KhachHangServiceImpl;
//import shared.services.HoaDonService;
//import shared.services.NhanVienService;
//import shared.services.SanPhamService;
//import shared.services.KhachHangService;
//
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
//public class MainServer {
//    public static void main(String[] args) {
//        try {
//            // Khởi tạo RMI registry
//            Registry registry = LocateRegistry.createRegistry(1099);
//
//            // Đăng ký dịch vụ NhanVienService
//            NhanVienService nhanVienService = new NhanVienServiceImpl();
//            registry.rebind("NhanVienService", nhanVienService);
//
//            // Đăng ký dịch vụ SanPhamService
//            SanPhamService sanPhamService = new SanPhamServiceImpl();
//            registry.rebind("SanPhamService", sanPhamService);
//
//            // Đăng ký dịch vụ KhachHangService
//            KhachHangService khachHangService = new KhachHangServiceImpl();
//            registry.rebind("KhachHangService", khachHangService);
//
//            // Đăng ký dịch vụ HoDonService
//            HoaDonService hoaDonService = new HoaDonServiceImpl();
//            registry.rebind("HoaDonService", hoaDonService);
//
//            System.out.println(" Server RMI đang chạy trên cổng 1099...");
//            System.out.println(" NhanVienService đã đăng ký!");
//            System.out.println(" SanPhamService đã đăng ký!");
//            System.out.println(" KhachHangService đã đăng ký!");
//            System.out.println(" HoaDonService đã đăng ký!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

package server;

import server.rmi.HoaDonServiceImpl;
import server.rmi.NhanVienServiceImpl;
import server.rmi.SanPhamServiceImpl;
import server.rmi.KhachHangServiceImpl;
import shared.services.HoaDonService;
import shared.services.NhanVienService;
import shared.services.SanPhamService;
import shared.services.KhachHangService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        try {
            String serverHostname = "nhom9rmi.duckdns.org";
            int port = 1099;

            // Cấu hình hostname để Client có thể kết nối từ xa
            System.setProperty("java.rmi.server.hostname", serverHostname);

            // Tạo Registry RMI trên cổng 1099
            LocateRegistry.createRegistry(port);

            // Khởi tạo dịch vụ
            NhanVienService nhanVienService = new NhanVienServiceImpl();
            SanPhamService sanPhamService = new SanPhamServiceImpl();
            KhachHangService khachHangService = new KhachHangServiceImpl();
            HoaDonService hoaDonService = new HoaDonServiceImpl();

            // Đăng ký dịch vụ RMI
            Naming.rebind("rmi://" + serverHostname + ":" + port + "/NhanVienService", nhanVienService);
            Naming.rebind("rmi://" + serverHostname + ":" + port + "/SanPhamService", sanPhamService);
            Naming.rebind("rmi://" + serverHostname + ":" + port + "/KhachHangService", khachHangService);
            Naming.rebind("rmi://" + serverHostname + ":" + port + "/HoaDonService", hoaDonService);

            System.out.println(" Server RMI đang chạy tại: " + serverHostname + ":" + port);
            System.out.println(" NhanVienService đã đăng ký!");
            System.out.println(" SanPhamService đã đăng ký!");
            System.out.println(" KhachHangService đã đăng ký!");
            System.out.println(" HoaDonService đã đăng ký!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


