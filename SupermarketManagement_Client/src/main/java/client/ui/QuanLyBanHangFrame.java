package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QuanLyBanHangFrame extends JPanel implements ActionListener, DocumentListener, ItemListener, MouseListener {
    private final SanPhamService sanPhamService;
    private JTable tableGioHang;
    private DefaultTableModel tableModelGioHang;
    private JTextField txtSearch;
    private JTextField txtMaVach;
    private JTextField txtSdt;
    private JTextField txtDiemDoi;
    private JTextField txtTienKhach;
    private JLabel lblTongTien;
    private JLabel lblTongTienHD;
    private JLabel lblValueTongTien;
    private JLabel lblValueTongTienHD;
    private JLabel lblValueTienThua;
    private JLabel lblValueDiem;
    private JLabel lblValueGiaCuoiCung;
    private JButton btnLamMoiGH;
    private JButton btnTimSDT;
    private JButton btnHuy;
    private JButton btnThemKhach;
    private JButton btnThanhToan;
    private JCheckBox checkBoxDiem;
    private List<SanPhamDTO> gioHang;
    private JPanel pSanPham;
    private boolean StatusDoiTichDiem = false;

    // Colors matching Gui_BanHang
    private static final Color PANEL_BG_COLOR = new Color(220, 239, 218);
    private static final Color CART_BG_COLOR = new Color(224, 240, 196);
    private static final Color PRICE_COLOR = new Color(0, 168, 84);

    public QuanLyBanHangFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
        this.gioHang = new ArrayList<>();

        setSize(913, 625);
        setLayout(null);

        // Cart Panel (Top Left)
        JPanel pHoaDon = new JPanel();
        pHoaDon.setBackground(CART_BG_COLOR);
        pHoaDon.setBounds(0, 0, 672, 246);
        pHoaDon.setLayout(new BorderLayout(0, 0));
        add(pHoaDon);

        String[] columnNamesGioHang = {"Mã SP", "Mã Giá", "Tên SP", "DVT", "SL", "Đơn Giá", "Thành Tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        tableGioHang.getColumnModel().getColumn(3).setPreferredWidth(90);
        tableGioHang.getColumnModel().getColumn(4).setPreferredWidth(40);
        tableGioHang.addMouseListener(this);
        JScrollPane scrollTableGioHang = new JScrollPane(tableGioHang);
        pHoaDon.add(scrollTableGioHang, BorderLayout.CENTER);

        // Product Panel (Bottom Left)
        JPanel pWrapSP = new JPanel();
        pWrapSP.setBackground(PANEL_BG_COLOR);
        pWrapSP.setBorder(new LineBorder(new Color(0, 0, 0)));
        pWrapSP.setBounds(0, 246, 672, 379);
        pWrapSP.setLayout(null);
        add(pWrapSP);

        JLabel lblTimSP = new JLabel("Tên hoặc mã SP:");
        lblTimSP.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblTimSP.setBounds(10, 11, 114, 26);
        pWrapSP.add(lblTimSP);

        txtSearch = new JTextField();
        txtSearch.setBounds(124, 16, 278, 20);
        txtSearch.setColumns(10);
        txtSearch.getDocument().addDocumentListener(this);
        pWrapSP.add(txtSearch);

        btnLamMoiGH = new JButton("Làm mới giỏ hàng");
        btnLamMoiGH.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        btnLamMoiGH.setBounds(412, 15, 161, 23);
        btnLamMoiGH.addActionListener(this);
        pWrapSP.add(btnLamMoiGH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 48, 652, 320);
        pWrapSP.add(scrollPane);

        pSanPham = new JPanel();
        pSanPham.setBackground(PANEL_BG_COLOR);
        pSanPham.setLayout(new FlowLayout(FlowLayout.LEFT));
        pSanPham.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setViewportView(pSanPham);

        // Checkout Panel (Right)
        JPanel pBarThanhToan = new JPanel();
        pBarThanhToan.setBackground(PANEL_BG_COLOR);
        pBarThanhToan.setBounds(673, 0, 240, 625);
        pBarThanhToan.setLayout(null);
        add(pBarThanhToan);

        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblMaVach.setBounds(10, 11, 64, 26);
        pBarThanhToan.add(lblMaVach);

        txtMaVach = new JTextField();
        txtMaVach.setBounds(10, 43, 208, 26);
        txtMaVach.setColumns(10);
        txtMaVach.getDocument().addDocumentListener(this);
        pBarThanhToan.add(txtMaVach);

        JLabel lblTongTienSP = new JLabel("Tổng tiền sản phẩm:");
        lblTongTienSP.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblTongTienSP.setBounds(10, 80, 155, 26);
        pBarThanhToan.add(lblTongTienSP);

        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblTongTien.setForeground(PRICE_COLOR);
        lblTongTien.setBounds(10, 117, 208, 26);
        pBarThanhToan.add(lblTongTien);

        lblTongTienHD = new JLabel("Tổng tiền hóa đơn:");
        lblTongTienHD.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblTongTienHD.setBounds(10, 155, 155, 26);
        pBarThanhToan.add(lblTongTienHD);

        lblValueTongTienHD = new JLabel("0");
        lblValueTongTienHD.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblValueTongTienHD.setForeground(PRICE_COLOR);
        lblValueTongTienHD.setBounds(10, 192, 100, 26);
        pBarThanhToan.add(lblValueTongTienHD);

        lblValueGiaCuoiCung = new JLabel("");
        lblValueGiaCuoiCung.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblValueGiaCuoiCung.setForeground(PRICE_COLOR);
        lblValueGiaCuoiCung.setBounds(118, 192, 100, 26);
        pBarThanhToan.add(lblValueGiaCuoiCung);

        JLabel lblTienKhach = new JLabel("Tiền khách trả:");
        lblTienKhach.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblTienKhach.setBounds(10, 229, 155, 26);
        pBarThanhToan.add(lblTienKhach);

        txtTienKhach = new JTextField();
        txtTienKhach.setForeground(PRICE_COLOR);
        txtTienKhach.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        txtTienKhach.setBounds(10, 266, 208, 26);
        txtTienKhach.setColumns(10);
        txtTienKhach.getDocument().addDocumentListener(this);
        pBarThanhToan.add(txtTienKhach);

        JLabel lblTienThua = new JLabel("Tiền thừa:");
        lblTienThua.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblTienThua.setBounds(10, 303, 155, 26);
        pBarThanhToan.add(lblTienThua);

        lblValueTienThua = new JLabel("0");
        lblValueTienThua.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblValueTienThua.setForeground(PRICE_COLOR);
        lblValueTienThua.setBounds(10, 333, 208, 26);
        pBarThanhToan.add(lblValueTienThua);

        JPanel pTichDiem = new JPanel();
        pTichDiem.setBackground(PANEL_BG_COLOR);
        pTichDiem.setBounds(10, 364, 220, 198);
        pTichDiem.setBorder(BorderFactory.createTitledBorder("Tích đổi điểm"));
        pTichDiem.setLayout(null);
        pBarThanhToan.add(pTichDiem);

        JLabel lblSdt = new JLabel("SDT:");
        lblSdt.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblSdt.setBounds(10, 23, 36, 26);
        pTichDiem.add(lblSdt);

        txtSdt = new JTextField();
        txtSdt.setColumns(10);
        txtSdt.setBounds(56, 25, 96, 26);
        pTichDiem.add(txtSdt);

        btnTimSDT = new JButton("Tìm");
        btnTimSDT.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11));
        btnTimSDT.setBounds(153, 24, 57, 26);
        btnTimSDT.addActionListener(this);
        pTichDiem.add(btnTimSDT);

        JLabel lblDiem = new JLabel("Điểm:");
        lblDiem.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblDiem.setBounds(10, 56, 47, 26);
        pTichDiem.add(lblDiem);

        lblValueDiem = new JLabel("0");
        lblValueDiem.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        lblValueDiem.setBounds(56, 56, 141, 26);
        pTichDiem.add(lblValueDiem);

        JLabel lblDiemDoi = new JLabel("Đổi:");
        lblDiemDoi.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        lblDiemDoi.setBounds(10, 90, 36, 26);
        pTichDiem.add(lblDiemDoi);

        txtDiemDoi = new JTextField();
        txtDiemDoi.setBounds(56, 90, 96, 26);
        txtDiemDoi.setColumns(10);
        txtDiemDoi.setEnabled(false);
        txtDiemDoi.getDocument().addDocumentListener(this);
        pTichDiem.add(txtDiemDoi);

        checkBoxDiem = new JCheckBox("Tối đa");
        checkBoxDiem.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        checkBoxDiem.setBounds(153, 90, 61, 26);
        checkBoxDiem.setEnabled(false);
        checkBoxDiem.setSelected(true);
        checkBoxDiem.addItemListener(this);
        pTichDiem.add(checkBoxDiem);

        btnHuy = new JButton("Hủy tích đổi điểm");
        btnHuy.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        btnHuy.setBounds(10, 129, 200, 26);
        btnHuy.addActionListener(this);
        pTichDiem.add(btnHuy);

        btnThemKhach = new JButton("Thêm khách");
        btnThemKhach.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        btnThemKhach.setBounds(10, 161, 200, 26);
        btnThemKhach.addActionListener(this);
        pTichDiem.add(btnThemKhach);

        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        btnThanhToan.setBounds(20, 568, 198, 46);
        btnThanhToan.addActionListener(this);
        pBarThanhToan.add(btnThanhToan);

        loadProductData();
    }

    private void loadProductData() {
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            renderProductCards(productList);
        } catch (Exception e) {
            e.printStackTrace();
            showCustomDialog("Lỗi", "Lỗi tải dữ liệu sản phẩm!", Color.RED);
        }
    }

    private void renderProductCards(List<SanPhamDTO> productList) {
        pSanPham.removeAll();
        DecimalFormat pattern = new DecimalFormat("#,###");
        for (SanPhamDTO sp : productList) {
            JPanel card = new CardProducts("/image/" + sp.getHinhAnh(), sp.getTenSanPham(), pattern.format(sp.getGia()), sp.getSoLuong());
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    themVaoGioHang(sp);
                }
            });
            pSanPham.add(card);
            pSanPham.add(Box.createHorizontalStrut(10));
        }
        pSanPham.revalidate();
        pSanPham.repaint();
    }

    private void searchProduct() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            List<SanPhamDTO> filteredList = new ArrayList<>();
            for (SanPhamDTO sp : productList) {
                if (sp.getTenSanPham().toLowerCase().contains(keyword) || sp.getMaSanPham().toLowerCase().contains(keyword)) {
                    filteredList.add(sp);
                }
            }
            renderProductCards(filteredList);
        } catch (Exception e) {
            e.printStackTrace();
            showCustomDialog("Lỗi", "Lỗi tìm kiếm sản phẩm!", Color.RED);
        }
    }

    private void themVaoGioHang(SanPhamDTO sp) {
        int sl = 1;
        String input = JOptionPane.showInputDialog(this, "Nhập số lượng:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            sl = Integer.parseInt(input.trim());
            if (sl <= 0) {
                showCustomDialog("Lỗi", "Số lượng phải lớn hơn 0!", Color.RED);
                return;
            }
        } catch (NumberFormatException ex) {
            showCustomDialog("Lỗi", "Số lượng phải là số!", Color.RED);
            return;
        }

        int row = kiemTraCongDon(sp.getMaSanPham());
        DecimalFormat pattern = new DecimalFormat("#,###");
        if (row != -1) {
            int slMoi = Integer.parseInt(tableModelGioHang.getValueAt(row, 4).toString()) + sl;
            capNhatRowSP(row, slMoi);
        } else {
            double thanhTien = sp.getGia() * sl;
            Object[] newRow = {
                    sp.getMaSanPham(),
                    "GIA" + sp.getMaSanPham(), // Placeholder for MaGiaSanPham
                    sp.getTenSanPham(),
                    "Cái", // Placeholder for DVT
                    sl,
                    pattern.format(sp.getGia()),
                    pattern.format(thanhTien)
            };
            tableModelGioHang.addRow(newRow);
        }
        capNhatTongTien();
        if (StatusDoiTichDiem) {
            if (checkBoxDiem.isSelected()) {
                doiDiemToida();
            } else {
                doiDiemKhongToiDa();
            }
        }
        updateTienThua();
    }

    private int kiemTraCongDon(String maSP) {
        for (int row = 0; row < tableModelGioHang.getRowCount(); row++) {
            String cellValue = (String) tableModelGioHang.getValueAt(row, 0);
            if (cellValue.equals(maSP)) {
                return row;
            }
        }
        return -1;
    }

    private void capNhatRowSP(int row, int slMoi) {
        double donGia = parseFormat(tableModelGioHang.getValueAt(row, 5).toString());
        double thanhTienMoi = donGia * slMoi;
        tableModelGioHang.setValueAt(slMoi, row, 4);
        tableModelGioHang.setValueAt(formatMonney(thanhTienMoi), row, 6);
    }

    private void capNhatTongTien() {
        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            tongTien += parseFormat(tableModelGioHang.getValueAt(i, 6).toString());
        }
        DecimalFormat pattern = new DecimalFormat("#,###");
        lblTongTien.setText(pattern.format(tongTien));
        lblValueTongTienHD.setText(pattern.format(tongTien));
        updateTienThua();
    }

    private void xoaKhoiGioHang(int row) {
        if (row >= 0) {
            if (tableModelGioHang.getRowCount() == 1) {
                lamMoiGioHang();
            } else {
                tableModelGioHang.removeRow(row);
                capNhatTongTien();
                if (StatusDoiTichDiem) {
                    if (checkBoxDiem.isSelected()) {
                        doiDiemToida();
                    } else {
                        doiDiemKhongToiDa();
                    }
                }
                updateTienThua();
            }
        } else {
            showCustomDialog("Thông báo", "Vui lòng chọn sản phẩm trong giỏ để xóa!", Color.YELLOW);
        }
    }

    private void lamMoiGioHang() {
        tableModelGioHang.setRowCount(0);
        lblValueGiaCuoiCung.setText("");
        lblValueTienThua.setText("0");
        lblTongTien.setText("0");
        txtTienKhach.setText("");
        lblValueTongTienHD.setText("0");
        huyTichDoiDiem();
    }

    private void thanhToan() {
        if (!validTruocThanhToan()) return;

        StringBuilder hoaDon = new StringBuilder("HÓA ĐƠN\n\n");
        hoaDon.append(String.format("%-15s %-20s %-10s %-10s %-10s\n", "Mã SP", "Tên SP", "Giá", "SL", "Thành tiền"));
        hoaDon.append("--------------------------------------------------------------\n");
        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            String maSP = tableModelGioHang.getValueAt(i, 0).toString();
            String tenSP = tableModelGioHang.getValueAt(i, 2).toString();
            double gia = parseFormat(tableModelGioHang.getValueAt(i, 5).toString());
            int soLuong = Integer.parseInt(tableModelGioHang.getValueAt(i, 4).toString());
            double thanhTien = parseFormat(tableModelGioHang.getValueAt(i, 6).toString());
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
            showCustomDialog("Thành công", "Thanh toán thành công bằng " + chonThanhToan + "!\nSố tiền: " + String.format("%,.0f", tongTien) + " VND", Color.GREEN);
            tableModelGioHang.setRowCount(0);
            capNhatTongTien();
        }
    }

    private boolean validTruocThanhToan() {
        if (tableModelGioHang.getRowCount() == 0) {
            showCustomDialog("Lỗi", "Chưa có sản phẩm nào trong giỏ!", Color.RED);
            return false;
        } else if (txtTienKhach.getText().isEmpty()) {
            showCustomDialog("Lỗi", "Chưa nhập tiền khách!", Color.RED);
            return false;
        } else if (parseFormat(lblValueTienThua.getText()) < 0) {
            showCustomDialog("Lỗi", "Tiền khách trả không đủ thanh toán!", Color.RED);
            return false;
        }
        return true;
    }

    private double parseFormat(String num) {
        DecimalFormat pattern = new DecimalFormat("#,###");
        try {
            return pattern.parse(num).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatMonney(double num) {
        DecimalFormat pattern = new DecimalFormat("#,###");
        return pattern.format(num);
    }

    private void themSPVaoGio(String maSP) {
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            SanPhamDTO sanPham = productList.stream()
                    .filter(sp -> sp.getMaSanPham().equals(maSP))
                    .findFirst()
                    .orElse(null);
            if (sanPham != null) {
                int row = kiemTraCongDon(maSP);
                DecimalFormat pattern = new DecimalFormat("#,###");
                if (row != -1) {
                    int slMoi = Integer.parseInt(tableModelGioHang.getValueAt(row, 4).toString()) + 1;
                    capNhatRowSP(row, slMoi);
                } else {
                    double thanhTien = sanPham.getGia();
                    Object[] newRow = {
                            sanPham.getMaSanPham(),
                            "GIA" + sanPham.getMaSanPham(),
                            sanPham.getTenSanPham(),
                            "Cái",
                            1,
                            pattern.format(sanPham.getGia()),
                            pattern.format(thanhTien)
                    };
                    tableModelGioHang.addRow(newRow);
                }
                capNhatTongTien();
                if (StatusDoiTichDiem) {
                    if (checkBoxDiem.isSelected()) {
                        doiDiemToida();
                    } else {
                        doiDiemKhongToiDa();
                    }
                }
                updateTienThua();
                txtMaVach.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTienThua() {
        if (txtTienKhach.getText().matches("^(?!0)\\d{1,}$")) {
            double tienThua;
            if (lblValueGiaCuoiCung.getText().isEmpty()) {
                tienThua = parseFormat(txtTienKhach.getText()) - parseFormat(lblValueTongTienHD.getText());
            } else {
                tienThua = parseFormat(txtTienKhach.getText()) - parseFormat(lblValueGiaCuoiCung.getText());
            }
            lblValueTienThua.setText(formatMonney(tienThua));
        }
    }

    private void updateTongTienSauDoiDiem(int diem) {
        if (diem > 0) {
            String tongTienHD = lblValueTongTienHD.getText();
            lblValueTongTienHD.setText(tongTienHD);
            lblValueTongTienHD.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
            lblValueTongTienHD.setForeground(Color.RED);
            lblValueGiaCuoiCung.setText(formatMonney(parseFormat(tongTienHD) - diem));
            lblValueGiaCuoiCung.setForeground(PRICE_COLOR);
        } else {
            capNhatTongTien();
            lblValueGiaCuoiCung.setText("");
            lblValueTongTienHD.setForeground(PRICE_COLOR);
        }
    }

    private void doiDiemToida() {
        double tongTienHD = parseFormat(lblValueTongTienHD.getText());
        int diemDoiToiDa = (int) Math.ceil(tongTienHD * 0.1);
        int diemKhach = Integer.parseInt(lblValueDiem.getText());
        if (diemKhach > diemDoiToiDa) {
            updateTongTienSauDoiDiem(diemDoiToiDa);
        } else {
            updateTongTienSauDoiDiem(diemKhach);
        }
        updateTienThua();
    }

    private void doiDiemKhongToiDa() {
        if (txtDiemDoi.getText().matches("^(?!0)\\d{1,}$")) {
            updateTongTienSauDoiDiem(Integer.parseInt(txtDiemDoi.getText()));
        } else {
            updateTongTienSauDoiDiem(0);
        }
        updateTienThua();
    }

    private void huyTichDoiDiem() {
        StatusDoiTichDiem = false;
        txtSdt.setText("");
        lblValueDiem.setText("0");
        txtDiemDoi.setText("");
        txtDiemDoi.setEnabled(false);
        checkBoxDiem.setEnabled(false);
        updateTongTienSauDoiDiem(0);
    }

    private void showCustomDialog(String title, String message, Color messageColor) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setUndecorated(true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);

        RoundedPanel dialogPanel = new RoundedPanel(15);
        dialogPanel.setBackground(new Color(42, 82, 39));
        dialogPanel.setLayout(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1));
        dialog.add(dialogPanel);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(messageColor);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(42, 82, 39));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        RoundedPanel okButton = new RoundedPanel(10);
        okButton.setBackground(new Color(59, 132, 52));
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.setLayout(new BorderLayout());
        JLabel okLabel = new JLabel("OK", SwingConstants.CENTER);
        okLabel.setForeground(Color.WHITE);
        okLabel.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.add(okLabel, BorderLayout.CENTER);
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                okButton.setBackground(new Color(123, 174, 94));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                okButton.setBackground(new Color(59, 132, 52));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(okButton);

        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(btnTimSDT)) {
            // Placeholder for customer lookup by phone number
            int choice = JOptionPane.showConfirmDialog(null, "Tìm thấy khách, bạn có muốn tích điểm?", "Tích điểm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                StatusDoiTichDiem = true;
                lblValueDiem.setText("1000"); // Placeholder for customer points
                checkBoxDiem.setEnabled(true);
                doiDiemToida();
            }
        } else if (obj.equals(btnHuy)) {
            huyTichDoiDiem();
        } else if (obj.equals(btnThemKhach)) {
            // Placeholder for adding a new customer
            JOptionPane.showMessageDialog(this, "Chức năng thêm khách chưa được triển khai!");
            doiDiemKhongToiDa();
        } else if (obj.equals(btnThanhToan)) {
            thanhToan();
        } else if (obj.equals(btnLamMoiGH)) {
            lamMoiGioHang();
        }
    }



    @Override
    public void removeUpdate(DocumentEvent e) {
        Document doc = e.getDocument();
        if (doc.equals(txtSearch.getDocument())) {
            searchProduct();
        } else if (!txtMaVach.getText().isEmpty() && doc.equals(txtMaVach.getDocument())) {
            String maVach = txtMaVach.getText();
            if (maVach.matches("^\\d{13}$")) {
                themSPVaoGio(maVach);
            }
        } else if (doc.equals(txtDiemDoi.getDocument())) {
            doiDiemKhongToiDa();
        } else if (doc.equals(txtTienKhach.getDocument())) {
            updateTienThua();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Document doc = e.getDocument();
        if (doc.equals(txtSearch.getDocument())) {
            searchProduct();
        } else if (doc.equals(txtMaVach.getDocument())) {
            String maVach = txtMaVach.getText();
            if (maVach.matches("^\\d{13}$")) {
                themSPVaoGio(maVach);
            }
        } else if (doc.equals(txtDiemDoi.getDocument())) {
            doiDiemKhongToiDa();
        } else if (doc.equals(txtTienKhach.getDocument())) {
            updateTienThua();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        if (checkBox.equals(checkBoxDiem)) {
            if (checkBox.isSelected()) {
                txtDiemDoi.setEnabled(false);
                txtDiemDoi.setText("");
                doiDiemToida();
            } else {
                txtDiemDoi.setEnabled(true);
                doiDiemKhongToiDa();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tableGioHang.getSelectedRow();
        if (row != -1) {
            int choice = JOptionPane.showOptionDialog(null, "Chỉnh sửa sản phẩm", "Edit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Xóa SP", "Sửa SP"}, null);
            if (choice == JOptionPane.YES_OPTION) {
                xoaKhoiGioHang(row);
            } else if (choice == JOptionPane.NO_OPTION) {
                String input = JOptionPane.showInputDialog(this, "Nhập số lượng mới:");
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int slNew = Integer.parseInt(input.trim());
                        if (slNew > 0) {
                            capNhatRowSP(row, slNew);
                            capNhatTongTien();
                            if (StatusDoiTichDiem) {
                                if (checkBoxDiem.isSelected()) {
                                    doiDiemToida();
                                } else {
                                    doiDiemKhongToiDa();
                                }
                            }
                            updateTienThua();
                        }
                    } catch (NumberFormatException ex) {
                        showCustomDialog("Lỗi", "Số lượng phải là số!", Color.RED);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void insertUpdate(DocumentEvent e) {

    }
}



// Placeholder for CardProducts (you need to implement this based on your requirements)
class CardProducts extends JPanel {
    public CardProducts(String imagePath, String tenSP, String gia, int soLuong) {
        setPreferredSize(new Dimension(150, 200));
        setBackground(new Color(220, 239, 218));
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel lblImage = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        if (icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        }
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImage, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(220, 239, 218));
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(new JLabel(tenSP, SwingConstants.CENTER));
        infoPanel.add(new JLabel("Giá: " + gia, SwingConstants.CENTER));
        infoPanel.add(new JLabel("SL: " + soLuong, SwingConstants.CENTER));
        add(infoPanel, BorderLayout.SOUTH);
    }
}