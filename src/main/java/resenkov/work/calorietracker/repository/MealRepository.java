package resenkov.work.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resenkov.work.calorietracker.entity.Meal;


import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate date);
}