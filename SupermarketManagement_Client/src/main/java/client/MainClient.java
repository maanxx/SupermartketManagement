package client;

import client.ui.AdminDashboard;
import client.ui.LoginFrame;
import client.ui.UserDashboard;
import shared.dto.NhanVienDTO;
import shared.services.*;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient {
    private static NhanVienService nhanVienService;
    private static SanPhamService sanPhamService;
    private static HoaDonService hoaDonService;
    private static KhachHangService khachHangService;

    private static NhanVienDTO nhanVien = new NhanVienDTO();

    public static void main(String[] args) {
        try {
            // Mở cmd -> gõ ipconfig -> địa chỉ ipv4
            String serverIp = "172.16.1.253";
            int port = 1099;
            Registry registry = LocateRegistry.getRegistry(serverIp, port);

            nhanVienService = (NhanVienService) registry.lookup("NhanVienService");
            sanPhamService = (SanPhamService) registry.lookup("SanPhamService");
            hoaDonService = (HoaDonService) registry.lookup("HoaDonService");
            khachHangService = (KhachHangService) registry.lookup("KhachHangService");

            System.out.println(" Đã kết nối đến RMI Server tại: " + serverIp + ":" + port);

//            SwingUtilities.invokeLater(() -> new LoginFrame(getNhanVienService()));
//            SwingUtilities.invokeLater(() -> new UserDashboard(nhanVien, getNhanVienService()));
            SwingUtilities.invokeLater(() -> new AdminDashboard(nhanVien, getNhanVienService()));


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    " Không thể kết nối đến Server RMI!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Getter
    public static NhanVienService getNhanVienService() {
        if (nhanVienService == null) throw new IllegalStateException("Lỗi: NhanVienService null!");
        return nhanVienService;
    }

    public static SanPhamService getSanPhamService() {
        if (sanPhamService == null) throw new IllegalStateException("Lỗi: SanPhamService null!");
        return sanPhamService;
    }

    public static HoaDonService getHoaDonService() {
        if (hoaDonService == null) throw new IllegalStateException("Lỗi: HoaDonService null!");
        return hoaDonService;
    }

    public static KhachHangService getKhachHangService() {
        if (khachHangService == null) throw new IllegalStateException("Lỗi: KhachHangService null!");
        return khachHangService;
    }

}