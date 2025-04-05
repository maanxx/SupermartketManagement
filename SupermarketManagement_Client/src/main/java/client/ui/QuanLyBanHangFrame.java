package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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

        setTitle("Quản Lý Bán Hàng");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        // Header Panel với gradient và animation
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(67, 99, 235), 0, getHeight(), new Color(142, 158, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lblHeader = new JLabel("QUẢN LÝ BÁN HÀNG", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 242, 245));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        // Search Panel với animation
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        searchPanel.setBackground(new Color(240, 242, 245));
        JLabel lblSearch = new JLabel("Tìm Kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSearch.setForeground(new Color(67, 99, 235));
        txtSearch = new JTextField(30);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 15, 10, 15)));
        txtSearch.setBackground(Color.WHITE);
        JButton btnSearch = createAnimatedButton("🔍 Tìm Kiếm");
        btnSearch.addActionListener(e -> searchProduct());
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Panel Sản Phẩm
        JPanel panelSanPham = new JPanel(new BorderLayout());
        panelSanPham.setBackground(Color.WHITE);
        panelSanPham.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)));
        panelSanPham.add(searchPanel, BorderLayout.NORTH);

        String[] columnNamesSanPham = {"Mã SP", "Tên Sản Phẩm", "Giá", "Số Lượng"};
        tableModelSanPham = new DefaultTableModel(columnNamesSanPham, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPham = new JTable(tableModelSanPham);
        styleTable(tableSanPham);
        JScrollPane scrollPaneSanPham = new JScrollPane(tableSanPham);
        scrollPaneSanPham.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSanPham.add(scrollPaneSanPham, BorderLayout.CENTER);

        JButton btnThemGioHang = createAnimatedButton("➕ Thêm Vào Giỏ");
        btnThemGioHang.addActionListener(e -> themVaoGioHang());
        JPanel btnPanelSanPham = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanelSanPham.setBackground(Color.WHITE);
        btnPanelSanPham.add(btnThemGioHang);
        panelSanPham.add(btnPanelSanPham, BorderLayout.SOUTH);

        // Panel Giỏ Hàng
        JPanel panelGioHang = new JPanel(new BorderLayout());
        panelGioHang.setBackground(Color.WHITE);
        panelGioHang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)));
        JLabel lblGioHang = new JLabel("GIỎ HÀNG", SwingConstants.CENTER);
        lblGioHang.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGioHang.setForeground(new Color(67, 99, 235));
        panelGioHang.add(lblGioHang, BorderLayout.NORTH);

        String[] columnNamesGioHang = {"Mã SP", "Tên Sản Phẩm", "Giá", "SL", "Thành Tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        styleTable(tableGioHang);
        JScrollPane scrollPaneGioHang = new JScrollPane(tableGioHang);
        scrollPaneGioHang.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelGioHang.add(scrollPaneGioHang, BorderLayout.CENTER);

        JButton btnXoaKhoiGio = createAnimatedButton("🗑️ Xóa Khỏi Giỏ");
        btnXoaKhoiGio.addActionListener(e -> xoaKhoiGioHang());
        JPanel btnPanelGioHang = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanelGioHang.setBackground(Color.WHITE);
        btnPanelGioHang.add(btnXoaKhoiGio);
        panelGioHang.add(btnPanelGioHang, BorderLayout.SOUTH);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelSanPham, panelGioHang);
        splitPane.setDividerLocation(600);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(splitPane, BorderLayout.CENTER);

        // Checkout Panel
        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        checkoutPanel.setBackground(new Color(240, 242, 245));
        checkoutPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        lblTongTien = new JLabel("Tổng Tiền: 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(new Color(67, 99, 235));
        JButton btnThanhToan = createAnimatedButton("💳 Thanh Toán");
        btnThanhToan.addActionListener(e -> thanhToan());
        checkoutPanel.add(lblTongTien);
        checkoutPanel.add(btnThanhToan);
        contentPanel.add(checkoutPanel, BorderLayout.SOUTH);

        // Animation khi mở frame
        setOpacity(0f);
        setVisible(true);
        Timer fadeInTimer = new Timer(20, new ActionListener() {
            float opacity = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) ((Timer) e.getSource()).stop();
            }
        });
        fadeInTimer.start();

        loadProductData();
    }

    private JButton createAnimatedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(67, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng hover và scale
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(92, 124, 250));
                animateScale(button, 1.05f);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(67, 99, 235));
                animateScale(button, 1.0f);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(47, 79, 215));
                animateScale(button, 0.95f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(67, 99, 235));
                animateScale(button, 1.0f);
            }
        });
        return button;
    }

    private void animateScale(JComponent component, float scale) {
        Timer timer = new Timer(10, new ActionListener() {
            float currentScale = component.getWidth() / (float) component.getPreferredSize().width;
            final float targetScale = scale;
            final float step = (targetScale - currentScale) / 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentScale += step;
                if ((step > 0 && currentScale >= targetScale) || (step < 0 && currentScale <= targetScale)) {
                    currentScale = targetScale;
                    ((Timer) e.getSource()).stop();
                }
                int newWidth = (int) (component.getPreferredSize().width * currentScale);
                int newHeight = (int) (component.getPreferredSize().height * currentScale);
                component.setSize(newWidth, newHeight);
                component.revalidate();
            }
        });
        timer.start();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(67, 99, 235));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && table.getRowCount() > 0) {
                    table.setRowSelectionInterval(row, row);
                }
            }
        });
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
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            String input = JOptionPane.showInputDialog(this, "Nhập số lượng:");
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
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trong giỏ để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void capNhatTongTien() {
        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            tongTien += Double.parseDouble(tableModelGioHang.getValueAt(i, 4).toString());
        }
        lblTongTien.setText("Tổng Tiền: " + String.format("%,.0f", tongTien) + " VND");
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
        hoaDon.append("\nTổng tiền: " + String.format("%,.0f", tongTien) + " VND\n");
        hoaDon.append("--------------------------------------------------------------\n");

        String[] phuongThuc = {"💵 Tiền mặt", "💳 Thẻ ngân hàng"};
        String chonThanhToan = (String) JOptionPane.showInputDialog(
                this,
                hoaDon.toString() + "\nChọn phương thức thanh toán:",
                "Thanh Toán",
                JOptionPane.PLAIN_MESSAGE,
                null,
                phuongThuc,
                phuongThuc[0]);

        if (chonThanhToan != null) {
            String maHoaDon = "HD" + System.currentTimeMillis();
            String ngayLap = java.time.LocalDate.now().toString();
            QuanLyHoaDonFrame.luuHoaDon(maHoaDon, ngayLap, tongTien, chonThanhToan);
            JOptionPane.showMessageDialog(this, "Thanh toán thành công bằng " + chonThanhToan + "!\nSố tiền: " + String.format("%,.0f", tongTien) + " VND", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            tableModelGioHang.setRowCount(0);
            capNhatTongTien();
        }
    }
}