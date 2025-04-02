package resenkov.work.calorietracker.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class DishDTO {
    private Long id;

    @NotBlank(message = "Название обязательно")
    private String name;

    @DecimalMin(value = "0.1", message = "Калории должны быть положительными")
    private Double caloriesPerServing;

    @DecimalMin(value = "0.0", message = "Белки не могут быть отрицательными")
    private Double protein;

    @DecimalMin(value = "0.0", message = "Жиры не могут быть отрицательными")
    private Double fats;

    @DecimalMin(value = "0.0", message = "Углеводы не могут быть отрицательными")
    private Double carbohydrates;
}