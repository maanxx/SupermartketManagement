package client.ui;

import shared.dto.LoaiSanPhamDTO;
import shared.dto.NhaCungCapDTO;
import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;

public class QuanLySanPhamFrame extends JFrame {
    private final SanPhamService sanPhamService;
    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboSort;

    public QuanLySanPhamFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;

        setTitle("Quản Lý Sản Phẩm");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Header Panel với gradient nền
//        JPanel headerPanel = new GradientHeaderPanel("Quản Lý Sản Phẩm");
//        headerPanel.setPreferredSize(new Dimension(1000, 80));
//        add(headerPanel, BorderLayout.NORTH);
        setTitle("Sản pham");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        JButton btnSearch = createButton("🔍 Tìm kiếm");
        btnSearch.addActionListener(e -> searchProduct());
        cboSort = new JComboBox<>(new String[]{"Sắp xếp theo", "Loại sản phẩm", "Giá tăng dần", "Giá giảm dần"});
        cboSort.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboSort.addActionListener(e -> sortProduct());
        filterPanel.add(lblSearch);
        filterPanel.add(txtSearch);
        filterPanel.add(btnSearch);
        filterPanel.add(new JLabel("Sắp xếp:"));
        filterPanel.add(cboSort);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại sản phẩm", "Nhà cung cấp", "Giá", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(30);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(new Color(41, 128, 185));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableSanPham.getColumnCount(); i++) {
            tableSanPham.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Xóa");

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        loadProductData();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(31, 102, 170));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
        return button;
    }

    private void loadProductData() {
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            tableModel.setRowCount(0);
            for (SanPhamDTO sp : productList) {
                tableModel.addRow(new Object[]{
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        sp.getMaLoaiSanPham(),
                        sp.getMaNhaCungCap(),
                        sp.getGia(),
                        sp.getSoLuong()
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void searchProduct() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            tableModel.setRowCount(0);
            for (SanPhamDTO sp : productList) {
                if (sp.getTenSanPham().toLowerCase().contains(keyword)) {
                    tableModel.addRow(new Object[]{
                            sp.getMaSanPham(),
                            sp.getTenSanPham(),
                            sp.getMaLoaiSanPham(),
                            sp.getMaNhaCungCap(),
                            sp.getGia(),
                            sp.getSoLuong()
                    });
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sortProduct() {
        String selectedSort = (String) cboSort.getSelectedItem();
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            if ("Giá tăng dần".equals(selectedSort)) {
                productList.sort(Comparator.comparingDouble(SanPhamDTO::getGia));
            } else if ("Giá giảm dần".equals(selectedSort)) {
                productList.sort(Comparator.comparingDouble(SanPhamDTO::getGia).reversed());
            }
            tableModel.setRowCount(0);
            for (SanPhamDTO sp : productList) {
                tableModel.addRow(new Object[]{
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        sp.getMaLoaiSanPham(),
                        sp.getMaNhaCungCap(),
                        sp.getGia(),
                        sp.getSoLuong()
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        try {
            List<LoaiSanPhamDTO> loaiSanPhamList = sanPhamService.getAllLoaiSanPham();
            List<NhaCungCapDTO> nhaCungCapList = sanPhamService.getAllNhaCungCap();

            JComboBox<String> cboLoaiSanPham = new JComboBox<>();
            JComboBox<String> cboNhaCungCap = new JComboBox<>();

            for (LoaiSanPhamDTO loai : loaiSanPhamList) {
                cboLoaiSanPham.addItem(loai.getMaLoai() + " - " + loai.getTenLoai());
            }
            for (NhaCungCapDTO ncc : nhaCungCapList) {
                cboNhaCungCap.addItem(ncc.getMaNhaCungCap() + " - " + ncc.getTenNhaCungCap());
            }

            JTextField txtMaSP = new JTextField();
            JTextField txtTenSP = new JTextField();
            JTextField txtGia = new JTextField();
            JTextField txtSoLuong = new JTextField();

            Object[] fields = {
                    "Mã sản phẩm:", txtMaSP,
                    "Tên sản phẩm:", txtTenSP,
                    "Loại sản phẩm:", cboLoaiSanPham,
                    "Nhà cung cấp:", cboNhaCungCap,
                    "Giá:", txtGia,
                    "Số lượng:", txtSoLuong
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Thêm sản phẩm mới", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String selectedLoai = (String) cboLoaiSanPham.getSelectedItem();
                String selectedNCC = (String) cboNhaCungCap.getSelectedItem();
                if (selectedLoai != null && selectedNCC != null) {
                    String maLoai = selectedLoai.split(" - ")[0];
                    String maNhaCungCap = selectedNCC.split(" - ")[0];
                    SanPhamDTO newProduct = new SanPhamDTO(
                            txtMaSP.getText(),
                            txtTenSP.getText(),
                            maNhaCungCap,
                            maLoai,
                            Double.parseDouble(txtGia.getText()),
                            Integer.parseInt(txtSoLuong.getText())
                    );
                    sanPhamService.addSanPham(newProduct);
                    loadProductData();
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm và nhà cung cấp hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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
