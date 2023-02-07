/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.eldelbit.rest.services;

import es.eldelbit.rest.models.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 *
 * @author virtualbox
 */
@ApplicationScoped
@Transactional
public class ClienteService {

    @PersistenceContext
    private EntityManager em;

    public Cliente find(Long id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> get() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

}
