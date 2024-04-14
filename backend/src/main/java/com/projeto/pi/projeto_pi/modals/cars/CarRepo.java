package com.projeto.pi.projeto_pi.modals.cars;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface CarRepo extends PagingAndSortingRepository<Car, Long> {
    Optional<Car> findById(long id);

    @Query(value = "Select * from CARROS c where COALESCE( ( Select ativo from interesses i where i.car_id = c.id and i.ativo = true ), false ) = false and c.id= :id order by c.id", nativeQuery = true)
    Optional<Car> findByIdActive(long id);

    @Query(value = "Select c.* from CARROS c Left join interesses i on i.car_id=c.id and i.data_interesse=(Select Max(data_interesse) from interesses where car_id=c.id) where (COALESCE(i.ativo,false)=false or COALESCE(i.ativo,false)=false) order by c.id", nativeQuery = true)
    Page<Car> findAllActive(Pageable pageable);

    @Query(value = "Select distinct marca from CARROS c", nativeQuery = true)
    List<String> findAllMarcas();

    @Query(value = "Select c.* from CARROS c Left join interesses i on i.car_id=c.id and i.data_interesse=(Select Max(data_interesse) from interesses where car_id=c.id and ativo = true) where (COALESCE(i.ativo,false)=false or COALESCE(i.ativo,false)= :ativos ) and (c.modelo like %:modelo% and c.marca in :marca) order by c.id", nativeQuery = true)
    Page<Car> searchBy(Pageable pageable, @Param("modelo") String modelo, @Param("marca") List<String> marca,
            @Param("ativos") Boolean ativos);

    Car save(Car item);

    void deleteById(Long id);
}