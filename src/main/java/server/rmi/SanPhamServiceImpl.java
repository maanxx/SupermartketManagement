package server.rmi;

import server.dao.SanPhamDAO;
import server.dao.NhaCungCapDAO;
import server.dao.LoaiSanPhamDAO;
import server.entity.SanPham;
import shared.dto.LoaiSanPhamDTO;
import shared.dto.NhaCungCapDTO;
import shared.dto.SanPhamDTO;
import shared.services.SanPhamService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

public class SanPhamServiceImpl extends UnicastRemoteObject implements SanPhamService {
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    private final LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();

    public SanPhamServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<SanPhamDTO> getAllSanPhams() throws RemoteException {
        return sanPhamDAO.findAll().stream()
                .map(sp -> new SanPhamDTO(
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        sp.getNhaCungCap().getMaNhaCungCap(),
                        sp.getLoaiSanPham().getMaLoai(),
                        sp.getGia(),
                        sp.getSoLuong()))
                .collect(Collectors.toList());
    }

    @Override
    public SanPhamDTO getSanPhamById(String id) throws RemoteException {
        SanPham sp = sanPhamDAO.findById(id);
        return sp != null ? new SanPhamDTO(
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                sp.getNhaCungCap().getMaNhaCungCap(),
                sp.getLoaiSanPham().getMaLoai(),
                sp.getGia(),
                sp.getSoLuong()) : null;
    }

    @Override
    public void addSanPham(SanPhamDTO sanPhamDTO) throws RemoteException {
        SanPham sp = new SanPham(
                sanPhamDTO.getMaSanPham(),
                sanPhamDTO.getTenSanPham(),
                nhaCungCapDAO.findById(sanPhamDTO.getMaNhaCungCap()),
                loaiSanPhamDAO.findById(sanPhamDTO.getMaLoaiSanPham()),
                null,
                sanPhamDTO.getGia(),
                sanPhamDTO.getSoLuong()
        );
        sanPhamDAO.save(sp);
    }

    @Override
    public void updateSanPham(SanPhamDTO sanPhamDTO) throws RemoteException {
        SanPham sp = sanPhamDAO.findById(sanPhamDTO.getMaSanPham());
        if (sp != null) {
            sp.setTenSanPham(sanPhamDTO.getTenSanPham());
            sp.setNhaCungCap(nhaCungCapDAO.findById(sanPhamDTO.getMaNhaCungCap()));
            sp.setLoaiSanPham(loaiSanPhamDAO.findById(sanPhamDTO.getMaLoaiSanPham()));
            sp.setGia(sanPhamDTO.getGia());
            sp.setSoLuong(sanPhamDTO.getSoLuong());
            sanPhamDAO.update(sp);
        }
    }

    @Override
    public void deleteSanPham(String id) throws RemoteException {

        try {
            SanPham sp = sanPhamDAO.findById(id);
            if (sp == null) {
                System.out.println("Không tìm thấy sản phẩm với mã: " + id);
                return;
            }
            sanPhamDAO.deleteSanPham(id);
            System.out.println(" Xóa thành công sản phẩm: " + id);
        } catch (Exception e) {
            System.out.println(" Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<LoaiSanPhamDTO> getAllLoaiSanPham() throws RemoteException {
        return loaiSanPhamDAO.findAll().stream()
                .map(loai -> new LoaiSanPhamDTO(loai.getMaLoai(), loai.getTenLoai()))
                .collect(Collectors.toList());
    }

    @Override
    public List<NhaCungCapDTO> getAllNhaCungCap() throws RemoteException {
        return nhaCungCapDAO.findAll().stream()
                .map(ncc -> new NhaCungCapDTO(ncc.getMaNhaCungCap(), ncc.getTenNhaCungCap()))
                .collect(Collectors.toList());
    }

}
