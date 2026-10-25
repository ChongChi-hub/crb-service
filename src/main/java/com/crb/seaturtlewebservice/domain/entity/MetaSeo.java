package com.crb.seaturtlewebservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "meta_seo", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "entityId", "entityType" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaSeo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long entityId; 

    @Column(nullable = false, length = 100)
    private String entityType; 

    private String metaTitle;

    @Column(length = 500)
    private String metaDescription;

    @Column(length = 500)
    private String metaKeywords;

    @Column(length = 1000)
    private String canonicalUrl;

    @Column(length = 100)
    private String robots;

    private String ogTitle;

    @Column(length = 500)
    private String ogDescription;

    @Column(length = 1000)
    private String ogImage;

    @Column(length = 100)
    private String ogType; 

    @Column(length = 100)
    private String twitterCard; 

    private String twitterTitle;

    @Column(length = 500)
    private String twitterDescription;

    @Column(length = 1000)
    private String twitterImage;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}