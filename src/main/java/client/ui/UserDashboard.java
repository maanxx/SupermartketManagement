package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        sidebar.setLayout(new GridLayout(4, 1, 10, 10));
        sidebar.setBackground(new Color(41, 128, 185));
        sidebar.setPreferredSize(new Dimension(220, 0));

        JButton btnBanHang = createSidebarButton(" Bán hàng");
        JButton btnSanPham = createSidebarButton(" Sản phẩm");
        JButton btnHoaDon = createSidebarButton(" Hóa đơn");
        JButton btnDangXuat = createSidebarButton(" Đăng xuất");

        btnBanHang.addActionListener(e -> openThanhToan());
        btnSanPham.addActionListener(e -> openQuanLySanPham());
        btnDangXuat.addActionListener(e -> logout());

        sidebar.add(btnBanHang);
        sidebar.add(btnSanPham);
        sidebar.add(btnHoaDon);
        sidebar.add(btnDangXuat);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel lblWelcome = new JLabel(" Xin chào, " + nhanVien.getHoTen(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(Color.WHITE);
        headerPanel.add(lblWelcome, BorderLayout.CENTER);

        // Product table
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại", "Giá", "Số lượng"};
        Object[][] sampleData = {
                {"SP01", "Gạo ST25", "Thực phẩm", "120.000", "50"},
                {"SP02", "Thịt bò Úc", "Thực phẩm", "250.000", "30"},
                {"SP03", "Bột giặt OMO", "Nhu yếu phẩm", "90.000", "40"},
                {"SP04", "Nồi cơm điện", "Đồ gia dụng", "750.000", "10"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(sampleData, columnNames);
        JTable tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(30);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableSanPham.getTableHeader().setBackground(new Color(41, 128, 185));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tableSanPham);

        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

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

    private void openThanhToan() {
        JOptionPane.showMessageDialog(this, " Chức năng thanh toán hóa đơn sẽ được triển khai sau!");
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
