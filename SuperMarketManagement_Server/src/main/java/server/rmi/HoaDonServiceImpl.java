package server.rmi;

import server.dao.HoaDonDAO;
import server.dao.NhanVienDAO;
import server.dao.KhachHangDAO;
import server.entity.HoaDon;
import shared.dto.HoaDonDTO;
import shared.services.HoaDonService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class HoaDonServiceImpl extends UnicastRemoteObject implements HoaDonService {
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public HoaDonServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<HoaDonDTO> getAllHoaDons() throws RemoteException {
        return hoaDonDAO.findAll().stream()
                .map(hd -> new HoaDonDTO(
                        hd.getMaHoaDon(),
                        hd.getNhanVien() != null ? hd.getNhanVien().getMaNhanVien() : null,
                        hd.getKhachHang() != null ? hd.getKhachHang().getMaKhachHang() : null,
                        hd.getNgayLap(),
                        hd.getTongTien()))
                .collect(Collectors.toList());
    }

    @Override
    public HoaDonDTO getHoaDonById(String id) throws RemoteException {
        HoaDon hd = hoaDonDAO.findById(id);
        return hd != null ? new HoaDonDTO(
                hd.getMaHoaDon(),
                hd.getNhanVien() != null ? hd.getNhanVien().getMaNhanVien() : null,
                hd.getKhachHang() != null ? hd.getKhachHang().getMaKhachHang() : null,
                hd.getNgayLap(),
                hd.getTongTien()) : null;
    }

    @Override
    public void addHoaDon(HoaDonDTO hoaDonDTO) throws RemoteException {
        HoaDon hd = new HoaDon(
                hoaDonDTO.getMaHoaDon(),
                nhanVienDAO.findById(hoaDonDTO.getMaNhanVien()),
                khachHangDAO.findById(hoaDonDTO.getMaKhachHang()),
                hoaDonDTO.getNgayLap(),
                hoaDonDTO.getTongTien(),
                null
        );
        hoaDonDAO.save(hd);
    }

    @Override
    public void updateHoaDon(HoaDonDTO hoaDonDTO) throws RemoteException {
        HoaDon hd = hoaDonDAO.findById(hoaDonDTO.getMaHoaDon());
        if (hd != null) {
            hd.setNhanVien(nhanVienDAO.findById(hoaDonDTO.getMaNhanVien()));
            hd.setKhachHang(khachHangDAO.findById(hoaDonDTO.getMaKhachHang()));
            hd.setNgayLap(hoaDonDTO.getNgayLap());
            hd.setTongTien(hoaDonDTO.getTongTien());
            hoaDonDAO.update(hd);
        }
    }

    @Override
    public void deleteHoaDon(String id) throws RemoteException {
        HoaDon hd = hoaDonDAO.findById(id);
        if (hd != null) {
            hoaDonDAO.delete(hd);
        }
    }

    @Override
    public int getTongDoanhThu() throws RemoteException {
        List<HoaDon> list = hoaDonDAO.findAll();
        return list.stream().mapToInt(hd -> (int) hd.getTongTien()).sum();
    }

    @Override
    public int getSoLuongDonHang() throws RemoteException {
        return hoaDonDAO.findAll().size();
    }
}