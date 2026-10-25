package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch,Long> {
}
