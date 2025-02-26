//
//import com.github.javafaker.Faker;
//import server.dao.NhanVienDAO;
//import server.entity.NhanVien;
//
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        NhanVienDAO nhanVienDAO = new NhanVienDAO();
//        Faker faker = new Faker();
//
//        // 1. Create
//        for (int i = 1; i <= 5; i++) {
//            NhanVien nhanVien = NhanVien.builder()
//                    .maNhanVien("NV" + i)
//                    .hoTen(faker.name().fullName())
//                    .ngaySinh(faker.date().birthday(20, 50).toString())
//                    .soDienThoai(faker.phoneNumber().cellPhone())
//                    .diaChi(faker.address().fullAddress())
//                    .chucDanh(faker.job().position())
//                    .role("USER")
//                    .build();
//            nhanVienDAO.save(nhanVien);
//        }
//
//        // 2. Read
//        System.out.println("\nDanh sách nhân viên trong cơ sở dữ liệu:");
//        List<NhanVien> danhSachNhanVien = nhanVienDAO.findAll();
//        danhSachNhanVien.forEach(nv -> System.out.println(nv.getMaNhanVien() + " - " + nv.getHoTen()));
//
//        // 3. Update
//        System.out.println("\nCập nhật tên cho nhân viên NV1...");
//        NhanVien nv1 = nhanVienDAO.findById("NV1");
//        if (nv1 != null) {
//            nv1.setHoTen("Nguyen Van Updated");
//            nv1.setChucDanh("Truong Phong");
//            nhanVienDAO.update(nv1);
//            System.out.println("Đã cập nhật: " + nv1.getMaNhanVien() + " - " + nv1.getHoTen() + " - " + nv1.getChucDanh());
//        }
//
//        // 4. Delete
//        System.out.println("\nXóa nhân viên NV2...");
//        NhanVien nv2 = nhanVienDAO.findById("NV2");
//        if (nv2 != null) {
//            nhanVienDAO.delete(nv2);
//            System.out.println("Đã xóa nhân viên NV2.");
//        }
//
//        // Kiểm tra danh sách sau khi xóa
//        System.out.println("\nDanh sách nhân viên còn lại:");
//        danhSachNhanVien = nhanVienDAO.findAll();
//        danhSachNhanVien.forEach(nv -> System.out.println(nv.getMaNhanVien() + " - " + nv.getHoTen()));
//    }
//}


import server.dao.*;
import server.entity.*;

public class Main {
    public static void main(String[] args) {
        // DAO Instances
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

        // 🗑 Xóa toàn bộ dữ liệu cũ
        System.out.println("🗑 Xóa toàn bộ dữ liệu...");
        chiTietHoaDonDAO.findAll().forEach(chiTietHoaDonDAO::delete);
        chiTietDonNhapDAO.findAll().forEach(chiTietDonNhapDAO::delete);
        hoaDonDAO.findAll().forEach(hoaDonDAO::delete);
        hoaDonNhapDAO.findAll().forEach(hoaDonNhapDAO::delete);
        sanPhamDAO.findAll().forEach(sanPhamDAO::delete);
        loaiSanPhamDAO.findAll().forEach(loaiSanPhamDAO::delete);
        nhaCungCapDAO.findAll().forEach(nhaCungCapDAO::delete);
        khachHangDAO.findAll().forEach(khachHangDAO::delete);
        nhanVienDAO.findAll().forEach(nhanVienDAO::delete);

        //  Tạo nhân viên
        System.out.println(" Thêm nhân viên...");
        NhanVien admin = NhanVien.builder()
                .maNhanVien("admin")
                .hoTen("Quản Lý")
                .ngaySinh("1980-05-15")
                .soDienThoai("0901234567")
                .diaChi("TP Hồ Chí Minh")
                .chucDanh("Quản lý cửa hàng")
                .role("ADMIN")
                .build();

        NhanVien user = NhanVien.builder()
                .maNhanVien("user")
                .hoTen("Nhân Viên")
                .ngaySinh("1995-08-20")
                .soDienThoai("0912345678")
                .diaChi("Hà Nội")
                .chucDanh("Nhân viên bán hàng")
                .role("USER")
                .build();


        System.out.println(" Thêm tài khoản...");
        TaiKhoan adminTaiKhoan = TaiKhoan.builder()
                .nhanVien(admin)
                .matKhau("1")
                .build();
        TaiKhoan userTaiKhoan = TaiKhoan.builder()
                .nhanVien(user)
                .matKhau("1")
                .build();


        admin.setTaiKhoan(adminTaiKhoan);
        user.setTaiKhoan(userTaiKhoan);

        nhanVienDAO.save(admin);
        nhanVienDAO.save(user);

        System.out.println(" Thêm loại sản phẩm...");
        LoaiSanPham loaiThucPham = LoaiSanPham.builder()
                .maLoai("LSP01")
                .tenLoai("Thực phẩm")
                .build();

        LoaiSanPham loaiDienTu = LoaiSanPham.builder()
                .maLoai("LSP02")
                .tenLoai("Điện tử")
                .build();

        loaiSanPhamDAO.save(loaiThucPham);
        loaiSanPhamDAO.save(loaiDienTu);

        //  Tạo nhà cung cấp
        System.out.println(" Thêm nhà cung cấp...");
        NhaCungCap nhaCungCap = NhaCungCap.builder()
                .maNhaCungCap("NCC01")
                .tenNhaCungCap("Công ty CP Thực phẩm ABC")
                .diaChi("Hồ Chí Minh")
                .build();

        nhaCungCapDAO.save(nhaCungCap);

        //  Tạo sản phẩm
        System.out.println(" Thêm sản phẩm...");
        SanPham sp1 = SanPham.builder()
                .maSanPham("SP01")
                .tenSanPham("Gạo ST25")
                .gia(25000.0)
                .soLuong(100)
                .nhaCungCap(nhaCungCap)
                .loaiSanPham(loaiThucPham)
                .build();

        SanPham sp2 = SanPham.builder()
                .maSanPham("SP02")
                .tenSanPham("Laptop Dell")
                .gia(20000000.0)
                .soLuong(10)
                .nhaCungCap(nhaCungCap)
                .loaiSanPham(loaiDienTu)
                .build();

        sanPhamDAO.save(sp1);
        sanPhamDAO.save(sp2);

        //  Kiểm tra dữ liệu sản phẩm
        System.out.println("\n Danh sách sản phẩm trong hệ thống:");
        sanPhamDAO.findAll().forEach(sp -> System.out.println(
                "Mã SP: " + sp.getMaSanPham() +
                        " | Tên: " + sp.getTenSanPham() +
                        " | Loại: " + sp.getLoaiSanPham().getTenLoai() +
                        " | Giá: " + sp.getGia() +
                        " | Số lượng: " + sp.getSoLuong()
        ));

        System.out.println("\nDữ liệu đã được khởi tạo thành công!");
    }
}





