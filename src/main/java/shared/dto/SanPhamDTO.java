package shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamDTO implements Serializable {
    private String maSanPham;
    private String tenSanPham;
    private String maNhaCungCap;
    private String maLoaiSanPham;
    private Double gia;
    private Integer soLuong;
}
