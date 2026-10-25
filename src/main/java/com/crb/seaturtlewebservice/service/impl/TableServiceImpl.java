package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.converter.PageResponseConverter;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.TableRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateTableRequestDto;
import com.crb.seaturtlewebservice.domain.dto.response.ListTableResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.PageResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.TableResponseDto;
import com.crb.seaturtlewebservice.domain.entity.TablePrice;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.TableRepository;
import com.crb.seaturtlewebservice.service.TableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableService.class);
    private final TableRepository tableRepository;
    private final PageResponseConverter<ListTableResponseDto> pageResponseConverter;

    @Override
    public ResponsePayload saveTable(TableRequestDto tableRequestDto) {
        LOGGER.info("TableServiceImpl -> Save table invoked");
        try {
            TablePrice table = new TablePrice();
            table.setTitleTable(tableRequestDto.getTitleTable());
            table.setContentTable(tableRequestDto.getContentTable());
            TablePrice response = tableRepository.save(table);
            return ResponsePayload.builder()
                    .data(response)
                    .message("Save table success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Save table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getListTable(Pageable pageableRequest, String keyword) {
        LOGGER.info("TableServiceImpl -> Get list table invoked");
        try {
            if (!keyword.isEmpty()) {
                Page<TablePrice> tablePrices = tableRepository.findAllByTitleTableContains(keyword, pageableRequest);
                List<ListTableResponseDto> listTableResponseDtos = tablePrices.stream()
                        .map(tablePrice -> {
                            ListTableResponseDto listTableResponseDto = new ListTableResponseDto();
                            BeanUtils.copyProperties(tablePrice, listTableResponseDto);
                            return listTableResponseDto;
                        })
                        .collect(Collectors.toList());
                PageResponseDto<ListTableResponseDto> pageResponseDto =
                        pageResponseConverter.buildPageTablePriceResponseDto(tablePrices, listTableResponseDtos);
                return ResponsePayload.builder()
                        .message("Get list post successful")
                        .status(HttpStatus.OK)
                        .data(pageResponseDto)
                        .build();
            }
            Page<TablePrice> tablePrices = tableRepository.findAll(pageableRequest);
            List<ListTableResponseDto> listTableResponseDtos = tablePrices.stream()
                    .map(tablePrice -> {
                        ListTableResponseDto listTableResponseDto = new ListTableResponseDto();
                        BeanUtils.copyProperties(tablePrice, listTableResponseDto);
                        return listTableResponseDto;
                    })
                    .collect(Collectors.toList());
            PageResponseDto<ListTableResponseDto> pageResponseDto =
                    pageResponseConverter.buildPageTablePriceResponseDto(tablePrices, listTableResponseDtos);
            return ResponsePayload.builder()
                    .data(pageResponseDto)
                    .message("Get list table success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Get list table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getTableById(Long id) {
        LOGGER.info("TableServiceImpl -> Get table with id: " + id + " invoked");
        try {
            TablePrice tablePrice = tableRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Table not found"));
            TableResponseDto tableResponseDto = new TableResponseDto();
            BeanUtils.copyProperties(tablePrice, tableResponseDto);
            return ResponsePayload.builder()
                    .data(tableResponseDto)
                    .message("Get table with id: " + id + " success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Get table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteTableById(Long id) {
        LOGGER.info("TableServiceImpl -> Delete table with id: " + id + " invoked");
        try {
            tableRepository.deleteById(id);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete table with id: " + id + "successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteListTables(DeleteListRequestDto deleteListTableRequestDto) {
        LOGGER.info("TableServiceImpl -> Delete list table invoked");
        try {
            List<Long> ids = deleteListTableRequestDto.getId();
            tableRepository.deleteAllByIdInBatch(ids);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete list tables successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete list tables fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload updateTable(UpdateTableRequestDto updateTableRequestDto) {
        LOGGER.info("TableServiceImpl -> Update table invoked");
        try {
            TablePrice tablePrice = tableRepository.findById(updateTableRequestDto.getId()).orElse(null);
            if (tablePrice != null) {
                tablePrice.setContentTable(updateTableRequestDto.getContentTable());
                tablePrice.setTitleTable(updateTableRequestDto.getTitleTable());
                tableRepository.save(tablePrice);
                return ResponsePayload.builder()
                        .data(tablePrice)
                        .message("Update table success")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ResponsePayload.builder()
                        .message("Table not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Update table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getTablesByListTableId(String listTableRequest) {
        LOGGER.info("TableServiceImpl -> Get table with lsit id: " + listTableRequest + " invoked");
        try {
            List<Long> listId = Arrays.stream(listTableRequest.split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            List<TablePrice> tablePrices = new ArrayList<>();
            for (Long id : listId) {
                TablePrice tablePrice = tableRepository.findById(id).orElse(null);
                if (tablePrice == null) {
                    continue;
                }
                tablePrices.add(tablePrice);
            }
            return ResponsePayload.builder()
                    .data(tablePrices)
                    .message("Get table with list id: " + " success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Get table fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }
}
