package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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

    // Colors matching Gui_BanHang and UserDashboard
    private static final Color PANEL_BG_COLOR = new Color(220, 239, 218);
    private static final Color CART_BG_COLOR = new Color(224, 240, 196);
    private static final Color PRICE_COLOR = new Color(0, 168, 84);
    private static final Color PRICE_BG_COLOR = new Color(255, 102, 102);
    private static final Color TITLE_COLOR = new Color(242, 127, 87); // From UserDashboard

    public QuanLyBanHangFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
        this.gioHang = new ArrayList<>();

        // Use BorderLayout for the main panel
        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding to avoid edge overlap

        // Add the three main panels
        add(createCartPanel(), BorderLayout.NORTH);
        add(createProductPanel(), BorderLayout.CENTER);
        add(createCheckoutPanel(), BorderLayout.EAST);
    }

    private JPanel createCartPanel() {
        JPanel pHoaDon = new RoundedPanel(10);
        pHoaDon.setBackground(CART_BG_COLOR);
        pHoaDon.setLayout(new BorderLayout());
        pHoaDon.setBorder(new LineBorder(Color.BLACK));

        String[] columnNamesGioHang = {"Mã SP", "Tên SP", "SL", "DVT", "Đơn Giá", "Thành Tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableGioHang.getColumnModel().getColumn(2).setPreferredWidth(40); // SL column
        tableGioHang.getColumnModel().getColumn(3).setPreferredWidth(90); // DVT column
        tableGioHang.addMouseListener(this);
        JScrollPane scrollTableGioHang = new JScrollPane(tableGioHang);
        pHoaDon.add(scrollTableGioHang, BorderLayout.CENTER);

        // Wrap in a container to control height
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setPreferredSize(new Dimension(0, 200)); // Adjusted height for better balance
        cartContainer.add(pHoaDon, BorderLayout.CENTER);
        return cartContainer;
    }

    private JPanel createProductPanel() {
        JPanel pWrapSP = new RoundedPanel(10);
        pWrapSP.setBackground(PANEL_BG_COLOR);
        pWrapSP.setBorder(new LineBorder(Color.BLACK));
        pWrapSP.setLayout(new BorderLayout(5, 5));

        // Search bar panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(PANEL_BG_COLOR);

        JLabel lblTimSP = new JLabel("Tên hoặc mã SP:");
        lblTimSP.setFont(new Font("Arial", Font.BOLD, 14));
        lblTimSP.setForeground(TITLE_COLOR);
        searchPanel.add(lblTimSP);

        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(this);
        searchPanel.add(txtSearch);

        btnLamMoiGH = new JButton("Làm mới giỏ hàng");
        btnLamMoiGH.setFont(new Font("Arial", Font.BOLD, 12));
        btnLamMoiGH.addActionListener(this);
        searchPanel.add(btnLamMoiGH);

        pWrapSP.add(searchPanel, BorderLayout.NORTH);

        // Product cards panel
        JScrollPane scrollPane = new JScrollPane();
        pSanPham = new JPanel();
        pSanPham.setBackground(PANEL_BG_COLOR);
        pSanPham.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        scrollPane.setViewportView(pSanPham);
        pWrapSP.add(scrollPane, BorderLayout.CENTER);

        return pWrapSP;
    }

    private JPanel createCheckoutPanel() {
        JPanel pBarThanhToan = new RoundedPanel(10);
        pBarThanhToan.setBackground(PANEL_BG_COLOR);
        pBarThanhToan.setLayout(new BoxLayout(pBarThanhToan, BoxLayout.Y_AXIS));
        pBarThanhToan.setBorder(new LineBorder(Color.BLACK));
        pBarThanhToan.setPreferredSize(new Dimension(240, 0)); // Keep width for balance

        // Mã vạch
        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setFont(new Font("Arial", Font.BOLD, 14));
        lblMaVach.setForeground(TITLE_COLOR);
        pBarThanhToan.add(lblMaVach);

        txtMaVach = new JTextField(15);
        txtMaVach.getDocument().addDocumentListener(this);
        pBarThanhToan.add(txtMaVach);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tổng tiền sản phẩm
        JLabel lblTongTienSP = new JLabel("Tổng tiền sản phẩm:");
        lblTongTienSP.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTienSP.setForeground(TITLE_COLOR);
        pBarThanhToan.add(lblTongTienSP);

        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongTien.setForeground(PRICE_COLOR);
        pBarThanhToan.add(lblTongTien);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tổng tiền hóa đơn
        lblTongTienHD = new JLabel("Tổng tiền hóa đơn:");
        lblTongTienHD.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTienHD.setForeground(TITLE_COLOR);
        pBarThanhToan.add(lblTongTienHD);

        JPanel totalBillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalBillPanel.setBackground(PANEL_BG_COLOR);
        lblValueTongTienHD = new JLabel("0");
        lblValueTongTienHD.setFont(new Font("Arial", Font.BOLD, 12));
        lblValueTongTienHD.setForeground(PRICE_COLOR);
        totalBillPanel.add(lblValueTongTienHD);

        lblValueGiaCuoiCung = new JLabel("");
        lblValueGiaCuoiCung.setFont(new Font("Arial", Font.BOLD, 12));
        lblValueGiaCuoiCung.setForeground(PRICE_COLOR);
        totalBillPanel.add(lblValueGiaCuoiCung);
        pBarThanhToan.add(totalBillPanel);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tiền khách trả
        JLabel lblTienKhach = new JLabel("Tiền khách trả:");
        lblTienKhach.setFont(new Font("Arial", Font.BOLD, 14));
        lblTienKhach.setForeground(TITLE_COLOR);
        pBarThanhToan.add(lblTienKhach);

        txtTienKhach = new JTextField(15);
        txtTienKhach.setForeground(PRICE_COLOR);
        txtTienKhach.setFont(new Font("Arial", Font.BOLD, 12));
        txtTienKhach.getDocument().addDocumentListener(this);
        pBarThanhToan.add(txtTienKhach);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tiền thừa
        JLabel lblTienThua = new JLabel("Tiền thừa:");
        lblTienThua.setFont(new Font("Arial", Font.BOLD, 14));
        lblTienThua.setForeground(TITLE_COLOR);
        pBarThanhToan.add(lblTienThua);

        lblValueTienThua = new JLabel("0");
        lblValueTienThua.setFont(new Font("Arial", Font.BOLD, 12));
        lblValueTienThua.setForeground(PRICE_COLOR);
        pBarThanhToan.add(lblValueTienThua);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tích đổi điểm section
        JPanel pTichDiem = new RoundedPanel(10);
        pTichDiem.setBackground(PANEL_BG_COLOR);
        pTichDiem.setBorder(BorderFactory.createTitledBorder("Tích đổi điểm"));
        pTichDiem.setLayout(new BoxLayout(pTichDiem, BoxLayout.Y_AXIS));
        pTichDiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 198));

        JPanel sdtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sdtPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblSdt = new JLabel("SDT:");
        lblSdt.setFont(new Font("Arial", Font.BOLD, 14));
        lblSdt.setForeground(TITLE_COLOR);
        sdtPanel.add(lblSdt);

        txtSdt = new JTextField(10);
        sdtPanel.add(txtSdt);

        btnTimSDT = new JButton("Tìm");
        btnTimSDT.setFont(new Font("Arial", Font.BOLD, 11));
        btnTimSDT.addActionListener(this);
        sdtPanel.add(btnTimSDT);
        pTichDiem.add(sdtPanel);

        JPanel diemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diemPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblDiem = new JLabel("Điểm:");
        lblDiem.setFont(new Font("Arial", Font.BOLD, 14));
        lblDiem.setForeground(TITLE_COLOR);
        diemPanel.add(lblDiem);

        lblValueDiem = new JLabel("0");
        lblValueDiem.setFont(new Font("Arial", Font.BOLD, 12));
        diemPanel.add(lblValueDiem);
        pTichDiem.add(diemPanel);

        JPanel diemDoiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diemDoiPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblDiemDoi = new JLabel("Đổi:");
        lblDiemDoi.setFont(new Font("Arial", Font.BOLD, 14));
        lblDiemDoi.setForeground(TITLE_COLOR);
        diemDoiPanel.add(lblDiemDoi);

        txtDiemDoi = new JTextField(10);
        txtDiemDoi.setEnabled(false);
        txtDiemDoi.getDocument().addDocumentListener(this);
        diemDoiPanel.add(txtDiemDoi);

        checkBoxDiem = new JCheckBox("Tối đa");
        checkBoxDiem.setFont(new Font("Arial", Font.BOLD, 12));
        checkBoxDiem.setEnabled(false);
        checkBoxDiem.setSelected(true);
        checkBoxDiem.addItemListener(this);
        diemDoiPanel.add(checkBoxDiem);
        pTichDiem.add(diemDoiPanel);

        btnHuy = new JButton("Hủy tích đổi điểm");
        btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
        btnHuy.addActionListener(this);
        pTichDiem.add(btnHuy);

        btnThemKhach = new JButton("Thêm khách");
        btnThemKhach.setFont(new Font("Arial", Font.BOLD, 14));
        btnThemKhach.addActionListener(this);
        pTichDiem.add(btnThemKhach);

        pBarThanhToan.add(pTichDiem);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Thanh toán button
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 20));
        btnThanhToan.addActionListener(this);
        pBarThanhToan.add(btnThanhToan);

        return pBarThanhToan;
    }



    @Override
    public void insertUpdate(DocumentEvent e) {}

    @Override
    public void removeUpdate(DocumentEvent e) {}

    @Override
    public void changedUpdate(DocumentEvent e) {}

    @Override
    public void itemStateChanged(ItemEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}

class CardProducts extends JPanel {
    public CardProducts(String imagePath, String tenSP, String gia, int soLuong) {
        setPreferredSize(new Dimension(150, 200)); // Adjustable based on parent size
        setBackground(new Color(220, 239, 218));
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel lblImage = new JLabel();
        ImageIcon icon = new ImageIcon("/client_images");
        if (icon.getImage() != null) {
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        }
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImage, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(220, 239, 218));
        infoPanel.setLayout(new GridLayout(3, 1));

        JLabel lblTenSP = new JLabel(tenSP, SwingConstants.CENTER);
        lblTenSP.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblTenSP);

        JLabel lblGia = new JLabel(" " + gia + " ", SwingConstants.CENTER);
        lblGia.setFont(new Font("Arial", Font.BOLD, 12));
        lblGia.setBackground(new Color(255, 102, 102));
        lblGia.setOpaque(true);
        infoPanel.add(lblGia);

        JLabel lblSoLuong = new JLabel("SL: " + soLuong, SwingConstants.CENTER);
        lblSoLuong.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblSoLuong);

        add(infoPanel, BorderLayout.SOUTH);
    }
}