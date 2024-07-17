package com.fof.server.controller;

import com.fof.server.model.entity.ScheduleDTO;
import com.fof.server.service.ScheduleService;
import com.fof.server.model.normal.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/list/{id}")
    public ResponseEntity<List<Map<String, String>>> getAllSchedules(
            @PathVariable Integer id
    ) {
        List<Map<String, String>> schedules = scheduleService.getAllSchedules(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedules);
    }


    @PostMapping("/add/{id}")
    public ResponseEntity<ResponseDTO> addSchedule(
            @PathVariable Integer id,
            @RequestBody ScheduleDTO scheduleDTO
    ) {
        return ResponseEntity.ok(scheduleService.addSchedule(id,scheduleDTO));
    }

    @PostMapping("EntrepreneurAdd/{id}")
    public ResponseEntity<ResponseDTO> addScheduleEntrepreneur(
            @PathVariable Integer id,
            @RequestBody ScheduleDTO scheduleDTO
    ) {
        return ResponseEntity.ok(scheduleService.addScheduleEntrepreneur(id,scheduleDTO));
    }

    @GetMapping("/listEntrepreneur/{id}")
    public ResponseEntity<List<Map<String, String>>> getAllSchedulesEntrepreneur(
            @PathVariable Integer id
    ) {
        List<Map<String, String>> schedules = scheduleService.getAllSchedulesEntrepreneur(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }
}