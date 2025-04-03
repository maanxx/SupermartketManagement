package client.ui;

import client.MainClient;
import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private final NhanVienService nhanVienService;

    public LoginFrame(NhanVienService nhanVienService) {
        this.nhanVienService = (nhanVienService != null) ? nhanVienService : MainClient.getNhanVienService();

        ImageIcon logoIcon = new ImageIcon("/img/supermarket.png");
        setTitle("Đăng nhập - Quản lý siêu thị");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(logoIcon.getImage());
        setLayout(new GridBagLayout());

        // westSide la hinh anh supermarket
        JPanel imgWest = new JPanel();
        imgWest.setPreferredSize(new Dimension(150, 0));
        JLabel imgLabel = new JLabel(new ImageIcon("/img/ImgFront.png"));
        imgWest.add(imgLabel);

        // eastSide chua noi dung login
        JPanel logiPanel = new JPanel(new GridLayout());


        //btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
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
