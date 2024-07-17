package com.fof.server.controller;

import com.fof.server.model.entity.AdminDTO;
import com.fof.server.model.entity.UserDTO;
import com.fof.server.model.normal.ResponseDTO;
import com.fof.server.service.CoAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coAdmin")
@RequiredArgsConstructor
public class CoAdminController {

    private final CoAdminService coAdminService;

    @GetMapping("/view")
    public ResponseEntity<List<AdminDTO>> getAllCoAdmins() {
        List<AdminDTO> coAdmins = coAdminService.getAllCoAdmins();
        return ResponseEntity.ok(coAdmins);
    }

    @GetMapping("/update/view/{id}")
    public ResponseEntity<AdminDTO> getCoAdminDetails(@PathVariable Integer id) {
        AdminDTO findCoAdmin =  coAdminService.getDetails(id);
        if (findCoAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(findCoAdmin);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCoAdmin(@RequestBody AdminDTO updatedAdmin , @PathVariable Integer id) {
        AdminDTO updatedCoAdmin = coAdminService.updateCoAdmin(updatedAdmin, id);

        if (updatedCoAdmin != null) {
            return ResponseEntity.ok("Co-admin updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/ban/{id}")
    public ResponseEntity<ResponseDTO> banCoAdmin(@PathVariable Integer id) {
        UserDTO updatedCoAdmin = coAdminService.banCoAdmin(id);

        if (updatedCoAdmin != null) {
            return ResponseEntity.ok(
                    ResponseDTO.builder()
                            .status("200")
                            .message("User " + id + " Banned Successfully")
                            .build()
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}