package server.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import server.config.HibernateUtil;
import server.entity.NhanVien;

public class NhanVienDAO extends BaseDAO<NhanVien> {
    public NhanVienDAO() {
        super(NhanVien.class);
    }

    public NhanVien findByUsernameAndPassword(String username, String password) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<NhanVien> query = em.createQuery(
                    "SELECT nv FROM NhanVien nv JOIN nv.taiKhoan tk WHERE nv.maNhanVien = :username AND tk.matKhau = :password",
                    NhanVien.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

}
