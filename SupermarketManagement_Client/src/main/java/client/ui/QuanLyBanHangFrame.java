package client.ui;

import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
    private HoverPanelButton btnLamMoiGH;
    private HoverPanelButton btnTimSDT;
    private HoverPanelButton btnHuy;
    private HoverPanelButton btnThemKhach;
    private HoverPanelButton btnThanhToan;
    private JCheckBox checkBoxDiem;
    private List<SanPhamDTO> gioHang;
    private JPanel pSanPham;

    private static final Color PANEL_BG_COLOR = new Color(220, 239, 218);
    private static final Color CART_BG_COLOR = new Color(224, 240, 196);
    private static final Color PRICE_BG_COLOR = new Color(255, 102, 102);
    private static final Color PRICE_COLOR = new Color(0, 168, 84);
    private static final Color BUTTON_HOVER_COLOR = new Color(0, 180, 0);

    public QuanLyBanHangFrame(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
        this.gioHang = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(PANEL_BG_COLOR);

        centerPanel.add(createCartPanel(), BorderLayout.NORTH);
        centerPanel.add(createProductPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(createCheckoutPanel(), BorderLayout.EAST);

        loadSampleProducts();
    }

    private JPanel createCartPanel() {
        JPanel pHoaDon = new JPanel();
        pHoaDon.setBackground(CART_BG_COLOR);
        pHoaDon.setLayout(new BorderLayout(5, 5));
        pHoaDon.setBorder(new LineBorder(Color.white));

        String[] columnNamesGioHang = {"Mã SP", "Tên SP", "DVT", "SL", "Đơn Giá", "Thành Tiền"};
        tableModelGioHang = new DefaultTableModel(columnNamesGioHang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableGioHang = new JTable(tableModelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tableGioHang.getColumnModel().getColumn(3).setPreferredWidth(40);
        tableGioHang.getColumnModel().getColumn(2).setPreferredWidth(90);
        tableGioHang.addMouseListener(this);

        tableGioHang.getTableHeader().setDefaultRenderer(new CustomTableHeaderRenderer(tableGioHang));

        tableGioHang.getTableHeader().setReorderingAllowed(false);
        tableGioHang.getTableHeader().setResizingAllowed(true);

        JScrollPane scrollTableGioHang = new JScrollPane(tableGioHang);
        pHoaDon.add(scrollTableGioHang, BorderLayout.CENTER);

        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setPreferredSize(new Dimension(0, 550));
        cartContainer.add(pHoaDon, BorderLayout.CENTER);
        return cartContainer;
    }

    private JPanel createProductPanel() {
        JPanel pWrapSP = new JPanel();
        pWrapSP.setBackground(PANEL_BG_COLOR);
        pWrapSP.setBorder(new LineBorder(Color.BLACK));
        pWrapSP.setLayout(new BorderLayout(5, 5));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(PANEL_BG_COLOR);

        JLabel lblTimSP = new JLabel("Tên hoặc mã SP:");
        lblTimSP.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(lblTimSP);

        txtSearch = new JTextField(20);
        txtSearch.getDocument().addDocumentListener(this);
        searchPanel.add(txtSearch);

        btnLamMoiGH = new HoverPanelButton("Làm mới giỏ hàng", 10, new Color(0, 153, 0), BUTTON_HOVER_COLOR);
        setPreferredSize(new Dimension(150, 30));
        btnLamMoiGH.addActionListener(this);
        searchPanel.add(btnLamMoiGH);

        pWrapSP.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();

        pSanPham = new JPanel();
        pSanPham.setBackground(PANEL_BG_COLOR);

        pSanPham.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        scrollPane.setViewportView(pSanPham);
        pWrapSP.add(scrollPane, BorderLayout.CENTER);

        return pWrapSP;
    }

    private JPanel createCheckoutPanel() {
        JPanel pBarThanhToan = new JPanel();
        pBarThanhToan.setBackground(PANEL_BG_COLOR);
        pBarThanhToan.setLayout(new BoxLayout(pBarThanhToan, BoxLayout.Y_AXIS));
        pBarThanhToan.setBorder(new LineBorder(Color.BLACK));
        pBarThanhToan.setPreferredSize(new Dimension(260, 0));
        pBarThanhToan.add(Box.createVerticalStrut(20));

        // Mã vạch
        JPanel maVachPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maVachPPanel.setBackground(PANEL_BG_COLOR);

        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setFont(new Font("Arial", Font.BOLD, 14));
        maVachPPanel.add(lblMaVach);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        txtMaVach = new JTextField(20);
        txtMaVach.getDocument().addDocumentListener(this);
        maVachPPanel.add(txtMaVach);

        pBarThanhToan.add(maVachPPanel);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        // Tổng tiền SP
        JPanel tongTienSPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tongTienSPPanel.setBackground(PANEL_BG_COLOR);

        JLabel lblTongTienSP = new JLabel("Tổng tiền sản phẩm:");
        lblTongTienSP.setFont(new Font("Arial", Font.BOLD, 14));
        tongTienSPPanel.add(lblTongTienSP);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        lblTongTien = new JLabel("0");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongTien.setForeground(PRICE_COLOR);
        lblTongTien.setHorizontalAlignment(SwingConstants.LEFT);
        lblTongTien.setPreferredSize(new Dimension(160, 30));
        lblTongTien.setMaximumSize(new Dimension(160, 30));
        tongTienSPPanel.add(lblTongTien);

        pBarThanhToan.add(tongTienSPPanel);
        pBarThanhToan.add(Box.createVerticalStrut(10));

// Tổng tiền HD
        JPanel tongTienHDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tongTienHDPanel.setBackground(PANEL_BG_COLOR);

        lblTongTienHD = new JLabel("Tổng tiền hóa đơn:");
        lblTongTienHD.setFont(new Font("Arial", Font.BOLD, 14));
        tongTienHDPanel.add(lblTongTienHD);
        pBarThanhToan.add(Box.createVerticalStrut(10));

        lblValueTongTienHD = new JLabel("0");
        lblValueTongTienHD.setFont(new Font("Arial", Font.BOLD, 12));
        lblValueTongTienHD.setForeground(PRICE_COLOR);
        lblValueTongTienHD.setHorizontalAlignment(SwingConstants.LEFT);
        lblValueTongTienHD.setPreferredSize(new Dimension(160, 30));
        lblValueTongTienHD.setMaximumSize(new Dimension(160, 30));
        tongTienHDPanel.add(lblValueTongTienHD);

        pBarThanhToan.add(tongTienHDPanel);
        pBarThanhToan.add(Box.createVerticalStrut(10));


        // Tiền khách
        JPanel tienKhachPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tienKhachPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblTienKhach = new JLabel("Tiền khách trả:");
        lblTienKhach.setFont(new Font("Arial", Font.BOLD, 14));
        tienKhachPanel.add(lblTienKhach);

        txtTienKhach = new JTextField(20);
        txtTienKhach.setFont(new Font("Arial", Font.BOLD, 12));
        txtTienKhach.setForeground(PRICE_COLOR);
        txtTienKhach.getDocument().addDocumentListener(this);
        tienKhachPanel.add(txtTienKhach);

        pBarThanhToan.add(tienKhachPanel);

        // Tiền thừa
        JPanel tienThuaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tienThuaPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblTienThua = new JLabel("Tiền thừa:");
        lblTienThua.setFont(new Font("Arial", Font.BOLD, 14));
        tienThuaPanel.add(lblTienThua);

        lblValueTienThua = new JLabel("0");
        lblValueTienThua.setFont(new Font("Arial", Font.BOLD, 12));
        lblValueTienThua.setForeground(PRICE_COLOR);
        lblValueTienThua.setHorizontalAlignment(SwingConstants.LEFT);
        lblValueTienThua.setPreferredSize(new Dimension(260, 30));
        lblValueTienThua.setMaximumSize(new Dimension(260, 30));
        tienThuaPanel.add(lblValueTienThua);

        pBarThanhToan.add(tienThuaPanel);

        // tích đểm
        JPanel pTichDiem = new JPanel();
        pTichDiem.setBackground(PANEL_BG_COLOR);
        pTichDiem.setBorder(BorderFactory.createTitledBorder("Tích đổi điểm"));
        pTichDiem.setLayout(new BoxLayout(pTichDiem, BoxLayout.Y_AXIS));
        pTichDiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JPanel sdtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sdtPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblSdt = new JLabel("SDT:");
        lblSdt.setFont(new Font("Arial", Font.BOLD, 14));
        sdtPanel.add(lblSdt);

        txtSdt = new JTextField(10);
        sdtPanel.add(txtSdt);

        btnTimSDT = new HoverPanelButton("Tìm", 10, new Color(0, 153, 0), BUTTON_HOVER_COLOR);
        btnTimSDT.setFont(new Font("Arial", Font.BOLD, 11));
        btnTimSDT.addActionListener(this);
        sdtPanel.add(btnTimSDT);
        pTichDiem.add(sdtPanel);

        JPanel diemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diemPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblDiem = new JLabel("Điểm:");
        lblDiem.setFont(new Font("Arial", Font.BOLD, 14));
        diemPanel.add(lblDiem);

        lblValueDiem = new JLabel("0");
        lblValueDiem.setFont(new Font("Arial", Font.BOLD, 12));
        diemPanel.add(lblValueDiem);
        pTichDiem.add(diemPanel);

        JPanel diemDoiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        diemDoiPanel.setBackground(PANEL_BG_COLOR);
        JLabel lblDoi = new JLabel("Đổi:");
        lblDoi.setFont(new Font("Arial", Font.BOLD, 14));
        diemDoiPanel.add(lblDoi);

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

        btnHuy = new HoverPanelButton("Hủy tích đổi điểm", 2,new Color(0, 153, 0), BUTTON_HOVER_COLOR);
        btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
        btnHuy.setMaximumSize(new Dimension(160, 30));
        btnHuy.setPreferredSize(new Dimension(160, 30));
        btnHuy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHuy.addActionListener(this);
        pTichDiem.add(Box.createVerticalStrut(5));
        pTichDiem.add(btnHuy);

        btnThemKhach = new HoverPanelButton("Thêm khách hàng", 2, new Color(0, 153, 0), BUTTON_HOVER_COLOR);
        btnThemKhach.setFont(new Font("Arial", Font.BOLD, 14));
        btnThemKhach.setMaximumSize(new Dimension(160, 30));
        btnThemKhach.setPreferredSize(new Dimension(160, 30));
        btnThemKhach.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThemKhach.addActionListener(this);
        pTichDiem.add(Box.createVerticalStrut(5));
        pTichDiem.add(btnThemKhach);

        pBarThanhToan.add(Box.createVerticalStrut(10));
        pBarThanhToan.add(pTichDiem);
        pBarThanhToan.add(Box.createVerticalStrut(20));

        btnThanhToan = new HoverPanelButton("Thanh toán", 2, new Color(0, 153, 0), BUTTON_HOVER_COLOR);
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 30));
        btnThanhToan.setMaximumSize(new Dimension(160, 36));
        btnThanhToan.setPreferredSize(new Dimension(160, 50));
        btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThanhToan.addActionListener(this);
        pBarThanhToan.add(btnThanhToan);

        return pBarThanhToan;
    }

    class HoverPanelButton extends RoundedPanel {
        private JButton button;
        private Color backgroundColor;
        private Color hoverColor;

        public HoverPanelButton(String text, int radius, Color backgroundColor, Color hoverColor) {
            super(radius);
            this.backgroundColor = backgroundColor;
            this.hoverColor = hoverColor;
            setBackground(backgroundColor);
            setLayout(new BorderLayout());

            button = new JButton(text);
            button.setForeground(Color.WHITE);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);

            MouseAdapter hoverAdapter = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(backgroundColor);
                }
            };
            addMouseListener(hoverAdapter);
            button.addMouseListener(hoverAdapter);

            add(button, BorderLayout.CENTER);
        }

        public void addActionListener(ActionListener listener) {
            button.addActionListener(listener);
        }
    }
    private void loadSampleProducts() {
        pSanPham.removeAll();
        pSanPham.add(new CardProducts("coca.jpg", "Lon Coaca", "18,000 VND", 82));
        pSanPham.add(new CardProducts("gao-st25.jpg", "Gạo ST25", "8,000 VND", 98));
        pSanPham.add(new CardProducts("laptop_dell.jpg", "Laptop Dell", "4,000 VND", 91));
        pSanPham.add(new CardProducts("oreo.jpg", "Bánh Oreo Vị Vani", "9,000 VND", 75));
        pSanPham.revalidate();
        pSanPham.repaint();
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
    public void actionPerformed(ActionEvent e) {}
}

class CardProducts extends JPanel {
    public CardProducts(String imagePath, String tenSP, String gia, int soLuong) {
        setPreferredSize(new Dimension(150, 200));
        setBackground(new Color(220, 239, 218));
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel lblImage = new JLabel();
        String fullImagePath = "client_images/" + imagePath;
        java.net.URL imageURL = getClass().getResource(fullImagePath);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        } else {
            lblImage.setText("Image not found");
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
            lblImage.setForeground(Color.RED);
        }
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImage, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(220, 239, 218));
        infoPanel.setLayout(new GridLayout(3, 1));

        JLabel lblTenSP = new JLabel(tenSP, SwingConstants.CENTER);
        lblTenSP.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblTenSP);

        JLabel lblGia = new JLabel(gia, SwingConstants.CENTER);
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

class CustomTableHeaderRenderer implements TableCellRenderer {
    DefaultTableCellRenderer renderer;

    public CustomTableHeaderRenderer(JTable table) {
        renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        component.setBackground(new Color(156, 209, 150));
        component.setForeground(Color.BLACK);
        return component;
    }
}


