package com.backend.backend.models.agreement;

import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

public interface AgreementRepo
        extends PagingAndSortingRepository<Agreement, Long>,
        CustomRepo<Agreement, Long> {

    @Async
    Future<Agreement> save(Agreement entity);
    
    @Async
    Future<Iterable<Agreement>> saveAll(Iterable<Agreement> entities);

    Optional<Agreement> findById(Long id);

    boolean existsById(Long id);

    Iterable<Agreement> findAll();

    Iterable<Agreement> findAllById(Iterable<Long> ids);

    void deleteById(Long id);

    void delete(Agreement entity);

    void deleteAllById(Iterable<? extends Long> ids);

    void deleteAll(Iterable<? extends Agreement> entities);

    void deleteAll();

    // <S extends Agreement> Iterable<S> saveAll(Iterable<S> entities);

}