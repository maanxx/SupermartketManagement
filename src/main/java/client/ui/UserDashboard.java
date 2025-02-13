package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {
    private final NhanVienService nhanVienService;
    private final SanPhamService sanPhamService;
    private final NhanVienDTO loggedInNhanVien;

    public UserDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;
        this.sanPhamService = MainClient.getSanPhamService();

        setTitle("🛒 User Dashboard - " + nhanVien.getHoTen());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(41, 128, 185));
        sidebar.setPreferredSize(new Dimension(220, 0));

        JButton btnBanHang = createSidebarButton(" Bán hàng");
        JButton btnSanPham = createSidebarButton(" Sản phẩm");
        JButton btnHoaDon = createSidebarButton(" Hóa đơn");
        JButton btnThongTinCaNhan = createSidebarButton(" Thông tin cá nhân");
        JButton btnDangXuat = createSidebarButton(" Đăng xuất");

        btnBanHang.addActionListener(e -> openBanHang());
        btnSanPham.addActionListener(e -> openQuanLySanPham());
        btnThongTinCaNhan.addActionListener(e -> openThongTinCaNhan());
        btnDangXuat.addActionListener(e -> logout());

        sidebar.add(btnBanHang);
        sidebar.add(btnSanPham);
        sidebar.add(btnHoaDon);
        sidebar.add(btnThongTinCaNhan);
        sidebar.add(btnDangXuat);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel lblWelcome = new JLabel(" Xin chào, " + nhanVien.getHoTen(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(Color.WHITE);
        headerPanel.add(lblWelcome, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
        return button;
    }

    private void openThongTinCaNhan() {
        JDialog dialog = new JDialog(this, "Thông tin cá nhân", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        dialog.add(new JLabel("Mã Nhân Viên:"));
        dialog.add(new JLabel(loggedInNhanVien.getMaNhanVien()));
        dialog.add(new JLabel("Họ Tên:"));
        dialog.add(new JLabel(loggedInNhanVien.getHoTen()));
        dialog.add(new JLabel("Số Điện Thoại:"));
        dialog.add(new JLabel(loggedInNhanVien.getSoDienThoai()));
        dialog.add(new JLabel("Chức Danh:"));
        dialog.add(new JLabel(loggedInNhanVien.getChucDanh()));
        dialog.add(new JLabel("Vai Trò:"));
        dialog.add(new JLabel(loggedInNhanVien.getRole()));

        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        dialog.add(new JLabel());
        dialog.add(btnDoiMatKhau);

        dialog.setVisible(true);
    }

    private void doiMatKhau() {
        JPasswordField newPasswordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this, newPasswordField, "Nhập mật khẩu mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPasswordField.getPassword()).trim();
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                nhanVienService.updatePassword(loggedInNhanVien.getMaNhanVien(), newPassword);
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi đổi mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openBanHang() {
        new QuanLyBanHangFrame(sanPhamService);
    }


    private void openQuanLySanPham() {
        new QuanLySanPhamFrame(sanPhamService);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame(MainClient.getNhanVienService()));
        }
    }
}
