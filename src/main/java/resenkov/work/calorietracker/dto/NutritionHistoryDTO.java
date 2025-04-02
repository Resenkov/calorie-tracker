package resenkov.work.calorietracker.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class NutritionHistoryDTO {
    private LocalDate date;
    private Double totalCalories;
    private Double totalProtein;
    private Double totalFats;
    private Double totalCarbohydrates;
    private List<MealResponseDTO> meals;



    public NutritionHistoryDTO() {}
}