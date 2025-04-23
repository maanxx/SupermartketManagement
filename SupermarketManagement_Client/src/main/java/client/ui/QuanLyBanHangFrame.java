package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyBanHangFrame extends JPanel implements ActionListener, DocumentListener, ItemListener, MouseListener {
    private final SanPhamService sanPhamService;
    private JTable tableGioHang;
    private DefaultTableModel tableModelGioHang;
    private JTextField txtSearch, txtMaVach, txtSdt, txtDiemDoi, txtTienKhach;
    private JLabel lblTongTien, lblTongTienHD, lblValueTongTien, lblValueTongTienHD, lblValueTienThua, lblValueDiem, lblValueGiaCuoiCung;
    private JButton btnLamMoiGH, btnTimSDT, btnHuy, btnThemKhach, btnThanhToan;
    private JCheckBox checkBoxDiem;
    private List<SanPhamDTO> gioHang;
    private JPanel pSanPham;
    private boolean StatusDoiTichDiem = false;

    // Colors matching Gui_BanHang
    private static final Color PANEL_BG_COLOR = new Color(220, 239, 218);
    private static final Color CART_BG_COLOR = new Color(224, 240, 196);
    private static final Color PRICE_COLOR = new Color(0, 168, 84);

    // Reference resolution
    private static final int REF_WIDTH = 913;
    private static final int REF_HEIGHT = 625;

    private double widthScale;
    private double heightScale;
    private double fontScale;

    public QuanLyBanHangFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
        this.gioHang = new ArrayList<>();
        initializeScalingFactors();
        setPreferredSize(new Dimension((int) (widthScale * REF_WIDTH), (int) (heightScale * REF_HEIGHT)));
        setLayout(new BorderLayout()); // Sử dụng BorderLayout để dễ dàng căn chỉnh các thành phần

        createUIComponents();
    }

    private void initializeScalingFactors() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        widthScale = screenWidth / (double) REF_WIDTH;
        heightScale = screenHeight / (double) REF_HEIGHT;
        fontScale = Math.min(widthScale, heightScale);
    }

    private void createUIComponents() {
        add(createHoaDonPanel(), BorderLayout.NORTH); // Đưa panel hóa đơn lên phía trên
        add(createProductPanel(), BorderLayout.CENTER); // Panel sản phẩm sẽ chiếm giữa
        add(createCheckoutPanel(), BorderLayout.SOUTH); // Panel thanh toán sẽ nằm ở dưới
    }

    private JPanel createHoaDonPanel() {
        JPanel pHoaDon = new JPanel();
        pHoaDon.setBackground(CART_BG_COLOR);
        pHoaDon.setBounds(0, 0, scaleWidth(600), scaleHeight(200));
        pHoaDon.setLayout(new BorderLayout(0, 0));

        tableModelGioHang = new DefaultTableModel(new String[]{"Mã SP", "Mã Giá", "Tên SP", "DVT", "SL", "Đơn Giá", "Thành Tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        tableGioHang.setRowHeight(scaleHeight(25));
        tableGioHang.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.BOLD, scaleFont(12)));
        tableGioHang.getColumnModel().getColumn(3).setPreferredWidth(scaleWidth(80));
        tableGioHang.getColumnModel().getColumn(4).setPreferredWidth(scaleWidth(40));
        tableGioHang.addMouseListener(this);

        JScrollPane scrollTableGioHang = new JScrollPane(tableGioHang);
        pHoaDon.add(scrollTableGioHang, BorderLayout.CENTER);
        return pHoaDon;
    }

    private JPanel createProductPanel() {
        JPanel pWrapSP = new JPanel();
        pWrapSP.setBackground(PANEL_BG_COLOR);
        pWrapSP.setBorder(new LineBorder(Color.BLACK));
        pWrapSP.setBounds(0, scaleHeight(200), scaleWidth(600), scaleHeight(300));
        pWrapSP.setLayout(null);

        JLabel lblTimSP = new JLabel("Tên hoặc mã SP:");
        lblTimSP.setFont(new Font("Segoe UI Semibold", Font.BOLD, scaleFont(12)));
        lblTimSP.setBounds(scaleWidth(10), scaleHeight(10), scaleWidth(100), scaleHeight(20));
        pWrapSP.add(lblTimSP);

        txtSearch = new JTextField();
        txtSearch.setBounds(scaleWidth(120), scaleHeight(10), scaleWidth(220), scaleHeight(20));
        txtSearch.setColumns(10);
        txtSearch.getDocument().addDocumentListener(this);
        pWrapSP.add(txtSearch);

        btnLamMoiGH = new JButton("Làm mới giỏ hàng");
        btnLamMoiGH.setFont(new Font("Segoe UI Semibold", Font.BOLD, scaleFont(10)));
        btnLamMoiGH.setBounds(scaleWidth(350), scaleHeight(10), scaleWidth(150), scaleHeight(20));
        btnLamMoiGH.addActionListener(this);
        pWrapSP.add(btnLamMoiGH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(scaleWidth(10), scaleHeight(40), scaleWidth(580), scaleHeight(230));
        pWrapSP.add(scrollPane);

        pSanPham = new JPanel();
        pSanPham.setBackground(PANEL_BG_COLOR);
        pSanPham.setLayout(new FlowLayout(FlowLayout.LEFT));
        pSanPham.setBorder(BorderFactory.createEmptyBorder(scaleWidth(10), scaleWidth(10), scaleHeight(10), scaleWidth(10)));
        scrollPane.setViewportView(pSanPham);
        return pWrapSP;
    }

    private JPanel createCheckoutPanel() {
        JPanel pBarThanhToan = new JPanel();
        pBarThanhToan.setBackground(PANEL_BG_COLOR);
        pBarThanhToan.setBounds(scaleWidth(610), 0, scaleWidth(240), scaleHeight(625));
        pBarThanhToan.setLayout(null);

        addCheckoutFields(pBarThanhToan);
        addCheckoutButtons(pBarThanhToan);

        return pBarThanhToan;
    }

    private void addCheckoutFields(JPanel pBarThanhToan) {
        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setFont(new Font("Segoe UI Semibold", Font.BOLD, scaleFont(12)));
        lblMaVach.setBounds(scaleWidth(10), scaleHeight(10), scaleWidth(60), scaleHeight(20));
        pBarThanhToan.add(lblMaVach);

        txtMaVach = new JTextField();
        txtMaVach.setBounds(scaleWidth(10), scaleHeight(30), scaleWidth(200), scaleHeight(20));
        txtMaVach.setColumns(10);
        txtMaVach.getDocument().addDocumentListener(this);
        pBarThanhToan.add(txtMaVach);
    }

    private void addCheckoutButtons(JPanel pBarThanhToan) {
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI Semibold", Font.BOLD, scaleFont(18)));
        btnThanhToan.setBounds(scaleWidth(20), scaleHeight(560), scaleWidth(200), scaleHeight(40));
        btnThanhToan.addActionListener(this);
        pBarThanhToan.add(btnThanhToan);
    }

    private int scaleWidth(int width) {
        return (int) (width * widthScale);
    }

    private int scaleHeight(int height) {
        return (int) (height * heightScale);
    }

    private int scaleFont(int fontSize) {
        return (int) (fontSize * fontScale);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button actions
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Handle item state changes
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Handle mouse clicks
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Handle mouse press
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Handle mouse release
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Handle mouse enter
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Handle mouse exit
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // Handle document insert update
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Handle document remove update
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Handle document change update
    }
}
