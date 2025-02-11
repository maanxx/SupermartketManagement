package client.ui;

import shared.dto.NhanVienDTO;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private NhanVienDTO loggedInNhanVien; // Lưu nhân viên đã đăng nhập

    public MainDashboard(NhanVienDTO nhanVien) {
        this.loggedInNhanVien = nhanVien;

        setTitle("Hệ thống quản lý siêu thị - " + nhanVien.getHoTen());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuBanHang = new JMenu("Bán hàng");
        JMenu menuQuanLy = new JMenu("Quản lý");

        JMenuItem menuItemThanhToan = new JMenuItem("Thanh toán hóa đơn");
        JMenuItem menuItemQuanLySanPham = new JMenuItem("Quản lý sản phẩm");
        JMenuItem menuItemQuanLyHoaDon = new JMenuItem("Quản lý hóa đơn");
        JMenuItem menuItemQuanLyNhanVien = new JMenuItem("Quản lý nhân viên");

        menuItemThanhToan.addActionListener(e -> openThanhToan());
        menuItemQuanLySanPham.addActionListener(e -> openQuanLySanPham());
        menuItemQuanLyHoaDon.addActionListener(e -> openQuanLyHoaDon());
        menuItemQuanLyNhanVien.addActionListener(e -> openQuanLyNhanVien());

        menuBanHang.add(menuItemThanhToan);
        menuQuanLy.add(menuItemQuanLySanPham);
        menuQuanLy.add(menuItemQuanLyHoaDon);
        menuQuanLy.add(menuItemQuanLyNhanVien);

        menuBar.add(menuBanHang);
        menuBar.add(menuQuanLy);

        setJMenuBar(menuBar);

        // Hiển thị thông tin người đăng nhập
        JLabel welcomeLabel = new JLabel("Xin chào: " + nhanVien.getHoTen(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void openThanhToan() {
        new ThanhToanFrame();
    }

    private void openQuanLySanPham() {
        new QuanLySanPhamFrame();
    }

    private void openQuanLyHoaDon() {
        new QuanLyHoaDonFrame();
    }

    private void openQuanLyNhanVien() {
        new QuanLyNhanVienFrame();
    }
}
