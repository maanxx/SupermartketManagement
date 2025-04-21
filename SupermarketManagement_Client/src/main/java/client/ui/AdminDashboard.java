package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private final NhanVienService nhanVienService;
    private final SanPhamService sanPhamService;
    private final NhanVienDTO loggedInNhanVien;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public AdminDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;
        this.sanPhamService = MainClient.getSanPhamService();

        setTitle("Admin Dashboard - " + nhanVien.getHoTen());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSidebarPanel(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        // Thêm các panel vào mainPanel
        mainPanel.add(new ThongKeChartAdminPanel(MainClient.getHoaDonService(), sanPhamService, nhanVienService), "DASHBOARD");

        JPanel hoaDonWrapper = new JPanel(new GridBagLayout());
        hoaDonWrapper.setOpaque(false);
        hoaDonWrapper.add(new QuanLyHoaDonPanel());
        mainPanel.add(hoaDonWrapper, "HOADON");

        // Panel quản lý sản phẩm
        mainPanel.add(new QuanLySanPhamPanel(sanPhamService), "SANPHAM");
        // Panel quản lý nhân viên
        mainPanel.add(new QuanLyNhanVienPanel(nhanVienService), "NHANVIEN");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 73, 94), w, h, new Color(41, 128, 185));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        header.setPreferredSize(new Dimension(0, 100)); // Chiều rộng linh hoạt
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Thêm logo hoặc tên hệ thống
        JLabel lblLogo = new JLabel(new ImageIcon("path_to_logo_image"));
        lblLogo.setPreferredSize(new Dimension(100, 100));

        // Xin chào admin
        JLabel lblWelcome = new JLabel("Xin chào, Quản lý: " + loggedInNhanVien.getHoTen(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblLogo, BorderLayout.WEST);
        topPanel.add(lblWelcome, BorderLayout.CENTER);

        header.add(topPanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 15, 15));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton btnQLSanPham = createSidebarButton("Quản lý Sản phẩm");
        JButton btnQLHoaDon = createSidebarButton("Quản lý Hóa đơn");
        JButton btnQLNhanVien = createSidebarButton("Quản lý Nhân viên");
        JButton btnThongKe = createSidebarButton("Thống kê");
        JButton btnDangXuat = createSidebarButton("Đăng xuất");

        btnQLSanPham.addActionListener(e -> switchPanel("SANPHAM"));
        btnQLHoaDon.addActionListener(e -> switchPanel("HOADON"));
        btnQLNhanVien.addActionListener(e -> switchPanel("NHANVIEN"));
        btnThongKe.addActionListener(e -> switchPanel("DASHBOARD"));
        btnDangXuat.addActionListener(e -> logout());

        sidebar.add(btnQLSanPham);
        sidebar.add(btnQLHoaDon);
        sidebar.add(btnQLNhanVien);
        sidebar.add(btnThongKe);
        sidebar.add(btnDangXuat);

        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm hiệu ứng hover
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

    private void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame(MainClient.getNhanVienService()));
        }
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, h, new Color(224, 238, 255));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }


}
