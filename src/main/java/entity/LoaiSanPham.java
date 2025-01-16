package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiSanPham {
    @Id
    @Column(name = "maLoai", nullable = false, length = 10)
    private String maLoai;

    @Column(name = "tenLoai")
    private String tenLoai;
}

