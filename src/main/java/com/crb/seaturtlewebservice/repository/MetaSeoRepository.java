package com.crb.seaturtlewebservice.repository; 

import com.crb.seaturtlewebservice.domain.entity.MetaSeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaSeoRepository extends JpaRepository<MetaSeo, Long> {
    Optional<MetaSeo> findByEntityIdAndEntityType(Long entityId, String entityType);
}