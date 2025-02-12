package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class QuanLySanPhamFrame extends JFrame {
    private SanPhamService sanPhamService;
    private JTable tableSanPham;
    private DefaultTableModel tableModel;

    public QuanLySanPhamFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;

        // Cấu hình cửa sổ chính
        setTitle("Quản lý sản phẩm");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel lblHeader = new JLabel("Quản Lý Sản Phẩm", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Bảng hiển thị sản phẩm
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại sản phẩm", "Nhà cung cấp", "Giá", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(25);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(new Color(41, 128, 185));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = createButton("Thêm");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Xóa");

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tải dữ liệu sản phẩm lên bảng
        loadProductData();

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

    private void loadProductData() {
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            for (SanPhamDTO sp : productList) {
                Object[] row = {
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        sp.getMaLoaiSanPham(),
                        sp.getMaNhaCungCap(),
                        sp.getGia(),
                        sp.getSoLuong()
                };
                tableModel.addRow(row);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        JTextField txtMaSP = new JTextField();
        JTextField txtTenSP = new JTextField();
        JTextField txtLoai = new JTextField();
        JTextField txtNhaCungCap = new JTextField();
        JTextField txtGia = new JTextField();
        JTextField txtSoLuong = new JTextField();

        Object[] fields = {
                "Mã sản phẩm:", txtMaSP,
                "Tên sản phẩm:", txtTenSP,
                "Loại sản phẩm:", txtLoai,
                "Nhà cung cấp:", txtNhaCungCap,
                "Giá:", txtGia,
                "Số lượng:", txtSoLuong
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Thêm sản phẩm mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                SanPhamDTO newProduct = new SanPhamDTO(
                        txtMaSP.getText(),
                        txtTenSP.getText(),
                        txtNhaCungCap.getText(),
                        txtLoai.getText(),
                        Double.parseDouble(txtGia.getText()),
                        Integer.parseInt(txtSoLuong.getText())
                );
                sanPhamService.addSanPham(newProduct);
                loadProductData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void editProduct() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để sửa!");
            return;
        }

        String maSP = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            SanPhamDTO product = sanPhamService.getSanPhamById(maSP);

            JTextField txtTenSP = new JTextField(product.getTenSanPham());
            JTextField txtLoai = new JTextField(product.getMaLoaiSanPham());
            JTextField txtNhaCungCap = new JTextField(product.getMaNhaCungCap());
            JTextField txtGia = new JTextField(String.valueOf(product.getGia()));
            JTextField txtSoLuong = new JTextField(String.valueOf(product.getSoLuong()));

            Object[] fields = {
                    "Tên sản phẩm:", txtTenSP,
                    "Loại sản phẩm:", txtLoai,
                    "Nhà cung cấp:", txtNhaCungCap,
                    "Giá:", txtGia,
                    "Số lượng:", txtSoLuong
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Sửa sản phẩm", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                product.setTenSanPham(txtTenSP.getText());
                product.setMaLoaiSanPham(txtLoai.getText());
                product.setMaNhaCungCap(txtNhaCungCap.getText());
                product.setGia(Double.parseDouble(txtGia.getText()));
                product.setSoLuong(Integer.parseInt(txtSoLuong.getText()));

                sanPhamService.updateSanPham(product);
                loadProductData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xóa!");
            return;
        }

        String maSP = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                sanPhamService.deleteSanPham(maSP);
                loadProductData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
