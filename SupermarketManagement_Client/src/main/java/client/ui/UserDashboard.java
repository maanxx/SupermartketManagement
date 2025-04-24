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
    public static final Color SIDEBAR_BG_COLOR = new Color(42, 82, 39); // Dark green
    public static final Color BUTTON_BG_COLOR = new Color(59, 132, 52); // Lighter green
    public static final Color BACKGROUND_LOGOUT = new Color(32, 143, 56);

    public static final Color BUTTON_HOVER_COLOR = new Color(238, 186, 13);
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
/////////////////////////////
        // Shopping cart icon
        logoPanel.setLayout(null);

        int iconSize = (int) (logoPanelHeight * 0.3); // Icon is 30% of logo panel height
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("/img/cart.png")); // Placeholder path
        ImageIcon icon = (ImageIcon) iconLabel.getIcon();
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }
        iconLabel.setBounds(0, 0, iconSize, iconSize);
        logoPanel.add(iconLabel);
/// /////////////////////////
        // Mega market
        JPanel textPanel = new JPanel();
        textPanel.setBackground(SIDEBAR_BG_COLOR);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBounds(iconSize + 10, 0, sidebarWidth - iconSize - 30, iconSize);
        logoPanel.add(textPanel);

// Tạo panel con để căn giữa "MEGA"
        JPanel megaPanel = new JPanel();
        megaPanel.setLayout(new BoxLayout(megaPanel, BoxLayout.X_AXIS));
        megaPanel.setBackground(SIDEBAR_BG_COLOR);
// "mega"
        megaPanel.add(Box.createHorizontalGlue());
        JLabel megaLabel = new JLabel("M E G A");
        megaLabel.setForeground(new Color(0xFC, 0xC5, 0x5F));
        megaLabel.setFont(new Font("Arial", Font.PLAIN | Font.ITALIC, 28));
        megaPanel.add(megaLabel);
        textPanel.add(Box.createHorizontalStrut(20));
        megaPanel.add(Box.createHorizontalGlue());

// "SUPERMARKET"
        JPanel supermarketPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        supermarketPanel.setBackground(SIDEBAR_BG_COLOR);
        JLabel supermarketLabel = new JLabel("S U P E R M A R K E T");
        supermarketLabel.setForeground(Color.WHITE);
        supermarketLabel.setFont(new Font("Arial", Font.BOLD, 20));
        supermarketPanel.add(supermarketLabel);

// add
        textPanel.add(megaPanel);
        textPanel.add(supermarketPanel);

//////////////////
        // Tạo panel chứa các menu
        int buttonX = 20;
        int currentY = logoPanelHeight + 20;

        RoundedPanel btnBanHang = createSidebarButtonPanel("Bán Hàng", "img/iconBanHang.png", buttonX, currentY);
        btnBanHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openBanHang();
            }
        });
        sidebarRoot.add(btnBanHang);
        currentY += btnBanHang.getHeight() + 30;

        RoundedPanel btnSanPham = createSidebarButtonPanel("Sản Phẩm", "img/iconSanPham.png", buttonX, currentY);
        btnSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openQuanLySanPham();
            }
        });
        sidebarRoot.add(btnSanPham);
        currentY += btnSanPham.getHeight() + 30;

        RoundedPanel btnKhachHang = createSidebarButtonPanel("Khách Hàng", "img/iconKhachHang.png", buttonX, currentY);
        btnKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng đang phát triển!");
            }
        });
        sidebarRoot.add(btnKhachHang);
        currentY += btnKhachHang.getHeight() + 30;

        RoundedPanel btnNhanVien = createSidebarButtonPanel("Nhân Viên", "img/iconNhanVien.png", buttonX, currentY);
        btnNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openThongTinCaNhan();
            }
        });
        sidebarRoot.add(btnNhanVien);
        currentY += btnNhanVien.getHeight() + 30;

        RoundedPanel btnHoaDon = createSidebarButtonPanel("Hóa Đơn", "img/iconBaoCao.png", buttonX, currentY);
        btnHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openQuanLyHoaDon();
            }
        });
        sidebarRoot.add(btnHoaDon);
        currentY += btnHoaDon.getHeight() + 10;

// Đăng xuất (nằm ở cuối sidebar)
        RoundedPanel btnDangXuat = createSidebarButtonPanel("Đăng Xuất", "img/iconDangXuat.png", buttonX, sidebarHeight - 70);
        btnDangXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
        });
        sidebarRoot.add(btnDangXuat);



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

        int iconSize = buttonHeight - 8;

        // Load icon từ thư mục tuyệt đối/tương đối như bạn đang dùng
        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon(iconPath);
        if (icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }
        // icon trong cái panel button
        iconLabel.setBounds(60, (buttonHeight - iconSize) / 2, iconSize, iconSize);
        panel.add(iconLabel);

        // Text label
        int fontSize = (int) (14 * (screenWidth / 1000.0));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        label.setBounds(iconSize + 70, 0, buttonWidth - iconSize - 30, buttonHeight);
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
        viewControlPanel.add(new QuanLyBanHangFrame(sanPhamService), BorderLayout.CENTER);

        viewControlPanel.revalidate();
        viewControlPanel.repaint();
    }

    private void openQuanLyHoaDon() {
        viewControlPanel.removeAll();
        viewControlPanel.add(new QuanLyHoaDonPanel());
        viewControlPanel.revalidate();
        viewControlPanel.repaint();
    }

    private void openQuanLySanPham() {
        new QuanLySanPhamUserFrame(sanPhamService);
    }


    private void logout() {
        JDialog dialog = new JDialog(this, "Xác nhận đăng xuất", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(0);
        dialogPanel.setBackground(Color.white);
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
                UserDashboard.this.dispose();
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