package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyHoaDonFrame extends JFrame {
    private JTable tableHoaDon;
    private DefaultTableModel tableModel;
    private static List<String[]> danhSachHoaDon = new ArrayList<>();

    public QuanLyHoaDonFrame() {
        setTitle("Quản Lý Hóa Đơn");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel headerPanel = new GradientHeaderPanel("Quản Lý Hóa Đơn");
        headerPanel.setPreferredSize(new Dimension(900, 80));
        add(headerPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Mã Hóa Đơn", "Ngày lập", "Tổng tiền", "Phương thức"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHoaDon = new JTable(tableModel);
        tableHoaDon.setRowHeight(30);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHoaDon.getTableHeader().setBackground(new Color(41, 128, 185));
        tableHoaDon.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableHoaDon.getColumnCount(); i++) {
            tableHoaDon.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
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

    private class GradientHeaderPanel extends JPanel {
        private String title;

        public GradientHeaderPanel(String title) {
            this.title = title;
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(52, 73, 94), w, h, new Color(41, 128, 185));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }

        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 + fm.getAscent() / 2 - 5);
        }
    }
}
