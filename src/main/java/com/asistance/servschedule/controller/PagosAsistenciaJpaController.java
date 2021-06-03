/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asistance.servschedule.controller;

import com.asistance.servschedule.controller.exceptions.NonexistentEntityException;
import com.asistance.servschedule.controller.exceptions.RollbackFailureException;
import com.asistance.servschedule.entity.PagosAsistencia;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author elgla
 */
public class PagosAsistenciaJpaController implements Serializable {

    public PagosAsistenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PagosAsistencia pagosAsistencia) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(pagosAsistencia);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PagosAsistencia pagosAsistencia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            pagosAsistencia = em.merge(pagosAsistencia);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = pagosAsistencia.getId();
                if (findPagosAsistencia(id) == null) {
                    throw new NonexistentEntityException("The pagosAsistencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PagosAsistencia pagosAsistencia;
            try {
                pagosAsistencia = em.getReference(PagosAsistencia.class, id);
                pagosAsistencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagosAsistencia with id " + id + " no longer exists.", enfe);
            }
            em.remove(pagosAsistencia);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PagosAsistencia> findPagosAsistenciaEntities() {
        return findPagosAsistenciaEntities(true, -1, -1);
    }

    public List<PagosAsistencia> findPagosAsistenciaEntities(int maxResults, int firstResult) {
        return findPagosAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<PagosAsistencia> findPagosAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PagosAsistencia.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PagosAsistencia findPagosAsistencia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PagosAsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagosAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PagosAsistencia> rt = cq.from(PagosAsistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
