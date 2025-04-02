package resenkov.work.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import resenkov.work.calorietracker.entity.Meal;
import resenkov.work.calorietracker.dto.NutritionSummaryDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate date);

    @Query("SELECT m FROM Meal m JOIN FETCH m.dish WHERE m.user.id = :userId AND m.mealDate = :date")
    List<Meal> findMealsWithDishesByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT m.mealDate as date, SUM(d.caloriesPerServing * m.servings) as totalCalories " +
            "FROM Meal m JOIN m.dish d WHERE m.user.id = :userId GROUP BY m.mealDate ORDER BY m.mealDate DESC")
    List<DailyCalories> findDailyCaloriesByUserId(@Param("userId") Long userId);

    @Query("SELECT NEW resenkov.work.calorietracker.dto.NutritionSummaryDTO(SUM(d.protein * m.servings), " +
            "SUM(d.fats * m.servings), SUM(d.carbohydrates * m.servings)) " +
            "FROM Meal m JOIN m.dish d WHERE m.user.id = :userId AND m.mealDate = :date")
    Optional<NutritionSummaryDTO> getDailyNutritionSummary(@Param("userId") Long userId, @Param("date") LocalDate date);

    interface DailyCalories {
        LocalDate getDate();
        Double getTotalCalories();
    }

}