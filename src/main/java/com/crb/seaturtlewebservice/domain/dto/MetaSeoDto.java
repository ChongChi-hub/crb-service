package com.crb.seaturtlewebservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaSeoDto {

    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    private String canonicalUrl;
    private String robots;

    private String ogTitle;
    private String ogDescription;
    private String ogImage;
    private String ogType;

    private String twitterCard;
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;
}