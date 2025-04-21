package shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonDTO implements Serializable {
    private String maHoaDon;
    private String maNhanVien;
    private String maKhachHang;
    private String ngayLap;
    private double tongTien;
}
