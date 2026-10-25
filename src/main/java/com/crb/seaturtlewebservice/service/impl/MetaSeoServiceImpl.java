package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.crb.seaturtlewebservice.domain.entity.MetaSeo;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.MetaSeoRepository;
import com.crb.seaturtlewebservice.service.MetaSeoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MetaSeoServiceImpl implements MetaSeoService {

    private final MetaSeoRepository metaSeoRepository;
    private final ModelMapper modelMapper; 

    @Override
    public ResponsePayload updateMetaSeo(String entityType, Long entityId, MetaSeoDto metaSeoDto) {
        try {
            Optional<MetaSeo> optionalMetaSeo = metaSeoRepository.findByEntityIdAndEntityType(entityId, entityType);

            MetaSeo metaSeoToSave;

            if (optionalMetaSeo.isPresent()) {
                metaSeoToSave = optionalMetaSeo.get();
                Long id = metaSeoToSave.getId();
                modelMapper.map(metaSeoDto, metaSeoToSave);
                metaSeoToSave.setId(id); 
                metaSeoToSave.setEntityId(entityId);
                metaSeoToSave.setEntityType(entityType);

            } else {
                metaSeoToSave = modelMapper.map(metaSeoDto, MetaSeo.class);
                metaSeoToSave.setEntityId(entityId);
                metaSeoToSave.setEntityType(entityType);
            }

            MetaSeo savedMetaSeo = metaSeoRepository.save(metaSeoToSave);
            MetaSeoDto responseDto = modelMapper.map(savedMetaSeo, MetaSeoDto.class);

            return ResponsePayload.builder()
                    .data(responseDto)
                    .status(HttpStatus.OK)
                    .message("Update MetaSEO for " + entityType + ":" + entityId + " successful")
                    .build();

        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Update MetaSEO fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload getMetaSeo(String entityType, Long entityId) {
        try {
            Optional<MetaSeo> optionalMetaSeo = metaSeoRepository.findByEntityIdAndEntityType(entityId, entityType);

            if (optionalMetaSeo.isPresent()) {
                MetaSeoDto responseDto = modelMapper.map(optionalMetaSeo.get(), MetaSeoDto.class);
                return ResponsePayload.builder()
                        .data(responseDto)
                        .status(HttpStatus.OK)
                        .message("Get MetaSEO successful")
                        .build();
            } else {
                return ResponsePayload.builder()
                        .data(null) 
                        .status(HttpStatus.OK)
                        .message("MetaSEO not found, create a new one.")
                        .build();
            }
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Get MetaSEO fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}