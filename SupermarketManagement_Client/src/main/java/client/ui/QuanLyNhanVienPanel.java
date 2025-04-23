package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class QuanLyNhanVienPanel extends JPanel {
    private final NhanVienService nhanVienService;
    private JTable tableNhanVien;
    private DefaultTableModel tableModel;
    private JTextField txtMaNV, txtHoTen, txtDiaChi, txtSoDienThoai, txtChucVu;
    private JDateChooser dateNgaySinh, dateNgayLamViec;
    private JComboBox<String> cbTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTiepLai;
    private JTextField txtSearchMaNV, txtSearchSoDienThoai;
    private boolean isEditMode = false;
    private String currentEditingMaNV = null;

    private void applyCustomTableHeader(JTable table) {
        table.getTableHeader().setDefaultRenderer(new CustomTableHeaderRenderer(table));
    }

    public QuanLyNhanVienPanel(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(220, 230, 240));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(180, 200, 220));
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 128)));
        add(contentPanel, BorderLayout.CENTER);

        // Top panel: Form and Buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(180, 200, 220));
        contentPanel.add(topPanel, BorderLayout.CENTER);

        // Form panel (left, ~80%)
        JPanel formPanel = createFormPanel();
        formPanel.setPreferredSize(new Dimension(720, 300)); // 80% of ~900px total width

        // Button panel (right, ~20%)
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setPreferredSize(new Dimension(180, 200)); // Reduced height to match image

        // Use JSplitPane to create a divider with proportions matching the image
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, buttonPanel);
        splitPane.setDividerLocation(0.8); // 80% for formPanel, 20% for buttonPanel
        splitPane.setDividerSize(10);
        splitPane.setBackground(new Color(180, 200, 220));
        splitPane.setOpaque(true);
        splitPane.setEnabled(false); // Disable resizing to match the fixed layout in the image
        topPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom panel: Search fields and Table
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(180, 200, 220));
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Search panel above the table
        JPanel searchPanel = createSearchPanel();
        bottomPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        setupTable();
        bottomPanel.add(new JScrollPane(tableNhanVien), BorderLayout.CENTER);

        applyCustomTableHeader(tableNhanVien);
    }

    private void setupTable() {
        String[] columnNames = {"Mã NV", "Họ tên", "Địa chỉ", "SĐT", "Chức vụ", "Ngày sinh", "Ngày LV", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableNhanVien.getTableHeader().setBackground(new Color(0, 102, 204));
        tableNhanVien.getTableHeader().setForeground(Color.WHITE);
        tableNhanVien.setSelectionBackground(new Color(200, 220, 255));
        tableNhanVien.setGridColor(new Color(220, 220, 220));
        tableNhanVien.setShowHorizontalLines(true);
        tableNhanVien.setShowVerticalLines(false);

        tableNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    int selectedRow = tableNhanVien.getSelectedRow();
                    if (selectedRow >= 0) {
                        showEditNhanVienForm(selectedRow);
                    }
                }
            }
        });

        tableNhanVien.getTableHeader().setReorderingAllowed(false);

    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(new Color(180, 200, 220));  // Màu nền giống khung chức năng

        // TitledBorder với màu chữ đen và không có viền
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Thông tin nhân viên");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 20)); // Font chữ to hơn
        titledBorder.setTitleColor(Color.BLACK); // Màu chữ đen

        panel.setBorder(titledBorder);

        // Tạo các JTextField và JDateChooser với kích thước giống nhau
        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setPreferredSize(new Dimension(200, 40)); // Kích thước giống nhau
        txtHoTen = new JTextField();
        txtHoTen.setPreferredSize(new Dimension(200, 40));
        txtDiaChi = new JTextField();
        txtDiaChi.setPreferredSize(new Dimension(200, 40));
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setPreferredSize(new Dimension(200, 40));
        txtChucVu = new JTextField();
        txtChucVu.setPreferredSize(new Dimension(200, 40));
        dateNgaySinh = new JDateChooser();
        dateNgaySinh.setDateFormatString("dd/MM/yyyy");
        dateNgaySinh.setPreferredSize(new Dimension(200, 40));
        dateNgayLamViec = new JDateChooser();
        dateNgayLamViec.setDateFormatString("dd/MM/yyyy");
        dateNgayLamViec.setPreferredSize(new Dimension(200, 40));
        cbTrangThai = new JComboBox<>(new String[]{"Đang làm", "Nghỉ việc"});
        cbTrangThai.setPreferredSize(new Dimension(200, 40));

        // Center the text in JComboBox
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        cbTrangThai.setRenderer(renderer);

        // Thêm các thành phần vào panel
        panel.add(new JLabel("Mã Nhân viên:"));
        panel.add(txtMaNV);
        panel.add(new JLabel("Tên nhân viên:"));
        panel.add(txtHoTen);
        panel.add(new JLabel("Địa chỉ:"));
        panel.add(txtDiaChi);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(txtSoDienThoai);
        panel.add(new JLabel("Chức vụ:"));
        panel.add(txtChucVu);
        panel.add(new JLabel("Ngày sinh:"));
        panel.add(dateNgaySinh);
        panel.add(new JLabel("Ngày làm việc:"));
        panel.add(dateNgayLamViec);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(cbTrangThai);

        return panel;
    }





    private JPanel createButtonPanel() {
        // Tạo panel chứa các nút
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(180, 200, 220));

        // TitledBorder với màu chữ trắng và nền xanh nhạt
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Chức năng");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 20)); // Font chữ to hơn
        titledBorder.setTitleColor(Color.BLACK); // Màu chữ trắng

        // Tạo màu nền xanh nhạt cho border
        Border outerBorder = BorderFactory.createLineBorder(new Color(173, 216, 230), 2); // Màu xanh nhạt
        titledBorder.setBorder(BorderFactory.createCompoundBorder(outerBorder, titledBorder.getBorder())); // Áp dụng border

        panel.setBorder(titledBorder);

        // Cấu hình GridBagConstraints để căn giữa các nút
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Kích thước và font chữ của các nút
        Dimension buttonSize = new Dimension(120, 40);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        // Tạo các nút
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnTiepLai = new JButton("Tải Lại");

        // Cài đặt các nút
        btnThem.setPreferredSize(buttonSize);
        btnThem.setFont(buttonFont);
        btnThem.setBackground(new Color(0, 102, 204));
        btnThem.setForeground(Color.WHITE);

        btnSua.setPreferredSize(buttonSize);
        btnSua.setFont(buttonFont);
        btnSua.setBackground(new Color(0, 102, 204));
        btnSua.setForeground(Color.WHITE);

        btnXoa.setPreferredSize(buttonSize);
        btnXoa.setFont(buttonFont);
        btnXoa.setBackground(new Color(0, 102, 204));
        btnXoa.setForeground(Color.WHITE);

        btnTiepLai.setPreferredSize(buttonSize);
        btnTiepLai.setFont(buttonFont);
        btnTiepLai.setBackground(new Color(0, 102, 204));
        btnTiepLai.setForeground(Color.WHITE);

        // Gắn sự kiện cho các nút
        btnThem.addActionListener(e -> showAddNhanVienForm());
        btnSua.addActionListener(e -> {
            int selectedRow = tableNhanVien.getSelectedRow();
            if (selectedRow >= 0) {
                showEditNhanVienForm(selectedRow);
            }
        });
        // btnXoa.addActionListener(e -> deleteNhanVien());
        btnTiepLai.addActionListener(e -> clearForm());

        // Thêm các nút vào panel theo chiều dọc và căn giữa
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnThem, gbc);

        gbc.gridy++;
        panel.add(btnSua, gbc);

        gbc.gridy++;
        panel.add(btnXoa, gbc);

        gbc.gridy++;
        panel.add(btnTiepLai, gbc);

        return panel;
    }






    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(180, 200, 220));

        panel.add(new JLabel("Nhập mã nhân viên:"));
        txtSearchMaNV = new JTextField(10);
        panel.add(txtSearchMaNV);

        panel.add(new JLabel("Nhập số điện thoại:"));
        txtSearchSoDienThoai = new JTextField(10);
        panel.add(txtSearchSoDienThoai);

        return panel;
    }

    private void showAddNhanVienForm() {
        isEditMode = false;
        currentEditingMaNV = null;
        clearForm();
        txtMaNV.setText(generateMaNhanVien());
    }

    private void showEditNhanVienForm(int selectedRow) {
        isEditMode = true;
        currentEditingMaNV = tableModel.getValueAt(selectedRow, 0).toString();

        txtMaNV.setText(currentEditingMaNV);
        txtHoTen.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtDiaChi.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtSoDienThoai.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtChucVu.setText(tableModel.getValueAt(selectedRow, 4).toString());

        try {
            String dateStr = tableModel.getValueAt(selectedRow, 5).toString();
            if (!dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(dateStr);
                dateNgaySinh.setDate(date);
            } else {
                dateNgaySinh.setDate(null);
            }
        } catch (Exception e) {
            dateNgaySinh.setDate(null);
        }

        try {
            String dateStr = tableModel.getValueAt(selectedRow, 6).toString();
            if (!dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(dateStr);
                dateNgayLamViec.setDate(date);
            } else {
                dateNgayLamViec.setDate(null);
            }
        } catch (Exception e) {
            dateNgayLamViec.setDate(null);
        }

        cbTrangThai.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
    }

    private void deleteNhanVien() {
        int selectedRow = tableNhanVien.getSelectedRow();
        if (selectedRow >= 0) {
            String maNV = tableModel.getValueAt(selectedRow, 0).toString();
            try {
                nhanVienService.deleteNhanVien(maNV);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Nhân viên đã được xóa.");
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa nhân viên.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa.");
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtChucVu.setText("");
        dateNgaySinh.setDate(null);
        dateNgayLamViec.setDate(null);
        cbTrangThai.setSelectedIndex(0);
        isEditMode = false;
        currentEditingMaNV = null;
    }

    private String generateMaNhanVien() {
        try {
            List<NhanVienDTO> list = nhanVienService.getAllNhanViens();
            return "NV" + String.format("%04d", list.size() + 1);
        } catch (RemoteException e) {
            return "NV0001";
        }
    }
}
