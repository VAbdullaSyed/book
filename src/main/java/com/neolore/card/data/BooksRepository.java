package com.neolore.card.data;

import com.neolore.card.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
  @Query(value = "select aurora_db_instance_identifier()", nativeQuery = true)
  String getNodeId();
}
