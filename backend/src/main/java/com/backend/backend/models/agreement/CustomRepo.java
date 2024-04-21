package com.backend.backend.models.agreement;

import java.util.concurrent.Future;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface CustomRepo<S, L> extends Repository<S, L> {
    Future<S> save(S entity);
    Future<Iterable<S>> saveAll(Iterable<S> entities);
}