package com.backend.backend.models.agreement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional
public class MyEntityRepositoryImpl implements CustomRepo<Agreement, Long> {

    // optionally specify unitName, if there are more than one
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository
     */
    @Transactional
    public Future<Agreement> save(Agreement entity) {

        JpaEntityInformation<Agreement, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(
                Agreement.class,
                entityManager);
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);
            return CompletableFuture.completedFuture(entity);
        } else {
            return CompletableFuture.completedFuture(entityManager.merge(entity));
        }
    }

    @Override
    public Future<Iterable<Agreement>> saveAll(Iterable<Agreement> entities) {

        List<Agreement> agreements = new ArrayList<>();
        JpaEntityInformation<Agreement, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(
                Agreement.class,
                entityManager);
        for (Agreement entity : entities) {
            if (entityInformation.isNew(entity)) {
                entityManager.persist(entity);
                agreements.add(entity);
            } else {
                Agreement merge = entityManager.merge(entity);
                agreements.add(merge);
            }
        }
        return CompletableFuture.completedFuture(agreements);
    }

}