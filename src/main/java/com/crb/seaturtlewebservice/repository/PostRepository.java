package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByCategoryIdAndIsHiddenFalse(Long id, Pageable pageable);

    List<Post> findAllByTopicIdAndIsHiddenFalse(Long topicId);

    Optional<Post> findByUrlNameAndIsHiddenFalse(String urlName);

    Page<Post> findAllByTopicIdAndIsHiddenFalse(Pageable pageable, Long topicId);

    List<Post> getAllByCategoryIdAndIsHiddenFalse(Long id);

    Page<Post> findAllByTopicIdNotIn(List<Long> excludedTopicIds, Pageable pageableRequest);

    List<Post> findAllByTopicIdAndIsHidden(Long topicId, Boolean isHidden);

    Page<Post> findAllByNameContains(String keyword, Pageable pageable);

    Page<Post> findAllByNameContainsAndTopicIdNotIn(String keyword, List<Long> excludedTopicIds, Pageable pageableRequest);

    List<Post> findAllByIsHiddenFalse();

    @Query(value = "SELECT * FROM post " +
            "WHERE is_hidden = false " +
            "AND name REGEXP :regex " +
            "ORDER BY date_post DESC", countQuery = "SELECT count(*) FROM post WHERE is_hidden = false AND name REGEXP :regex", nativeQuery = true)
    Page<Post> findRelatedPostsByRegex(@Param("regex") String regex, Pageable pageable);
}
