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
    private JTextField txtMaNV, txtHoTen, txtSoDienThoai, txtChucVu;
    private JComboBox<String> cbRole;
    private JButton btnThem, btnSua, btnXoa, btnTiepLai, btnLuu;
    private boolean isEditMode = false;
    private String currentEditingMaNV = null;

    public QuanLyNhanVienPanel(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(245, 250, 255));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtHoTen = new JTextField();
        txtChucVu = new JTextField();
        txtSoDienThoai = new JTextField();
        cbRole = new JComboBox<>(new String[]{"user", "admin"});

        formPanel.add(new JLabel("Mã nhân viên:"));
        formPanel.add(txtMaNV);
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Chức vụ:"));
        formPanel.add(txtChucVu);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(txtSoDienThoai);
        formPanel.add(new JLabel("Vai trò:"));
        formPanel.add(cbRole);

        btnLuu = new JButton("Lưu");

        JPanel buttonFormPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonFormPanel.add(btnLuu);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonFormPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Mã NV", "Họ tên", "Chức vụ", "Số điện thoại", "Vai trò"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableNhanVien.setSelectionBackground(new Color(200, 220, 255));
        tableNhanVien.getTableHeader().setReorderingAllowed(false);

        tableNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableNhanVien.getSelectedRow();
                    if (row >= 0) {
                        showEditNhanVienForm(row);
                    }
                }
            }
        });

        add(new JScrollPane(tableNhanVien), BorderLayout.CENTER);

        // Bottom button panel
        btnThem = new JButton("Thêm mới");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnTiepLai = new JButton("Tải lại");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnThem);
        bottomPanel.add(btnSua);
        bottomPanel.add(btnXoa);
        bottomPanel.add(btnTiepLai);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        btnThem.addActionListener(e -> showAddNhanVienForm());
        btnLuu.addActionListener(e -> saveNhanVien());
        btnSua.addActionListener(e -> {
            int row = tableNhanVien.getSelectedRow();
            if (row >= 0) showEditNhanVienForm(row);
        });
        btnXoa.addActionListener(e -> deleteNhanVien());
        btnTiepLai.addActionListener(e -> loadNhanVienData());

        loadNhanVienData();
    }

    private void loadNhanVienData() {
        try {
            List<NhanVienDTO> list = nhanVienService.getAllNhanViens();
            tableModel.setRowCount(0);
            for (NhanVienDTO nv : list) {
                Object[] row = {nv.getMaNhanVien(), nv.getHoTen(), nv.getChucDanh(), nv.getSoDienThoai(), nv.getRole()};
                tableModel.addRow(row);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showAddNhanVienForm() {
        isEditMode = false;
        currentEditingMaNV = null;
        clearForm();
        txtMaNV.setText(generateMaNhanVien());
    }

    private void showEditNhanVienForm(int row) {
        isEditMode = true;
        currentEditingMaNV = tableModel.getValueAt(row, 0).toString();
        txtMaNV.setText(currentEditingMaNV);
        txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
        txtChucVu.setText(tableModel.getValueAt(row, 2).toString());
        txtSoDienThoai.setText(tableModel.getValueAt(row, 3).toString());
        cbRole.setSelectedItem(tableModel.getValueAt(row, 4).toString());
    }

    private void deleteNhanVien() {
        int row = tableNhanVien.getSelectedRow();
        if (row >= 0) {
            String maNV = tableModel.getValueAt(row, 0).toString();
            try {
                nhanVienService.deleteNhanVien(maNV);
                tableModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên.");
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên.");
            }
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtChucVu.setText("");
        txtSoDienThoai.setText("");
        cbRole.setSelectedIndex(0);
    }

    private void saveNhanVien() {
        try {
            NhanVienDTO nv = new NhanVienDTO(
                    txtMaNV.getText(),
                    txtHoTen.getText(),
                    "",
                    txtSoDienThoai.getText(),
                    "",
                    txtChucVu.getText(),
                    cbRole.getSelectedItem().toString()
            );

            if (isEditMode) {
                nhanVienService.updateNhanVien(nv);
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công.");
            } else {
                nhanVienService.addNhanVien(nv);
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công.");
            }
            loadNhanVienData();
            clearForm();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu nhân viên.");
        }
    }

    private String generateMaNhanVien() {
        try {
            List<NhanVienDTO> list = nhanVienService.getAllNhanViens();
            return "NV" + String.format("%04d", list.size() + 1);
        } catch (RemoteException e) {
            return "NV0001";
        }
    }
}
