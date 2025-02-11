package client.ui;

import shared.dto.NhanVienDTO;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private NhanVienDTO loggedInNhanVien;

    public AdminDashboard(NhanVienDTO nhanVien) {
        this.loggedInNhanVien = nhanVien;

        setTitle("Admin Dashboard - " + nhanVien.getHoTen());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuQuanLy = new JMenu("Quản lý");

        JMenuItem menuItemQuanLySanPham = new JMenuItem("Quản lý sản phẩm");
        JMenuItem menuItemQuanLyHoaDon = new JMenuItem("Quản lý hóa đơn");
        JMenuItem menuItemQuanLyNhanVien = new JMenuItem("Quản lý nhân viên");

        menuItemQuanLySanPham.addActionListener(e -> openQuanLySanPham());
        menuItemQuanLyHoaDon.addActionListener(e -> openQuanLyHoaDon());
        menuItemQuanLyNhanVien.addActionListener(e -> openQuanLyNhanVien());

        menuQuanLy.add(menuItemQuanLySanPham);
        menuQuanLy.add(menuItemQuanLyHoaDon);
        menuQuanLy.add(menuItemQuanLyNhanVien);

        menuBar.add(menuQuanLy);
        setJMenuBar(menuBar);

        // Hiển thị thông tin admin đăng nhập
        JLabel welcomeLabel = new JLabel("Xin chào Quản lý: " + nhanVien.getHoTen(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void openQuanLySanPham() {
        JOptionPane.showMessageDialog(this, "Chức năng quản lý sản phẩm chưa được triển khai!");
    }

    private void openQuanLyHoaDon() {
        JOptionPane.showMessageDialog(this, "Chức năng quản lý hóa đơn chưa được triển khai!");
    }

    private void openQuanLyNhanVien() {
        JOptionPane.showMessageDialog(this, "Chức năng quản lý nhân viên chưa được triển khai!");
    }
}
