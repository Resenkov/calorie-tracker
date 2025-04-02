package resenkov.work.calorietracker.dto;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MealCreateDTO {
    @NotNull(message = "ID пользователя обязательно")
    private Long userId;

    @NotNull(message = "Дата приема пищи обязательна")
    private LocalDate mealDate;

    @Valid
    @NotEmpty(message = "Список блюд не может быть пустым")
    private List<MealItemDTO> mealItems;
}

