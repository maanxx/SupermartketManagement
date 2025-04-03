//package client;
//
//import client.ui.LoginFrame;
//import shared.services.HoaDonService;
//import shared.services.KhachHangService;
//import shared.services.NhanVienService;
//import shared.services.SanPhamService;
//
//import javax.swing.*;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
//public class MainClient {
//    private static NhanVienService nhanVienService;
//    private static SanPhamService sanPhamService;
//    private static HoaDonService hoaDonService;
//    private static KhachHangService khachHangService;
//
//    public static void main(String[] args) {
//        try {
//            // Kết nối đến RMI Server
//            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
//            nhanVienService = (NhanVienService) registry.lookup("NhanVienService");
//            sanPhamService = (SanPhamService) registry.lookup("SanPhamService");
//            hoaDonService = (HoaDonService) registry.lookup("HoaDonService");
//            khachHangService = (KhachHangService) registry.lookup("KhachHangService");
//
//            if (nhanVienService == null) {
//                throw new RuntimeException(" Không thể kết nối đến Server RMI!");
//            }
//
//            SwingUtilities.invokeLater(() -> new LoginFrame(getNhanVienService()));
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, " Không thể kết nối đến Server RMI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//
//    public static NhanVienService getNhanVienService() {
//        if (nhanVienService == null) {
//            throw new IllegalStateException("Lỗi: NhanVienService không được null!");
//        }
//        return nhanVienService;
//    }
//
//    public static SanPhamService getSanPhamService() {
//        if (sanPhamService == null) {
//            throw new IllegalStateException("Lỗi: SanPhamService không được null!");
//        }
//        return sanPhamService;
//    }
//}

package client;

import client.ui.LoginFrame;
import shared.services.HoaDonService;
import shared.services.KhachHangService;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient {
    private static NhanVienService nhanVienService;
    private static SanPhamService sanPhamService;
    private static HoaDonService hoaDonService;
    private static KhachHangService khachHangService;

    public static void main(String[] args) {
        try {
            String serverHostname = "nhom9rmi.duckdns.org";
            int port = 1099;

            // Kết nối đến RMI Server từ xa
            Registry registry = LocateRegistry.getRegistry(serverHostname, port);
            nhanVienService = (NhanVienService) registry.lookup("NhanVienService");
            sanPhamService = (SanPhamService) registry.lookup("SanPhamService");
            hoaDonService = (HoaDonService) registry.lookup("HoaDonService");
            khachHangService = (KhachHangService) registry.lookup("KhachHangService");

            // Kiểm tra kết nối
            if (nhanVienService == null || sanPhamService == null || hoaDonService == null || khachHangService == null) {
                throw new RuntimeException(" Không thể kết nối đến Server RMI!");
            }

            System.out.println(" Kết nối RMI thành công với Server: " + serverHostname + ":" + port);

            // Mở giao diện Login
            SwingUtilities.invokeLater(() -> new LoginFrame(getNhanVienService()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, " Không thể kết nối đến Server RMI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static NhanVienService getNhanVienService() {
        if (nhanVienService == null) {
            throw new IllegalStateException(" Lỗi: NhanVienService không được null!");
        }
        return nhanVienService;
    }

    public static SanPhamService getSanPhamService() {
        if (sanPhamService == null) {
            throw new IllegalStateException(" Lỗi: SanPhamService không được null!");
        }
        return sanPhamService;
    }

    public static HoaDonService getHoaDonService() {
        if (hoaDonService == null) {
            throw new IllegalStateException(" Lỗi: HoaDonService không được null!");
        }
        return hoaDonService;
    }

    public static KhachHangService getKhachHangService() {
        if (khachHangService == null) {
            throw new IllegalStateException(" Lỗi: KhachHangService không được null!");
        }
        return khachHangService;
    }
}


