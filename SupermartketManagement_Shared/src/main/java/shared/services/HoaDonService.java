package shared.services;

import shared.dto.HoaDonDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HoaDonService extends Remote {
    List<HoaDonDTO> getAllHoaDons() throws RemoteException;
    HoaDonDTO getHoaDonById(String id) throws RemoteException;
    void addHoaDon(HoaDonDTO hoaDon) throws RemoteException;
    void updateHoaDon(HoaDonDTO hoaDon) throws RemoteException;
    void deleteHoaDon(String id) throws RemoteException;
    int getTongDoanhThu() throws RemoteException;
    int getSoLuongDonHang() throws RemoteException;
}