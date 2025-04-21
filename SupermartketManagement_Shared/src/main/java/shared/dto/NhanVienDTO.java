package shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienDTO implements Serializable {
    private String maNhanVien;
    private String hoTen;
    private String ngaySinh;
    private String soDienThoai;
    private String diaChi;
    private String chucDanh;
    private String role;
}
