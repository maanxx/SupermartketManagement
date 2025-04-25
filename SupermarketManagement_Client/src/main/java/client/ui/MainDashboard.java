package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;
import shared.services.HoaDonService;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class MainDashboard extends JFrame {
    private NhanVienDTO loggedInNhanVien; // Lưu nhân viên đã đăng nhập
    private final NhanVienService nhanVienService;
    private final SanPhamService sanPhamService;
    private final HoaDonService hoaDonService;

    public MainDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService, SanPhamService sanPhamService, HoaDonService hoaDonService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;
        this.sanPhamService = sanPhamService;
        this.hoaDonService = hoaDonService;

        setTitle("Hệ thống quản lý siêu thị - " + nhanVien.getHoTen());
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuBanHang = new JMenu("Bán hàng");
        JMenu menuQuanLy = new JMenu("Quản lý");
        JMenu menuTaiKhoan = new JMenu("Tài khoản");

        JMenuItem menuItemThanhToan = new JMenuItem("Thanh toán hóa đơn");
        JMenuItem menuItemQuanLySanPham = new JMenuItem("Quản lý sản phẩm");
        JMenuItem menuItemQuanLyHoaDon = new JMenuItem("Quản lý hóa đơn");
        JMenuItem menuItemQuanLyNhanVien = new JMenuItem("Quản lý nhân viên");
        JMenuItem menuItemDoiMatKhau = new JMenuItem("Đổi mật khẩu");
        JMenuItem menuItemDangXuat = new JMenuItem("Đăng xuất");

        menuItemThanhToan.addActionListener(e -> openThanhToan());
        menuItemQuanLySanPham.addActionListener(e -> openQuanLySanPham());
        menuItemQuanLyHoaDon.addActionListener(e -> openQuanLyHoaDon());
        menuItemQuanLyNhanVien.addActionListener(e -> openQuanLyNhanVien());
        menuItemDoiMatKhau.addActionListener(e -> doiMatKhau());
        menuItemDangXuat.addActionListener(e -> logout());

        menuBanHang.add(menuItemThanhToan);
        menuQuanLy.add(menuItemQuanLySanPham);
        menuQuanLy.add(menuItemQuanLyHoaDon);

        // Chỉ Admin mới có quyền quản lý nhân viên
        if ("ADMIN".equalsIgnoreCase(nhanVien.getRole())) {
            menuQuanLy.add(menuItemQuanLyNhanVien);
        }

        menuTaiKhoan.add(menuItemDoiMatKhau);
        menuTaiKhoan.add(menuItemDangXuat);

        menuBar.add(menuBanHang);
        menuBar.add(menuQuanLy);
        menuBar.add(menuTaiKhoan);

        setJMenuBar(menuBar);

        // Hiển thị thông tin người đăng nhập
        JLabel welcomeLabel = new JLabel("Xin chào: " + nhanVien.getHoTen(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void openThanhToan() {
        JOptionPane.showMessageDialog(this, "Chức năng thanh toán hóa đơn đang phát triển.");
    }

    private void openQuanLySanPham() {
        new QuanLySanPhamPanel(sanPhamService);
    }

    private void openQuanLyHoaDon() {
        JOptionPane.showMessageDialog(this, "Chức năng quản lý hóa đơn đang phát triển.");
    }

    private void openQuanLyNhanVien() {
        if ("ADMIN".equalsIgnoreCase(loggedInNhanVien.getRole())) {
            new QuanLyNhanVienPanel(nhanVienService);
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doiMatKhau() {
        JTextField txtOldPass = new JPasswordField();
        JTextField txtNewPass = new JPasswordField();
        JTextField txtConfirmPass = new JPasswordField();

        Object[] fields = {
                "Mật khẩu cũ:", txtOldPass,
                "Mật khẩu mới:", txtNewPass,
                "Xác nhận mật khẩu mới:", txtConfirmPass
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String oldPass = txtOldPass.getText();
            String newPass = txtNewPass.getText();
            String confirmPass = txtConfirmPass.getText();

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                nhanVienService.updatePassword(loggedInNhanVien.getMaNhanVien(), newPass);
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối máy chủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame(nhanVienService); // Quay lại màn hình đăng nhập
        }
    }
}
