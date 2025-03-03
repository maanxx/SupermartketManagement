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
        return nv != null ? convertToDTO(nv) : null;
    }

    @Override
    public NhanVienDTO getNhanVienById(String id) throws RemoteException {
        NhanVien nv = nhanVienDAO.findById(id);
        return nv != null ? convertToDTO(nv) : null;
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
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addNhanVien(NhanVienDTO nhanVienDTO) throws RemoteException {
        NhanVien nv = new NhanVien(
                nhanVienDTO.getMaNhanVien(),
                nhanVienDTO.getHoTen(),
                nhanVienDTO.getNgaySinh(),
                nhanVienDTO.getSoDienThoai(),
                nhanVienDTO.getDiaChi(),
                nhanVienDTO.getChucDanh(),
                nhanVienDTO.getRole(),
                null, // Chưa có tài khoản
                null, // Hóa đơn rỗng ban đầu
                null  // Hóa đơn nhập rỗng ban đầu
        );
        nhanVienDAO.save(nv);
    }

    @Override
    public void updateNhanVien(NhanVienDTO nhanVienDTO) throws RemoteException {
        NhanVien nv = nhanVienDAO.findById(nhanVienDTO.getMaNhanVien());
        if (nv != null) {
            nv.setHoTen(nhanVienDTO.getHoTen());
            nv.setNgaySinh(nhanVienDTO.getNgaySinh());
            nv.setSoDienThoai(nhanVienDTO.getSoDienThoai());
            nv.setDiaChi(nhanVienDTO.getDiaChi());
            nv.setChucDanh(nhanVienDTO.getChucDanh());
            nv.setRole(nhanVienDTO.getRole());
            nhanVienDAO.update(nv);
        }
    }

    @Override
    public void deleteNhanVien(String id) throws RemoteException {
        NhanVien nv = nhanVienDAO.findById(id);
        if (nv != null) {
            nhanVienDAO.delete(nv);
        }
    }

    private NhanVienDTO convertToDTO(NhanVien nv) {
        return new NhanVienDTO(
                nv.getMaNhanVien(),
                nv.getHoTen(),
                nv.getNgaySinh(),
                nv.getSoDienThoai(),
                nv.getDiaChi(),
                nv.getChucDanh(),
                nv.getRole()
        );
    }
}
