package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JFrame {
    private final NhanVienService nhanVienService;
    private final SanPhamService sanPhamService;
    private final NhanVienDTO loggedInNhanVien;
    private final CardLayout cardLayout;
    private JPanel viewControlPanel;

    // Colors from UserDashboard
    public static final Color SIDEBAR_BG_COLOR = new Color(42, 82, 39); // Dark green
    public static final Color BUTTON_BG_COLOR = new Color(59, 132, 52); // Lighter green
    public static final Color BUTTON_HOVER_COLOR = new Color(238, 186, 13);

    public AdminDashboard(NhanVienDTO nhanVien, NhanVienService nhanVienService) {
        this.loggedInNhanVien = nhanVien;
        this.nhanVienService = nhanVienService;
        this.sanPhamService = MainClient.getSanPhamService();

        setTitle("🛒 Admin Dashboard - " + nhanVien.getHoTen());
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
        logoPanel.setLayout(null);
        sidebarRoot.add(logoPanel);

        // Shopping cart icon
        int iconSize = (int) (logoPanelHeight * 0.3); // Icon is 30% of logo panel height
        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon("img/cart.png"); // Ensure path is correct
        if (icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }
        iconLabel.setBounds(0, 0, iconSize, iconSize);
        logoPanel.add(iconLabel);

        // Mega market text
        JPanel textPanel = new JPanel();
        textPanel.setBackground(SIDEBAR_BG_COLOR);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBounds(iconSize + 10, 0, sidebarWidth - iconSize - 30, iconSize);
        logoPanel.add(textPanel);

        // "MEGA" panel
        JPanel megaPanel = new JPanel();
        megaPanel.setLayout(new BoxLayout(megaPanel, BoxLayout.X_AXIS));
        megaPanel.setBackground(SIDEBAR_BG_COLOR);
        megaPanel.add(Box.createHorizontalGlue());
        JLabel megaLabel = new JLabel("M E G A");
        megaLabel.setForeground(new Color(0xFC, 0xC5, 0x5F));
        megaLabel.setFont(new Font("Arial", Font.PLAIN | Font.ITALIC, 28));
        megaPanel.add(megaLabel);
        megaPanel.add(Box.createHorizontalGlue());
        textPanel.add(Box.createHorizontalStrut(20));
        textPanel.add(megaPanel);

        // "SUPERMARKET" panel
        JPanel supermarketPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        supermarketPanel.setBackground(SIDEBAR_BG_COLOR);
        JLabel supermarketLabel = new JLabel("S U P E R M A R K E T");
        supermarketLabel.setForeground(Color.WHITE);
        supermarketLabel.setFont(new Font("Arial", Font.BOLD, 20));
        supermarketPanel.add(supermarketLabel);
        textPanel.add(supermarketPanel);

        // Create sidebar buttons
        int buttonX = 20;
        int currentY = logoPanelHeight + 20;

        RoundedPanel btnQLSanPham = createSidebarButtonPanel("Quản lý Sản phẩm", "img/iconSanPham.png", buttonX, currentY);
        btnQLSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel("SANPHAM");
            }
        });
        sidebarRoot.add(btnQLSanPham);
        currentY += btnQLSanPham.getHeight() + 30;

        RoundedPanel btnQLHoaDon = createSidebarButtonPanel("Quản lý Hóa đơn", "img/iconHoaDon.png", buttonX, currentY);
        btnQLHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel("HOADON");
            }
        });
        sidebarRoot.add(btnQLHoaDon);
        currentY += btnQLHoaDon.getHeight() + 30;

        RoundedPanel btnQLNhanVien = createSidebarButtonPanel("Quản lý Nhân viên", "img/iconNhanVien.png", buttonX, currentY);
        btnQLNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel("NHANVIEN");
            }
        });
        sidebarRoot.add(btnQLNhanVien);
        currentY += btnQLNhanVien.getHeight() + 30;

        RoundedPanel btnThongKe = createSidebarButtonPanel("Thống kê", "img/iconThongKe.png", buttonX, currentY);
        btnThongKe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel("DASHBOARD");
            }
        });
        sidebarRoot.add(btnThongKe);
        currentY += btnThongKe.getHeight() + 30;

        RoundedPanel btnDangXuat = createSidebarButtonPanel("Đăng Xuất", "img/iconDangXuat.png", buttonX, sidebarHeight - 70);
        btnDangXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
        });
        sidebarRoot.add(btnDangXuat);

        // Main content area
        cardLayout = new CardLayout();
        viewControlPanel = new RoundedPanel(10);
        viewControlPanel.setLayout(cardLayout);
        viewControlPanel.setBounds(contentX, 10, contentWidth, contentHeight);
        mainPanel.add(viewControlPanel);

        // Add panels to viewControlPanel
        viewControlPanel.add(new ThongKeChartAdminPanel(MainClient.getHoaDonService(), sanPhamService, nhanVienService), "DASHBOARD");

        JPanel hoaDonWrapper = new JPanel(new BorderLayout());
        hoaDonWrapper.setOpaque(false);
        hoaDonWrapper.add(new QuanLyHoaDonPanel(), BorderLayout.CENTER);
        viewControlPanel.add(hoaDonWrapper, "HOADON");

        viewControlPanel.add(new QuanLySanPhamPanel(sanPhamService), "SANPHAM");
        viewControlPanel.add(new QuanLyNhanVienPanel(nhanVienService), "NHANVIEN");

        // Show initial panel
        switchPanel("DASHBOARD");

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

        int iconSize = buttonHeight - 8;

        // Load icon
        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon(iconPath);
        if (icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }
        iconLabel.setBounds(30, (buttonHeight - iconSize) / 2, iconSize, iconSize);
        panel.add(iconLabel);

        // Text label
        int fontSize = (int) (14 * (screenWidth / 1000.0));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        label.setBounds(iconSize + 50, 0, buttonWidth - iconSize - 30, buttonHeight);
        panel.add(label);

        // Hover effects
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

    private void switchPanel(String panelName) {
        cardLayout.show(viewControlPanel, panelName);
    }

    private void logout() {
        JDialog dialog = new JDialog(this, "Xác nhận đăng xuất", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(0);
        dialogPanel.setBackground(Color.WHITE);
        dialogPanel.setLayout(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1));
        dialog.add(dialogPanel);

        ImageIcon icon = new ImageIcon("img/check.png");
        Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn đăng xuất?", scaledIcon, SwingConstants.LEFT);
        messageLabel.setIconTextGap(15);
        messageLabel.setForeground(BUTTON_BG_COLOR);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(SIDEBAR_BG_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        RoundedPanel yesButton = new RoundedPanel(10);
        yesButton.setBackground(new Color(0, 153, 0));
        yesButton.setPreferredSize(new Dimension(100, 36));
        yesButton.setLayout(new BorderLayout());
        JLabel yesLabel = new JLabel("Xác nhận", SwingConstants.CENTER);
        yesLabel.setForeground(Color.WHITE);
        yesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        yesButton.add(yesLabel, BorderLayout.CENTER);
        yesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                yesButton.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                yesButton.setBackground(new Color(0, 153, 0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
                AdminDashboard.this.dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame(MainClient.getNhanVienService()));
            }
        });
        buttonPanel.add(yesButton);

        RoundedPanel noButton = new RoundedPanel(10);
        noButton.setBackground(new Color(0, 153, 0));
        noButton.setPreferredSize(new Dimension(100, 36));
        noButton.setLayout(new BorderLayout());
        JLabel noLabel = new JLabel("Từ chối", SwingConstants.CENTER);
        noLabel.setForeground(Color.WHITE);
        noLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noButton.add(noLabel, BorderLayout.CENTER);
        noButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                noButton.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                noButton.setBackground(new Color(0, 153, 0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(noButton);

        dialog.setVisible(true);
    }
}
