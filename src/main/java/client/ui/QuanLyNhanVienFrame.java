package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class QuanLyNhanVienFrame extends JFrame {
    private NhanVienService nhanVienService;
    private JTable tableNhanVien;
    private DefaultTableModel tableModel;

    public QuanLyNhanVienFrame(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;

        setTitle("Quản lý nhân viên");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel lblHeader = new JLabel("Quản Lý Nhân Viên", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"Mã NV", "Họ tên", "Chức danh", "Số điện thoại", "Vai trò"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(25);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(tableNhanVien);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = createButton("Thêm");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Xóa");

        btnAdd.addActionListener(e -> addNhanVien());
        btnEdit.addActionListener(e -> editNhanVien());
        btnDelete.addActionListener(e -> deleteNhanVien());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        loadNhanVienData();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void loadNhanVienData() {
        try {
            List<NhanVienDTO> nhanVienList = nhanVienService.getAllNhanViens();
            tableModel.setRowCount(0);
            for (NhanVienDTO nv : nhanVienList) {
                Object[] row = {nv.getMaNhanVien(), nv.getHoTen(), nv.getChucDanh(), nv.getSoDienThoai(), nv.getRole()};
                tableModel.addRow(row);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addNhanVien() {
        JTextField txtMaNV = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtChucDanh = new JTextField();
        JTextField txtSoDienThoai = new JTextField();
        JTextField txtRole = new JTextField();

        Object[] fields = {
                "Mã nhân viên:", txtMaNV,
                "Họ tên:", txtHoTen,
                "Chức danh:", txtChucDanh,
                "Số điện thoại:", txtSoDienThoai,
                "Vai trò:", txtRole
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Thêm nhân viên mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                NhanVienDTO newNV = new NhanVienDTO(txtMaNV.getText(), txtHoTen.getText(), "", txtSoDienThoai.getText(), "", txtChucDanh.getText(), txtRole.getText());
                nhanVienService.addNhanVien(newNV);
                loadNhanVienData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void editNhanVien() {
        int selectedRow = tableNhanVien.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa!");
            return;
        }

        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            NhanVienDTO nv = nhanVienService.getNhanVienById(maNV);

            JTextField txtHoTen = new JTextField(nv.getHoTen());
            JTextField txtChucDanh = new JTextField(nv.getChucDanh());
            JTextField txtSoDienThoai = new JTextField(nv.getSoDienThoai());
            JTextField txtRole = new JTextField(nv.getRole());

            Object[] fields = {
                    "Họ tên:", txtHoTen,
                    "Chức danh:", txtChucDanh,
                    "Số điện thoại:", txtSoDienThoai,
                    "Vai trò:", txtRole
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Sửa nhân viên", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                nv.setHoTen(txtHoTen.getText());
                nv.setChucDanh(txtChucDanh.getText());
                nv.setSoDienThoai(txtSoDienThoai.getText());
                nv.setRole(txtRole.getText());
                nhanVienService.updateNhanVien(nv);
                loadNhanVienData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void deleteNhanVien() {
        int selectedRow = tableNhanVien.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!");
            return;
        }

        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                nhanVienService.deleteNhanVien(maNV);
                loadNhanVienData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
