package client.ui;

import shared.dto.NhanVienDTO;
import shared.services.NhanVienService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JDateChooser dateNgaySinh, dateNgayLamViec; // Changed to JDateChooser
    private JComboBox<String> cbTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTiepLai;
    private JTextField txtSearchMaNV, txtSearchSoDienThoai;
    private boolean isEditMode = false;
    private String currentEditingMaNV = null;

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
    }

    private void setupTable() {
        // Match table columns to the image
        String[] columnNames = {"Mã Nhân viên", "Tên nhân viên", "Địa Chỉ", "Số Điện Thoại", "Chức vụ",
                "Ngày Sinh", "Ngày làm việc", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableNhanVien = new JTable(tableModel);
        tableNhanVien.setRowHeight(30);
        tableNhanVien.setFont(new Font("Arial", Font.PLAIN, 12));
        tableNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableNhanVien.setSelectionBackground(new Color(200, 220, 240));
        tableNhanVien.setGridColor(new Color(200, 200, 200));

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
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(new Color(180, 200, 220));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setHorizontalAlignment(JTextField.CENTER); // Center text
        txtHoTen = new JTextField();
        txtHoTen.setHorizontalAlignment(JTextField.CENTER); // Center text
        txtDiaChi = new JTextField();
        txtDiaChi.setHorizontalAlignment(JTextField.CENTER); // Center text
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setHorizontalAlignment(JTextField.CENTER); // Center text
        txtChucVu = new JTextField();
        txtChucVu.setHorizontalAlignment(JTextField.CENTER); // Center text
        dateNgaySinh = new JDateChooser(); // Use JDateChooser for date selection
        dateNgaySinh.setDateFormatString("dd/MM/yyyy"); // Set date format
        dateNgayLamViec = new JDateChooser(); // Use JDateChooser for date selection
        dateNgayLamViec.setDateFormatString("dd/MM/yyyy"); // Set date format
        cbTrangThai = new JComboBox<>(new String[]{"Đang làm", "Nghỉ việc"});

        // Center the text in JComboBox
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        cbTrangThai.setRenderer(renderer);

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
        // Use GridLayout with smaller gaps to match the image
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 2)); // Reduced vertical gap to 2px
        panel.setBackground(new Color(180, 200, 220));
        panel.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnTiepLai = new JButton("Tải Lại");

        // Set button properties with smaller size
        Dimension buttonSize = new Dimension(90, 30); // Button size matches image
        Font buttonFont = new Font("Arial", Font.PLAIN, 12); // Font size matches image

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

        btnThem.addActionListener(e -> showAddNhanVienForm());
        btnSua.addActionListener(e -> {
            int selectedRow = tableNhanVien.getSelectedRow();
            if (selectedRow >= 0) {
                showEditNhanVienForm(selectedRow);
            }
        });
        // btnXoa.addActionListener(e -> deleteNhanVien());
        btnTiepLai.addActionListener(e -> clearForm());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnTiepLai);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(180, 200, 220));
        panel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nhân viên")); // Added titled border

        panel.add(new JLabel("Nhập mã nhân viên:"));
        txtSearchMaNV = new JTextField(10); // Reduced size to match image
        panel.add(txtSearchMaNV);

        panel.add(new JLabel("Nhập số điện thoại:"));
        txtSearchSoDienThoai = new JTextField(10); // Reduced size to match image
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

        // Parse and set date for Ngày Sinh
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

        // Parse and set date for Ngày làm việc
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

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtChucVu.setText("");
        dateNgaySinh.setDate(null); // Clear date picker
        dateNgayLamViec.setDate(null); // Clear date picker
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