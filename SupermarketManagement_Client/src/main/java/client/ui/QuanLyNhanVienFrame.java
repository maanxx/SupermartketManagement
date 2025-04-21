package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuanLyNhanVienFrame extends JFrame {
    private NhanVienService nhanVienService;
    private JTable tableNhanVien;
    private DefaultTableModel tableModel;
    private JPanel sidePanel;
    private JPanel contentPanel;
    private JLabel statusLabel;
    private Timer animationTimer;

    public QuanLyNhanVienFrame(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;

        // Frame setup
        setTitle("Quản Lý Nhân Viên");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(30, 45, 65), 2));

        // Main container with shadow effect
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
            }
        };
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header with gradient and shadow
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Content panel
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 235, 240)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Table setup
        setupTable();

        // Button panel with animation
        JPanel buttonPanel = createButtonPanel();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Status bar
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.PAGE_END);

        // Load initial data
        loadNhanVienData();

        // Window controls
        addWindowControls(headerPanel);

        setVisible(true);
        animateEntrance();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 60, 85), getWidth(), 0, new Color(25, 40, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRect(0, getHeight() - 5, getWidth(), 5);
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 100));

        JLabel lblHeader = new JLabel("Quản Lý Nhân Viên", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        headerPanel.add(lblHeader, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 15));
        panel.setBackground(new Color(30, 45, 65));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(new EmptyBorder(20, 15, 20, 15));

        String[] menuItems = {"Tổng quan", "Thêm mới", "Cập nhật", "Xóa"};
        Color[] colors = {
                new Color(46, 204, 113),
                new Color(52, 152, 219),
                new Color(241, 196, 15),
                new Color(231, 76, 60)
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createSideButton(menuItems[i], colors[i]);
            final int index = i;
            btn.addActionListener(e -> handleSideMenuClick(index));
            panel.add(btn);
        }

        return panel;
    }

    private JButton createSideButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setFocusPainted(false);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return btn;
    }

    private void setupTable() {
        String[] columnNames = {"Mã NV", "Họ tên", "Chức danh", "Số điện thoại", "Vai trò"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        styleTable(tableNhanVien);

        // Thêm sự kiện nhấp đúp chuột để hiển thị dialog chỉnh sửa
        tableNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) { // Nhấp đúp chuột
                    e.consume();
                    int selectedRow = tableNhanVien.getSelectedRow();
                    if (selectedRow >= 0) {
                        editNhanVien(); // Gọi phương thức chỉnh sửa nhân viên
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableNhanVien) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRect(0, getHeight() - 5, getWidth(), 5);
            }
        };
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(40, 60, 85));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(240, 243, 245));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(40, 60, 85));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                return c;
            }
        };
        renderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, renderer);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row > -1) {
                    table.setRowSelectionInterval(row, row);
                }
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.WHITE);

        JButton btnAdd = createAnimatedButton("Thêm", new Color(46, 204, 113));
        JButton btnEdit = createAnimatedButton("Sửa", new Color(52, 152, 219));
        JButton btnDelete = createAnimatedButton("Xóa", new Color(231, 76, 60));

        btnAdd.addActionListener(e -> animateButtonClick(btnAdd, this::addNhanVien));
        btnEdit.addActionListener(e -> animateButtonClick(btnEdit, this::editNhanVien));
        btnDelete.addActionListener(e -> animateButtonClick(btnDelete, this::deleteNhanVien));

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);

        return panel;
    }

    private JButton createAnimatedButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            private float scale = 1.0f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, baseColor.brighter(), 0, getHeight(), baseColor);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.scale(scale, scale);
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateButtonHover(btn, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateButtonHover(btn, false);
            }
        });

        return btn;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 60, 85));
        panel.setPreferredSize(new Dimension(0, 30));

        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }

    private void addWindowControls(JPanel headerPanel) {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        controlPanel.setOpaque(false);

        JButton btnMinimize = createControlButton("—", new Color(241, 196, 15));
        JButton btnClose = createControlButton("✕", new Color(231, 76, 60));

        btnMinimize.addActionListener(e -> setState(Frame.ICONIFIED));
        btnClose.addActionListener(e -> System.exit(0));

        controlPanel.add(btnMinimize);
        controlPanel.add(btnClose);
        headerPanel.add(controlPanel, BorderLayout.EAST);
    }

    private JButton createControlButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(30, 30));
        return btn;
    }

    private void animateEntrance() {
        setOpacity(0f);
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            float opacity = 0f;
            @Override
            public void run() {
                opacity += 0.1f;
                setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    animationTimer.cancel();
                }
            }
        }, 0, 50);
    }

    private void animateButtonHover(JButton btn, boolean entering) {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            float scale = entering ? 1.0f : 1.1f;
            @Override
            public void run() {
                scale += entering ? 0.02f : -0.02f;
                if (entering && scale >= 1.1f || !entering && scale <= 1.0f) {
                    animationTimer.cancel();
                }
                btn.repaint();
            }
        }, 0, 20);
    }

    private void animateButtonClick(JButton btn, Runnable action) {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            float scale = 1.1f;
            boolean shrinking = true;
            @Override
            public void run() {
                if (shrinking) {
                    scale -= 0.05f;
                    if (scale <= 0.95f) shrinking = false;
                } else {
                    scale += 0.05f;
                    if (scale >= 1.0f) {
                        animationTimer.cancel();
                        action.run();
                    }
                }
                btn.repaint();
            }
        }, 0, 20);
    }

    private void handleSideMenuClick(int index) {
        switch (index) {
            case 0: loadNhanVienData(); break;
            case 1: addNhanVien(); break;
            case 2: editNhanVien(); break;
            case 3: deleteNhanVien(); break;
        }
    }

    private void loadNhanVienData() {
        try {
            statusLabel.setText("Đang tải dữ liệu...");
            List<NhanVienDTO> nhanVienList = nhanVienService.getAllNhanViens();
            tableModel.setRowCount(0);
            for (NhanVienDTO nv : nhanVienList) {
                Object[] row = {nv.getMaNhanVien(), nv.getHoTen(), nv.getChucDanh(), nv.getSoDienThoai(), nv.getRole()};
                tableModel.addRow(row);
            }
            statusLabel.setText("Đã tải " + nhanVienList.size() + " nhân viên");
        } catch (RemoteException e) {
            e.printStackTrace();
            showErrorMessage("Lỗi khi tải dữ liệu!");
        }
    }

    private void addNhanVien() {
        JPanel panel = createInputPanel();
        JTextField txtMaNV = createStyledTextField();
        JTextField txtHoTen = createStyledTextField();
        JTextField txtChucDanh = createStyledTextField();
        JTextField txtSoDienThoai = createStyledTextField();
        JTextField txtRole = createStyledTextField();

        addInputFields(panel, txtMaNV, txtHoTen, txtChucDanh, txtSoDienThoai, txtRole);

        showCustomDialog("Thêm Nhân Viên Mới", panel, () -> {
            try {
                NhanVienDTO newNV = new NhanVienDTO(
                        txtMaNV.getText(), txtHoTen.getText(), "",
                        txtSoDienThoai.getText(), "", txtChucDanh.getText(), txtRole.getText()
                );
                nhanVienService.addNhanVien(newNV);
                loadNhanVienData();
                showSuccessMessage("Thêm nhân viên thành công!");
            } catch (RemoteException e) {
                e.printStackTrace();
                showErrorMessage("Lỗi khi thêm nhân viên!");
            }
        });
    }

    private void editNhanVien() {
        int selectedRow = tableNhanVien.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Vui lòng chọn một nhân viên để sửa!");
            return;
        }

        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            NhanVienDTO nv = nhanVienService.getNhanVienById(maNV);

            JPanel panel = createInputPanel();
            JTextField txtHoTen = createStyledTextField(nv.getHoTen());
            JTextField txtChucDanh = createStyledTextField(nv.getChucDanh());
            JTextField txtSoDienThoai = createStyledTextField(nv.getSoDienThoai());
            JTextField txtRole = createStyledTextField(nv.getRole());

            addInputFields(panel, null, txtHoTen, txtChucDanh, txtSoDienThoai, txtRole);

            showCustomDialog("Sửa Thông Tin Nhân Viên", panel, () -> {
                try {
                    nv.setHoTen(txtHoTen.getText());
                    nv.setChucDanh(txtChucDanh.getText());
                    nv.setSoDienThoai(txtSoDienThoai.getText());
                    nv.setRole(txtRole.getText());
                    nhanVienService.updateNhanVien(nv);
                    loadNhanVienData();
                    showSuccessMessage("Cập nhật thành công!");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    showErrorMessage("Lỗi khi sửa thông tin!");
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            showErrorMessage("Lỗi khi tải thông tin nhân viên!");
        }
    }

    private void deleteNhanVien() {
        int selectedRow = tableNhanVien.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Vui lòng chọn một nhân viên để xóa!");
            return;
        }

        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nhân viên này?",
                "Xác Nhận Xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                nhanVienService.deleteNhanVien(maNV);
                loadNhanVienData();
                showSuccessMessage("Xóa nhân viên thành công!");
            } catch (RemoteException e) {
                e.printStackTrace();
                showErrorMessage("Lỗi khi xóa nhân viên!");
            }
        }
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JTextField createStyledTextField() {
        return createStyledTextField("");
    }

    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text, 15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(40, 60, 85)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 210, 220)),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });
        return field;
    }

    private void addInputFields(JPanel panel, JTextField txtMaNV, JTextField txtHoTen,
                                JTextField txtChucDanh, JTextField txtSoDienThoai, JTextField txtRole) {
        if (txtMaNV != null) {
            panel.add(createStyledLabel("Mã nhân viên:"));
            panel.add(txtMaNV);
        }
        panel.add(createStyledLabel("Họ tên:"));
        panel.add(txtHoTen);
        panel.add(createStyledLabel("Chức danh:"));
        panel.add(txtChucDanh);
        panel.add(createStyledLabel("Số điện thoại:"));
        panel.add(txtSoDienThoai);
        panel.add(createStyledLabel("Vai trò:"));
        panel.add(txtRole);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(40, 60, 85));
        return label;
    }

    private void showCustomDialog(String title, JPanel panel, Runnable onConfirm) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(40, 60, 85), 2));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(40, 60, 85));
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        content.add(titleLabel, BorderLayout.NORTH);

        content.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnConfirm = createAnimatedButton("Xác nhận", new Color(46, 204, 113));
        JButton btnCancel = createAnimatedButton("Hủy", new Color(231, 76, 60));

        btnConfirm.addActionListener(e -> {
            onConfirm.run();
            dialog.dispose();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        content.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        animateDialog(dialog);
    }

    private void animateDialog(JDialog dialog) {
        dialog.setOpacity(0f);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            float opacity = 0f;
            @Override
            public void run() {
                opacity += 0.1f;
                dialog.setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    timer.cancel();
                }
            }
        }, 0, 30);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(message);
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
        statusLabel.setText(message);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        statusLabel.setText(message);
    }
}