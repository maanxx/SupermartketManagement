package shared.services;

import shared.dto.NhanVienDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NhanVienService extends Remote {
    NhanVienDTO login(String username, String password) throws RemoteException;
    NhanVienDTO getNhanVienById(String id) throws RemoteException;
    void updatePassword(String id, String newPassword) throws RemoteException;
    List<NhanVienDTO> getAllNhanViens() throws RemoteException;
    void addNhanVien(NhanVienDTO nhanVien) throws RemoteException;
    void updateNhanVien(NhanVienDTO nhanVien) throws RemoteException;
    void deleteNhanVien(String id) throws RemoteException;
    int getNhanVienMoiTheoThang(String thang) throws RemoteException;
}