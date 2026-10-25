package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.TableRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateTableRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import org.springframework.data.domain.Pageable;

public interface TableService {
    ResponsePayload saveTable(TableRequestDto tableRequestDto);

    ResponsePayload getListTable(Pageable pageableRequest, String keyword);

    ResponsePayload getTableById(Long id);

    ResponsePayload deleteTableById(Long id);

    ResponsePayload deleteListTables(DeleteListRequestDto deleteListTableRequestDto);

    ResponsePayload updateTable(UpdateTableRequestDto updateTableRequestDto);

    ResponsePayload getTablesByListTableId(String listTableRequest);
}
