package client.ui;

import shared.services.HoaDonService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Map;

public class ThongKeChartUserPanel extends JPanel {

    private final HoaDonService hoaDonService;
    private final SanPhamService sanPhamService;

    private int soHoaDon;
    private int tongDoanhThu;
    private String sanPhamBanChay = "Không rõ";
    private String[] pieCategories;
    private double[] pieValues;

    public ThongKeChartUserPanel(HoaDonService hoaDonService, SanPhamService sanPhamService) {
        this.hoaDonService = hoaDonService;
        this.sanPhamService = sanPhamService;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 500));
        fetchData();
    }

    private void fetchData() {
        try {
            soHoaDon = hoaDonService.getSoLuongDonHang();
            tongDoanhThu = hoaDonService.getTongDoanhThu();

            Map<String, Integer> thongKeLoai = sanPhamService.getThongKeSoLuongTheoLoai();
            pieCategories = thongKeLoai.keySet().toArray(new String[0]);
            pieValues = new double[thongKeLoai.size()];
            int index = 0;
            int maxCount = -1;

            for (String key : thongKeLoai.keySet()) {
                int count = thongKeLoai.get(key);
                pieValues[index++] = count;
                if (count > maxCount) {
                    maxCount = count;
                    sanPhamBanChay = key;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        String overallTitle = "Tổng Quan Doanh Số";
        FontMetrics fmOverall = g2.getFontMetrics();
        int titleWidth = fmOverall.stringWidth(overallTitle);
        g2.drawString(overallTitle, (width - titleWidth) / 2, 30);

        int summaryHeight = height / 4;
        drawSummaryMetrics(g2, width, summaryHeight);
        int lowerY = summaryHeight;
        int lowerHeight = height - summaryHeight;
        drawPieChart(g2, 0, lowerY, width, lowerHeight);
    }

    private void drawSummaryMetrics(Graphics2D g2, int width, int height) {
        String[] labels = {"Số hóa đơn", "Tổng doanh thu", "SP bán chạy"};
        String[] values = {
                String.valueOf(soHoaDon),
                String.format("%,d VND", tongDoanhThu),
                sanPhamBanChay
        };
        int boxWidth = width / 3 - 20;
        int boxHeight = height - 20;
        int x = 10, y = 40;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        for (int i = 0; i < labels.length; i++) {
            g2.setColor(new Color(240, 240, 240));
            g2.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, boxWidth, boxHeight, 20, 20);
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int titleWidth = fm.stringWidth(labels[i]);
            g2.drawString(labels[i], x + (boxWidth - titleWidth) / 2, y + 30);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            fm = g2.getFontMetrics();
            int valueWidth = fm.stringWidth(values[i]);
            g2.drawString(values[i], x + (boxWidth - valueWidth) / 2, y + boxHeight / 2);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            x += boxWidth + 20;
        }
    }

    private void drawPieChart(Graphics2D g2, int startX, int startY, int areaWidth, int areaHeight) {
        if (pieValues == null || pieValues.length == 0) return;

        int diameter = Math.min(areaWidth, areaHeight) - 120;
        int x = startX + (areaWidth - diameter) / 2;
        int y = startY + 60;

        double total = 0;
        for (double v : pieValues) total += v;

        Color[] colors = {
                new Color(66, 133, 244),
                new Color(219, 68, 55),
                new Color(244, 180, 0),
                new Color(15, 157, 88)
        };

        double startAngle = 0;
        DecimalFormat df = new DecimalFormat("0.0");
        for (int i = 0; i < pieValues.length; i++) {
            double angle = 360 * (pieValues[i] / total);
            g2.setColor(colors[i % colors.length]);
            g2.fill(new Arc2D.Double(x, y, diameter, diameter, startAngle, angle, Arc2D.PIE));
            g2.setColor(Color.WHITE);
            g2.draw(new Arc2D.Double(x, y, diameter, diameter, startAngle, angle, Arc2D.PIE));
            startAngle += angle;
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        String title = "Phân bố sản phẩm theo loại";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, startX + (areaWidth - titleWidth) / 2, startY + 30);

        int legendX = x + diameter + 20;
        int legendY = y;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (int i = 0; i < pieCategories.length; i++) {
            double percent = (pieValues[i] / total) * 100;
            String legendText = pieCategories[i] + " (" + df.format(percent) + "%)";
            g2.setColor(colors[i % colors.length]);
            g2.fillRect(legendX, legendY, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawRect(legendX, legendY, 20, 20);
            g2.drawString(legendText, legendX + 30, legendY + 15);
            legendY += 30;
        }
    }
}
