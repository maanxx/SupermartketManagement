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
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;

public class QuanLySanPhamPanel extends JPanel {
    private final SanPhamService sanPhamService;
    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboSort;

    public QuanLySanPhamPanel(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

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

        add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"Hình ảnh", "Mã SP", "Tên sản phẩm", "Loại", "Nhà cung cấp", "Giá", "Số lượng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(tableModel);
        tableSanPham.setRowHeight(65);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(new Color(41, 128, 185));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);

        // Custom renderer cho cột ảnh
        tableSanPham.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel();
                lbl.setHorizontalAlignment(JLabel.CENTER);
                if (value instanceof ImageIcon) {
                    lbl.setIcon((ImageIcon) value);
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

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
        add(buttonPanel, BorderLayout.SOUTH);

        loadProductData();
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
            // Tạo thư mục client_images nếu chưa có
            File imageFolder = new File("client_images");
            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            tableModel.setRowCount(0);

            for (SanPhamDTO sp : productList) {
                ImageIcon icon = null;
                String fileName = sp.getHinhAnh();
                File localImgFile = new File("client_images/" + fileName);

                //  Nếu ảnh chưa có thì tải từ server
                if (!localImgFile.exists() && fileName != null && !fileName.isEmpty()) {
                    try {
                        byte[] imageBytes = sanPhamService.downloadHinhAnh(fileName);
                        java.nio.file.Files.write(localImgFile.toPath(), imageBytes);
                    } catch (Exception e) {
                        System.err.println(" Không thể tải ảnh: " + fileName);
                    }
                }

                //  Tạo icon nếu có ảnh
                if (localImgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(localImgFile.getAbsolutePath());
                    Image scaledImg = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImg);
                }

                // Thêm dòng vào bảng
                tableModel.addRow(new Object[]{
                        icon,
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
                    ImageIcon icon = loadImageIcon(sp.getHinhAnh());
                    tableModel.addRow(new Object[]{
                            icon,
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
                ImageIcon icon = loadImageIcon(sp.getHinhAnh());
                tableModel.addRow(new Object[]{
                        icon,
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
    public class ThemSanPhamPanel extends JPanel {
        public ThemSanPhamPanel(SanPhamService sanPhamService, Runnable onSuccess) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            setBackground(Color.WHITE);

            JTextField txtMaSP = new JTextField(20);
            JTextField txtTenSP = new JTextField(20);
            JTextField txtGia = new JTextField(20);
            JTextField txtSoLuong = new JTextField(20);

            JComboBox<String> cboLoai = new JComboBox<>();
            JComboBox<String> cboNCC = new JComboBox<>();

            JLabel lblAnh = new JLabel("Chưa chọn ảnh");
            JButton btnChonAnh = new JButton("Chọn ảnh");
            final byte[][] imageData = new byte[1][];
            final String[] imageFileName = new String[1];

            btnChonAnh.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    imageFileName[0] = file.getName();
                    lblAnh.setText(file.getName());
                    try {
                        imageData[0] = java.nio.file.Files.readAllBytes(file.toPath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Load dữ liệu loại và NCC
            try {
                for (LoaiSanPhamDTO l : sanPhamService.getAllLoaiSanPham()) {
                    cboLoai.addItem(l.getMaLoai() + " - " + l.getTenLoai());
                }
                for (NhaCungCapDTO ncc : sanPhamService.getAllNhaCungCap()) {
                    cboNCC.addItem(ncc.getMaNhaCungCap() + " - " + ncc.getTenNhaCungCap());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JButton btnLuu = new JButton("Lưu");
            btnLuu.addActionListener(e -> {
                try {
                    SanPhamDTO sp = new SanPhamDTO(
                            txtMaSP.getText(),
                            txtTenSP.getText(),
                            cboNCC.getSelectedItem().toString().split(" - ")[0],
                            cboLoai.getSelectedItem().toString().split(" - ")[0],
                            Double.parseDouble(txtGia.getText()),
                            Integer.parseInt(txtSoLuong.getText()),
                            imageFileName[0]
                    );
                    sanPhamService.addSanPham(sp);
                    if (imageData[0] != null) {
                        sanPhamService.uploadHinhAnh(imageFileName[0], imageData[0]);
                    }
                    JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm thành công!");
                    onSuccess.run(); // Quay lại panel danh sách
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            });

            add(new JLabel("Mã sản phẩm:")); add(txtMaSP);
            add(new JLabel("Tên sản phẩm:")); add(txtTenSP);
            add(new JLabel("Loại sản phẩm:")); add(cboLoai);
            add(new JLabel("Nhà cung cấp:")); add(cboNCC);
            add(new JLabel("Giá:")); add(txtGia);
            add(new JLabel("Số lượng:")); add(txtSoLuong);
            add(btnChonAnh); add(lblAnh);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(btnLuu);
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
            JButton btnChonAnh = new JButton("Chọn ảnh");
            JLabel lblTenAnh = new JLabel("Chưa chọn ảnh");
            final byte[][] imageData = new byte[1][];
            final String[] imageFileName = new String[1];

            btnChonAnh.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    lblTenAnh.setText(selectedFile.getName());
                    imageFileName[0] = selectedFile.getName();
                    try {
                        imageData[0] = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi đọc ảnh: " + ex.getMessage());
                    }
                }
            });

            Object[] fields = {
                    "Mã sản phẩm:", txtMaSP,
                    "Tên sản phẩm:", txtTenSP,
                    "Loại sản phẩm:", cboLoaiSanPham,
                    "Nhà cung cấp:", cboNhaCungCap,
                    "Giá:", txtGia,
                    "Số lượng:", txtSoLuong,
                    btnChonAnh, lblTenAnh
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
                            Integer.parseInt(txtSoLuong.getText()),
                            imageFileName[0] // tên ảnh
                    );
                    sanPhamService.addSanPham(newProduct);
                    if (imageData[0] != null && imageFileName[0] != null) {
                        sanPhamService.uploadHinhAnh(imageFileName[0], imageData[0]);
                    }
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
        String maSP = tableModel.getValueAt(selectedRow, 1).toString();
        try {
            SanPhamDTO product = sanPhamService.getSanPhamById(maSP);

            JTextField txtTenSP = new JTextField(product.getTenSanPham());
            JTextField txtLoai = new JTextField(product.getMaLoaiSanPham());
            JTextField txtNhaCungCap = new JTextField(product.getMaNhaCungCap());
            JTextField txtGia = new JTextField(String.valueOf(product.getGia()));
            JTextField txtSoLuong = new JTextField(String.valueOf(product.getSoLuong()));

            JButton btnChonAnh = new JButton("Chọn ảnh mới");
            JLabel lblTenAnh = new JLabel(product.getHinhAnh() != null ? product.getHinhAnh() : "Chưa có ảnh");
            final byte[][] imageData = new byte[1][];
            final String[] imageFileName = new String[]{product.getHinhAnh()};

            btnChonAnh.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    lblTenAnh.setText(selectedFile.getName());
                    imageFileName[0] = selectedFile.getName();
                    try {
                        imageData[0] = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi đọc ảnh: " + ex.getMessage());
                    }
                }
            });

            Object[] fields = {
                    "Tên sản phẩm:", txtTenSP,
                    "Loại sản phẩm:", txtLoai,
                    "Nhà cung cấp:", txtNhaCungCap,
                    "Giá:", txtGia,
                    "Số lượng:", txtSoLuong,
                    btnChonAnh, lblTenAnh
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Sửa sản phẩm", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                product.setTenSanPham(txtTenSP.getText());
                product.setMaLoaiSanPham(txtLoai.getText());
                product.setMaNhaCungCap(txtNhaCungCap.getText());
                product.setGia(Double.parseDouble(txtGia.getText()));
                product.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                product.setHinhAnh(imageFileName[0]);

                sanPhamService.updateSanPham(product);
                if (imageData[0] != null && imageFileName[0] != null) {
                    sanPhamService.uploadHinhAnh(imageFileName[0], imageData[0]);
                }
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
        String maSP = tableModel.getValueAt(selectedRow, 1).toString();
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
    private ImageIcon loadImageIcon(String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;
        File localImgFile = new File("client_images/" + fileName);

        // Nếu ảnh chưa tồn tại → tải từ server
        if (!localImgFile.exists()) {
            try {
                byte[] imageBytes = sanPhamService.downloadHinhAnh(fileName);
                java.nio.file.Files.write(localImgFile.toPath(), imageBytes);
            } catch (Exception e) {
                System.err.println("❌ Không thể tải ảnh từ server: " + fileName);
                return null;
            }
        }

        // Tạo và resize ImageIcon
        ImageIcon originalIcon = new ImageIcon(localImgFile.getAbsolutePath());
        Image scaledImg = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

}
