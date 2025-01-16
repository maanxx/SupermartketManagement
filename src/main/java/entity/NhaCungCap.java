package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "NhaCungCap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhaCungCap {
    @Id
    @Column(name = "maNhaCungCap", nullable = false, length = 10)
    private String maNhaCungCap;

    @Column(name = "tenNhaCungCap")
    private String tenNhaCungCap;

    @Column(name = "diaChi")
    private String diaChi;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL)
    private List<SanPham> sanPhams;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL)
    private List<HoaDonNhap> hoaDonNhaps;
}

