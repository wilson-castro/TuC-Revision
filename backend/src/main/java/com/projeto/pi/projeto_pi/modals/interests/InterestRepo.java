package com.projeto.pi.projeto_pi.modals.interests;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.projeto.pi.projeto_pi.modals.cars.Car;

@Repository
public interface InterestRepo extends PagingAndSortingRepository<Interest, Long> {
    Optional<Interest> findById(long id);

    Optional<Interest> findByNome(String nome);

    Optional<Interest> findByNomeIgnoreCase(String nome);

    Optional<Interest> findByCarro(Car carro);

    Interest save(Interest item);

    Iterable<Interest> findAll();

    Page<Interest> findAllByNome(String nome, Pageable pageable);

    Iterable<Interest> findAllByCarro(Car carro);

    void deleteById(Long id);

    // @Modifying
    // @Query(value = "delete from interesses where car_id= :id", nativeQuery =
    // true)
    // @Transactional
    // void deleteByCarId(Long id);

    @Query(value = "select * from interesses where car_id = :id and ativo=true", nativeQuery = true)
    Optional<Interest> findByCarId(Long id);

}