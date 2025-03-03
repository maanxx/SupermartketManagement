package shared.services;

import shared.dto.KhachHangDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface KhachHangService extends Remote {
    List<KhachHangDTO> getAllKhachHangs() throws RemoteException;
    KhachHangDTO getKhachHangById(String id) throws RemoteException;
    void addKhachHang(KhachHangDTO khachHang) throws RemoteException;
    void updateKhachHang(KhachHangDTO khachHang) throws RemoteException;
    void deleteKhachHang(String id) throws RemoteException;
}
