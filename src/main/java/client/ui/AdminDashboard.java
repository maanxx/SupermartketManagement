package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private NhanVienService nhanVienService;
    private NhanVienDTO loggedInNhanVien;
    private JTable tableNhanVien;

    public AdminDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;

        // Frame settings
        setTitle("🛠 Admin Dashboard - " + nhanVien.getHoTen());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(41, 128, 185));
        sidebar.setPreferredSize(new Dimension(220, 0));

        JButton btnQLSanPham = createSidebarButton(" Quản lý Sản phẩm");
        JButton btnQLHoaDon = createSidebarButton(" Quản lý Hóa đơn");
        JButton btnQLNhanVien = createSidebarButton(" Quản lý Nhân viên");
        JButton btnDangXuat = createSidebarButton(" Đăng xuất");

        btnQLSanPham.addActionListener(e -> openQuanLySanPham());
        btnQLHoaDon.addActionListener(e -> openQuanLyHoaDon());
        btnQLNhanVien.addActionListener(e -> openQuanLyNhanVien());
        btnDangXuat.addActionListener(e -> logout());

        sidebar.add(btnQLSanPham);
        sidebar.add(btnQLHoaDon);
        sidebar.add(btnQLNhanVien);
        sidebar.add(btnDangXuat);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel lblWelcome = new JLabel(" Xin chào, Quản lý: " + nhanVien.getHoTen(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(Color.WHITE);
        headerPanel.add(lblWelcome, BorderLayout.CENTER);

        // Employee table
        String[] columnNames = {"Mã NV", "Họ tên", "Chức danh", "Số điện thoại", "Vai trò"};
        Object[][] sampleData = {
                {"NV01", "Nguyễn Văn A", "Nhân viên bán hàng", "0901234567", "USER"},
                {"NV02", "Trần Thị B", "Nhân viên bán hàng", "0912345678", "USER"},
                {"admin", "J96", "Quản lý cửa hàng", "0987654321", "ADMIN"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(sampleData, columnNames);
        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(30);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableNhanVien.getTableHeader().setBackground(new Color(41, 128, 185));
        tableNhanVien.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);

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

    private void openQuanLySanPham() {
        JOptionPane.showMessageDialog(this, " Chức năng quản lý sản phẩm sẽ được triển khai sau!");
    }

    private void openQuanLyHoaDon() {
        JOptionPane.showMessageDialog(this, " Chức năng quản lý hóa đơn sẽ được triển khai sau!");
    }

    private void openQuanLyNhanVien() {
        JOptionPane.showMessageDialog(this, " Chức năng quản lý nhân viên sẽ được triển khai sau!");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame(MainClient.getNhanVienService()));
        }
    }
}
