package resenkov.work.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import resenkov.work.calorietracker.entity.Meal;


import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate date);

    @Query("SELECT SUM(d.caloriesPerServing * m.servings) " +
            "FROM Meal m JOIN m.dish d " +
            "WHERE m.user.id = :userId AND m.mealDate = :date")
    Double calculateDailyCalories(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    @Query("SELECT m FROM Meal m JOIN m.dish d WHERE m.user.id = :userId " +
            "AND (cast(:startDate as date) IS NULL OR m.mealDate >= :startDate) " +
            "AND (cast(:endDate as date) IS NULL OR m.mealDate <= :endDate)")
    List<Meal> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}