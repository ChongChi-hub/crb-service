package com.crb.seaturtlewebservice.domain.converter;

import com.crb.seaturtlewebservice.domain.dto.CategoryDto;
import com.crb.seaturtlewebservice.domain.dto.response.CategoryResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;

public interface CategoryConverter {
    CategoryResponseDto convertToDto(Category category);
    CategoryDto entityToDto(Category category);
}
