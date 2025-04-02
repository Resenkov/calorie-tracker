package resenkov.work.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resenkov.work.calorietracker.dto.MealCreateDTO;
import resenkov.work.calorietracker.dto.MealResponseDTO;
import resenkov.work.calorietracker.service.MealService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping("/create")
    public ResponseEntity<MealResponseDTO> createMeal(
            @RequestBody @Valid MealCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealService.createMeal(dto));
    }

    @GetMapping("/byuser/{userId}/date/{date}")
    public ResponseEntity<List<MealResponseDTO>> getMealsByDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(mealService.getMealsByUserAndDate(userId, date));
    }
}