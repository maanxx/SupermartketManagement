package server;

import server.rmi.HoaDonServiceImpl;
import server.rmi.NhanVienServiceImpl;
import server.rmi.SanPhamServiceImpl;
import server.rmi.KhachHangServiceImpl;
import shared.services.HoaDonService;
import shared.services.NhanVienService;
import shared.services.SanPhamService;
import shared.services.KhachHangService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {
    public static void main(String[] args) {
        try {
            // Khởi tạo RMI registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // Đăng ký dịch vụ NhanVienService
            NhanVienService nhanVienService = new NhanVienServiceImpl();
            registry.rebind("NhanVienService", nhanVienService);

            // Đăng ký dịch vụ SanPhamService
            SanPhamService sanPhamService = new SanPhamServiceImpl();
            registry.rebind("SanPhamService", sanPhamService);

            // Đăng ký dịch vụ KhachHangService
            KhachHangService khachHangService = new KhachHangServiceImpl();
            registry.rebind("KhachHangService", khachHangService);

            // Đăng ký dịch vụ HoDonService
            HoaDonService hoaDonService = new HoaDonServiceImpl();
            registry.rebind("HoaDonService", hoaDonService);

            System.out.println(" Server RMI đang chạy trên cổng 1099...");
            System.out.println(" NhanVienService đã đăng ký!");
            System.out.println(" SanPhamService đã đăng ký!");
            System.out.println(" KhachHangService đã đăng ký!");
            System.out.println(" HoaDonService đã đăng ký!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
