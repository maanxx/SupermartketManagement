package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "HoaDonNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDonNhap {
    @Id
    @Column(name = "maDonNhap", nullable = false)
    private String maDonNhap;

    @ManyToOne
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Column(name = "ngayLap")
    private String ngayLap;

    @Column(name = "tongTien")
    private double tongTien;

    @OneToMany(mappedBy = "hoaDonNhap", cascade = CascadeType.ALL)
    private List<ChiTietDonNhap> chiTietDonNhaps;
}

