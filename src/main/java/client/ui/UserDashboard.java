package client.ui;

import shared.dto.NhanVienDTO;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {
    private NhanVienDTO loggedInNhanVien;

    public UserDashboard(NhanVienDTO nhanVien) {
        this.loggedInNhanVien = nhanVien;

        setTitle("User Dashboard - " + nhanVien.getHoTen());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuBanHang = new JMenu("Bán hàng");

        JMenuItem menuItemThanhToan = new JMenuItem("Thanh toán hóa đơn");

        menuItemThanhToan.addActionListener(e -> openThanhToan());

        menuBanHang.add(menuItemThanhToan);

        menuBar.add(menuBanHang);
        setJMenuBar(menuBar);

        // Hiển thị thông tin nhân viên đăng nhập
        JLabel welcomeLabel = new JLabel("Xin chào Nhân viên: " + nhanVien.getHoTen(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void openThanhToan() {
        JOptionPane.showMessageDialog(this, "Chức năng thanh toán hóa đơn chưa được triển khai!");
    }
}
