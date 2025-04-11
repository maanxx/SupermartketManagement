package server.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import server.config.HibernateUtil;
import server.entity.ChiTietDonNhap;

public class ChiTietDonNhapDAO extends BaseDAO<ChiTietDonNhap> {
    public ChiTietDonNhapDAO() {
        super(ChiTietDonNhap.class);
    }

    public void deleteAllWithInvalidSanPham() {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int deleted = em.createQuery(
                    "DELETE FROM ChiTietDonNhap c WHERE c.sanPham.maSanPham IS NULL OR c.sanPham.hinhAnh IS NULL"
            ).executeUpdate();
            tx.commit();
            System.out.println(" Đã xóa " + deleted + " chi tiết đơn nhập có sản phẩm null hoặc không ảnh.");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}

