package dao;


import config.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public abstract class BaseDAO<T> {

    private final Class<T> entityClass;

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public T findById(Object id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String query = "FROM " + entityClass.getSimpleName();
            return em.createQuery(query, entityClass).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}


