package resenkov.work.calorietracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MealItemDTO {
    @NotNull(message = "ID блюда обязательно")
    private Long dishId;

    @Min(value = 1, message = "Количество порций должно быть не менее 1")
    private Integer servings;
}
