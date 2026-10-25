package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.ImageDir;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDirRepository extends JpaRepository<ImageDir, Long> {
}