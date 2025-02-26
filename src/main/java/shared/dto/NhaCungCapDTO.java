package shared.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCapDTO implements Serializable {
    private String maNhaCungCap;
    private String tenNhaCungCap;
}
