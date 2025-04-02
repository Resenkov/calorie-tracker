package resenkov.work.calorietracker.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('meals_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Min(value = 1, message = "Порции не могут быть отрицательными!")
    @Column(name = "servings")
    private Integer servings;

    public double getTotalCalories() {
        return dish.getCaloriesPerServing() * servings;
    }

    public double getTotalProtein() {
        return dish.getProtein() * servings;
    }

    public double getTotalFats() {
        return dish.getFats() * servings;
    }

    public double getTotalCarbohydrates() {
        return dish.getCarbohydrates() * servings;
    }

}