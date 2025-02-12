package client;

import client.ui.LoginFrame;
import shared.services.NhanVienService;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient {
    private static NhanVienService nhanVienService;

    public static void main(String[] args) {
        try {
            // Kết nối đến RMI Server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            nhanVienService = (NhanVienService) registry.lookup("NhanVienService");

            if (nhanVienService == null) {
                throw new RuntimeException(" Không thể kết nối đến Server RMI!");
            }

            SwingUtilities.invokeLater(() -> new LoginFrame(getNhanVienService()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, " Không thể kết nối đến Server RMI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static NhanVienService getNhanVienService() {
        if (nhanVienService == null) {
            throw new IllegalStateException("Lỗi: NhanVienService không được null!");
        }
        return nhanVienService;
    }
}
