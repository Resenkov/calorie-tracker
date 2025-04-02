package resenkov.work.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NutritionSummaryDTO {
    private double totalProtein;
    private double totalFats;
    private double totalCarbohydrates;
}
