package resenkov.work.calorietracker.dto;

import lombok.Data;

@Data
public class MealItemResponseDTO {
    private Long dishId;
    private String dishName;
    private Integer servings;
    private Double calories;
    private Double protein;
    private Double fats;
    private Double carbohydrates;
}
