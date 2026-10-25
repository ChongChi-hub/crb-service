package com.crb.seaturtlewebservice.domain.converter;

import com.crb.seaturtlewebservice.domain.dto.response.PageResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.entity.TablePrice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PageResponseConverter<T> {
    public PageResponseDto<T> buildPageResponseDto(Page<T> page) {
        PageResponseDto<T> dtoPageResponseDto = new PageResponseDto<>();
        dtoPageResponseDto.setContent(page.getContent());
        dtoPageResponseDto.setPageSize(page.getSize());
        dtoPageResponseDto.setTotalPages(page.getTotalPages());
        dtoPageResponseDto.setHasNext(page.hasNext());
        dtoPageResponseDto.setHasPrevious(page.hasPrevious());
        dtoPageResponseDto.setTotalElements(page.getTotalElements());
        dtoPageResponseDto.setCurrentPageNumber(page.getNumber());
        return dtoPageResponseDto;
    }

    public PageResponseDto<T> buildPageResponseDto(Page<Post> postPage, List<T> listPostResponseDtos) {
        PageResponseDto<T> dtoPageResponseDto = new PageResponseDto<>();
        dtoPageResponseDto.setContent(listPostResponseDtos);
        dtoPageResponseDto.setPageSize(postPage.getSize());
        dtoPageResponseDto.setTotalPages(postPage.getTotalPages());
        dtoPageResponseDto.setHasNext(postPage.hasNext());
        dtoPageResponseDto.setHasPrevious(postPage.hasPrevious());
        dtoPageResponseDto.setTotalElements(postPage.getTotalElements());
        dtoPageResponseDto.setCurrentPageNumber(postPage.getNumber());
        return dtoPageResponseDto;
    }

    public PageResponseDto<T> buildPageTablePriceResponseDto(Page<TablePrice> postPage, List<T> listPostResponseDtos) {
        PageResponseDto<T> dtoPageResponseDto = new PageResponseDto<>();
        dtoPageResponseDto.setContent(listPostResponseDtos);
        dtoPageResponseDto.setPageSize(postPage.getSize());
        dtoPageResponseDto.setTotalPages(postPage.getTotalPages());
        dtoPageResponseDto.setHasNext(postPage.hasNext());
        dtoPageResponseDto.setHasPrevious(postPage.hasPrevious());
        dtoPageResponseDto.setTotalElements(postPage.getTotalElements());
        dtoPageResponseDto.setCurrentPageNumber(postPage.getNumber());
        return dtoPageResponseDto;
    }
}
