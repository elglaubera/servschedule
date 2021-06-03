/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asistance.servschedule.controller;

import com.asistance.servschedule.controller.exceptions.NonexistentEntityException;
import com.asistance.servschedule.controller.exceptions.RollbackFailureException;
import com.asistance.servschedule.entity.TipoAsistencia;
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
public class TipoAsistenciaJpaController implements Serializable {

    public TipoAsistenciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAsistencia tipoAsistencia) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(tipoAsistencia);
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

    public void edit(TipoAsistencia tipoAsistencia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            tipoAsistencia = em.merge(tipoAsistencia);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tipoAsistencia.getId();
                if (findTipoAsistencia(id) == null) {
                    throw new NonexistentEntityException("The tipoAsistencia with id " + id + " no longer exists.");
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
            TipoAsistencia tipoAsistencia;
            try {
                tipoAsistencia = em.getReference(TipoAsistencia.class, id);
                tipoAsistencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAsistencia with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoAsistencia);
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

    public List<TipoAsistencia> findTipoAsistenciaEntities() {
        return findTipoAsistenciaEntities(true, -1, -1);
    }

    public List<TipoAsistencia> findTipoAsistenciaEntities(int maxResults, int firstResult) {
        return findTipoAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<TipoAsistencia> findTipoAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAsistencia.class));
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

    public TipoAsistencia findTipoAsistencia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAsistencia> rt = cq.from(TipoAsistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
