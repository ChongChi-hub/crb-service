package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Iterable<Topic> getAllByCategoryId(Long id);

    List<Topic> findAllByCategoryId(long id);

    Optional<Topic> getTopicByUrlName(String urlName);
    Optional<Topic> getTopicByName(String keyword);

    Optional<Topic> findByName(String name);
}
