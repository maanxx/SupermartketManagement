package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoan {
    @Id
    @OneToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;
}


