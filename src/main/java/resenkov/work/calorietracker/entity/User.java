package resenkov.work.calorietracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('users_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100, min = 1, message = "Ваше имя должно быть не менее 1 символа и не более 100!")
    @NotBlank(message = "Имя обязательно для заполнения!")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email обязателен для заполнения!")
    @Email(message = "Введите правильный формат почты!")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Min(value = 1,message = "Возраст должен быть больше 0!")
    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    @DecimalMin(value = "0.1", message = "Weight must be positive")
    private double weight;

    @Column(name = "height")
    @DecimalMin(value = "0.1", message = "Height must be positive")
    private double height;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) CHECK (goal IN ('Похудение', 'Поддержание', 'Набор массы'))")
    private Goal goal;

    @Column(name = "daily_calories")
    private double dailyCalories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    @Getter
    public enum Goal {
        Похудение("Похудение"),
        Поддержание("Поддержание"),
        Набор("Набор массы");

        private final String displayName;

        Goal(String displayName) {
            this.displayName = displayName;
        }

    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER'))")
    private Gender gender;

    @Getter
    public enum Gender {
        MALE("Мужской"),
        FEMALE("Женский"),
        OTHER("Другое");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }
    }
}