package server.rmi;

import server.dao.NhanVienDAO;
import server.entity.NhanVien;
import shared.services.NhanVienService;
import shared.dto.NhanVienDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class NhanVienServiceImpl extends UnicastRemoteObject implements NhanVienService {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public NhanVienServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public NhanVienDTO login(String username, String password) throws RemoteException {
        NhanVien nv = nhanVienDAO.findByUsernameAndPassword(username, password);
        return nv != null ? new NhanVienDTO(
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getChucDanh(),
                nv.getRole()  // ✅ Trả về role của nhân viên
        ) : null;
    }

    @Override
    public NhanVienDTO getNhanVienById(String id) throws RemoteException {
        NhanVien nv = nhanVienDAO.findById(id);
        return nv != null ? new NhanVienDTO(
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getChucDanh(),
                nv.getRole()  // ✅ Thêm role vào DTO
        ) : null;
    }

    @Override
    public void updatePassword(String id, String newPassword) throws RemoteException {
        NhanVien nv = nhanVienDAO.findById(id);
        if (nv != null && nv.getTaiKhoan() != null) {
            nv.getTaiKhoan().setMatKhau(newPassword);
            nhanVienDAO.update(nv);
        }
    }

    @Override
    public List<NhanVienDTO> getAllNhanViens() throws RemoteException {
        return nhanVienDAO.findAll().stream()
                .map(nv -> new NhanVienDTO(
                        nv.getMaNhanVien(),
                        nv.getHoTen(),
                        nv.getSoDienThoai(),
                        nv.getDiaChi(),
                        nv.getChucDanh(),
                        nv.getRole()  // ✅ Đảm bảo role được gửi về
                ))
                .collect(Collectors.toList());
    }
}
