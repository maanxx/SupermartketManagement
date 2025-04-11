package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "SanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPham {
    @Id
    @Column(name = "maSanPham", nullable = false, length = 10)
    private String maSanPham;

    @Column(name = "tenSanPham")
    private String tenSanPham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loaiSanPham")
    private LoaiSanPham loaiSanPham;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    private List<ChiTietHoaDon> chiTietHoaDons;

    @Column(name = "gia")
    private Double gia;

    @Column(name = "hinhAnh")
    private String hinhAnh;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Setter
    @Getter
    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    public SanPham(String maSanPham, String tenSanPham, NhaCungCap nhaCungCap, LoaiSanPham loaiSanPham, Double gia, Integer soLuong, String hinhAnh) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.nhaCungCap = nhaCungCap;
        this.loaiSanPham = loaiSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
    }

}

