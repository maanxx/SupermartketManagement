package server.rmi;

import server.dao.KhachHangDAO;
import server.entity.KhachHang;
import shared.dto.KhachHangDTO;
import shared.services.KhachHangService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class KhachHangServiceImpl extends UnicastRemoteObject implements KhachHangService {
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public KhachHangServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<KhachHangDTO> getAllKhachHangs() throws RemoteException {
        return khachHangDAO.findAll().stream()
                .map(kh -> new KhachHangDTO(
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai(),
                        kh.getMail(),
                        kh.getDiemTichLuy()))
                .collect(Collectors.toList());
    }

    @Override
    public KhachHangDTO getKhachHangById(String id) throws RemoteException {
        KhachHang kh = khachHangDAO.findById(id);
        return kh != null ? new KhachHangDTO(
                kh.getMaKhachHang(),
                kh.getTenKhachHang(),
                kh.getSoDienThoai(),
                kh.getMail(),
                kh.getDiemTichLuy()) : null;
    }

    @Override
    public void addKhachHang(KhachHangDTO khachHangDTO) throws RemoteException {
        KhachHang kh = new KhachHang(
                khachHangDTO.getMaKhachHang(),
                khachHangDTO.getTenKhachHang(),
                khachHangDTO.getSoDienThoai(),
                khachHangDTO.getEmail(),
                khachHangDTO.getDiemTichLuy(),
                null
        );
        khachHangDAO.save(kh);
    }

    @Override
    public void updateKhachHang(KhachHangDTO khachHangDTO) throws RemoteException {
        KhachHang kh = khachHangDAO.findById(khachHangDTO.getMaKhachHang());
        if (kh != null) {
            kh.setTenKhachHang(khachHangDTO.getTenKhachHang());
            kh.setSoDienThoai(khachHangDTO.getSoDienThoai());
            kh.setMail(khachHangDTO.getEmail());
            kh.setDiemTichLuy(khachHangDTO.getDiemTichLuy());
            khachHangDAO.update(kh);
        }
    }

    @Override
    public void deleteKhachHang(String id) throws RemoteException {
        KhachHang kh = khachHangDAO.findById(id);
        if (kh != null) {
            khachHangDAO.delete(kh);
        }
    }
}
