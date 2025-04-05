package server;

import server.dao.*;
import server.entity.*;

import java.util.List;

public class SeedDataMain {
    public static void main(String[] args) {
        System.out.println("💾 BẮT ĐẦU KHỞI TẠO DỮ LIỆU...");

        // DAO khởi tạo
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
        LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();
        SanPhamDAO sanPhamDAO = new SanPhamDAO();
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        HoaDonNhapDAO hoaDonNhapDAO = new HoaDonNhapDAO();
        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        ChiTietDonNhapDAO chiTietDonNhapDAO = new ChiTietDonNhapDAO();
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

        // ===========================
        // 1. XÓA DỮ LIỆU CŨ
        // ===========================
        System.out.println("🧹 XÓA DỮ LIỆU CŨ...");
        try {
            deleteAllSafe(chiTietHoaDonDAO.findAll(), chiTietHoaDonDAO);
            deleteAllSafe(chiTietDonNhapDAO.findAll(), chiTietDonNhapDAO);
            deleteAllSafe(hoaDonDAO.findAll(), hoaDonDAO);
            deleteAllSafe(hoaDonNhapDAO.findAll(), hoaDonNhapDAO);
            deleteAllSafe(sanPhamDAO.findAll(), sanPhamDAO);
            deleteAllSafe(loaiSanPhamDAO.findAll(), loaiSanPhamDAO);
            deleteAllSafe(nhaCungCapDAO.findAll(), nhaCungCapDAO);
            deleteAllSafe(khachHangDAO.findAll(), khachHangDAO);
            deleteAllSafe(nhanVienDAO.findAll(), nhanVienDAO);
        } catch (Exception ex) {
            System.err.println("❌ Lỗi khi xóa dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        // ===========================
        // 2. TẠO DỮ LIỆU MẪU
        // ===========================

        // Nhân viên + tài khoản
        System.out.println("👤 Thêm nhân viên...");
        NhanVien admin = NhanVien.builder()
                .maNhanVien("admin")
                .hoTen("Quản Lý")
                .ngaySinh("1980-05-15")
                .soDienThoai("0901234567")
                .diaChi("TP Hồ Chí Minh")
                .chucDanh("Quản lý cửa hàng")
                .role("ADMIN")
                .build();
        admin.setTaiKhoan(TaiKhoan.builder().nhanVien(admin).matKhau("1").build());

        NhanVien user = NhanVien.builder()
                .maNhanVien("user")
                .hoTen("Nhân Viên")
                .ngaySinh("1995-08-20")
                .soDienThoai("0912345678")
                .diaChi("Hà Nội")
                .chucDanh("Nhân viên bán hàng")
                .role("USER")
                .build();
        user.setTaiKhoan(TaiKhoan.builder().nhanVien(user).matKhau("1").build());

        nhanVienDAO.save(admin);
        nhanVienDAO.save(user);

        // Loại sản phẩm
        System.out.println("📦 Thêm loại sản phẩm...");
        LoaiSanPham thucPham = LoaiSanPham.builder().maLoai("LSP01").tenLoai("Thực phẩm").build();
        LoaiSanPham dienTu = LoaiSanPham.builder().maLoai("LSP02").tenLoai("Điện tử").build();
        loaiSanPhamDAO.save(thucPham);
        loaiSanPhamDAO.save(dienTu);

        // Nhà cung cấp
        System.out.println("🏢 Thêm nhà cung cấp...");
        NhaCungCap ncc = NhaCungCap.builder()
                .maNhaCungCap("NCC01")
                .tenNhaCungCap("Công ty CP Thực phẩm ABC")
                .diaChi("Hồ Chí Minh")
                .build();
        nhaCungCapDAO.save(ncc);

        // Sản phẩm
        System.out.println("🛒 Thêm sản phẩm...");
        SanPham sp1 = SanPham.builder()
                .maSanPham("SP01")
                .tenSanPham("Gạo ST25")
                .gia(25000.0)
                .soLuong(100)
                .nhaCungCap(ncc)
                .loaiSanPham(thucPham)
                .build();
        SanPham sp2 = SanPham.builder()
                .maSanPham("SP02")
                .tenSanPham("Laptop Dell")
                .gia(20000000.0)
                .soLuong(10)
                .nhaCungCap(ncc)
                .loaiSanPham(dienTu)
                .build();
        sanPhamDAO.save(sp1);
        sanPhamDAO.save(sp2);

        // In sản phẩm
        System.out.println("\n📋 Danh sách sản phẩm:");
        sanPhamDAO.findAll().forEach(sp -> {
            System.out.printf(" - %s | %s | %s | %,d VND | SL: %d\n",
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getLoaiSanPham().getTenLoai(),
                    sp.getGia().longValue(),
                    sp.getSoLuong());
        });

        System.out.println("\n✅ DỮ LIỆU MẪU ĐÃ ĐƯỢC KHỞI TẠO THÀNH CÔNG!");
    }

    private static <T> void deleteAllSafe(List<T> list, BaseDAO<T> dao) {
        for (T item : list) {
            try {
                dao.delete(item);
            } catch (Exception e) {
                System.err.println("⚠️ Không thể xóa: " + item + " - " + e.getMessage());
            }
        }
    }
}
