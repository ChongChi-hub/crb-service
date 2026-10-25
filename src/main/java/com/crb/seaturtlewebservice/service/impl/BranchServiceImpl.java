package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.request.BranchRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateBranchRequestDto;
import com.crb.seaturtlewebservice.domain.entity.Branch;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.BranchRepository;
import com.crb.seaturtlewebservice.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchServiceImpl.class);
    @Override
    public ResponsePayload getBranches() {
        LOGGER.info("BranchesServiceImpl -> get all Branches invoked");
        try{
            Iterable  <Branch> branches = branchRepository.findAll();
            return ResponsePayload.builder()
                    .data(branches)
                    .status(HttpStatus.OK)
                    .message("Find all data of branches")
                    .build();
        } catch (Exception e){
            return ResponsePayload.builder()
                    .message("Not find data of branches")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload addBranch(BranchRequestDto branchRequestDto) {
        LOGGER.info("BranchesServiceImpl -> Add new branch invoked");
        try {
            Branch branch = new Branch();
            branch.setName(branchRequestDto.getName());
            branch.setAddress(branchRequestDto.getAddress());
            Branch newBranch = branchRepository.save(branch);
            return ResponsePayload.builder()
                    .data(newBranch)
                    .message("Save branch success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Save branch fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload updateBranch(UpdateBranchRequestDto updateBranchRequestDto) {
        LOGGER.info("BranchesServiceImpl -> Update branch invoked");
        try {
            Branch branch = branchRepository.findById(updateBranchRequestDto.getId()).orElse(null);
            if (branch != null) {
                branch.setAddress(updateBranchRequestDto.getAddress());
                branch.setName(updateBranchRequestDto.getName());
                Branch updateBranch = branchRepository.save(branch);
                return ResponsePayload.builder()
                        .data(updateBranch)
                        .message("Update branch success")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ResponsePayload.builder()
                        .message("Branch not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Update branch fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteBranchList(DeleteListRequestDto deleteListBranchRequestDto) {
        LOGGER.info("BranchesServiceImpl -> Delete list branch invoked");
        try {
            List<Long> ids = deleteListBranchRequestDto.getId();
            branchRepository.deleteAllByIdInBatch(ids);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete list branch successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete branch list fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteBranchById(Long id) {
        LOGGER.info("BranchesServiceImpl -> Delete branch with id: " + id + " invoked");
        try {
            branchRepository.deleteById(id);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete branch with id: " + id + "successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete branch fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }
}
