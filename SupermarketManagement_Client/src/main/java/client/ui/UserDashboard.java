package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserDashboard extends JFrame {
    private final NhanVienService nhanVienService;
    private final SanPhamService sanPhamService;
    private final NhanVienDTO loggedInNhanVien;

    // Colors from App_Main
    private static final Color SIDEBAR_BG_COLOR = new Color(42, 82, 39); // Dark green
    private static final Color BUTTON_BG_COLOR = new Color(59, 132, 52); // Lighter green
    private static final Color BUTTON_HOVER_COLOR = new Color(238, 186, 13);
    private static final Color TITLE_COLOR = new Color(242, 127, 87); // Peach shade for title

    private JPanel viewControlPanel; // Main content area

    public UserDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;
        this.sanPhamService = MainClient.getSanPhamService();

        setTitle("🛒 User Dashboard - " + nhanVien.getHoTen());
        // Set fullscreen
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Main panel with null layout
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BUTTON_BG_COLOR);
        mainPanel.setLayout(null);
        add(mainPanel);

        // Calculate scaled dimensions
        int sidebarWidth = (int) (screenWidth * 0.22);
        int logoPanelHeight = (int) (screenHeight * 0.3077);
        int sidebarHeight = screenHeight;
        int menuPanelHeight = (int) ((sidebarHeight - logoPanelHeight) * 0.6395);
        int contentX = sidebarWidth + 10;
        int contentWidth = screenWidth - sidebarWidth - 20;
        int contentHeight = screenHeight - 20;

        // Sidebar (root panel)
        JPanel sidebarRoot = new JPanel();
        sidebarRoot.setBackground(SIDEBAR_BG_COLOR);
        sidebarRoot.setBounds(0, 0, sidebarWidth, sidebarHeight);
        sidebarRoot.setLayout(null);
        mainPanel.add(sidebarRoot);

        // Logo panel in sidebar
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_BG_COLOR);
        logoPanel.setBounds(10, 11, sidebarWidth - 20, logoPanelHeight);
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Icon on left, text on right
        sidebarRoot.add(logoPanel);

        // Shopping cart icon
        int iconSize = (int) (logoPanelHeight * 0.5); // Icon is 50% of logo panel height
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("/img/cart.png")); // Placeholder path
        ImageIcon icon = (ImageIcon) iconLabel.getIcon();
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        logoPanel.add(iconLabel);

        // "MEGA SUPERMARKET" text
        JPanel textPanel = new JPanel();
        textPanel.setBackground(SIDEBAR_BG_COLOR);
        textPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Small gap between "MEGA" and "SUPERMARKET"
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        logoPanel.add(textPanel);

        int megaFontSize = (int) (40 * (screenWidth / 1000.0)); // Larger font for "MEGA"
        int supermarketFontSize = (int) (20 * (screenWidth / 1000.0)); // Smaller font for "SUPERMARKET"
        JLabel megaLabel = new JLabel("MEGA");
        megaLabel.setForeground(TITLE_COLOR);
        megaLabel.setFont(new Font("Segoe UI", Font.BOLD, megaFontSize));
        textPanel.add(megaLabel);

        JLabel supermarketLabel = new JLabel("SUPERMARKET");
        supermarketLabel.setForeground(Color.WHITE);
        supermarketLabel.setFont(new Font("Segoe UI", Font.BOLD, supermarketFontSize));
        textPanel.add(supermarketLabel);

        // Center the content vertically in logoPanel
        int totalContentHeight = Math.max(iconSize, megaLabel.getPreferredSize().height);
        int verticalPadding = (logoPanelHeight - totalContentHeight) / 2;
        logoPanel.setBorder(BorderFactory.createEmptyBorder(verticalPadding, 0, verticalPadding, 0));

        // Menu panel in sidebar
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_BG_COLOR);
        menuPanel.setBounds(10, logoPanelHeight + 11, sidebarWidth - 20, menuPanelHeight);
        sidebarRoot.add(menuPanel);
        menuPanel.setLayout(null);

        // Sidebar buttons with RoundedPanel
        int buttonHeight = (int) (38 * (screenWidth / 1000.0));
        int buttonWidth = sidebarWidth - 40;
        int buttonSpacing = (menuPanelHeight - (5 * buttonHeight)) / 6;
        RoundedPanel btnBanHangPanel = createSidebarButtonPanel("Bán hàng", "/img/buy.png", 10, buttonSpacing);
        RoundedPanel btnSanPhamPanel = createSidebarButtonPanel("Sản phẩm", "/img/box.png", 10, buttonSpacing + buttonHeight + buttonSpacing);
        RoundedPanel btnHoaDonPanel = createSidebarButtonPanel("Hóa đơn", "/img/invoice.png", 10, buttonSpacing + (buttonHeight + buttonSpacing) * 2);
        RoundedPanel btnThongTinCaNhanPanel = createSidebarButtonPanel("Thông tin cá nhân", "/img/employee.png", 10, buttonSpacing + (buttonHeight + buttonSpacing) * 3);
        RoundedPanel btnDangXuatPanel = createSidebarButtonPanel("Đăng xuất", "/img/logout.png", 10, buttonSpacing + (buttonHeight + buttonSpacing) * 4);

        btnBanHangPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openBanHang();
            }
        });
        btnSanPhamPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openQuanLySanPham();
            }
        });
        btnHoaDonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openQuanLyHoaDon();
            }
        });
        btnThongTinCaNhanPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openThongTinCaNhan();
            }
        });
        btnDangXuatPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
        });

        menuPanel.add(btnBanHangPanel);
        menuPanel.add(btnSanPhamPanel);
        menuPanel.add(btnHoaDonPanel);
        menuPanel.add(btnThongTinCaNhanPanel);
        menuPanel.add(btnDangXuatPanel);

        // Main content area
        viewControlPanel = new RoundedPanel(10);
        viewControlPanel.setLayout(new BorderLayout());
        viewControlPanel.setBounds(contentX, 10, contentWidth, contentHeight);
        mainPanel.add(viewControlPanel);

        // Initial content
        viewControlPanel.add(new ThongKeChartUserPanel(MainClient.getHoaDonService(), MainClient.getSanPhamService()), BorderLayout.CENTER);

        setVisible(true);
    }

    private RoundedPanel createSidebarButtonPanel(String text, String iconPath, int x, int y) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int sidebarWidth = (int) (screenWidth * 0.22);
        int buttonWidth = sidebarWidth - 40;
        int buttonHeight = (int) (38 * (screenWidth / 1000.0));

        RoundedPanel panel = new RoundedPanel(12);
        panel.setBackground(BUTTON_BG_COLOR);
        panel.setBounds(x, y, buttonWidth, buttonHeight);
        panel.setLayout(null);

        int fontSize = (int) (14 * (screenWidth / 1000.0));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        label.setBounds(23, 0, buttonWidth - 23, buttonHeight);
        // Uncomment if you have icons
        // label.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        panel.add(label);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(BUTTON_BG_COLOR);
            }
        });

        return panel;
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
        viewControlPanel.removeAll();
        viewControlPanel.add(new QuanLyBanHangFrame(sanPhamService));
        viewControlPanel.revalidate();
        viewControlPanel.repaint();
    }

    private void openQuanLyHoaDon() {
        viewControlPanel.removeAll();
        viewControlPanel.add(new QuanLyHoaDonFrame());
        viewControlPanel.revalidate();
        viewControlPanel.repaint();
    }

    private void openQuanLySanPham() {
        viewControlPanel.removeAll();
        viewControlPanel.add(new QuanLySanPhamUserFrame(sanPhamService));
        viewControlPanel.revalidate();
        viewControlPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame(MainClient.getNhanVienService()));
        }
    }
}

class RoundedPanel extends JPanel {
    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }
}