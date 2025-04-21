package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class QuanLyNhanVienPanel extends JPanel {
    private final NhanVienService nhanVienService;
    private JTable tableNhanVien;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JPanel formNhanVienPanel;
    private JTextField txtMaNV, txtHoTen, txtSoDienThoai;
    private JComboBox<String> cbChucDanh, cbRole;
    private JButton btnOK, btnCancel;
    private boolean isEditMode = false;
    private String currentEditingMaNV = null;

    public QuanLyNhanVienPanel(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 250));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 235, 240)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        add(contentPanel, BorderLayout.CENTER);

        setupTable();
        contentPanel.add(new JScrollPane(tableNhanVien), BorderLayout.CENTER);

        JPanel topPanel = createTopPanel();
        contentPanel.add(topPanel, BorderLayout.NORTH);

        add(createStatusPanel(), BorderLayout.SOUTH);
        loadNhanVienData();

        formNhanVienPanel = createFormNhanVienPanel();
        formNhanVienPanel.setVisible(false);
        contentPanel.add(formNhanVienPanel, BorderLayout.SOUTH);
    }

    private void setupTable() {
        String[] columnNames = {"Mã NV", "Họ tên", "Chức danh", "Số điện thoại", "Vai trò"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(40);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableNhanVien.setSelectionBackground(new Color(220, 240, 255));
        tableNhanVien.setGridColor(new Color(230, 230, 230));

        tableNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    int selectedRow = tableNhanVien.getSelectedRow();
                    if (selectedRow >= 0) {
                        showEditNhanVienForm(selectedRow);
                    }
                }
            }
        });
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Thêm Nhân Viên");
        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAdd.setBackground(new Color(30, 144, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddNhanVienForm());
        panel.add(btnAdd);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 60, 85));
        panel.setPreferredSize(new Dimension(0, 30));

        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }

    private void loadNhanVienData() {
        try {
            statusLabel.setText("Đang tải dữ liệu...");
            List<NhanVienDTO> list = nhanVienService.getAllNhanViens();
            tableModel.setRowCount(0);
            for (NhanVienDTO nv : list) {
                tableModel.addRow(new Object[]{nv.getMaNhanVien(), nv.getHoTen(), nv.getChucDanh(), nv.getSoDienThoai(), nv.getRole()});
            }
            statusLabel.setText("Đã tải " + list.size() + " nhân viên");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createFormNhanVienPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(400, 300));

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);

        txtHoTen = new JTextField();
        cbChucDanh = new JComboBox<>(new String[]{"Quản lý cửa hàng", "Nhân viên bán hàng"});
        cbRole = new JComboBox<>(new String[]{"User", "Admin"});
        txtSoDienThoai = new JTextField();

        cbChucDanh.addActionListener(e -> {
            if (!isEditMode) {
                txtMaNV.setText(generateMaNhanVien(cbChucDanh.getSelectedItem().toString()));
            }

            if (cbChucDanh.getSelectedItem().toString().equals("Quản lý cửa hàng")) {
                cbRole.setSelectedItem("Admin");
            } else {
                cbRole.setSelectedItem("User");
            }
        });

        panel.add(new JLabel("Mã nhân viên:"));
        panel.add(txtMaNV);
        panel.add(new JLabel("Họ tên:"));
        panel.add(txtHoTen);
        panel.add(new JLabel("Chức danh:"));
        panel.add(cbChucDanh);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(txtSoDienThoai);
        panel.add(new JLabel("Vai trò:"));
        panel.add(cbRole);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        btnOK = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOK.setBackground(new Color(30, 144, 255));
        btnOK.setForeground(Color.WHITE);
        btnCancel.setBackground(Color.RED);
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);

        btnOK.addActionListener(e -> handleFormOK());
        btnCancel.addActionListener(e -> {
            formNhanVienPanel.setVisible(false);
            tableNhanVien.setVisible(true);
        });

        panel.add(buttonPanel);
        return panel;
    }

    private void handleFormOK() {
        try {
            NhanVienDTO nv = new NhanVienDTO(
                    txtMaNV.getText(),
                    txtHoTen.getText(),
                    "",
                    txtSoDienThoai.getText(),
                    "",
                    cbChucDanh.getSelectedItem().toString(),
                    cbRole.getSelectedItem().toString()
            );

            if (isEditMode) {
                nhanVienService.updateNhanVien(nv);
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                nhanVienService.addNhanVien(nv);
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }

            loadNhanVienData();
            formNhanVienPanel.setVisible(false);
            tableNhanVien.setVisible(true);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xử lý!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateMaNhanVien(String chucDanh) {
        try {
            List<NhanVienDTO> list = nhanVienService.getAllNhanViens();
            int count = (int) list.stream().filter(nv -> nv.getChucDanh().equals(chucDanh)).count();
            String prefix = chucDanh.equals("Quản lý cửa hàng") ? "ADMIN" : "USER";
            return prefix + String.format("%04d", count + 1);
        } catch (RemoteException e) {
            return chucDanh.equals("Quản lý cửa hàng") ? "ADMIN0001" : "USER0001";
        }
    }

    private void showAddNhanVienForm() {
        isEditMode = false;
        currentEditingMaNV = null;

        cbChucDanh.setSelectedIndex(0); // Gọi lại action listener để tự sinh mã
        txtHoTen.setText("");
        txtSoDienThoai.setText("");

        tableNhanVien.setVisible(false);
        formNhanVienPanel.setVisible(true);
    }

    private void showEditNhanVienForm(int selectedRow) {
        isEditMode = true;
        currentEditingMaNV = tableModel.getValueAt(selectedRow, 0).toString();

        txtMaNV.setText(currentEditingMaNV);
        txtHoTen.setText(tableModel.getValueAt(selectedRow, 1).toString());
        cbChucDanh.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
        txtSoDienThoai.setText(tableModel.getValueAt(selectedRow, 3).toString());
        cbRole.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());

        tableNhanVien.setVisible(false);
        formNhanVienPanel.setVisible(true);
    }
}
