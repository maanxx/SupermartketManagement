package server.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import server.config.HibernateUtil;
import server.entity.SanPham;

public class SanPhamDAO extends BaseDAO<SanPham> {
    public SanPhamDAO() {
        super(SanPham.class);
    }

    public void deleteSanPham(String id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            SanPham sp = em.createQuery("SELECT s FROM SanPham s WHERE s.maSanPham = :id", SanPham.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (sp != null) {
                System.out.println("Tìm thấy sản phẩm: " + sp);
                em.remove(sp);
                System.out.println("Xóa thành công sản phẩm: " + id);
            } else {
                System.out.println(" Không tìm thấy sản phẩm: " + id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.out.println(" Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public SanPham findById(String id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT sp FROM SanPham sp " +
                                    "JOIN FETCH sp.nhaCungCap " +
                                    "JOIN FETCH sp.loaiSanPham " +
                                    "WHERE sp.maSanPham = :id",
                            SanPham.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}


