package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Points;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Points entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointsRepository extends JpaRepository<Points, Long> {

    @Query("select points from Points points where points.person.login = ?#{principal.username}")
    List<Points> findByPersonIsCurrentUser();

    @Query("select points from Points points where points.person.login = ?#{principal.username}")
    Page<Points> findByPersonIsCurrentUser(Pageable pageable);

//    List<Points> findAllByDateBetweenAndUserLogin(LocalDate firstDate,LocalDate secondDate, String login);
    
//    Page<Points> findAllByOrderByDateDesc (Pageable pageable);
}
