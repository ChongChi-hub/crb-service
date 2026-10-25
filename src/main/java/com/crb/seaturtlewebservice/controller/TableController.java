package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.TableRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateTableRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    @PostMapping
    private ResponseEntity<ResponsePayload> saveNewTable(@RequestBody TableRequestDto tableRequestDto) {
        ResponsePayload responsePayload = tableService.saveTable(tableRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping
    private ResponseEntity<ResponsePayload> getTables(Pageable pageable, @RequestParam(name = "search") String keyword) {
        Pageable pageableRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        ResponsePayload responsePayload = tableService.getListTable(pageableRequest, keyword);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("{id}")
    private ResponseEntity<ResponsePayload> getTableById(@PathVariable Long id) {
        ResponsePayload responsePayload = tableService.getTableById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping
    private ResponseEntity<ResponsePayload> deleteListTables(@RequestBody DeleteListRequestDto deleteListTableRequestDto) {
        ResponsePayload responsePayload = tableService.deleteListTables(deleteListTableRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("{id}")
    private ResponseEntity<ResponsePayload> deleteTableById(@PathVariable Long id) {
        ResponsePayload responsePayload = tableService.deleteTableById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping
    private ResponseEntity<ResponsePayload> updateTable(@RequestBody UpdateTableRequestDto updateTableRequestDto) {
        ResponsePayload responsePayload = tableService.updateTable(updateTableRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/list-table-in-edit-post")
    private ResponseEntity<ResponsePayload> getListTaleByListTableId(@RequestParam(value="value") String listTableRequest) {
        ResponsePayload responsePayload = tableService.getTablesByListTableId(listTableRequest);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

}
