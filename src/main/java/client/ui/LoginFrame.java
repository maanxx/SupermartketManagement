package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private final NhanVienService nhanVienService;

    public LoginFrame(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService; // Nhận từ MainClient

        setTitle("Đăng nhập - Quản lý siêu thị");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Hệ thống Quản lý Siêu thị", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        JLabel lblUsername = new JLabel("Mã nhân viên:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblUsername, gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        btnLogin = new JButton("Đăng nhập");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra login với RMI Server
            NhanVienDTO nhanVien = nhanVienService.login(username, password);
            if (nhanVien != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào " + nhanVien.getHoTen());

                // Phân quyền giữa Admin và Nhân viên
                if ("ADMIN".equalsIgnoreCase(nhanVien.getRole())) {
                    new AdminDashboard(nhanVien, nhanVienService);  // Mở giao diện Admin
                } else {
                    new UserDashboard(nhanVien, nhanVienService);  // Mở giao diện Nhân viên
                }

                dispose(); // Đóng cửa sổ đăng nhập
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối server! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
