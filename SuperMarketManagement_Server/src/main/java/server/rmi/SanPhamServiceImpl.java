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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                        sp.getSoLuong(),
                        sp.getHinhAnh()
                ))
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
                sp.getSoLuong(),
                sp.getHinhAnh()
        ) : null;
    }

    @Override
    public void addSanPham(SanPhamDTO sanPhamDTO) throws RemoteException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(sanPhamDTO.getMaSanPham());
        sp.setTenSanPham(sanPhamDTO.getTenSanPham());
        sp.setNhaCungCap(nhaCungCapDAO.findById(sanPhamDTO.getMaNhaCungCap()));
        sp.setLoaiSanPham(loaiSanPhamDAO.findById(sanPhamDTO.getMaLoaiSanPham()));
        sp.setGia(sanPhamDTO.getGia());
        sp.setSoLuong(sanPhamDTO.getSoLuong());
        sp.setHinhAnh(sanPhamDTO.getHinhAnh());

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
            sp.setHinhAnh(sanPhamDTO.getHinhAnh());
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

    @Override
    public void uploadHinhAnh(String fileName, byte[] fileData) throws RemoteException {
        try {
            String folderPath = "src/main/resources/images/"; // Đường dẫn ảnh trong server
            java.nio.file.Path imagePath = java.nio.file.Paths.get(folderPath + fileName);
            java.nio.file.Files.write(imagePath, fileData);
            System.out.println("📸 Ảnh đã được lưu tại: " + imagePath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(" Lỗi khi lưu ảnh", e);
        }
    }

    @Override
    public byte[] downloadHinhAnh(String fileName) throws RemoteException {
        try {
            String folderPath = "src/main/resources/images/";
            java.nio.file.Path imagePath = java.nio.file.Paths.get(folderPath + fileName);
            return java.nio.file.Files.readAllBytes(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(" Lỗi khi đọc ảnh từ server", e);
        }
    }

    @Override
    public int getSanPhamMoiTheoThang(String thang) throws RemoteException {
        return (int) sanPhamDAO.findAll().stream()
                .filter(sp -> sp.getNgayTao() != null && String.format("%02d", sp.getNgayTao().getMonthValue()).equals(thang))
                .count();
    }

    @Override
    public Map<String, Integer> getThongKeSoLuongTheoLoai() throws RemoteException {
        List<SanPham> list = sanPhamDAO.findAll();
        Map<String, Integer> result = new HashMap<>();

        for (SanPham sp : list) {
            if (sp.getLoaiSanPham() != null && sp.getLoaiSanPham().getTenLoai() != null) {
                String tenLoai = sp.getLoaiSanPham().getTenLoai();
                result.put(tenLoai, result.getOrDefault(tenLoai, 0) + 1);
            }
        }

        return result;
    }


}