package resenkov.work.calorietracker.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MealResponseDTO {
    private Long id;
    private LocalDate mealDate;
    private List<MealItemResponseDTO> items;
    private Double totalCalories;
    private Double totalProtein;
    private Double totalFats;
    private Double totalCarbohydrates;
}

