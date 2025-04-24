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
import java.io.File;
import java.rmi.RemoteException;
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

        centerPanel.add(createProductPanel(), BorderLayout.CENTER);
        centerPanel.add(createCartPanel(), BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(createCheckoutPanel(), BorderLayout.EAST);

        loadProductData();
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
        cartContainer.setPreferredSize(new Dimension(0, 200));
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
        btnLamMoiGH.addActionListener(this);
        searchPanel.add(btnLamMoiGH);

        pWrapSP.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();

        // 🔽 Panel chứa các sản phẩm
        pSanPham = new JPanel();
        pSanPham.setBackground(PANEL_BG_COLOR);
        pSanPham.setLayout(new GridLayout(0, 3, 15, 15));

        // 🔁 Wrapper để GridLayout có thể cuộn dọc
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(PANEL_BG_COLOR);
        wrapper.add(pSanPham);

        scrollPane.setViewportView(wrapper);
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
        btnThanhToan.getButton().addActionListener(this);
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

        public JButton getButton() {
            return button;
        }

        public void addActionListener(ActionListener listener) {
            button.addActionListener(listener);
        }
    }

    private void loadProductData() {
        try {
            List<SanPhamDTO> productList = sanPhamService.getAllSanPhams();
            pSanPham.removeAll();

            for (SanPhamDTO sp : productList) {
                String tenSP = sp.getTenSanPham();
                String gia = String.format("%,.0f VND", sp.getGia());
                int soLuong = sp.getSoLuong();
                String imagePath = sp.getHinhAnh(); // giả sử có field hình ảnh

                pSanPham.add(new CardProducts(sp, this));
            }

            pSanPham.revalidate();
            pSanPham.repaint();
            int rowCount = (int) Math.ceil(productList.size() / 3.0); // 3 cột
            int cellHeight = 220; // chiều cao mỗi card
            pSanPham.setPreferredSize(new Dimension(0, rowCount * cellHeight));

        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themVaoGioHang(SanPhamDTO sp) {
        // Tăng SL nếu đã có
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            if (tableModelGioHang.getValueAt(i, 0).toString().equals(sp.getMaSanPham())) {
                int oldSL = Integer.parseInt(tableModelGioHang.getValueAt(i, 3).toString());
                tableModelGioHang.setValueAt(oldSL + 1, i, 3);
                tableModelGioHang.setValueAt((oldSL + 1) * sp.getGia(), i, 5);
                capNhatTongTien(); // ⬅ cần viết thêm
                return;
            }
        }

        // Nếu chưa có
        tableModelGioHang.addRow(new Object[]{
                sp.getMaSanPham(), sp.getTenSanPham(), "cái", 1, sp.getGia(), sp.getGia()
        });

        // Trừ tồn kho
        for (Component comp : pSanPham.getComponents()) {
            if (comp instanceof CardProducts card) {
                if (card.getSanPham().getMaSanPham().equals(sp.getMaSanPham())) {
                    card.decreaseSoLuong();
                    break;
                }
            }
        }

        capNhatTongTien();
    }
    private void capNhatTongTien() {
        double tongTien = 0;
        for (int i = 0; i < tableModelGioHang.getRowCount(); i++) {
            tongTien += Double.parseDouble(tableModelGioHang.getValueAt(i, 5).toString());
        }
        lblTongTien.setText(String.format("%,.0f VND", tongTien));
        lblValueTongTienHD.setText(String.format("%,.0f VND", tongTien));
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
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getSource() == tableGioHang) {
            int row = tableGioHang.getSelectedRow();
            if (row >= 0) {
                String maSP = tableModelGioHang.getValueAt(row, 0).toString();
                int soLuong = Integer.parseInt(tableModelGioHang.getValueAt(row, 3).toString());
                double donGia = Double.parseDouble(tableModelGioHang.getValueAt(row, 4).toString());

                if (soLuong > 1) {
                    tableModelGioHang.setValueAt(soLuong - 1, row, 3);
                    tableModelGioHang.setValueAt((soLuong - 1) * donGia, row, 5);
                } else {
                    tableModelGioHang.removeRow(row);
                }

                for (Component comp : pSanPham.getComponents()) {
                    if (comp instanceof CardProducts card) {
                        if (card.getSanPham().getMaSanPham().equals(maSP)) {
                            card.increaseSoLuong();
                            break;
                        }
                    }
                }
                capNhatTongTien();
            }
        }
    }

    private void xuLyThanhToan() {
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
            double gia = Double.parseDouble(tableModelGioHang.getValueAt(i, 4).toString());
            int soLuong = Integer.parseInt(tableModelGioHang.getValueAt(i, 3).toString());
            double thanhTien = gia * soLuong;

            hoaDon.append(String.format("%-15s %-20s %-10.0f %-10d %-10.0f\n", maSP, tenSP, gia, soLuong, thanhTien));
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
            QuanLyHoaDonPanel.luuHoaDon(maHoaDon, ngayLap, tongTien, chonThanhToan);

            JOptionPane.showMessageDialog(this,
                    "Thanh toán thành công bằng " + chonThanhToan +
                            "!\nSố tiền: " + String.format("%,.0f", tongTien) + " VND",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            tableModelGioHang.setRowCount(0);
            capNhatTongTien();
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
    public void actionPerformed(ActionEvent e) {
        if ("add-to-cart".equals(e.getActionCommand())) {
            if (e.getSource() instanceof SanPhamDTO sanPham) {
                themVaoGioHang(sanPham);
            }
        } else if (e.getSource() == btnThanhToan.getButton()) {
            xuLyThanhToan();
        }
    }
}

class CardProducts extends JPanel {
    private SanPhamDTO sanPham;
    private JLabel lblSoLuong;

    public CardProducts(SanPhamDTO sanPham, ActionListener listener) {
        this.sanPham = sanPham;

        setPreferredSize(new Dimension(150, 200));
        setBackground(new Color(220, 239, 218));
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Hiển thị ảnh sản phẩm
        JLabel lblImage = new JLabel();
        File imgFile = new File("client_images/" + sanPham.getHinhAnh());
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        } else {
            lblImage.setText("Image not found");
            lblImage.setForeground(Color.RED);
        }
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImage, BorderLayout.CENTER);

        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(getBackground());
        infoPanel.add(new JLabel(sanPham.getTenSanPham(), SwingConstants.CENTER));

        JLabel lblGia = new JLabel(String.format("%,.0f VND", sanPham.getGia()), SwingConstants.CENTER);
        lblGia.setOpaque(true);
        lblGia.setBackground(new Color(255, 102, 102));
        infoPanel.add(lblGia);

        lblSoLuong = new JLabel("SL: " + sanPham.getSoLuong(), SwingConstants.CENTER);
        infoPanel.add(lblSoLuong);

        add(infoPanel, BorderLayout.SOUTH);

        // Bắt sự kiện click
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionEvent evt = new ActionEvent(sanPham, ActionEvent.ACTION_PERFORMED, "add-to-cart");
                listener.actionPerformed(evt);
            }
        });
    }

    public SanPhamDTO getSanPham() {
        return sanPham;
    }

    public void decreaseSoLuong() {
        if (sanPham.getSoLuong() > 0) {
            sanPham.setSoLuong(sanPham.getSoLuong() - 1);
            lblSoLuong.setText("SL: " + sanPham.getSoLuong());
        }
    }

    public void increaseSoLuong() {
        sanPham.setSoLuong(sanPham.getSoLuong() + 1);
        lblSoLuong.setText("SL: " + sanPham.getSoLuong());
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


