package server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietDonNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietDonNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maDonNhap")
    private HoaDonNhap hoaDonNhap;

    @ManyToOne
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    @Column(name = "giaVon")
    private double giaVon;

    @Column(name = "soLuongNhap")
    private int soLuongNhap;

    @Column(name = "thanhTien")
    private double thanhTien;
}

