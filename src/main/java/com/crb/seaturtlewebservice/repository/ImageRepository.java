package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableJpaRepositories
public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findAllByIsDeleteFalseAndTypeNull(Pageable pageable);

    @Query("SELECT i FROM Image i WHERE (i.name LIKE CONCAT('%', :value, '%') OR " +
            "i.description LIKE CONCAT('%', :value, '%') OR " +
            "i.imageCategory LIKE CONCAT('%', :value, '%')) AND " +
            "(i.type IS NULL) AND " +
            "i.isDelete = false")
    Page<Image> findByNameContainingOrDescriptionContainingOrImageCategoryContainingAndIsDeleteFalse(
            @Param("value") String value,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query("insert into Image(name, link, topicName, description) " +
            "VALUES (:name, :link, :topicName, :description)")
    void insertImage(@Param("name") String name, @Param("link") String link,
                     @Param("topicName") String topicName, @Param("description") String description);

    @Query("SELECT i FROM Image i WHERE i.isDelete = false AND i.year = :year  AND (i.name LIKE CONCAT('%', :keyword, '%') OR i.description LIKE CONCAT('%', :keyword, '%') OR i.imageCategory LIKE CONCAT('%', :keyword, '%'))")
    Page<Image> findImagesByNameAndNotDeleted(@Param("keyword") String keyword, Pageable pageable, @Param("year") Integer year);

    @Query("SELECT i FROM Image i WHERE i.isDelete = false AND i.year = :year")
    Page<Image> findImagesNotDeleted(Pageable pageable, @Param("year") Integer year);
    

    List<Image> findAllByIsDeleteFalseAndTypeEquals(String type);
}