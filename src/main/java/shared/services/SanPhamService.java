package shared.services;

import shared.dto.LoaiSanPhamDTO;
import shared.dto.NhaCungCapDTO;
import shared.dto.SanPhamDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SanPhamService extends Remote {
    List<SanPhamDTO> getAllSanPhams() throws RemoteException;
    SanPhamDTO getSanPhamById(String id) throws RemoteException;
    void addSanPham(SanPhamDTO sanPham) throws RemoteException;
    void updateSanPham(SanPhamDTO sanPham) throws RemoteException;
    void deleteSanPham(String id) throws RemoteException;
    List<LoaiSanPhamDTO> getAllLoaiSanPham() throws RemoteException;
    List<NhaCungCapDTO> getAllNhaCungCap() throws RemoteException;
}
