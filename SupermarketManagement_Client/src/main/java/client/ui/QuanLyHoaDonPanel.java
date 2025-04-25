package client.ui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static client.ui.AdminDashboard.BUTTON_BG_COLOR;

public class QuanLyHoaDonPanel extends JPanel {
    private JTable tableHoaDon;
    private DefaultTableModel tableModel;
    private static final List<String[]> danhSachHoaDon = new ArrayList<>();
    private JDateChooser dateChooser;

    private void applyCustomTableHeader(JTable table) {
        table.getTableHeader().setDefaultRenderer(new CustomTableHeaderRenderer(table));
    }

    public QuanLyHoaDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE); // Nền trắng cho panel chính

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE); // Nền trắng cho contentPanel
        add(contentPanel, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE); // Nền trắng cho tablePanel
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        tablePanel.add(createDateFilterPanel(), BorderLayout.NORTH); // Panel chọn ngày
        tablePanel.add(createTablePanel(), BorderLayout.CENTER); // Panel bảng hóa đơn

        add(createButtonPanel(), BorderLayout.SOUTH);

        loadHoaDonData();
        setPreferredSize(new Dimension(1100, 700));
        applyCustomTableHeader(tableHoaDon);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new GradientHeaderPanel("Quản Lý Hóa Đơn");
        headerPanel.setPreferredSize(new Dimension(0, 80));
        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Mã Hóa Đơn", "Ngày lập", "Tổng tiền", "Phương thức", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tableHoaDon = new JTable(tableModel) {
            private int hoveredRow = -1;

            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }
                    }
                });
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    if (row == hoveredRow) {
                        c.setBackground(new Color(230, 240, 255));
                    }
                } else {
                    c.setBackground(new Color(179, 216, 255));
                }
                if (c instanceof JLabel l) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                }
                return c;
            }
        };

        tableHoaDon.setRowHeight(40);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHoaDon.getTableHeader().setBackground(new Color(34, 193, 195));
        tableHoaDon.getTableHeader().setForeground(Color.WHITE);

        tableHoaDon.getTableHeader().setReorderingAllowed(false);

        tableHoaDon.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("\uD83D\uDC41 Xem");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setBackground(new Color(34, 193, 195));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        });

        tableHoaDon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableHoaDon.rowAtPoint(e.getPoint());
                int col = tableHoaDon.columnAtPoint(e.getPoint());
                if (col == 4) {
                    tableHoaDon.setRowSelectionInterval(row, row);
                    xemChiTietHoaDon();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE); // Nền trắng cho buttonPanel
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return buttonPanel;
    }

    private JPanel createDateFilterPanel() {
        JPanel dateFilterPanel = new JPanel(new GridBagLayout());
        dateFilterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dateFilterPanel.setBackground(Color.WHITE); // Nền trắng cho dateFilterPanel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel("Chọn ngày:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        dateFilterPanel.add(label, gbc);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        dateFilterPanel.add(dateChooser, gbc);

        JButton btnLocNgay = new JButton("Lọc theo ngày");
        btnLocNgay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLocNgay.setBackground(new Color(139, 195, 74));
        btnLocNgay.setForeground(Color.BLACK);
        btnLocNgay.setFocusPainted(false);
        btnLocNgay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLocNgay.addActionListener(e -> locHoaDonTheoNgay());
        gbc.gridx = 2;
        dateFilterPanel.add(btnLocNgay, gbc);

        return dateFilterPanel;
    }

    private void locHoaDonTheoNgay() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDateString = sdf.format(selectedDate);

        List<String[]> filteredHoaDon = new ArrayList<>();
        for (String[] hoaDon : danhSachHoaDon) {
            if (hoaDon[1].equals(selectedDateString)) {
                filteredHoaDon.add(hoaDon);
            }
        }

        tableModel.setRowCount(0);
        for (String[] hoaDon : filteredHoaDon) {
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

    private void loadHoaDonData() {
        tableModel.setRowCount(0);
        for (String[] hoaDon : danhSachHoaDon) {
            tableModel.addRow(hoaDon);
        }
    }

    public static void luuHoaDon(String maHoaDon, String ngayLap, double tongTien, String phuongThuc) {
        danhSachHoaDon.add(new String[]{maHoaDon, ngayLap, String.format("%.2f VND", tongTien), phuongThuc, "\uD83D\uDC41"});
    }

    private static class GradientHeaderPanel extends JPanel {
        private final String title;

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

            // Vẽ nền màu xanh lá nhạt
            GradientPaint gp = new GradientPaint(0, 0, BUTTON_BG_COLOR, w, h, BUTTON_BG_COLOR);
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
