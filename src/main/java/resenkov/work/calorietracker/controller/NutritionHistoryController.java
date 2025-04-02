package resenkov.work.calorietracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resenkov.work.calorietracker.dto.NutritionHistoryDTO;
import resenkov.work.calorietracker.service.NutritionHistoryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/{userId}/nutrition-history")
@RequiredArgsConstructor
public class NutritionHistoryController {
    private final NutritionHistoryService nutritionHistoryService;

    @GetMapping
    public ResponseEntity<List<NutritionHistoryDTO>> getNutritionHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Если даты не указаны - берем последние 7 дней
        if (startDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(7);
        } else if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(
                nutritionHistoryService.getNutritionHistory(userId, startDate, endDate)
        );
    }
}