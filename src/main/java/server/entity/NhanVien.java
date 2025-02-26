package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVien {
    @Id
    @Column(name = "maNhanVien", nullable = false, length = 10)
    private String maNhanVien;

    @Column(name = "hoTen")
    private String hoTen;

    @Column(name = "ngaySinh")
    private String ngaySinh;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "chucDanh")
    private String chucDanh;

    @Column(name = "role", nullable = false, length = 10)
    private String role = "USER";

    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<HoaDonNhap> hoaDonNhaps;
}
