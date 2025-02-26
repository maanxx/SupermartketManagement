package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 73, 94), 0, h, new Color(41, 128, 185));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(1100, 80));
        JLabel lblHeader = new JLabel("Bán Hàng", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        JButton btnSearch = createButton(" Tìm kiếm");
        btnSearch.addActionListener(e -> searchProduct());
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel panelSanPham = new JPanel(new BorderLayout());
        panelSanPham.setBackground(Color.WHITE);
        panelSanPham.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSanPham.add(searchPanel, BorderLayout.NORTH);

        String[] columnNamesSanPham = {"Mã SP", "Tên sản phẩm", "Giá", "Số lượng"};
        tableModelSanPham = new DefaultTableModel(columnNamesSanPham, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(tableModelSanPham);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.setRowHeight(30);
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(new Color(52, 73, 94));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableSanPham.getColumnCount(); i++) {
            tableSanPham.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPaneSanPham = new JScrollPane(tableSanPham);
        scrollPaneSanPham.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSanPham.add(scrollPaneSanPham, BorderLayout.CENTER);

        JButton btnThemGioHang = createButton("Thêm vào giỏ");
        btnThemGioHang.addActionListener(e -> themVaoGioHang());
        panelSanPham.add(btnThemGioHang, BorderLayout.SOUTH);

        JPanel panelGioHang = new JPanel(new BorderLayout());
        panelGioHang.setBackground(Color.WHITE);
        panelGioHang.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblGioHang = new JLabel("Giỏ Hàng", SwingConstants.CENTER);
        lblGioHang.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelGioHang.add(lblGioHang, BorderLayout.NORTH);

        String[] columnNamesGioHang = {"Mã SP", "Tên sản phẩm", "Giá", "SL", "Thành tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        tableGioHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableGioHang.setRowHeight(30);
        tableGioHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableGioHang.getTableHeader().setBackground(new Color(52, 73, 94));
        tableGioHang.getTableHeader().setForeground(Color.WHITE);
        for (int i = 0; i < tableGioHang.getColumnCount(); i++) {
            tableGioHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPaneGioHang = new JScrollPane(tableGioHang);
        scrollPaneGioHang.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelGioHang.add(scrollPaneGioHang, BorderLayout.CENTER);

        JButton btnXoaKhoiGio = createButton("Xóa khỏi giỏ");
        btnXoaKhoiGio.addActionListener(e -> xoaKhoiGioHang());
        panelGioHang.add(btnXoaKhoiGio, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelSanPham, panelGioHang);
        splitPane.setDividerLocation(550);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(splitPane, BorderLayout.CENTER);

        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        checkoutPanel.setBackground(Color.WHITE);
        checkoutPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        lblTongTien = new JLabel("Tổng tiền: 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JButton btnThanhToan = createButton("💳 Thanh toán");
        btnThanhToan.addActionListener(e -> thanhToan());
        checkoutPanel.add(lblTongTien);
        checkoutPanel.add(btnThanhToan);
        contentPanel.add(checkoutPanel, BorderLayout.SOUTH);

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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maSP = tableModelSanPham.getValueAt(selectedRow, 0).toString();
        String tenSP = tableModelSanPham.getValueAt(selectedRow, 1).toString();
        double gia = Double.parseDouble(tableModelSanPham.getValueAt(selectedRow, 2).toString());
        int soLuong;
        try {
            String input = JOptionPane.showInputDialog("Nhập số lượng:");
            if (input == null || input.trim().isEmpty()) return;
            soLuong = Integer.parseInt(input.trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
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
            String maHoaDon = "HD" + System.currentTimeMillis();
            String ngayLap = java.time.LocalDate.now().toString();
            QuanLyHoaDonFrame.luuHoaDon(maHoaDon, ngayLap, tongTien, chonThanhToan);
            JOptionPane.showMessageDialog(this, "Thanh toán thành công bằng " + chonThanhToan + "!\nSố tiền: " + tongTien + " VND", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            tableModelGioHang.setRowCount(0);
            capNhatTongTien();
        }
    }
}
