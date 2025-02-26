package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "SanPham")
@Data
@ToString(exclude = { "chiTietHoaDons", "nhaCungCap", "loaiSanPham" })
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

    @Column(name = "soLuong")
    private Integer soLuong;
}

