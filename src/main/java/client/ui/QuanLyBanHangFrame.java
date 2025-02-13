package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

public class QuanLyBanHangFrame extends JFrame {
    private final SanPhamService sanPhamService;
    private JTable tableSanPham, tableGioHang;
    private DefaultTableModel tableModelSanPham, tableModelGioHang;
    private JTextField txtSearch;
    private JLabel lblTongTien;
    private List<SanPhamDTO> gioHang;

    public QuanLyBanHangFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
        this.gioHang = new ArrayList<>();

        setTitle("Bán Hàng");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel lblHeader = new JLabel("Bán Hàng", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtSearch = new JTextField(20);
        JButton btnSearch = createButton("🔍 Tìm kiếm");
        btnSearch.addActionListener(e -> searchProduct());
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Product Table
        String[] columnNamesSanPham = {"Mã SP", "Tên sản phẩm", "Giá", "Số lượng"};
        tableModelSanPham = new DefaultTableModel(columnNamesSanPham, 0);
        tableSanPham = new JTable(tableModelSanPham);
        JScrollPane scrollPaneSanPham = new JScrollPane(tableSanPham);

        JButton btnThemGioHang = createButton(" Thêm vào giỏ");
        btnThemGioHang.addActionListener(e -> themVaoGioHang());

        JPanel panelSanPham = new JPanel(new BorderLayout());
        panelSanPham.add(searchPanel, BorderLayout.NORTH);
        panelSanPham.add(scrollPaneSanPham, BorderLayout.CENTER);
        panelSanPham.add(btnThemGioHang, BorderLayout.SOUTH);

        // Cart Table (Giỏ hàng)
        String[] columnNamesGioHang = {"Mã SP", "Tên sản phẩm", "Giá", "Số lượng", "Thành tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0);
        tableGioHang = new JTable(tableModelGioHang);
        JScrollPane scrollPaneGioHang = new JScrollPane(tableGioHang);

        JButton btnXoaKhoiGio = createButton(" Xóa khỏi giỏ");
        btnXoaKhoiGio.addActionListener(e -> xoaKhoiGioHang());

        JPanel panelGioHang = new JPanel(new BorderLayout());
        panelGioHang.add(new JLabel("Giỏ Hàng", SwingConstants.CENTER), BorderLayout.NORTH);
        panelGioHang.add(scrollPaneGioHang, BorderLayout.CENTER);
        panelGioHang.add(btnXoaKhoiGio, BorderLayout.SOUTH);

        // Checkout Panel
        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 VND");
        JButton btnThanhToan = createButton("💳 Thanh toán");
        btnThanhToan.addActionListener(e -> thanhToan());

        checkoutPanel.add(lblTongTien);
        checkoutPanel.add(btnThanhToan);

        // Main Content Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelSanPham, panelGioHang);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
        add(checkoutPanel, BorderLayout.SOUTH);

        // Load data
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
            tableModelSanPham.setRowCount(0);
            for (SanPhamDTO sp : productList) {
                tableModelSanPham.addRow(new Object[]{
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
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
            tableModelSanPham.setRowCount(0);
            for (SanPhamDTO sp : productList) {
                if (sp.getTenSanPham().toLowerCase().contains(keyword)) {
                    tableModelSanPham.addRow(new Object[]{
                            sp.getMaSanPham(),
                            sp.getTenSanPham(),
                            sp.getGia(),
                            sp.getSoLuong()
                    });
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void themVaoGioHang() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!");
            return;
        }

        String maSP = tableModelSanPham.getValueAt(selectedRow, 0).toString();
        String tenSP = tableModelSanPham.getValueAt(selectedRow, 1).toString();
        double gia = Double.parseDouble(tableModelSanPham.getValueAt(selectedRow, 2).toString());
        int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập số lượng:"));

        double thanhTien = gia * soLuong;
        tableModelGioHang.addRow(new Object[]{maSP, tenSP, gia, soLuong, thanhTien});
        capNhatTongTien();
    }

    private void xoaKhoiGioHang() {
        int selectedRow = tableGioHang.getSelectedRow();
        if (selectedRow >= 0) {
            tableModelGioHang.removeRow(selectedRow);
            capNhatTongTien();
        }
    }

    private void capNhatTongTien() {
        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            tongTien += Double.parseDouble(tableModelGioHang.getValueAt(i, 4).toString());
        }
        lblTongTien.setText("Tổng tiền: " + tongTien + " VND");
    }

    private void thanhToan() {
        if (tableModelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị hóa đơn
        StringBuilder hoaDon = new StringBuilder("HÓA ĐƠN\n\n");
        hoaDon.append(String.format("%-15s %-20s %-10s %-10s %-10s\n", "Mã SP", "Tên SP", "Giá", "SL", "Thành tiền"));
        hoaDon.append("--------------------------------------------------------------\n");

        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            String maSP = tableModelGioHang.getValueAt(i, 0).toString();
            String tenSP = tableModelGioHang.getValueAt(i, 1).toString();
            double gia = Double.parseDouble(tableModelGioHang.getValueAt(i, 2).toString());
            int soLuong = Integer.parseInt(tableModelGioHang.getValueAt(i, 3).toString());
            double thanhTien = gia * soLuong;

            hoaDon.append(String.format("%-15s %-20s %-10.2f %-10d %-10.2f\n", maSP, tenSP, gia, soLuong, thanhTien));
            tongTien += thanhTien;
        }

        hoaDon.append("\nTổng tiền: " + tongTien + " VND\n");
        hoaDon.append("--------------------------------------------------------------\n");

        // Chọn phương thức thanh toán
        String[] phuongThuc = {"💵 Tiền mặt", "💳 Thẻ ngân hàng"};
        String chonThanhToan = (String) JOptionPane.showInputDialog(
                this,
                hoaDon.toString() + "\nChọn phương thức thanh toán:",
                "Thanh toán",
                JOptionPane.PLAIN_MESSAGE,
                null,
                phuongThuc,
                phuongThuc[0]);

        if (chonThanhToan != null) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công bằng " + chonThanhToan + "!\nSố tiền: " + tongTien + " VND", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            tableModelGioHang.setRowCount(0); // Xóa giỏ hàng sau khi thanh toán
            capNhatTongTien();
        }
    }

}
