package resenkov.work.calorietracker.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import resenkov.work.calorietracker.entity.User;

@Getter
@Setter
public class UserDTO {

    private Long id;
    @Size(message = "Ваше имя должно быть не менее 1 символа и не более 100!", min = 1, max = 100)
    @NotBlank(message = "Имя обязательно для заполнения!")
    private String name;
    @Email(message = "Введите правильный формат почты!")
    @NotBlank(message = "Email обязателен для заполнения!")
    private String email;
    private String password;
    @Min(message = "Возраст должен быть больше 0!", value = 1)
    private Integer age;
    private double weight;
    private double height;
    private User.Goal goal;
    private double dailyCalories;
    @NotNull(message = "Пол обязателен")
    private User.Gender gender;
}
