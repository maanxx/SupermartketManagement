package client;

import client.ui.LoginFrame;
import shared.services.NhanVienService;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient {
    public static void main(String[] args) {
        try {
            // Kiểm tra kết nối với Server RMI trước
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            NhanVienService nhanVienService = (NhanVienService) registry.lookup("NhanVienService");

            // Nếu kết nối thành công, mở giao diện đăng nhập
            SwingUtilities.invokeLater(() -> new LoginFrame(nhanVienService));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến Server RMI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
