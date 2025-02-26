package shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangDTO implements Serializable {
    private String maKhachHang;
    private String tenKhachHang;
    private String soDienThoai;
    private String email;
    private float diemTichLuy;
}
