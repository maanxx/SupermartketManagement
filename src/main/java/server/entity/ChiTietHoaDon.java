package server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietHoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    @Column(name = "donGia")
    private double donGia;

    @Column(name = "soLuong")
    private int soLuong;

    @Column(name = "thanhTien")
    private double thanhTien;
}

