package resenkov.work.calorietracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Min(value = 1, message = "Количество порций должно быть не менее 1")
    @Column(nullable = false)
    private Integer servings;

    // Методы для расчета нутриентов
    public Double getTotalCalories() {
        return dish.getCaloriesPerServing() * servings;
    }

    public Double getTotalProtein() {
        return dish.getProtein() * servings;
    }

    public Double getTotalFats() {
        return dish.getFats() * servings;
    }

    public Double getTotalCarbohydrates() {
        return dish.getCarbohydrates() * servings;
    }
}