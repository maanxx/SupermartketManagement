package client.ui;

import shared.services.HoaDonService;
import shared.services.NhanVienService;
import shared.services.SanPhamService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Map;

public class ThongKeChartAdminPanel extends JPanel {
    private final HoaDonService hoaDonService;
    private final SanPhamService sanPhamService;
    private final NhanVienService nhanVienService;

    private int tongDoanhThu;
    private int tongDonHang;
    private int soNhanVienMoi;
    private int soSanPhamMoi;

    private String[] pieCategories;
    private double[] pieValues;
    private String[] barCategories = {"Doanh thu", "Đơn hàng", "NV mới", "SP mới"};
    private int[] barValues;

    public ThongKeChartAdminPanel(HoaDonService hoaDonService, SanPhamService sanPhamService, NhanVienService nhanVienService) {
        this.hoaDonService = hoaDonService;
        this.sanPhamService = sanPhamService;
        this.nhanVienService = nhanVienService;
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);
        fetchData();
    }

    private void fetchData() {
        try {
            tongDoanhThu = hoaDonService.getTongDoanhThu();
            tongDonHang = hoaDonService.getSoLuongDonHang();
            soNhanVienMoi = nhanVienService.getNhanVienMoiTheoThang("04");
            soSanPhamMoi = sanPhamService.getSanPhamMoiTheoThang("04");
            barValues = new int[]{tongDoanhThu, tongDonHang, soNhanVienMoi, soSanPhamMoi};

            Map<String, Integer> thongKeLoai = sanPhamService.getThongKeSoLuongTheoLoai();
            pieCategories = thongKeLoai.keySet().toArray(new String[0]);
            pieValues = new double[thongKeLoai.size()];
            int index = 0;
            for (String key : thongKeLoai.keySet()) {
                pieValues[index++] = thongKeLoai.get(key);
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
        g2.setColor(new Color(41, 128, 185));
        String overallTitle = "Dashboard Tổng Quan";
        FontMetrics fmOverall = g2.getFontMetrics();
        int titleWidth = fmOverall.stringWidth(overallTitle);
        g2.drawString(overallTitle, (width - titleWidth) / 2, 40);

        drawSummaryMetrics(g2, width, height / 4);
        int barChartX = 30;
        int barChartY = height / 4 + 50;
        int barChartWidth = width / 2 - 50;
        int barChartHeight = height / 2 - 80;
        drawBarChart(g2, barChartX, barChartY, barChartWidth, barChartHeight);

        int pieChartDiameter = Math.min(width / 2 - 50, height / 2 - 80);
        int pieChartX = width / 2 + 20;
        int pieChartY = height / 4 + 50;
        drawPieChart(g2, pieChartX, pieChartY, pieChartDiameter, pieChartDiameter);
    }

    private void drawSummaryMetrics(Graphics2D g2, int panelWidth, int panelHeight) {
        String[] labels = {"Tổng doanh thu", "Tổng đơn hàng", "NV mới", "SP mới"};
        String[] data = {
                String.format("%,d VND", tongDoanhThu),
                String.valueOf(tongDonHang),
                String.valueOf(soNhanVienMoi),
                String.valueOf(soSanPhamMoi)
        };
        int numBoxes = labels.length;
        int boxWidth = (panelWidth - 40) / numBoxes;
        int boxHeight = panelHeight - 20;
        int x = 20, y = 60;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        for (int i = 0; i < numBoxes; i++) {
            g2.setColor(new Color(240, 240, 240));
            g2.fillRoundRect(x, y, boxWidth - 10, boxHeight, 20, 20);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, boxWidth - 10, boxHeight, 20, 20);
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(labels[i]);
            g2.drawString(labels[i], x + (boxWidth - 10 - labelWidth) / 2, y + 30);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            fm = g2.getFontMetrics();
            int dataWidth = fm.stringWidth(data[i]);
            g2.drawString(data[i], x + (boxWidth - 10 - dataWidth) / 2, y + boxHeight / 2);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            x += boxWidth;
        }
    }

    private void drawBarChart(Graphics2D g2, int startX, int startY, int areaWidth, int areaHeight) {
        int marginLeft = 40;
        int marginRight = 20;
        int marginTop = 20;
        int marginBottom = 40;
        int chartWidth = areaWidth - marginLeft - marginRight;
        int chartHeight = areaHeight - marginTop - marginBottom;

        g2.setColor(Color.WHITE);
        g2.fillRect(startX + marginLeft, startY + marginTop, chartWidth, chartHeight);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(startX + marginLeft, startY + marginTop, chartWidth, chartHeight);

        int maxValue = 0;
        for (int v : barValues) {
            if (v > maxValue) maxValue = v;
        }
        maxValue = (int)(maxValue * 1.2);

        int n = barCategories.length;
        int gap = 10;
        int barWidth = (chartWidth - gap * (n + 1)) / n;
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        g2.setFont(labelFont);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < n; i++) {
            int barHeight = (int)(((double) barValues[i] / maxValue) * chartHeight);
            int x = startX + marginLeft + gap + i * (barWidth + gap);
            int y = startY + marginTop + chartHeight - barHeight;

            GradientPaint gp = new GradientPaint(x, y, new Color(66, 133, 244), x, y + barHeight, new Color(15, 157, 88));
            g2.setPaint(gp);
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, barWidth, barHeight);

            String category = barCategories[i];
            int labelWidth = fm.stringWidth(category);
            int labelX = x + (barWidth - labelWidth) / 2;
            int labelY = startY + marginTop + chartHeight + fm.getAscent() + 2;
            g2.setColor(Color.BLACK);
            g2.drawString(category, labelX, labelY);

            String valueStr = String.valueOf(barValues[i]);
            int valueWidth = fm.stringWidth(valueStr);
            int valueX = x + (barWidth - valueWidth) / 2;
            int valueY = y - 5;
            g2.drawString(valueStr, valueX, valueY);
        }

        g2.setColor(Color.BLACK);
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            int valueLabel = (int)(maxValue * i / (double) numYLabels);
            int labelY = startY + marginTop + chartHeight - (int)(chartHeight * i / (double) numYLabels);
            g2.drawLine(startX + marginLeft - 5, labelY, startX + marginLeft, labelY);
            String yLabel = String.valueOf(valueLabel);
            int labelWidth = fm.stringWidth(yLabel);
            g2.drawString(yLabel, startX + marginLeft - labelWidth - 10, labelY + fm.getAscent()/2 - 2);
        }

        g2.drawLine(startX + marginLeft, startY + marginTop, startX + marginLeft, startY + marginTop + chartHeight);
        g2.drawLine(startX + marginLeft, startY + marginTop + chartHeight, startX + marginLeft + chartWidth, startY + marginTop + chartHeight);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        String title = "Doanh số bán hàng theo tháng";
        FontMetrics fmTitle = g2.getFontMetrics();
        int titleWidth = fmTitle.stringWidth(title);
        g2.drawString(title, startX + (areaWidth - titleWidth) / 2, startY + 20);
    }

    private void drawPieChart(Graphics2D g2, int startX, int startY, int areaWidth, int areaHeight) {
        int diameter = Math.min(areaWidth, areaHeight) - 100;
        int x = startX + (areaWidth - diameter) / 2;
        int y = startY + (areaHeight - diameter) / 2;

        double total = 0;
        for (double v : pieValues) {
            total += v;
        }

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
