package com.backend.backend.models.importer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImporterRepo extends PagingAndSortingRepository<Import, Long>, CrudRepository<Import, Long> {

}
