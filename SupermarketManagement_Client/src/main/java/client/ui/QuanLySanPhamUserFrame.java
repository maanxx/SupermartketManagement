package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;

public class QuanLySanPhamUserFrame extends JFrame {
    private final SanPhamService sanPhamService;
    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboSort;

    public QuanLySanPhamUserFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;

        setTitle("Sản Phẩm - Dành cho Thu Ngân");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

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
        cboSort = new JComboBox<>(new String[]{"Sắp xếp theo", "Giá tăng dần", "Giá giảm dần"});
        cboSort.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboSort.addActionListener(e -> sortProduct());
        filterPanel.add(lblSearch);
        filterPanel.add(txtSearch);
        filterPanel.add(btnSearch);
        filterPanel.add(new JLabel("Sắp xếp:"));
        filterPanel.add(cboSort);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

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
        centerPanel.add(scrollPane, BorderLayout.CENTER);

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
            File imageFolder = new File("client_images");
            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
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

    private ImageIcon loadImageIcon(String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;
        File localImgFile = new File("client_images/" + fileName);
        if (!localImgFile.exists()) {
            try {
                byte[] imageBytes = sanPhamService.downloadHinhAnh(fileName);
                java.nio.file.Files.write(localImgFile.toPath(), imageBytes);
            } catch (Exception e) {
                System.err.println("❌ Không thể tải ảnh từ server: " + fileName);
                return null;
            }
        }
        ImageIcon originalIcon = new ImageIcon(localImgFile.getAbsolutePath());
        Image scaledImg = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
}