
import com.github.javafaker.Faker;
import dao.NhanVienDAO;
import entity.NhanVien;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        Faker faker = new Faker();

        // 1. Create
        System.out.println("Tạo dữ liệu nhân viên...");
        for (int i = 1; i <= 5; i++) {
            NhanVien nhanVien = NhanVien.builder()
                    .maNhanVien("NV" + i)
                    .hoTen(faker.name().fullName())
                    .ngaySinh(faker.date().birthday(20, 50).toString())
                    .soDienThoai(faker.phoneNumber().cellPhone())
                    .diaChi(faker.address().fullAddress())
                    .chucDanh(faker.job().position())
                    .build();
            nhanVienDAO.save(nhanVien);
        }

        // 2. Read
        System.out.println("\nDanh sách nhân viên trong cơ sở dữ liệu:");
        List<NhanVien> danhSachNhanVien = nhanVienDAO.findAll();
        danhSachNhanVien.forEach(nv -> System.out.println(nv.getMaNhanVien() + " - " + nv.getHoTen()));

        // 3. Update
        System.out.println("\nCập nhật tên cho nhân viên NV1...");
        NhanVien nv1 = nhanVienDAO.findById("NV1");
        if (nv1 != null) {
            nv1.setHoTen("Nguyen Van Updated");
            nv1.setChucDanh("Truong Phong");
            nhanVienDAO.update(nv1);
            System.out.println("Đã cập nhật: " + nv1.getMaNhanVien() + " - " + nv1.getHoTen() + " - " + nv1.getChucDanh());
        }

        // 4. Delete
        System.out.println("\nXóa nhân viên NV2...");
        NhanVien nv2 = nhanVienDAO.findById("NV2");
        if (nv2 != null) {
            nhanVienDAO.delete(nv2);
            System.out.println("Đã xóa nhân viên NV2.");
        }

        // Kiểm tra danh sách sau khi xóa
        System.out.println("\nDanh sách nhân viên còn lại:");
        danhSachNhanVien = nhanVienDAO.findAll();
        danhSachNhanVien.forEach(nv -> System.out.println(nv.getMaNhanVien() + " - " + nv.getHoTen()));
    }
}
