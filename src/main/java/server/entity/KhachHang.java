    package server.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.util.List;

    @Entity
    @Table(name = "KhachHang")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class KhachHang {
        @Id
        @Column(name = "maKhachHang", nullable = false, length = 10)
        private String maKhachHang;

        @Column(name = "tenKhachHang")
        private String tenKhachHang;

        @Column(name = "soDienThoai")
        private String soDienThoai;

        @Column(name = "email")
        private String mail;

        @Column(name = "diemTichLuy")
        private float diemTichLuy;

        @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
        private List<HoaDon> hoaDons;
    }

