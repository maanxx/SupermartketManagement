package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DonVi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonVi {
    @Id
    @Column(name = "maDonVi", nullable = false, length = 10)
    private String maDonVi;

    @Column(name = "tenDonVi")
    private String tenDonVi;
}

