package client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyHoaDonFrame extends JFrame {
    private JTable tableHoaDon;
    private DefaultTableModel tableModel;
    private static List<String[]> danhSachHoaDon = new ArrayList<>();

    public QuanLyHoaDonFrame() {
        setTitle("Quản lý hóa đơn");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel lblHeader = new JLabel("Quản Lý Hóa Đơn", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Mã Hóa Đơn", "Ngày lập", "Tổng tiền", "Phương thức"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableHoaDon = new JTable(tableModel);
        tableHoaDon.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton btnXemChiTiet = createButton("🔍 Xem Chi Tiết");
        btnXemChiTiet.addActionListener(e -> xemChiTietHoaDon());
        buttonPanel.add(btnXemChiTiet);
        add(buttonPanel, BorderLayout.SOUTH);

        loadHoaDonData();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static void luuHoaDon(String maHoaDon, String ngayLap, double tongTien, String phuongThuc) {
        danhSachHoaDon.add(new String[]{maHoaDon, ngayLap, String.format("%.2f VND", tongTien), phuongThuc});
    }

    private void loadHoaDonData() {
        tableModel.setRowCount(0);
        for (String[] hoaDon : danhSachHoaDon) {
            tableModel.addRow(hoaDon);
        }
    }

    private void xemChiTietHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xem chi tiết!");
            return;
        }

        String maHoaDon = tableModel.getValueAt(selectedRow, 0).toString();
        String ngayLap = tableModel.getValueAt(selectedRow, 1).toString();
        String tongTien = tableModel.getValueAt(selectedRow, 2).toString();
        String phuongThuc = tableModel.getValueAt(selectedRow, 3).toString();

        JOptionPane.showMessageDialog(this,
                "Mã Hóa Đơn: " + maHoaDon + "\n" +
                        "Ngày Lập: " + ngayLap + "\n" +
                        "Tổng Tiền: " + tongTien + "\n" +
                        "Phương Thức: " + phuongThuc,
                "Chi Tiết Hóa Đơn",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
