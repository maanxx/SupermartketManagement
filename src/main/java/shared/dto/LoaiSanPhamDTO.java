package shared.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPhamDTO implements Serializable {
    private String maLoai;
    private String tenLoai;
}
