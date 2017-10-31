package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message where message.author.login = ?#{principal.username}")
    Page<Message> findByAuthorIsCurrentUser(Pageable pageable);


    Page<Message> findAllByOrderByIdDesc (Pageable pageable);
    
}
