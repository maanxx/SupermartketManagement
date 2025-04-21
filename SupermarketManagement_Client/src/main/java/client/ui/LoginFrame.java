package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private static final String APP_TITLE = "Đăng nhập";
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color GREEN_COLOR = new Color(38, 174, 96);
    private static final Color GRAY_COLOR = new Color(64, 64, 64);
    private static final Color INPUT_BG_COLOR = new Color(230, 245, 230);
    private static final Color LINK_COLOR = new Color(0, 102, 204);
    private static final int SQUARE_SIZE = 360;

    private final JTextField  txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("Đăng nhập");
    private final NhanVienService nhanVienService;

    public LoginFrame(NhanVienService nhanVienService) {
        this.nhanVienService = (nhanVienService != null) ? nhanVienService : MainClient.getNhanVienService();
        initializeUI();
        addPlaceholders();
    }

    private void initializeUI() {
        setTitle(APP_TITLE);
        setSize(740, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        addComponent(mainPanel, createImagePanel(), gbc, 0, 0, 1, 1, GridBagConstraints.WEST);
        addComponent(mainPanel, createFormPanel(), gbc, 1, 0, 1, 1, GridBagConstraints.CENTER);

        add(mainPanel);
        setVisible(true);
        txtUsername.requestFocus();
    }

    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y, int width, int height, int anchor) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = anchor;
        panel.add(comp, gbc);
    }

    private void setSquareShape(JPanel panel) {
        panel.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        panel.setMinimumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
        panel.setMaximumSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        setSquareShape(panel);

        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("/img/ImgFront.png");
        if (icon.getImage() != null) {
            Image image = icon.getImage();
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();
            int maxSize = SQUARE_SIZE - 50;
            double scale = Math.min((double) maxSize / originalWidth, (double) maxSize / originalHeight);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(imageLabel, gbc);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 30));
        setSquareShape(panel);

        // Title in NORTH
        JPanel titlePanel = createTitlePanel();
        panel.add(titlePanel, BorderLayout.NORTH);

        // Form fields in CENTER
        JPanel formFieldsPanel = new JPanel();
        formFieldsPanel.setBackground(BACKGROUND_COLOR);
        formFieldsPanel.setLayout(new BoxLayout(formFieldsPanel, BoxLayout.Y_AXIS));

        formFieldsPanel.add(createLabel("Tài khoản:", new Font("Arial", Font.BOLD, 18), Color.BLACK));
        styleTextField(txtUsername);
        formFieldsPanel.add(txtUsername);
        formFieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formFieldsPanel.add(createLabel("Mật khẩu:", new Font("Arial", Font.BOLD, 18), Color.BLACK));
        stylePasswordField();
        formFieldsPanel.add(txtPassword);
        formFieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formFieldsPanel.add(createForgotPasswordLink());

        panel.add(formFieldsPanel, BorderLayout.CENTER);

        // Login button in SOUTH
        formFieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        styleLoginButton();
        panel.add(btnLogin, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setMaximumSize(new Dimension(SQUARE_SIZE - 40, Integer.MAX_VALUE));
        titlePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel superLabel = createLabel("Super", new Font("Arial", Font.BOLD | Font.ITALIC, 50), GREEN_COLOR);
        JLabel marketLabel = createLabel("Market", new Font("Arial", Font.BOLD | Font.ITALIC, 50), GRAY_COLOR);

        superLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        marketLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        titlePanel.add(superLabel);
        titlePanel.add(marketLabel);

        return titlePanel;
    }
// Quên mật khẩu
    private JLabel createForgotPasswordLink() {
        JLabel forgotPassword = createLabel("Quên mật khẩu ?", new Font("Arial", Font.PLAIN, 12), GREEN_COLOR);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Chức năng quên mật khẩu chưa làm!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        forgotPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        return forgotPassword;
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(INPUT_BG_COLOR);
        field.setBorder(new RoundedBorder(15));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setMaximumSize(new Dimension(SQUARE_SIZE - 40, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void stylePasswordField() {
        txtPassword.setBackground(INPUT_BG_COLOR);
        txtPassword.setBorder(new RoundedBorder(15));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setMaximumSize(new Dimension(SQUARE_SIZE - 40, 40));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword.setLayout(new BorderLayout());
    }

    private void styleLoginButton() {
        btnLogin.setBackground(GREEN_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(150, 45));
        btnLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(GREEN_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(GREEN_COLOR);
            }
        });

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login();
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
    }

    private void addPlaceholders() {
        // Placeholder cho Username
        txtUsername.setForeground(Color.GRAY);
        txtUsername.setText("Nhập tài khoản của bạn");
        txtUsername.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtUsername.getText().equals("Nhập tài khoản của bạn")) {
                    txtUsername.setText("");
                    txtUsername.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtUsername.getText().isEmpty()) {
                    txtUsername.setForeground(Color.GRAY);
                    txtUsername.setText("Nhập tài khoản của bạn");
                }
            }
        });

        // Placeholder cho Password
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setEchoChar((char) 0);
        txtPassword.setText("Nhập mật khẩu của bạn");
        txtPassword.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(txtPassword.getPassword()).equals("Nhập mật khẩu của bạn")) {
                    txtPassword.setText("");
                    txtPassword.setForeground(Color.BLACK);
                    txtPassword.setEchoChar('*');
                }
            }

            public void focusLost(FocusEvent evt) {
                if (String.valueOf(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setForeground(Color.GRAY);
                    txtPassword.setEchoChar((char) 0);
                    txtPassword.setText("Nhập mật khẩu của bạn");
                }
            }
        });
    }


    private void login() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            NhanVienDTO nhanVien = nhanVienService.login(username, password);
            if (nhanVien != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào " + nhanVien.getHoTen());
                if ("ADMIN".equalsIgnoreCase(nhanVien.getRole())) {
                    new AdminDashboard(nhanVien, nhanVienService);
                } else {
                    new UserDashboard(nhanVien, nhanVienService);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối server! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

}

class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;

    public RoundedBorder(int radius) {
        this.radius = radius;
        this.color = new Color(0,0,0,0);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));  // Border thickness
        g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5); // Padding for the border
    }
}





