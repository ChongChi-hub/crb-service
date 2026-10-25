package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostPageableRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findAllByCategoryId(Pageable pageable, long topicId);
}
