package server;

import server.dao.*;
import server.entity.*;

import java.time.LocalDate;
import java.util.List;

public class SeedDataMain {
    public static void main(String[] args) {
        System.out.println(" BẮT ĐẦU KHỞI TẠO DỮ LIỆU...");

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
        System.out.println(" XÓA DỮ LIỆU CŨ...");
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
            System.err.println(" Lỗi khi xóa dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        System.out.println(" XÓA SẢN PHẨM LỖI (null mã, tên, hình ảnh)...");
        sanPhamDAO.findAll().forEach(sp -> {
            if (sp.getMaSanPham() == null || sp.getTenSanPham() == null || sp.getHinhAnh() == null) {
                try {
                    sanPhamDAO.delete(sp);
                    System.out.println(" Đã xóa sản phẩm lỗi: " + sp);
                } catch (Exception e) {
                    System.err.println(" Không thể xóa sản phẩm lỗi: " + e.getMessage());
                }
            }
        });

        System.out.println(" XÓA CHI TIẾT NHẬP LIÊN KẾT SẢN PHẨM LỖI...");
        chiTietDonNhapDAO.deleteAllWithInvalidSanPham();

        System.out.println(" XÓA SẢN PHẨM LỖI...");
        sanPhamDAO.deleteSanPhamBiLoi();



        // ===========================
        // 2. TẠO DỮ LIỆU MẪU
        // ===========================

        System.out.println("👤 Thêm nhân viên...");
        if (nhanVienDAO.findById("admin") == null) {
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
            nhanVienDAO.save(admin);
        }

        if (nhanVienDAO.findById("user") == null) {
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
            nhanVienDAO.save(user);
        }

        System.out.println(" Thêm nhà cung cấp thực tế...");
        if (nhaCungCapDAO.findById("NCC01") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC01")
                    .tenNhaCungCap("Công ty TNHH Gạo ST25 Việt Nam")
                    .diaChi("Sóc Trăng")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC02") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC02")
                    .tenNhaCungCap("Coca-Cola Việt Nam")
                    .diaChi("TP. Hồ Chí Minh")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC03") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC03")
                    .tenNhaCungCap("Vinamilk")
                    .diaChi("TP. Hồ Chí Minh")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC04") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC04")
                    .tenNhaCungCap("Unilever Việt Nam")
                    .diaChi("TP. Hồ Chí Minh")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC05") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC05")
                    .tenNhaCungCap("Công ty Mondelez Kinh Đô")
                    .diaChi("Bình Dương")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC06") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC06")
                    .tenNhaCungCap("Công ty TNHH Dell Việt Nam")
                    .diaChi("Hà Nội")
                    .build());
        }
        if (nhaCungCapDAO.findById("NCC07") == null) {
            nhaCungCapDAO.save(NhaCungCap.builder()
                    .maNhaCungCap("NCC07")
                    .tenNhaCungCap("Điện máy Xanh")
                    .diaChi("TP. Hồ Chí Minh")
                    .build());
        }

        System.out.println(" Thêm loại sản phẩm...");
        if (loaiSanPhamDAO.findById("LSP01") == null) {
            loaiSanPhamDAO.save(LoaiSanPham.builder()
                    .maLoai("LSP01")
                    .tenLoai("Thực phẩm")
                    .build());
        }
        if (loaiSanPhamDAO.findById("LSP02") == null) {
            loaiSanPhamDAO.save(LoaiSanPham.builder()
                    .maLoai("LSP02")
                    .tenLoai("Điện tử")
                    .build());
        }
        if (loaiSanPhamDAO.findById("LSP03") == null) {
            loaiSanPhamDAO.save(LoaiSanPham.builder()
                    .maLoai("LSP03")
                    .tenLoai("Gia dụng")
                    .build());
        }

        System.out.println("🛒 Thêm sản phẩm...");

        if (sanPhamDAO.findById("SP01") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP01")
                    .tenSanPham("Gạo ST25")
                    .gia(25000.0)
                    .soLuong(100)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC01"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP01"))
                    .hinhAnh("gao-st25.jpg")
                    .ngayTao(LocalDate.of(2025, 4, 1))
                    .build());
        }

        if (sanPhamDAO.findById("SP02") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP02")
                    .tenSanPham("Coca-Cola")
                    .gia(10000.0)
                    .soLuong(300)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC01"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP01"))
                    .hinhAnh("coca.jpg")
                    .ngayTao(LocalDate.of(2025, 4, 2))
                    .build());
        }

        if (sanPhamDAO.findById("SP03") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP03")
                    .tenSanPham("Sữa Vinamilk")
                    .gia(8000.0)
                    .soLuong(400)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC01"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP01"))
                    .hinhAnh("vinamilk.jpg")
                    .ngayTao(LocalDate.of(2025, 3, 28))
                    .build());
        }

        if (sanPhamDAO.findById("SP04") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP04")
                    .tenSanPham("Bột giặt Omo 3kg")
                    .gia(120000.0)
                    .soLuong(60)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC01"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP03"))
                    .hinhAnh("omo.jpg")
                    .ngayTao(LocalDate.of(2025, 3, 20))
                    .build());
        }

        if (sanPhamDAO.findById("SP05") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP05")
                    .tenSanPham("Bánh Oreo")
                    .gia(15000.0)
                    .soLuong(200)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC01"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP01"))
                    .hinhAnh("oreo.jpg")
                    .ngayTao(LocalDate.of(2025, 4, 5))
                    .build());
        }

        if (sanPhamDAO.findById("SP06") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP06")
                    .tenSanPham("Laptop Dell")
                    .gia(20000000.0)
                    .soLuong(15)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC02"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP02"))
                    .hinhAnh("laptop-dell.jpg")
                    .ngayTao(LocalDate.of(2025, 2, 12))
                    .build());
        }

        if (sanPhamDAO.findById("SP07") == null) {
            sanPhamDAO.save(SanPham.builder()
                    .maSanPham("SP07")
                    .tenSanPham("Nồi cơm điện")
                    .gia(850000.0)
                    .soLuong(25)
                    .nhaCungCap(nhaCungCapDAO.findById("NCC02"))
                    .loaiSanPham(loaiSanPhamDAO.findById("LSP03"))
                    .hinhAnh("noi-com.jpg")
                    .ngayTao(LocalDate.of(2025, 4, 10))
                    .build());
        }

        System.out.println("\n Danh sách sản phẩm:");
        sanPhamDAO.findAll().forEach(sp -> {
            System.out.printf(" - %s | %s | %s | %,d VND | SL: %d | Ảnh: %s\n",
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getLoaiSanPham().getTenLoai(),
                    sp.getGia().longValue(),
                    sp.getSoLuong(),
                    sp.getHinhAnh());
        });

        System.out.println("\n DỮ LIỆU MẪU ĐÃ ĐƯỢC KHỞI TẠO THÀNH CÔNG!");
    }

    private static <T> void deleteAllSafe(List<T> list, BaseDAO<T> dao) {
        for (T item : list) {
            try {
                dao.delete(item);
            } catch (Exception e) {
                System.err.println("️ Không thể xóa " + item.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }
    }
}