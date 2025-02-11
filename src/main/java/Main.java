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


import server.dao.NhanVienDAO;
import server.entity.NhanVien;
import server.entity.TaiKhoan;

public class Main {
    public static void main(String[] args) {
        NhanVienDAO nhanVienDAO = new NhanVienDAO();

        System.out.println("🗑️ Xóa toàn bộ dữ liệu cũ...");
        nhanVienDAO.findAll().forEach(nhanVienDAO::delete);

        //  Tạo tài khoản quản lý (ADMIN) với mật khẩu "1"
        System.out.println("✅ Tạo tài khoản quản lý...");
        NhanVien admin = NhanVien.builder()
                .maNhanVien("admin")
                .hoTen("J96")
                .ngaySinh("1980-05-15")
                .soDienThoai("0901234567")
                .diaChi("TP Hồ Chí Minh")
                .chucDanh("Quản lý cửa hàng")
                .role("ADMIN")
                .build();
        TaiKhoan adminTaiKhoan = TaiKhoan.builder()
                .nhanVien(admin)
                .matKhau("1")
                .build();
        admin.setTaiKhoan(adminTaiKhoan);
        nhanVienDAO.save(admin);

        //  Tạo tài khoản nhân viên (USER) với mật khẩu "1"
        System.out.println("Tạo tài khoản nhân viên...");
        NhanVien user = NhanVien.builder()
                .maNhanVien("user")
                .hoTen("TA")
                .ngaySinh("1995-08-20")
                .soDienThoai("0912345678")
                .diaChi("Hà Nội")
                .chucDanh("Nhân viên bán hàng")
                .role("USER")
                .build();
        TaiKhoan userTaiKhoan = TaiKhoan.builder()
                .nhanVien(user)
                .matKhau("1") //  Mật khẩu mặc định là "1"
                .build();
        user.setTaiKhoan(userTaiKhoan);
        nhanVienDAO.save(user);

        //  Kiểm tra danh sách nhân viên sau khi thêm
        System.out.println("\n Danh sách nhân viên trong hệ thống:");
        nhanVienDAO.findAll().forEach(nv ->
                System.out.println(nv.getMaNhanVien() + " | " + nv.getHoTen() + " | " + nv.getRole()));

        System.out.println("\n Dữ liệu đã được khởi tạo thành công!");
    }
}


