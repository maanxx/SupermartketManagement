package client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyBanHangFrame extends JFrame {
    private JTable tableSanPham;
    private JTable tableGioHang;
    private JLabel lblTongTien;

    public QuanLyBanHangFrame() {
        // Frame settings
        setTitle(" Quản Lý Bán Hàng");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(800, 100));
        JLabel lblHeader = new JLabel(" Quản Lý Bán Hàng", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);

        // Left panel (Product List)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(450, 0));
        JLabel lblSanPham = new JLabel(" Danh Sách Sản Phẩm", SwingConstants.CENTER);
        lblSanPham.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSanPham.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(lblSanPham, BorderLayout.NORTH);

        String[] columnSanPham = {"Mã SP", "Tên SP", "Loại", "Giá", "SL"};
        Object[][] dataSanPham = {}; // Load data từ DB
        DefaultTableModel modelSanPham = new DefaultTableModel(dataSanPham, columnSanPham);
        tableSanPham = new JTable(modelSanPham);
        JScrollPane scrollSanPham = new JScrollPane(tableSanPham);
        leftPanel.add(scrollSanPham, BorderLayout.CENTER);

        JButton btnThemVaoGio = new JButton("➕ Thêm vào Giỏ Hàng");
        btnThemVaoGio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnThemVaoGio.addActionListener(e -> themVaoGioHang());
        leftPanel.add(btnThemVaoGio, BorderLayout.SOUTH);

        // Right panel (Cart and Total)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(700, 0));

        JLabel lblGioHang = new JLabel(" Giỏ Hàng", SwingConstants.CENTER);
        lblGioHang.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGioHang.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(lblGioHang, BorderLayout.NORTH);

        String[] columnGioHang = {"Mã SP", "Tên SP", "SL", "Giá", "Thành Tiền"};
        Object[][] dataGioHang = {}; // Giỏ hàng ban đầu trống
        DefaultTableModel modelGioHang = new DefaultTableModel(dataGioHang, columnGioHang);
        tableGioHang = new JTable(modelGioHang);
        JScrollPane scrollGioHang = new JScrollPane(tableGioHang);
        rightPanel.add(scrollGioHang, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng Tiền: 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPanel.add(lblTongTien);

        JButton btnThanhToan = new JButton(" Thanh Toán");
        btnThanhToan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnThanhToan.addActionListener(e -> thanhToan());
        totalPanel.add(btnThanhToan);

        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        // Adding panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void themVaoGioHang() {
        // Logic thêm sản phẩm từ tableSanPham vào tableGioHang
        // Ví dụ: Lấy dòng được chọn từ tableSanPham, thêm vào model của tableGioHang
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel modelSanPham = (DefaultTableModel) tableSanPham.getModel();
            DefaultTableModel modelGioHang = (DefaultTableModel) tableGioHang.getModel();

            String maSP = modelSanPham.getValueAt(selectedRow, 0).toString();
            String tenSP = modelSanPham.getValueAt(selectedRow, 1).toString();
            int soLuong = 1; // Mặc định là 1 sản phẩm, có thể thêm logic nhập số lượng
            String giaStr = modelSanPham.getValueAt(selectedRow, 3).toString();
            double gia = Double.parseDouble(giaStr.replaceAll("[^\\d.]", "")); // Chuyển giá thành số

            boolean daTonTai = false;
            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                if (modelGioHang.getValueAt(i, 0).equals(maSP)) {
                    // Nếu sản phẩm đã tồn tại, tăng số lượng
                    int soLuongHienTai = (int) modelGioHang.getValueAt(i, 2);
                    modelGioHang.setValueAt(soLuongHienTai + 1, i, 2);
                    modelGioHang.setValueAt((soLuongHienTai + 1) * gia, i, 4);
                    daTonTai = true;
                    break;
                }
            }

            if (!daTonTai) {
                // Nếu sản phẩm chưa có, thêm dòng mới
                modelGioHang.addRow(new Object[]{maSP, tenSP, soLuong, gia, soLuong * gia});
            }

            capNhatTongTien();
        } else {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm để thêm vào giỏ hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void capNhatTongTien() {
        DefaultTableModel modelGioHang = (DefaultTableModel) tableGioHang.getModel();
        double tongTien = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            tongTien += (double) modelGioHang.getValueAt(i, 4);
        }
        lblTongTien.setText(String.format("Tổng Tiền: %.0f VND", tongTien));
    }

    private void thanhToan() {
        // Logic thanh toán giỏ hàng
        JOptionPane.showMessageDialog(this, "Thanh toán thành công! Cảm ơn bạn đã mua hàng.");
        ((DefaultTableModel) tableGioHang.getModel()).setRowCount(0); // Xóa giỏ hàng sau thanh toán
        lblTongTien.setText("Tổng Tiền: 0 VND");
    }
}
