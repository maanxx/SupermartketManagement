package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPham {
    @Id
    @Column(name = "maSanPham", nullable = false, length = 10)
    private String maSanPham;

    @Column(name = "tenSanPham")
    private String tenSanPham;

    @ManyToOne
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "maLoaiSan")
    private LoaiSanPham loaiSanPham;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL)
    private List<ChiTietHoaDon> chiTietHoaDons;
}

