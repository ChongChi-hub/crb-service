package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.TablePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TablePrice, Long> {
    Page<TablePrice> findAllByTitleTableContains(String keyword, Pageable pageable);
}
