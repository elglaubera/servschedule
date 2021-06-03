/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asistance.servschedule.controller;

import com.asistance.servschedule.entity.PagosAsistencia;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author elgla
 */
@Stateless
public class PagosAsistenciaFacade extends AbstractFacade<PagosAsistencia> {

    @PersistenceContext(unitName = "com.asistance_ServSchedule_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagosAsistenciaFacade() {
        super(PagosAsistencia.class);
    }
    
}
