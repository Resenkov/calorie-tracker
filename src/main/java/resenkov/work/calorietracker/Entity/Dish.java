package resenkov.work.calorietracker.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;



@Getter
@Setter
@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('dishes_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotBlank(message = "Название обязательно для заполнения!")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @DecimalMin(value = "0.0", message = "Калории не могут быть отрицательными!")
    @Column(name = "calories_per_serving")
    private double caloriesPerServing;

    @DecimalMin(value = "0.0", message = "Количество белков не может быть отрицательным!")
    @Column(name = "protein")
    private double protein;

    @DecimalMin(value = "0.0", message = "Количество жиров не может быть отрицательным!")
    @Column(name = "fats")
    private double fats;

    @DecimalMin(value = "0.0", message = "Количество углеводов не может быть отрицательным!")
    @Column(name = "carbohydrates")
    private double carbohydrates;

    @OneToMany(mappedBy = "dish")
    private List<Meal> meals;

}